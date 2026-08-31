package com.cityplatform.user.application;

import com.cityplatform.platform.authentication.TokenService;
import com.cityplatform.platform.authentication.WechatClient;
import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.user.application.dto.UpdateMyProfileRequest;
import com.cityplatform.user.application.dto.WechatLoginResponse;
import com.cityplatform.user.application.readmodel.UserReadModel;
import com.cityplatform.user.domain.User;
import com.cityplatform.user.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户应用服务。负责微信登录编排与当前用户资料用例。
 * 登录链路：WechatClient.code2Session -> 按 openid 查找/创建 User -> 签发平台 token。
 * 本服务不感知微信 SDK 细节，仅依赖 Platform Authentication 接口。
 */
@Service
public class UserApplicationService {

    private static final Logger log = LoggerFactory.getLogger(UserApplicationService.class);

    private final WechatClient wechatClient;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final UserAssembler assembler;

    public UserApplicationService(WechatClient wechatClient,
                                  TokenService tokenService,
                                  UserRepository userRepository,
                                  UserQueryRepository userQueryRepository,
                                  UserAssembler assembler) {
        this.wechatClient = wechatClient;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.userQueryRepository = userQueryRepository;
        this.assembler = assembler;
    }

    /**
     * 微信小程序登录。code 一次性使用，openid 为用户唯一识别依据；
     * 不存在则创建，已存在则按需补充 unionid。返回平台登录态 token。
     */
    @Transactional
    public WechatLoginResponse wechatLogin(String code) {
        WechatClient.WechatSession session = wechatClient.code2Session(code);
        User user = userRepository.findByOpenid(session.openid());
        if (user == null) {
            user = User.register(session.openid(), session.unionid());
            userRepository.insert(user);
            log.info("微信登录创建新用户: userId={}", user.getId());
        } else if (session.unionid() != null && user.getUnionid() == null) {
            user.fillUnionid(session.unionid());
            userRepository.update(user);
            log.info("微信登录补充unionid: userId={}", user.getId());
        }
        String token = tokenService.issue(user.getId());
        return new WechatLoginResponse(token, userQueryRepository.findById(user.getId()));
    }

    public UserReadModel me(Long userId) {
        UserReadModel rm = userQueryRepository.findById(userId);
        if (rm == null) {
            throw new NotFoundException("用户不存在: " + userId);
        }
        return rm;
    }

    @Transactional
    public UserReadModel updateMe(Long userId, UpdateMyProfileRequest request) {
        User user = loadOrThrow(userId);
        assembler.applyUpdate(user, request);
        userRepository.update(user);
        return userQueryRepository.findById(userId);
    }

    /**
     * 发放积分（最小积分能力，供任务奖励等跨域用例在 Application 层协调调用）。
     * 调用方（如 TaskApplicationService）的事务与本方法合并，保证原子性。
     */
    @Transactional
    public long addPoints(Long userId, long delta) {
        User user = loadOrThrow(userId);
        user.addPoints(delta);
        userRepository.update(user);
        log.info("积分发放: userId={}, delta={}, points={}", userId, delta, user.getPoints());
        return user.getPoints();
    }

    private User loadOrThrow(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("用户不存在: " + userId);
        }
        return user;
    }
}
