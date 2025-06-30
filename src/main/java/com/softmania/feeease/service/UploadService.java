package com.softmania.feeease.service;

import com.softmania.feeease.component.PresignedUrl;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    public boolean uploadFile(String key, MultipartFile file);
    public PresignedUrl generatePresignedUrl(String key);
}
