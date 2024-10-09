package com.github.bccyben.common.config;

import com.github.bccyben.common.exception.InvalidRequestException;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * s3コンフィグ
 */
@Data
@ConditionalOnProperty(value = "fileserver.storage.enabled", havingValue = "true", matchIfMissing = false)
@Component
@ConfigurationProperties(prefix = "fileserver.storage")
public class FileServerConfig {

    private String publicBucket;

    private String productFolder;

    private String biddingAttachmentFolder;

    private String endpoint;

    public String getPublicPath() {
        return this.getEndpoint() + "/" + this.getPublicBucket();
    }

    public String getFullPath(String path) {
        return this.getPublicPath() + "/" + path;
    }

    public String getPublicRelativePath(String path) {
        if (path.indexOf(this.getPublicPath()) != 0) {
            throw new InvalidRequestException("パスは正しくありません。");
        }
        return path.replace(this.getPublicPath() + "/", "");
    }

}
