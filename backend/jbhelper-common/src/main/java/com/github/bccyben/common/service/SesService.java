package com.github.bccyben.common.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2AsyncClient;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * AWS SES service
 */
@ConditionalOnProperty(value = "ses.enabled", havingValue = "true", matchIfMissing = false)
@Service
@Slf4j
public class SesService {
    private final static Region REGION = Region.AP_NORTHEAST_1;
    private static SesV2Client client;
    private static SesV2AsyncClient asyncClient;
    /**
     * アクセスキー
     */
    @Value("${ses.accessKey}")
    private String accessKey;
    /**
     * シークレットキー
     */
    @Value("${ses.secretKey}")
    private String secretKey;
    /**
     * 送信元
     */
    @Value("${ses.sender}")
    private String sender;
    @Value("${ses.support}")
    private String supportEmail;
    /**
     * Graceful Shutdown用queue
     */
    private BlockingQueue<String> asyncQueue = new LinkedBlockingQueue<>(50);

    /**
     * async用thread pool
     */
    private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(50, 50,
            100, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder().build());

    @PostConstruct
    private void init() {
        // client
        AwsCredentials credentials = AwsBasicCredentials.create(
                accessKey,
                secretKey);
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);
        client = SesV2Client.builder()
                .credentialsProvider(provider)
                .region(REGION)
                .build();
        // async
        asyncClient = SesV2AsyncClient.builder()
                .asyncConfiguration(b -> b.advancedOption(SdkAdvancedAsyncClientOption.FUTURE_COMPLETION_EXECUTOR,
                        poolExecutor))
                .credentialsProvider(provider)
                .region(REGION)
                .build();
    }

    /**
     * メール送信
     *
     * @param recipient
     * @param subject
     * @param bodyHTML
     * @param cc
     */
    public void send(
            String recipient,
            String subject,
            String bodyHTML,
            String... cc) {
        client.sendEmail(buildRequest(recipient, subject, bodyHTML, cc));
    }

    /**
     * メール送信（非同期）
     *
     * @param recipient
     * @param subject
     * @param bodyHTML
     * @param cc
     */
    public void sendAsync(String recipient,
                          String subject,
                          String bodyHTML,
                          String... cc) {
        final String recipient_ = StringUtils.hasText(recipient) ? recipient : supportEmail;
        asyncQueue.add(recipient_);
        asyncClient.sendEmail(buildRequest(recipient_, subject, bodyHTML, cc))
                .thenAccept((x) -> {
                    log.info("Email sent successfully to " + recipient_);
                    try {
                        asyncQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * リクエスト作成
     *
     * @param recipient
     * @param subject
     * @param bodyHTML
     * @param cc
     * @return
     */
    private SendEmailRequest buildRequest(String recipient,
                                          String subject,
                                          String bodyHTML,
                                          String... cc) {
        Destination destination = Destination.builder()
                .toAddresses(recipient)
                .ccAddresses(cc)
                .build();
        Content content = Content.builder()
                .data(bodyHTML)
                .build();
        Content sub = Content.builder()
                .data(subject)
                .build();
        Body body = Body.builder()
                .html(content)
                .build();
        Message msg = Message.builder()
                .subject(sub)
                .body(body)
                .build();
        EmailContent emailContent = EmailContent.builder()
                .simple(msg)
                .build();
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(emailContent)
                .fromEmailAddress(sender)
                .build();
        return emailRequest;
    }

    /**
     * Graceful Shutdown
     *
     * @throws InterruptedException
     */
    @PreDestroy
    public void destroy() throws InterruptedException {
        while (!asyncQueue.isEmpty()) {
            log.warn("Email is sending count={}", asyncQueue.size());
            Thread.sleep(1000);
        }
    }

}
