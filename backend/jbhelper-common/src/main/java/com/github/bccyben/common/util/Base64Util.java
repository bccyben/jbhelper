package com.github.bccyben.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

    /**
     * 暗号化 64Base(
     *
     * @param source 暗号化対象の文字列
     * @return
     */
    public static String encryptBase64(String source) {
        try {
            return Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 暗号化 64Base(
     *
     * @param bytes 暗号化対象のバイナリ
     * @return
     */
    public static String encryptBase64(byte[] bytes) {
        try {
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 復号化 64Base
     *
     * @param source 復号化対象の文字列
     * @return
     */
    public static String decryptBase64(String source) {
        try {
            return new String(Base64.getDecoder().decode(source.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return null;
        }
    }
}
