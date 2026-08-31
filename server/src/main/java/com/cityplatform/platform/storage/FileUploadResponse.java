package com.cityplatform.platform.storage;

/**
 * 文件上传响应 ReadModel。只包含前端需要的展示字段，不暴露 path/provider 等存储细节。
 */
public record FileUploadResponse(Long fileId, String url, String mimeType, long size) {
}
