package com.cityplatform.street.domain;

import com.cityplatform.platform.exception.BusinessException;

/**
 * POI 类型。
 * STORE 类型必须关联 storeId，其余类型 storeId 必须为空。
 */
public enum POIType {
    STORE,
    SCENIC,
    FACILITY,
    OTHER;

    public static POIType from(String value) {
        if (value == null) {
            throw new BusinessException("ILLEGAL_POI_TYPE", "POI类型不能为空");
        }
        try {
            return POIType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("ILLEGAL_POI_TYPE", "非法POI类型: " + value);
        }
    }
}
