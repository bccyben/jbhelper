package com.github.bccyben.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * <pre>
 * Controllerクラスの共通インタセプター
 *
 * ・リクエストのアクセスログを出力
 * </pre>
 */
@Component
@Slf4j
public class ControllerInterceptor implements HandlerInterceptor {

    /**
     * アクセスログ形式。
     */
    private static final String LOG_FMT = "url:{}, method:{}, status:{}";


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!request.getServletPath().startsWith("/actuator") // ヘルスチェックURLをスキップ
                && !request.getServletPath().startsWith("/swagger-ui") // swagger ui スキップ
                && !request.getServletPath().startsWith("/api-docs") //
                && !request.getServletPath().equals("/")) {
            log.info(LOG_FMT, request.getRequestURI(), request.getMethod(), response.getStatus());
        }
    }
}
