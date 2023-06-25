package com.daniel.tweet.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;

public class TelegramApiUtil {
    public static String TOKEN = "6022115634:AAHzDItEpowQpQgUn4Jh2laMoLdyJUH775Y";
    public static Integer WARNING_CHAT_ID = -982580839;

    public static void main(String[] args) {
        try {
            String url = StrUtil.format("https://api.telegram.org/bot{}/sendMessage", TOKEN);

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("chat_id", WARNING_CHAT_ID);
            paramMap.put("text", "hello 1 2 3");

            String result = HttpUtil.post(url, paramMap);
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
