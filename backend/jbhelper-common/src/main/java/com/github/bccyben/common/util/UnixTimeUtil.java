package com.github.bccyben.common.util;

public class UnixTimeUtil {
    /**
     * linux timestampを取得
     *
     * @return linux timestamp
     */
    public static Long getUnixTime() {
        Long timestamp = System.currentTimeMillis();
        return timestamp;
    }
}
