package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.dto.ChangeMerchantStatusRequest;
import com.cityplatform.merchant.application.dto.CreateMerchantRequest;
import com.cityplatform.merchant.application.dto.MerchantQuery;
import com.cityplatform.merchant.application.dto.UpdateMerchantRequest;
import com.cityplatform.merchant.application.readmodel.MerchantReadModel;
import com.cityplatform.merchant.application.readmodel.MerchantSummary;
import com.cityplatform.merchant.domain.Merchant;
import com.cityplatform.merchant.domain.MerchantRepository;
import com.cityplatform.merchant.domain.MerchantStatus;
import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户应用服务。负责用例编排、事务管理。
 * 写操作走 Domain Repository（Entity）；查询走 QueryRepository（ReadModel）。
 */
@Service
public class MerchantApplicationService {

    private final MerchantRepository merchantRepository;
    private final MerchantQueryRepository merchantQueryRepository;
    private final MerchantAssembler assembler;

    public MerchantApplicationService(MerchantRepository merchantRepository,
                                      MerchantQueryRepository merchantQueryRepository,
                                      MerchantAssembler assembler) {
        this.merchantRepository = merchantRepository;
        this.merchantQueryRepository = merchantQueryRepository;
        this.assembler = assembler;
    }

    @Transactional
    public MerchantReadModel create(CreateMerchantRequest request) {
        Merchant merchant = assembler.toDomain(request);
        merchantRepository.insert(merchant);
        return merchantQueryRepository.findById(merchant.getId());
    }

    @Transactional
    public MerchantReadModel update(Long id, UpdateMerchantRequest request) {
        Merchant merchant = loadOrThrow(id);
        assembler.applyUpdate(merchant, request);
        merchantRepository.update(merchant);
        return merchantQueryRepository.findById(id);
    }

    public MerchantReadModel get(Long id) {
        MerchantReadModel rm = merchantQueryRepository.findById(id);
        if (rm == null) {
            throw new NotFoundException("商户不存在: " + id);
        }
        return rm;
    }

    public PageResult<MerchantSummary> list(MerchantQuery query, PageParam page) {
        List<MerchantSummary> data = merchantQueryRepository.findAll(query, page);
        long total = merchantQueryRepository.count(query);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    @Transactional
    public MerchantReadModel changeStatus(Long id, ChangeMerchantStatusRequest request) {
        Merchant merchant = loadOrThrow(id);
        MerchantStatus target = MerchantStatus.valueOf(request.getStatus());
        if (target == MerchantStatus.ACTIVE) {
            merchant.activate();
        } else {
            merchant.deactivate();
        }
        merchantRepository.update(merchant);
        return merchantQueryRepository.findById(id);
    }

    private Merchant loadOrThrow(Long id) {
        Merchant merchant = merchantRepository.findById(id);
        if (merchant == null) {
            throw new NotFoundException("商户不存在: " + id);
        }
        return merchant;
    }
}
