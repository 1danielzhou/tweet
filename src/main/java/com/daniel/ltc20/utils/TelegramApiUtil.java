package com.daniel.ltc20.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelegramApiUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TWEET_TEXT_REDIS_LIST = "log_text";

    public void sendMessage(String text) {
        if (StrUtil.isBlank(text)) {
            return;
        }
        redisTemplate.opsForList().rightPush(TWEET_TEXT_REDIS_LIST, text);
    }
}
