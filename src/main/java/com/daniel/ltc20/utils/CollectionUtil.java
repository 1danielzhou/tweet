package com.daniel.ltc20.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtil {
    public static List<String> removeDuplicates(List<String> list) {
        if(CollUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        Set<String> uniqueString = new HashSet<>(list);
        return new ArrayList<>(uniqueString);
    }
}