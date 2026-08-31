package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.dto.CreateMerchantRequest;
import com.cityplatform.merchant.application.dto.UpdateMerchantRequest;
import com.cityplatform.merchant.domain.Merchant;
import com.cityplatform.merchant.domain.MerchantType;
import org.springframework.stereotype.Component;

/**
 * 商户 DTO/Entity 转换器。规范：DTO 转换统一放 Assembler，Controller/Domain 不负责。
 * Entity→ReadModel 不在此处（由 QueryRepository 直接 SQL 映射）。
 */
@Component
public class MerchantAssembler {

    public Merchant toDomain(CreateMerchantRequest req) {
        return Merchant.create(
                req.getName(),
                MerchantType.from(req.getType()),
                req.getContactPerson(),
                req.getContactPhone(),
                req.getIntroduction(),
                req.getLogoFileId()
        );
    }

    public void applyUpdate(Merchant merchant, UpdateMerchantRequest req) {
        merchant.updateInfo(
                req.getName(),
                MerchantType.from(req.getType()),
                req.getContactPerson(),
                req.getContactPhone(),
                req.getIntroduction(),
                req.getLogoFileId()
        );
    }
}
