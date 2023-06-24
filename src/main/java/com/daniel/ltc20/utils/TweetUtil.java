package com.daniel.ltc20.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.domain.TweetRelationMention;
import com.daniel.ltc20.domain.TweetRelationTopic;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TweetUtil {
    public static String getTweetContentCreateTime(WebDriver driver, Integer index) {
        if (driver == null || index == null) {
            return "";
        }
        try {
            WebElement tweetContentCreateTimeDiv = driver.findElement(By.xpath(String.format("//div[@data-testid='cellInnerDiv'][%d]//time", index)));
            return tweetContentCreateTimeDiv.getAttribute("datetime");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Integer locateIndex(WebDriver driver, String tweetUrl) {
        if (driver == null || tweetUrl == null) {
            return null;
        }
        try {
            String tweetContentId = tweetUrl.substring(tweetUrl.lastIndexOf('/') + 1);
            for (int index = 1; index < 10; index++) {
                List<String> matchedLinks = getMatchedLinksByIndex(driver, index);
                for (String link : matchedLinks) {
                    if (link.endsWith("/status/" + tweetContentId)) {
                        return index;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getMatchedLinksByIndex(WebDriver driver, int index) {
        List<String> matchedLinks = new ArrayList<>();
        try {
            List<WebElement> elements = driver.findElements(By.xpath(
                    String.format("//div[@data-testid='cellInnerDiv'][%d]//a[@role='link' and contains(@href, '/status/')]",
                            index)));
            for (WebElement element : elements) {
                String href = element.getAttribute("href");
                Pattern pattern = Pattern.compile("/status/\\d+$");
                Matcher matcher = pattern.matcher(href);
                if (matcher.find()) {
                    matchedLinks.add(href);
                }
            }
        } catch (Exception e) {
            log.error("解析错误，{}", e);
        }
        return matchedLinks;
    }

    public static String extractUsername(String tweetUrl) {
        try {
            // 定义用户名的正则表达式模式
            String pattern = "twitter\\.com/([a-zA-Z0-9_]+)";
            // 编译正则表达式模式
            Pattern regex = Pattern.compile(pattern);
            // 创建Matcher对象，用于匹配输入字符串和正则表达式模式
            Matcher matcher = regex.matcher(tweetUrl);
            // 查找匹配项
            if (matcher.find()) {
                // 提取匹配到的用户名
                return matcher.group(1);
            }
        } catch (Exception e) {
            log.error("解析用户名失败，{}", tweetUrl);
        }
        return null;
    }

    public static String getTweetContentText(WebDriver driver, Integer index) {
        if (driver == null || index == null) {
            return "";
        }
        try {
            WebElement firstTweetText = driver.findElement(By.xpath(
                    String.format("//div[@data-testid='cellInnerDiv'][%d]//div[@data-testid='tweetText']", index)));
            return firstTweetText.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getTweetViewsNumber(WebDriver driver, Integer index) {
        if (driver == null || index == null) {
            return 0;
        }
        String number = "";
        try {
            WebElement viewsNumber = driver.findElement(By.xpath(
                    String.format("//div[@data-testid='cellInnerDiv'][%d]//div[@class='css-901oao r-18jsvk2 r-37j5jr r-a023e6 r-16dba41 r-rjixqe r-bcqeeo r-qvutc0']", index)));
            String text = viewsNumber.getText();
            String[] parts = text.split("\n");
            if (parts.length > 0) {
                number = parts[0];
            }
        } catch (Exception e) {
            try {
                WebElement viewsNumber = driver.findElement(By.xpath(
                        String.format("//div[@data-testid='cellInnerDiv'][%d]//div[@class='css-1dbjc4n r-18u37iz r-1h0z5md'][4]", index)));
                number = viewsNumber.getText();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return formatNumber(number);
    }

    public static long formatNumber(String number) {
        if (number.matches("\\d+")) {
            return Long.parseLong(number);
        } else if (number.endsWith("k") || number.endsWith("K")) {
            float value = Float.parseFloat(number.substring(0, number.length() - 1));
            return (long) (value * 1000);
        } else if (number.endsWith("M")) {
            float value = Float.parseFloat(number.substring(0, number.length() - 1));
            return (long) (value * 1000000);
        } else {
            return Long.parseLong(number.replace(",", ""));
        }
    }

    public static List<TweetRelationMention> getTweetMentions(WebDriver driver, Integer index, String tweetId) {
        if (driver == null || index == null) {
            return new ArrayList<>();
        }
        List<TweetRelationMention> tweetMentions = new ArrayList<>();
        try {
            List<WebElement> tweetMentionsDivs = driver.findElements(By.xpath(
                    String.format("//div[@data-testid='cellInnerDiv'][%d]//div[@data-testid='tweetText']//span[@class='r-18u37iz']", index)));
            if (tweetMentionsDivs != null && !tweetMentionsDivs.isEmpty()) {
                for (WebElement tweetMentionsDiv : tweetMentionsDivs) {
                    if (tweetMentionsDiv != null && tweetMentionsDiv.getText().startsWith("@")) {
                        tweetMentions.add(TweetRelationMention.builder()
                                .tweetId(tweetId)
                                .tweetMention(tweetMentionsDiv.getText())
                                .createTime(new Date())
                                .build());
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取mentions失败，{}", e);
        }
        return tweetMentions;
    }

    public static List<TweetRelationTopic> getTweetTopics(WebDriver driver, Integer index, String tweetId) {
        if (driver == null || index == null) {
            return new ArrayList<>();
        }
        List<TweetRelationTopic> tweetTopics = new ArrayList<>();
        try {
            List<WebElement> tweetTopicsDivs = driver.findElements(By.xpath(
                    String.format("//div[@data-testid='cellInnerDiv'][%d]//div[@data-testid='tweetText']//span[@class='r-18u37iz']", index)));
            if (tweetTopicsDivs != null && !tweetTopicsDivs.isEmpty()) {
                for (WebElement tweetTopicsDiv : tweetTopicsDivs) {
                    if (tweetTopicsDiv != null && tweetTopicsDiv.getText().startsWith("#")) {
                        tweetTopics.add(TweetRelationTopic.builder()
                                .tweetId(tweetId)
                                .tweetTopic(tweetTopicsDiv.getText())
                                .createTime(new Date())
                                .build());
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取topics失败，{}", e);
        }
        return tweetTopics;
    }

    public static boolean isWithinIntervalHour(WebElement cellInnerDivElement, int interval) {
        if (ObjectUtil.isEmpty(cellInnerDivElement)) {
            return false;
        }
        int RETRY_LIMIT = 3;
        for (int i = 0; i < RETRY_LIMIT; i++) {
            try {
                WebElement tweetContentCreateTimeDiv = cellInnerDivElement.findElement(By.xpath(".//time"));
                String utcDatetime = tweetContentCreateTimeDiv.getAttribute("datetime");
                Date shanghaiDate = TimeUtil.convertToShanghaiTime(utcDatetime);
                log.info(StrUtil.format("解析获取到的时间为：{}", shanghaiDate));
                return TimeUtil.isWithinIntervalHours(shanghaiDate,interval);
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    log.error("{}", e);
                }
            }
        }
        return true;
    }

    public static Date getTweetCreateTime(WebElement cellInnerDivElement) {
        if (ObjectUtil.isEmpty(cellInnerDivElement)) {
            return null;
        }
        int RETRY_LIMIT = 3;
        for (int i = 0; i < RETRY_LIMIT; i++) {
            try {
                WebElement tweetContentCreateTimeDiv = cellInnerDivElement.findElement(By.xpath(".//time"));
                String utcDatetime = tweetContentCreateTimeDiv.getAttribute("datetime");
                Date shanghaiDate = TimeUtil.convertToShanghaiTime(utcDatetime);
                log.info(StrUtil.format("解析获取到的时间为：{}", shanghaiDate));
                return shanghaiDate;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    log.error("{}", e);
                }
            }
        }
        return null;
    }
}
