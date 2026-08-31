package com.cityplatform.merchant.controller;

import com.cityplatform.merchant.application.StoreApplicationService;
import com.cityplatform.merchant.application.dto.ChangeStoreBusinessStatusRequest;
import com.cityplatform.merchant.application.dto.ChangeStoreStatusRequest;
import com.cityplatform.merchant.application.dto.CreateStoreRequest;
import com.cityplatform.merchant.application.dto.StoreQuery;
import com.cityplatform.merchant.application.dto.UpdateStoreRequest;
import com.cityplatform.merchant.application.readmodel.StoreReadModel;
import com.cityplatform.merchant.application.readmodel.StoreSummary;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门店 Controller。不编写业务逻辑，不处理 File/MultipartFile。
 */
@RestController
@RequestMapping("/api")
public class StoreController {

    private final StoreApplicationService service;

    public StoreController(StoreApplicationService service) {
        this.service = service;
    }

    @PostMapping("/merchants/{merchantId}/stores")
    public StoreReadModel create(@PathVariable Long merchantId,
                                 @Valid @RequestBody CreateStoreRequest request) {
        return service.create(merchantId, request);
    }

    @PutMapping("/stores/{id}")
    public StoreReadModel update(@PathVariable Long id,
                                 @Valid @RequestBody UpdateStoreRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/stores/{id}")
    public StoreReadModel get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/stores")
    public PageResult<StoreSummary> list(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String businessStatus,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        StoreQuery query = new StoreQuery();
        query.setMerchantId(merchantId);
        query.setBusinessStatus(businessStatus);
        query.setStatus(status);
        return service.list(query, new PageParam(page, size));
    }

    @PatchMapping("/stores/{id}/business-status")
    public StoreReadModel changeBusinessStatus(@PathVariable Long id,
                                               @Valid @RequestBody ChangeStoreBusinessStatusRequest request) {
        return service.changeBusinessStatus(id, request);
    }

    @PatchMapping("/stores/{id}/status")
    public StoreReadModel changeStatus(@PathVariable Long id,
                                       @Valid @RequestBody ChangeStoreStatusRequest request) {
        return service.changeStatus(id, request);
    }
}
