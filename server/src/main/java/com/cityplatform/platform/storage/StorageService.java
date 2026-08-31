package com.cityplatform.platform.storage;

/**
 * 文件存储能力接口（Platform Service）。
 * 业务域统一调用 Platform Service，不得直接操作文件系统。
 * 当前 Merchant 域查询通过 SQL JOIN stored_file 获取 url，不直接调用此接口；
 * 文件上传统一走 POST /api/files（FileController），业务域不得出现 MultipartFile。
 */
public interface StorageService {

    /**
     * 存储文件并返回元数据。
     */
    StoredFile store(byte[] content, String fileName, String mimeType);

    /**
     * 按主键查询文件元数据。
     */
    StoredFile findById(Long id);
}
