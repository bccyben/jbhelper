package com.github.bccyben.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import com.github.bccyben.common.util.JsonUtil;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>
 *
 * MVCに関す共通設定
 * ・Interceptor有効化設定
 *
 * </pre>
 */
@Configuration
@ServletComponentScan(basePackages = "com.github.bccyben")
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * アクセスログ出力
     */
    @Autowired
    ControllerInterceptor accessLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLogInterceptor) //
                .addPathPatterns("/**") //
                .excludePathPatterns("/", //
                        "/actuator/**"); //
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtil.MAPPER;
    }

    /**
     * メッセージ定義
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    @Bean
    @Primary
    public org.springframework.validation.Validator getValidator() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/validator_messages");
        messageSource.setDefaultEncoding("UTF-8");
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        bean.setProviderClass(HibernateValidator.class);
        return bean;
    }

    /**
     * spring以外で使用される{@link Validator}
     *
     * @return {@link Validator}
     */
    @Bean("innerValidator")
    public Validator getInnerValidator() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/validator_messages");
        messageSource.setDefaultEncoding("UTF-8");
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        bean.setProviderClass(HibernateValidator.class);
        return bean;
    }

}
