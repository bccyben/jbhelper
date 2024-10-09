package com.github.bccyben.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 * Serviceメソッド呼び出しパラメータ出力
 * </pre>
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class AspectLogConfig {

    /**
     * <pre>
     * コンポーネント作成。
     * </pre>
     *
     * @return コンポーネント
     */
    @Bean
    ComponentAspect componentAspect() {
        return new ComponentAspect();
    }

    /**
     * <pre>
     * Springコンポーネントのメソッドを呼び出しの際のパラメータと実行時間ログを出力する。
     * </pre>
     */
    @Aspect
    public class ComponentAspect {

        /**
         * JSON ObjectMapper
         */
        @Autowired
        private ObjectMapper objectMapper;

        /**
         * <pre>
         * Service呼び出しの際の実行時間ログを出力する。
         * </pre>
         *
         * @param pjp ProceedingJoinPointクラス
         * @return 元返却結果
         * @throws Throwable 例外
         */
        @Around("execution(public * com.github.bccyben..*Service.*(..))")
        public Object invokeService(ProceedingJoinPoint pjp) throws Throwable {
            final String className = pjp.getTarget().getClass().getSimpleName();
            final Signature signature = pjp.getStaticPart().getSignature();

            String methodName = "";
            Class<?>[] parameterTypes = null;
            if (signature instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) signature;
                parameterTypes = methodSignature.getParameterTypes();
                methodName = methodSignature.getName();
            }
            Object[] args = pjp.getArgs();

            Map<String, Object> argsMap = new LinkedHashMap<>();
            for (int i = 0; i < args.length; i++) {
                if (parameterTypes != null) {
                    argsMap.put(i + 1 + "_" + parameterTypes[i].getSimpleName(),
                            args[i] instanceof MultipartFile ? "file"
                                    : args[i] instanceof HttpServletResponse ? "response" : args[i]);
                }
            }

            Long startNanoTime = System.nanoTime();
            try {
                return pjp.proceed();
            } finally {
                String argString = objectMapper.writeValueAsString(argsMap);
                String elapsedTime = formattingElapsedTime(System.nanoTime() - startNanoTime);
                log.info("call:{}#{}, args:{}, elapsed:{}", className, methodName, argString, elapsedTime);
            }
        }

        /**
         * ナノ秒をX.XXXXXXと秒を基準とした文字列に変換します。 除算を行わずに実施し、高速化を行っています。
         *
         * @param elapsedTime ナノ秒
         * @return 秒単位に整形された文字列
         */
        public String formattingElapsedTime(long elapsedTime) {
            StringBuilder builder = new StringBuilder();
            builder.append(elapsedTime);
            if (builder.length() < 10) {
                for (int j = builder.length(); j < 10; j++) {
                    if (j == 9) {
                        builder.insert(0, '.');
                    }
                    builder.insert(0, '0');
                }
            } else {
                builder.insert(builder.length() - 9, '.');
            }
            return builder.toString();
        }
    }
}
