package com.daniel.ltc20.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BrowserUtils {
    public static boolean judgeExisted(String value, String domainSubStr) {
        try {
            Set<String> allFullNames = getAllFullNames(value, domainSubStr);
            log.info("查询{}，查询到{}条数据", value, allFullNames.size());
            if (allFullNames.contains(value)) {
                log.info("域名：{},已经存在", value);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("域名：{},还不存在", value + "." + domainSubStr);
        return false;
    }

    private static Set<String> getAllFullNames(String value, String domainSubStr) throws IOException {
        String url = "https://search.idclub.io/domains/page";
        String data = "{\"name\":\"{}\",\"category\":\"\",\"typeList\":[\"{}\"],\"orderType\":\"numDesc\",\"startWith\":\"\",\"endWith\":\"\",\"minWidth\":\"\",\"maxWidth\":\"\",\"notLike\":\"\",\"wordsType\":\"\",\"pageNum\":1,\"pageSize\":30000}";
        data = StrUtil.format(data, value, domainSubStr);
        String response = sendPostRequest(url, data);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray records = jsonObject.getJSONObject("data").getJSONArray("records");
        Set<String> fullNameSet = new HashSet<>();
        if (!records.isEmpty()) {
            for (Object record : records) {
                JSONObject entries = new JSONObject(record);
                fullNameSet.add(entries.getStr("fullName"));
            }
        }
        return fullNameSet;
    }

    private static String sendPostRequest(String url, String data) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("authority", "search.idclub.io");
            connection.setRequestProperty("accept", "application/json, text/plain, */*");
            connection.setRequestProperty("accept-language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestProperty("origin", "https://beta.idclub.io");
            connection.setRequestProperty("referer", "https://beta.idclub.io/");
            connection.setRequestProperty("sec-ch-ua", "^\\^Not.A/Brand^^;v=^\\^8^^, ^\\^Chromium^^;v=^\\^114^^, ^\\^Google");
            connection.setRequestProperty("sec-ch-ua-mobile", "?0");
            connection.setRequestProperty("sec-ch-ua-platform", "^\\^Windows^^");
            connection.setRequestProperty("sec-fetch-dest", "empty");
            connection.setRequestProperty("sec-fetch-mode", "cors");
            connection.setRequestProperty("sec-fetch-site", "same-site");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
            connection.setDoOutput(true);

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(data);
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    StringBuilder response = new StringBuilder();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        response.append(new String(buffer, 0, bytesRead));
                    }
                    return response.toString();
                }
            }
        } finally {
            connection.disconnect();
        }
        return url;
    }
}
