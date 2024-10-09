package com.github.bccyben.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 非同期処理のラッピングサービス
 */
@Service
public class AsyncWrapService {

    @Autowired(required = false)
    private StorageService storageService;

    @Async("s3")
    public void executeS3Delete(String... paths) {
        for (var path : paths) {
            storageService.delete(path);
        }
    }
}
