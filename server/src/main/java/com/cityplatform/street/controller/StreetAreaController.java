package com.cityplatform.street.controller;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.street.application.StreetAreaApplicationService;
import com.cityplatform.street.application.dto.ChangeStreetAreaStatusRequest;
import com.cityplatform.street.application.dto.CreateStreetAreaRequest;
import com.cityplatform.street.application.dto.StreetAreaQuery;
import com.cityplatform.street.application.dto.UpdateStreetAreaRequest;
import com.cityplatform.street.application.readmodel.StreetAreaReadModel;
import com.cityplatform.street.application.readmodel.StreetAreaSummary;
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
 * 街区 Controller。仅负责接入 HTTP、参数校验、调用 ApplicationService。
 * 不编写业务逻辑，不处理 File/MultipartFile。
 */
@RestController
@RequestMapping("/api/street-areas")
public class StreetAreaController {

    private final StreetAreaApplicationService service;

    public StreetAreaController(StreetAreaApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public StreetAreaReadModel create(@Valid @RequestBody CreateStreetAreaRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public StreetAreaReadModel update(@PathVariable Long id,
                                      @Valid @RequestBody UpdateStreetAreaRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public StreetAreaReadModel get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public PageResult<StreetAreaSummary> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        StreetAreaQuery query = new StreetAreaQuery();
        query.setName(name);
        query.setStatus(status);
        return service.list(query, new PageParam(page, size));
    }

    @PatchMapping("/{id}/status")
    public StreetAreaReadModel changeStatus(@PathVariable Long id,
                                            @Valid @RequestBody ChangeStreetAreaStatusRequest request) {
        return service.changeStatus(id, request);
    }
}
