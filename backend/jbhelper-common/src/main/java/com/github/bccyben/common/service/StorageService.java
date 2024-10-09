package com.github.bccyben.common.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.endpoints.Endpoint;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.endpoints.S3EndpointParams;
import software.amazon.awssdk.services.s3.endpoints.S3EndpointProvider;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * s3/minion通信用
 */
@ConditionalOnProperty(value = "fileserver.storage.enabled", havingValue = "true", matchIfMissing = false)
@Service
@Slf4j
public class StorageService {

    private final static Region REGION = Region.AP_NORTHEAST_1;
    @Value("${fileserver.storage.accessKey}")
    private String accessKey;
    @Value("${fileserver.storage.secretKey}")
    private String secretKey;
    @Value("${fileserver.storage.endpoint}")
    private String endpoint;
    @Value("${fileserver.storage.public-bucket}")
    private String bucketName;

    /**
     * S3
     *
     * @return
     */
    private S3Client getS3Client() {
        if (StringUtils.isNotEmpty(endpoint)) {
            // use minio
            return S3Client.builder()
                    .endpointProvider(new S3EndpointProvider() {
                        @Override
                        public CompletableFuture<Endpoint> resolveEndpoint(S3EndpointParams endpointParams) {
                            return CompletableFuture.completedFuture(Endpoint.builder()
                                    .url(URI.create(endpoint + "/" + endpointParams.bucket()))
                                    .build());
                        }
                    })
                    // SDK requires us to provide region for local development, so we use a
                    // placeholder region here.
                    // The local instance doesn't have a specified region.
                    .region(
                            REGION)
                    .credentialsProvider(() -> AwsBasicCredentials.create(
                            accessKey,
                            secretKey))
                    .forcePathStyle(true)
                    .build();
        } else {
            // use aws s3
            return S3Client.builder()
                    .region(
                            REGION)
                    .credentialsProvider(() -> AwsBasicCredentials.create(
                            accessKey,
                            secretKey))
                    .forcePathStyle(false)
                    .build();
        }
    }

    /**
     * 初期起動時、認証やbucket存在チェックを行う
     */
    @PostConstruct
    private void init() {
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
        getS3Client().headBucket(headBucketRequest);
        log.info("bucket {} check passed.", bucketName);
    }

    /**
     * ファイル存在するかどうか
     *
     * @param filePath <li>例：product/01J2AG44DVKFK8DHBSPJD9EEJE/1721093246821.jpg</li>
     * @return
     */
    public boolean exist(String filePath) {
        HeadObjectRequest request = HeadObjectRequest.builder().bucket(bucketName).key(filePath).build();
        try {
            getS3Client().headObject(request);
        } catch (S3Exception e) {
            log.warn(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * ファイル保存
     *
     * @param file
     * @param filePath
     */
    public void save(MultipartFile file, String filePath) {
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(filePath).build();
        try {
            getS3Client().putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * ファイル保存
     *
     * @param file
     * @param filePath
     */
    public void save(MultipartFile file, Path path) {
        save(file, path.toString().replace("\\", "/"));
    }

    /**
     * ファイル削除
     *
     * @param filePath
     */
    public void delete(String filePath) {
        getS3Client().deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(filePath).build());
    }

    /**
     * フォルダ削除
     *
     * @param prefix
     */
    public void deleteFolder(String prefix) {
        ListObjectsV2Response response = getS3Client()
                .listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).prefix(prefix).build());
        if (response.contents().isEmpty()) {
            return;
        }

        Delete del = Delete.builder()
                .objects(response.contents().stream().map(obj -> ObjectIdentifier.builder().key(
                        obj.key()).build()).collect(Collectors.toList()))
                .build();
        getS3Client().deleteObjects(DeleteObjectsRequest.builder().bucket(bucketName).delete(del).build());
    }

    /**
     * Results in the correct copied file
     * aws s3 sdk v1 -> v2
     *
     * @param filePath
     * @param dst
     */
    public void copyObject(String filePath, String dst) {
        CopyObjectRequest copyReq = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(filePath)
                .destinationBucket(bucketName)
                .destinationKey(dst)
                .build();
        getS3Client().copyObject(copyReq);
    }
}
