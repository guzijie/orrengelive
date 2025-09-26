package com.orrange.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VerificationCodeCache {
    private static final Map<String, String> codeCache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static String key(String scene, String phone) {
        return scene + ":" + phone;
    }

    public static void storeCode(String scene, String phone, String code) {
        codeCache.put(key(scene, phone), code);
        scheduler.schedule(() -> codeCache.remove(key(scene, phone)), 60, TimeUnit.SECONDS);
    }

    public static String getCode(String scene, String phone) {
        return codeCache.get(key(scene, phone));
    }

    public static void removeCode(String scene, String phone) {
        codeCache.remove(key(scene, phone));
    }

    public static boolean isValidCode(String scene, String phone, String code) {
        String storedCode = getCode(scene, phone);
        if (storedCode != null && storedCode.equals(code)) {
            removeCode(scene, phone);
            return true;
        }
        return false;
    }
} 