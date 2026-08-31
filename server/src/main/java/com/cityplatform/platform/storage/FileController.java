package com.cityplatform.platform.storage;

import com.cityplatform.platform.exception.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传 Controller（Platform Storage 能力，不属于业务域）。
 * 仅负责：接收 multipart 文件、参数校验、调用 StorageService、返回响应对象。
 * 文件存储逻辑在 StorageService/LocalStorageClient，业务域不得出现 MultipartFile。
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public FileUploadResponse upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("FILE_EMPTY", "上传文件不能为空");
        }
        String mimeType = file.getContentType();
        if (mimeType == null || mimeType.isBlank()) {
            mimeType = "application/octet-stream";
        }
        try {
            StoredFile stored = storageService.store(file.getBytes(), file.getOriginalFilename(), mimeType);
            return new FileUploadResponse(stored.getId(), stored.getUrl(), stored.getMimeType(), stored.getSize());
        } catch (IOException e) {
            throw new BusinessException("FILE_READ_ERROR", "读取上传文件失败");
        }
    }
}
