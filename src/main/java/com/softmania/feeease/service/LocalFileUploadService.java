package com.softmania.feeease.service;

import com.softmania.feeease.component.PresignedUrl;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Profile("default")
public class LocalFileUploadService implements UploadService{
    @Override
    public boolean uploadFile(String key, MultipartFile file) {
        try {
            File dir = new File("C:/upload"+key.substring(0,key.lastIndexOf("/")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Path filePath = Paths.get("C:/upload",key);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public PresignedUrl generatePresignedUrl(String key) {
        PresignedUrl url = new PresignedUrl();
        url.setUrl("/files"+key);
        return url;
    }
}
