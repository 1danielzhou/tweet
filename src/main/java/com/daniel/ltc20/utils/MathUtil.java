package com.daniel.ltc20.utils;

public class MathUtil {
    public static Long subtract(Long value1, Long value2) {
        if (value2 == null) {
            return value1; // 如果 value2 为 null，则返回 value1 的值
        }

        if (value1 == null) {
            return null; // 处理空指针情况，如果 value1 为 null，则返回 null
        }

        return value1 - value2;
    }

}
