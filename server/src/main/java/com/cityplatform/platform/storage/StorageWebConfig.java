package com.cityplatform.platform.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * 上传文件静态资源映射（Web / Storage 基础设施能力）。
 * 将 /uploads/** 映射到本地存储目录，使 StorageService 返回的 URL 可直接 HTTP 访问。
 * 与 LocalStorageClient 共用 platform.storage.local.root 配置，不改变其存储职责。
 */
@Configuration
public class StorageWebConfig implements WebMvcConfigurer {

    private final String storageRoot;

    public StorageWebConfig(@Value("${platform.storage.local.root:./uploads}") String storageRoot) {
        this.storageRoot = storageRoot;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Paths.get(storageRoot).toAbsolutePath().normalize().toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}
