package com.orrange.common.utils;

import org.springframework.beans.BeanUtils;

public class BeanCopyUtils {
    public static <S, T> T copy(S source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }
} 