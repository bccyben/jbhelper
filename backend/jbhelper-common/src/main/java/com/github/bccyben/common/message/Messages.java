package com.github.bccyben.common.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * メッセージ共通
 * </pre>
 */
@Component
public class Messages {

    /**
     * メッセージリソース
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * コードにより、メッセージ内容を取得
     *
     * @param code コード
     * @return
     */
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    /**
     * コードにより、メッセージ内容を取得。パラメータあり
     *
     * @param code コード
     * @param args パラメータ
     * @return
     */
    public String getMessage(String code, String... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * コードにより、メッセージ内容を取得。パラメータあり
     *
     * @param code コード
     * @param args パラメータ
     * @return
     */
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * JSON形式エラーオブジェクトを返す
     *
     * @param code エラーコード
     * @param args パラメーター。省略可
     * @return
     */
    public Map<String, String> getJsonMessage(String code, String... args) {
        HashMap<String, String> errorHash = new HashMap<>();
        errorHash.put("code", code);
        errorHash.put("message", getMessage(code, args));
        return errorHash;
    }

}
