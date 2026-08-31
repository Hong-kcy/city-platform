package com.cityplatform.merchant.domain;

/**
 * 商户类型。
 */
public enum MerchantType {
    FOOD,
    RETAIL,
    ENTERTAINMENT,
    SERVICE,
    OTHER;

    public static MerchantType from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("商户类型不能为空");
        }
        try {
            return MerchantType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("非法商户类型: " + value);
        }
    }
}
