package com.cityplatform.merchant.controller;

import com.cityplatform.merchant.application.MerchantApplicationService;
import com.cityplatform.merchant.application.dto.ChangeMerchantStatusRequest;
import com.cityplatform.merchant.application.dto.CreateMerchantRequest;
import com.cityplatform.merchant.application.dto.MerchantQuery;
import com.cityplatform.merchant.application.dto.UpdateMerchantRequest;
import com.cityplatform.merchant.application.readmodel.MerchantReadModel;
import com.cityplatform.merchant.application.readmodel.MerchantSummary;
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
 * 商户 Controller。仅负责接入 HTTP、参数校验、调用 ApplicationService。
 * 不编写业务逻辑，不处理 File/MultipartFile。
 */
@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantApplicationService service;

    public MerchantController(MerchantApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public MerchantReadModel create(@Valid @RequestBody CreateMerchantRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MerchantReadModel update(@PathVariable Long id,
                                    @Valid @RequestBody UpdateMerchantRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public MerchantReadModel get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public PageResult<MerchantSummary> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        MerchantQuery query = new MerchantQuery();
        query.setName(name);
        query.setType(type);
        query.setStatus(status);
        return service.list(query, new PageParam(page, size));
    }

    @PatchMapping("/{id}/status")
    public MerchantReadModel changeStatus(@PathVariable Long id,
                                          @Valid @RequestBody ChangeMerchantStatusRequest request) {
        return service.changeStatus(id, request);
    }
}
