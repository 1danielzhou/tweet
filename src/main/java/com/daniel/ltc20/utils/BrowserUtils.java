package com.daniel.ltc20.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class BrowserUtils {
    public static boolean judgeDomainExist(String domain_sub_Str, String url_format, String number_str) {
        try {
            Random random = new Random();
            int minDelay = 1000; // 最小延迟时间（毫秒）
            int maxDelay = 5000; // 最大延迟时间（毫秒）
            int delay = random.nextInt(maxDelay - minDelay + 1) + minDelay;
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String url = StrUtil.format(url_format, number_str);
        int maxRetries = 3; // 设置最大重试次数
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                String s = HttpUtil.get(url);
                JSONObject jsonObject = new JSONObject(s);
                Integer count = jsonObject.getJSONObject("data").getInt("matchCount");
                log.info("domain_name={},details.size={}", number_str + "." + domain_sub_Str, count);

                if (count != null && count == 0) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                retryCount++;
                log.error("An error occurred while accessing the URL: {}", url);
                log.error("Error details: {}", e.getMessage());

                if (retryCount < maxRetries) {
                    log.info("Retrying ({}/{})...", retryCount, maxRetries);
                    try {
                        Random random = new Random();
                        int minDelay = 3000; // 最小延迟时间（毫秒）
                        int maxDelay = 5000; // 最大延迟时间（毫秒）
                        int delay = random.nextInt(maxDelay - minDelay + 1) + minDelay;
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        log.error("Thread interrupted while waiting for retry.");
                    }
                }
            }
        }
        return false;
    }
}
