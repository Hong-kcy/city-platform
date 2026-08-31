package com.cityplatform.platform.storage;

import java.time.LocalDateTime;

/**
 * 文件存储元数据。规范第六章：统一采用 StoredFile。
 * 当前本地存储，未来切换 COS/OSS/MinIO 时业务无需修改。
 */
public class StoredFile {

    private Long id;
    private String path;
    private String url;
    private String mimeType;
    private Long size;
    private String provider;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
