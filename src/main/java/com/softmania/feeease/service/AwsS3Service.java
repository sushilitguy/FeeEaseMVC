package com.softmania.feeease.service;

import com.softmania.feeease.component.PresignedUrl;
import com.softmania.feeease.component.AwsUrlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Profile("dev-aws")
public class AwsS3Service implements UploadService {
    private final AwsUrlManager urlManager;
    private final String bucketName;
    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Autowired
    public AwsS3Service(AwsUrlManager urlManager, @Value("${aws.s3.bucket-name}") String bucketName, @Value("${aws.s3.region}") String awsRegion,
                        @Value("${aws.s3.access-key}") String accessKey, @Value("${aws.s3.secret-key}") String secretKey) {
        this.s3Client = S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
        this.presigner = S3Presigner.builder().
                region(Region.of(awsRegion)).
                credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))).
                build();
        this.bucketName = bucketName;
        this.urlManager = urlManager;
    }

    public boolean uploadFile(String key, MultipartFile file) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public PresignedUrl generatePresignedUrl(String key) {
        PresignedUrl url = urlManager.getExistingUrl(key);
        if(url == null || LocalDateTime.now().isAfter(url.getExpiry())) {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(b -> b
                            .bucket(bucketName)
                            .key(key))
                    .signatureDuration(Duration.ofMinutes(15))
                    .build();

            url = new PresignedUrl();
            url.setExpiry(LocalDateTime.now().plusMinutes(14));
            url.setUrl(presigner.presignGetObject(presignRequest).url().toString());

            urlManager.addUrl(key, url);
        }
        return url;
    }
}
