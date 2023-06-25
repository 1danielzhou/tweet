package com.daniel.ltc20.utils;

import cn.hutool.core.collection.CollUtil;
import com.daniel.ltc20.domain.TweetUrl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtil {
    public static List<TweetUrl> removeDuplicates(List<TweetUrl> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<TweetUrl> uniqueTweetUrls = new ArrayList<>();
        Set<String> uniqueTweetIds = new HashSet<>();
        for (TweetUrl tweetUrl : list) {
            if (uniqueTweetIds.contains(tweetUrl.getTweetId())) {
                continue;
            }
            uniqueTweetUrls.add(tweetUrl);
            uniqueTweetIds.add(tweetUrl.getTweetId());
        }
        return uniqueTweetUrls;
    }
}