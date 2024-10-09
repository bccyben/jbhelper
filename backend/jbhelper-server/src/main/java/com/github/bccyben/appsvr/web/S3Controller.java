package com.github.bccyben.appsvr.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.github.bccyben.common.config.FileServerConfig;
import com.github.bccyben.common.service.StorageService;

@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {
    @Autowired(required = false)
    private StorageService storageService;

    @Autowired(required = false)
    private FileServerConfig fileServerConfig;

    @PostMapping("/upload")
    @RequestMapping(path = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "ファイルアップロード")
    @ApiResponse(responseCode = "200", description = "成功")
    public String upload(@Parameter(required = true, description = "ファイル") @RequestParam("file") MultipartFile file) {
        storageService.save(file, "sample/" + file.getOriginalFilename());
        return fileServerConfig.getFullPath("sample/" + file.getOriginalFilename());
    }
}
