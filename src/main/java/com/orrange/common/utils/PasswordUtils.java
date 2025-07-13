package com.orrange.common.utils;

import org.springframework.util.DigestUtils;

public class PasswordUtils {
    public static String encrypt(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
} 