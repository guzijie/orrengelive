package com.orrange.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanCopyUtils {
    public static <S, T> T copy(S source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }
    
    public static <S, T> List<T> copyBeanList(List<S> sourceList, Class<T> targetClass) {
        List<T> targetList = new ArrayList<>();
        for (S source : sourceList) {
            try {
                T target = targetClass.newInstance();
                BeanUtils.copyProperties(source, target);
                targetList.add(target);
            } catch (Exception e) {
                throw new RuntimeException("Bean copy failed", e);
            }
        }
        return targetList;
    }
} 