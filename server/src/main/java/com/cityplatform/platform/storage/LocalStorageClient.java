package com.cityplatform.platform.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 本地文件存储实现（Infrastructure）。
 * 规范第六章：当前 Local Storage，未来可切换 COS/OSS/MinIO。
 * 当前阶段 stored_file 表的占位记录由 data.sql 初始化，本实现为文件上传能力就绪。
 */
@Component
public class LocalStorageClient implements StorageService {

    private final JdbcTemplate jdbcTemplate;
    private final String storageRoot;

    public LocalStorageClient(JdbcTemplate jdbcTemplate,
                              @Value("${platform.storage.local.root:./uploads}") String storageRoot) {
        this.jdbcTemplate = jdbcTemplate;
        this.storageRoot = storageRoot;
    }

    @Override
    public StoredFile store(byte[] content, String fileName, String mimeType) {
        String ext = "";
        if (fileName != null && fileName.contains(".")) {
            ext = fileName.substring(fileName.lastIndexOf("."));
        }
        String storedName = UUID.randomUUID() + ext;
        Path dir = Paths.get(storageRoot);
        try {
            Files.createDirectories(dir);
            Files.write(dir.resolve(storedName), content);
        } catch (IOException e) {
            throw new IllegalStateException("文件存储失败: " + fileName, e);
        }
        String path = storedName;
        String url = "/uploads/" + storedName;
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(
                "INSERT INTO stored_file (path, url, mime_type, size, provider, created_at) VALUES (?,?,?,?,?,?)",
                path, url, mimeType, (long) content.length, "local", now
        );
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        StoredFile sf = new StoredFile();
        sf.setId(id);
        sf.setPath(path);
        sf.setUrl(url);
        sf.setMimeType(mimeType);
        sf.setSize((long) content.length);
        sf.setProvider("local");
        sf.setCreatedAt(now);
        return sf;
    }

    @Override
    public StoredFile findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, path, url, mime_type, size, provider, created_at FROM stored_file WHERE id=?",
                (rs, i) -> {
                    StoredFile sf = new StoredFile();
                    sf.setId(rs.getLong("id"));
                    sf.setPath(rs.getString("path"));
                    sf.setUrl(rs.getString("url"));
                    sf.setMimeType(rs.getString("mime_type"));
                    sf.setSize(rs.getLong("size"));
                    sf.setProvider(rs.getString("provider"));
                    sf.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return sf;
                },
                id
        );
    }
}
