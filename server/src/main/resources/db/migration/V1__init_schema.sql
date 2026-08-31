-- ============================================================
-- Platform Storage: stored_file
-- 规范第六章：统一文件存储元数据表
-- ============================================================
CREATE TABLE IF NOT EXISTS stored_file (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    path        VARCHAR(500) NOT NULL COMMENT '存储路径',
    url         VARCHAR(500) NOT NULL COMMENT '访问URL',
    mime_type   VARCHAR(100)          COMMENT 'MIME类型',
    size        BIGINT                COMMENT '文件大小(字节)',
    provider    VARCHAR(50)  NOT NULL DEFAULT 'local' COMMENT '存储提供方',
    created_at  DATETIME     NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件存储元数据';

-- ============================================================
-- Merchant 域: merchant 商户
-- ============================================================
CREATE TABLE IF NOT EXISTS merchant (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    name            VARCHAR(100) NOT NULL COMMENT '商户名称(非唯一,不同主体可同名)',
    type            VARCHAR(50)  NOT NULL COMMENT '商户类型:FOOD/RETAIL/ENTERTAINMENT/SERVICE/OTHER',
    contact_person  VARCHAR(50)           COMMENT '联系人',
    contact_phone   VARCHAR(20)           COMMENT '联系电话',
    introduction    VARCHAR(500)          COMMENT '简介',
    logo_file_id    BIGINT                COMMENT 'Logo文件ID(关联stored_file,可为空)',
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态:ACTIVE/INACTIVE',
    created_at      DATETIME     NOT NULL,
    updated_at      DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_merchant_name (name),
    INDEX idx_merchant_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户';

-- ============================================================
-- Merchant 域: store 门店
-- ============================================================
CREATE TABLE IF NOT EXISTS store (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    merchant_id          BIGINT       NOT NULL COMMENT '所属商户ID',
    name                 VARCHAR(100) NOT NULL COMMENT '门店名称(非唯一)',
    address              VARCHAR(255) NOT NULL COMMENT '门店地址(工商运营数据)',
    longitude            DECIMAL(10,7)         COMMENT '经度',
    latitude             DECIMAL(10,7)         COMMENT '纬度',
    phone                VARCHAR(20)           COMMENT '门店电话',
    business_hours       VARCHAR(100)          COMMENT '营业时间',
    cover_image_file_id  BIGINT                COMMENT '封面图文件ID',
    business_status      VARCHAR(20)  NOT NULL DEFAULT 'OPEN' COMMENT '营业状态:OPEN/CLOSED',
    status               VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '管理状态:ACTIVE/INACTIVE',
    created_at           DATETIME     NOT NULL,
    updated_at           DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_store_merchant_id (merchant_id),
    INDEX idx_store_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店';

-- ============================================================
-- Street 域: street_area 街区
-- ============================================================
CREATE TABLE IF NOT EXISTS street_area (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    name                 VARCHAR(100) NOT NULL COMMENT '街区名称(非唯一)',
    introduction         VARCHAR(500)          COMMENT '街区简介',
    cover_image_file_id  BIGINT                COMMENT '封面图文件ID(关联stored_file)',
    longitude            DECIMAL(10,7) NOT NULL COMMENT '中心经度',
    latitude             DECIMAL(10,7) NOT NULL COMMENT '中心纬度',
    status               VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态:ACTIVE/INACTIVE',
    created_at           DATETIME     NOT NULL,
    updated_at           DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_street_area_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='街区';

-- ============================================================
-- Street 域: poi 空间兴趣点
-- store_id 为弱关联,无物理外键,跨域引用 Merchant.store
-- POI status 与 StreetArea status 独立,各自维护生命周期
-- ============================================================
CREATE TABLE IF NOT EXISTS poi (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    street_area_id  BIGINT       NOT NULL COMMENT '所属街区ID',
    name            VARCHAR(100) NOT NULL COMMENT 'POI名称(非唯一)',
    poi_type        VARCHAR(50)  NOT NULL COMMENT 'POI类型:STORE/SCENIC/FACILITY/OTHER',
    store_id        BIGINT                COMMENT '关联门店ID(可空,STORE类型必填,其余必须为空)',
    longitude       DECIMAL(10,7) NOT NULL COMMENT '经度',
    latitude        DECIMAL(10,7) NOT NULL COMMENT '纬度',
    description     VARCHAR(500)          COMMENT 'POI描述',
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态:ACTIVE/INACTIVE',
    created_at      DATETIME     NOT NULL,
    updated_at      DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_poi_street_area_status (street_area_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='空间兴趣点';

-- ============================================================
-- Platform Authentication: auth_token 平台登录态
-- 微信登录成功后由平台签发的会话令牌，小程序持 token 访问需登录接口
-- 不存储微信 session_key（当前阶段无微信加密数据解密用例）
-- ============================================================
CREATE TABLE IF NOT EXISTS auth_token (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    token       VARCHAR(64)  NOT NULL COMMENT '平台登录令牌(随机生成)',
    user_id     BIGINT       NOT NULL COMMENT '关联用户ID',
    created_at  DATETIME     NOT NULL,
    expires_at  DATETIME     NOT NULL COMMENT '过期时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_auth_token (token),
    INDEX idx_auth_token_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台登录令牌';

-- ============================================================
-- User 域: user 用户
-- openid 为微信身份，小程序范围内唯一；昵称头像允许为空(微信授权资料另行完善)
-- 消息偏好/轻量画像字段数量少，直接放 user 表，不单独建表
-- ============================================================
CREATE TABLE IF NOT EXISTS `user` (
    id                            BIGINT       NOT NULL AUTO_INCREMENT,
    openid                        VARCHAR(64)  NOT NULL COMMENT '微信openid(小程序范围唯一)',
    unionid                       VARCHAR(64)           COMMENT '微信unionid(可为空,开放平台绑定后返回)',
    nickname                      VARCHAR(100)          COMMENT '昵称(用户资料完善后填写)',
    avatar_file_id                BIGINT                COMMENT '头像文件ID(关联stored_file,用户上传后填写)',
    preferred_activity_types      VARCHAR(200)          COMMENT '偏好活动类型(逗号分隔,轻量画像)',
    preferred_experience_types    VARCHAR(200)          COMMENT '偏好空间体验类型(逗号分隔,轻量画像)',
    activity_reminder_enabled     TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '活动提醒开关',
    system_notification_enabled   TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '系统通知开关',
    points                        BIGINT       NOT NULL DEFAULT 0 COMMENT '积分(能力预留,本阶段无积分流水)',
    status                        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态:ACTIVE/INACTIVE',
    created_at                    DATETIME     NOT NULL,
    updated_at                    DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_openid (openid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

-- ============================================================
-- Activity 域: activity 活动
-- 活动独立于 Merchant，不属于商户附属字段
-- street_area_id 强关联街区(创建时校验)；poi_id 为可选弱关联(活动可发生在整个街区)
-- 状态生命周期: DRAFT -> PUBLISHED -> OFFLINE，状态规则由 Domain 维护
-- ============================================================
CREATE TABLE IF NOT EXISTS activity (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    street_area_id  BIGINT       NOT NULL COMMENT '所属街区ID',
    title           VARCHAR(100) NOT NULL COMMENT '活动标题(非唯一)',
    summary         VARCHAR(500)          COMMENT '活动摘要',
    description     TEXT                  COMMENT '活动详细描述',
    cover_file_id   BIGINT                COMMENT '封面图文件ID(关联stored_file)',
    activity_type   VARCHAR(50)  NOT NULL COMMENT '活动类型:FESTIVAL/PERFORMANCE/EXHIBITION/PROMOTION/CULTURE/OTHER',
    start_time      DATETIME     NOT NULL COMMENT '开始时间',
    end_time        DATETIME     NOT NULL COMMENT '结束时间',
    location        VARCHAR(255)          COMMENT '活动地点文字描述(可为空)',
    poi_id          BIGINT                COMMENT '关联POI ID(可空,活动可发生在整个街区)',
    status          VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' COMMENT '状态:DRAFT/PUBLISHED/OFFLINE',
    created_at      DATETIME     NOT NULL,
    updated_at      DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_activity_area_status (street_area_id, status),
    INDEX idx_activity_status_start (status, start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动';

-- ============================================================
-- Route 域: experience_route 主题体验路线
-- 路线强归属单个街区(与 activity 同模式)，只引用 POI，不负责导航
-- 状态 ACTIVE/INACTIVE，软删除模式，不做审核流
-- ============================================================
CREATE TABLE IF NOT EXISTS experience_route (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    street_area_id      BIGINT       NOT NULL COMMENT '所属街区ID',
    name                VARCHAR(100) NOT NULL COMMENT '路线名称(非唯一)',
    theme               VARCHAR(50)  NOT NULL COMMENT '主题:FRIEND_PHOTO/SOLO_RELAX/FAMILY_FUN/SLOW_WALK/OTHER',
    description         VARCHAR(500)          COMMENT '路线描述',
    estimated_duration  INT          NOT NULL COMMENT '预计时长(分钟)',
    status              VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态:ACTIVE/INACTIVE',
    created_at          DATETIME     NOT NULL,
    updated_at          DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_route_area_status (street_area_id, status),
    INDEX idx_route_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主题体验路线';

-- ============================================================
-- Route 域: experience_route_item 路线POI关联(多对多,带顺序业务属性)
-- UNIQUE(route_id, sequence) 保证顺序唯一
-- UNIQUE(route_id, poi_id) 防止同一POI重复加入同一路线
-- ============================================================
CREATE TABLE IF NOT EXISTS experience_route_item (
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    route_id              BIGINT       NOT NULL COMMENT '所属路线ID',
    poi_id                BIGINT       NOT NULL COMMENT 'POI ID(弱关联street.poi)',
    sequence              INT          NOT NULL COMMENT '体验顺序,从1开始',
    recommendation_reason VARCHAR(255)          COMMENT '推荐理由/到访提示',
    created_at            DATETIME     NOT NULL,
    updated_at            DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_route_item_sequence (route_id, sequence),
    UNIQUE KEY uk_route_item_poi (route_id, poi_id),
    INDEX idx_route_item_poi (poi_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='路线POI关联';

-- ============================================================
-- Merchant 域: coupon 优惠券(商户商业权益)
-- 归属 Merchant Domain：优惠券是商户/门店经营能力，主归属 store_id，
-- merchant_id 冗余存储便于商户维度查询；与 Task 无任何绑定
-- 有效期 EXPIRED 为读时计算(当前时间超出 valid_to)，不做定时任务刷状态
-- ============================================================
CREATE TABLE IF NOT EXISTS coupon (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    merchant_id    BIGINT       NOT NULL COMMENT '所属商户ID',
    store_id       BIGINT       NOT NULL COMMENT '主归属门店ID',
    name           VARCHAR(100) NOT NULL COMMENT '优惠券名称(非唯一)',
    description    VARCHAR(500)          COMMENT '使用说明',
    discount_text  VARCHAR(100) NOT NULL COMMENT '权益文本,如:满100减20',
    valid_from     DATETIME     NOT NULL COMMENT '有效期开始',
    valid_to       DATETIME     NOT NULL COMMENT '有效期结束',
    status         VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态:ACTIVE/INACTIVE',
    created_at     DATETIME     NOT NULL,
    updated_at     DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_coupon_store_status (store_id, status),
    INDEX idx_coupon_merchant (merchant_id),
    INDEX idx_coupon_status_valid (status, valid_to)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券';

-- ============================================================
-- Merchant 域: user_coupon 用户领取记录(独立领取关系)
-- UNIQUE(user_id, coupon_id) 保证一人一券，与 Application 校验双重防护
-- redeem_code 全局唯一，作为到店核销凭证；二维码由前端根据该值生成
-- ============================================================
CREATE TABLE IF NOT EXISTS user_coupon (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    coupon_id            BIGINT       NOT NULL COMMENT '优惠券ID',
    user_id              BIGINT       NOT NULL COMMENT '用户ID',
    status               VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE' COMMENT '状态:AVAILABLE/REDEEMED',
    redeem_code          VARCHAR(20)  NOT NULL COMMENT '核销码(领取时生成,全局唯一)',
    redeemed_at          DATETIME                COMMENT '核销时间',
    redeemed_by_store_id BIGINT                 COMMENT '核销门店ID',
    created_at           DATETIME     NOT NULL,
    updated_at           DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_coupon (user_id, coupon_id),
    UNIQUE KEY uk_user_coupon_code (redeem_code),
    INDEX idx_user_coupon_coupon (coupon_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券';

-- ============================================================
-- Task 域: task 任务(独立业务域,非 Activity/Merchant 从属实体)
-- source_type/source_id 仅为来源引用元数据,不建立强实体依赖
-- 到店任务 Demo 阶段采用核销码验证,不做 GPS/围栏判断
-- 状态: DRAFT -> ACTIVE -> DISABLED,完成态由 UserTask 表达
-- ============================================================
CREATE TABLE IF NOT EXISTS task (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    title        VARCHAR(100) NOT NULL COMMENT '任务标题(非唯一)',
    description  VARCHAR(500)          COMMENT '任务说明',
    task_type    VARCHAR(30)  NOT NULL COMMENT '任务类型:STORE_VISIT到店任务',
    source_type  VARCHAR(30)  NOT NULL COMMENT '来源类型:ACTIVITY/RECOMMENDATION/OPERATION/MERCHANT',
    source_id    BIGINT                COMMENT '来源对象ID(弱引用,可空)',
    store_id     BIGINT                COMMENT '到店任务关联门店ID(可空)',
    reward_type  VARCHAR(20)  NOT NULL COMMENT '奖励类型:POINT积分',
    reward_value INT          NOT NULL DEFAULT 0 COMMENT '奖励数值(积分值)',
    start_at     DATETIME     NOT NULL COMMENT '任务开始时间',
    end_at       DATETIME     NOT NULL COMMENT '任务结束时间',
    status       VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' COMMENT '状态:DRAFT/ACTIVE/DISABLED',
    created_at   DATETIME     NOT NULL,
    updated_at   DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_task_status (status),
    INDEX idx_task_store (store_id),
    INDEX idx_task_source (source_type, source_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务';

-- ============================================================
-- Task 域: user_task 用户任务参与记录
-- UNIQUE(user_id, task_id) 防止重复参与；
-- task_code 为完成任务核销码(参与时生成,商户后台输入完成)
-- ============================================================
CREATE TABLE IF NOT EXISTS user_task (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    task_id      BIGINT       NOT NULL COMMENT '任务ID',
    user_id      BIGINT       NOT NULL COMMENT '用户ID',
    status       VARCHAR(20)  NOT NULL DEFAULT 'JOINED' COMMENT '状态:JOINED/COMPLETED',
    task_code    VARCHAR(20)  NOT NULL COMMENT '任务完成核销码(全局唯一)',
    completed_at DATETIME                COMMENT '完成时间',
    reward_issued TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '奖励是否已发放',
    created_at   DATETIME     NOT NULL,
    updated_at   DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_task (user_id, task_id),
    UNIQUE KEY uk_user_task_code (task_code),
    INDEX idx_user_task_task (task_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户任务';

-- ============================================================
-- Activity 域: activity_subscription 活动订阅(用户"想去")
-- UNIQUE(user_id, activity_id) 保证同一用户对同一活动只有一条订阅记录
-- 取消订阅为软删除(状态置 CANCELLED)，重新订阅复用原记录
-- ============================================================
CREATE TABLE IF NOT EXISTS activity_subscription (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    activity_id  BIGINT       NOT NULL COMMENT '活动ID',
    user_id      BIGINT       NOT NULL COMMENT '用户ID',
    status       VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态:ACTIVE/CANCELLED',
    created_at   DATETIME     NOT NULL,
    updated_at   DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_activity_subscription (user_id, activity_id),
    INDEX idx_activity_subscription_activity (activity_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动订阅';
