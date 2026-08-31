package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.dto.ChangeStoreBusinessStatusRequest;
import com.cityplatform.merchant.application.dto.ChangeStoreStatusRequest;
import com.cityplatform.merchant.application.dto.CreateStoreRequest;
import com.cityplatform.merchant.application.dto.StoreQuery;
import com.cityplatform.merchant.application.dto.UpdateStoreRequest;
import com.cityplatform.merchant.application.readmodel.StoreReadModel;
import com.cityplatform.merchant.application.readmodel.StoreSummary;
import com.cityplatform.merchant.domain.BusinessStatus;
import com.cityplatform.merchant.domain.MerchantRepository;
import com.cityplatform.merchant.domain.Store;
import com.cityplatform.merchant.domain.StoreRepository;
import com.cityplatform.merchant.domain.StoreStatus;
import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 门店应用服务。创建门店时校验商户存在（跨实体校验，由 Application 协调）。
 */
@Service
public class StoreApplicationService {

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final StoreAssembler assembler;
    private final MerchantRepository merchantRepository;

    public StoreApplicationService(StoreRepository storeRepository,
                                   StoreQueryRepository storeQueryRepository,
                                   StoreAssembler assembler,
                                   MerchantRepository merchantRepository) {
        this.storeRepository = storeRepository;
        this.storeQueryRepository = storeQueryRepository;
        this.assembler = assembler;
        this.merchantRepository = merchantRepository;
    }

    @Transactional
    public StoreReadModel create(Long merchantId, CreateStoreRequest request) {
        if (merchantRepository.findById(merchantId) == null) {
            throw new NotFoundException("商户不存在: " + merchantId);
        }
        Store store = assembler.toDomain(merchantId, request);
        storeRepository.insert(store);
        return storeQueryRepository.findById(store.getId());
    }

    @Transactional
    public StoreReadModel update(Long id, UpdateStoreRequest request) {
        Store store = loadOrThrow(id);
        assembler.applyUpdate(store, request);
        storeRepository.update(store);
        return storeQueryRepository.findById(id);
    }

    public StoreReadModel get(Long id) {
        StoreReadModel rm = storeQueryRepository.findById(id);
        if (rm == null) {
            throw new NotFoundException("门店不存在: " + id);
        }
        return rm;
    }

    public PageResult<StoreSummary> list(StoreQuery query, PageParam page) {
        List<StoreSummary> data = storeQueryRepository.findAll(query, page);
        long total = storeQueryRepository.count(query);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    @Transactional
    public StoreReadModel changeBusinessStatus(Long id, ChangeStoreBusinessStatusRequest request) {
        Store store = loadOrThrow(id);
        store.changeBusinessStatus(BusinessStatus.valueOf(request.getBusinessStatus()));
        storeRepository.update(store);
        return storeQueryRepository.findById(id);
    }

    @Transactional
    public StoreReadModel changeStatus(Long id, ChangeStoreStatusRequest request) {
        Store store = loadOrThrow(id);
        StoreStatus target = StoreStatus.valueOf(request.getStatus());
        if (target == StoreStatus.ACTIVE) {
            store.activate();
        } else {
            store.deactivate();
        }
        storeRepository.update(store);
        return storeQueryRepository.findById(id);
    }

    private Store loadOrThrow(Long id) {
        Store store = storeRepository.findById(id);
        if (store == null) {
            throw new NotFoundException("门店不存在: " + id);
        }
        return store;
    }
}
