package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.dto.CreateStoreRequest;
import com.cityplatform.merchant.application.dto.UpdateStoreRequest;
import com.cityplatform.merchant.domain.Store;
import org.springframework.stereotype.Component;

/**
 * 门店 DTO/Entity 转换器。
 */
@Component
public class StoreAssembler {

    public Store toDomain(Long merchantId, CreateStoreRequest req) {
        return Store.create(
                merchantId,
                req.getName(),
                req.getAddress(),
                req.getLongitude(),
                req.getLatitude(),
                req.getPhone(),
                req.getBusinessHours(),
                req.getCoverImageFileId()
        );
    }

    public void applyUpdate(Store store, UpdateStoreRequest req) {
        store.updateInfo(
                req.getName(),
                req.getAddress(),
                req.getLongitude(),
                req.getLatitude(),
                req.getPhone(),
                req.getBusinessHours(),
                req.getCoverImageFileId()
        );
    }
}
