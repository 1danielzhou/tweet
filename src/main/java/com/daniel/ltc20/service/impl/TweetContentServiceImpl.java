package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetBaseContentDao;
import com.daniel.ltc20.dao.TweetRelationMentionDao;
import com.daniel.ltc20.dao.TweetRelationPostViewDao;
import com.daniel.ltc20.dao.TweetRelationTopicDao;
import com.daniel.ltc20.domain.TweetRelationPostView;
import com.daniel.ltc20.model.TweetContent;
import com.daniel.ltc20.service.TweetContentService;
import com.daniel.ltc20.service.TweetLoginService;
import com.daniel.ltc20.utils.TimeUtil;
import com.daniel.ltc20.utils.TweetUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class TweetContentServiceImpl implements TweetContentService {
    @Autowired
    private TweetBaseContentDao tweetBaseContentDao;

    @Autowired
    private TweetRelationMentionDao tweetRelationMentionDao;

    @Autowired
    private TweetRelationPostViewDao tweetRelationPostViewDao;

    @Autowired
    private TweetRelationTopicDao tweetRelationTopicDao;

    @Autowired
    private TweetLoginService tweetLoginService;

    @Override
    public void insertTweetContent(TweetContent tweetContent) {
        if (ObjectUtil.isEmpty(tweetContent)) {
            return;
        }
        if (ObjectUtil.isNotEmpty(tweetContent.getTweetBaseContent())) {
            tweetBaseContentDao.insertTweetBaseContent(tweetContent.getTweetBaseContent());
        }
        if (CollUtil.isNotEmpty(tweetContent.getTweetMentions())) {
            tweetRelationMentionDao.insertTweetRelationMentions(tweetContent.getTweetMentions());
        }
        if (CollUtil.isNotEmpty(tweetContent.getPostViews())) {
            tweetRelationPostViewDao.insertTweetRelationPostViews(tweetContent.getPostViews());
        }
        if (CollUtil.isNotEmpty(tweetContent.getTweetTopics())) {
            tweetRelationTopicDao.insertTweetRelationTopics(tweetContent.getTweetTopics());
        }
        log.info("写入数据库成功，{}", tweetContent);
    }

    @Override
    public TweetContent queryTweetContentByUrl(WebDriver browser, String tweetUrl) {
        if (ObjectUtil.isEmpty(browser) || StrUtil.isBlank(tweetUrl)) {
            log.info("browser和tweetUrl不应该是空！！！");
            return null;
        }
        try {
            TweetContent tweetContent = new TweetContent();
            browser.get(tweetUrl);
            Thread.sleep(3000);
            Integer index = TweetUtil.locateIndex(browser, tweetUrl);
            log.info("该链接的内容位于页面的第{}个", index);
            if (ObjectUtil.isEmpty(index)) {
                return null;
            }
            String tweetContentCreateTime = TweetUtil.getTweetContentCreateTime(browser, index);
            String tweetId = tweetUrl.substring(tweetUrl.lastIndexOf('/') + 1);
            String tweetContentText = TweetUtil.getTweetContentText(browser, index);
            long tweetViewsNumber = TweetUtil.getTweetViewsNumber(browser, index);

            tweetContent.getTweetBaseContent().setTweetId(tweetId);
            tweetContent.getTweetBaseContent().setUserId(TweetUtil.extractUsername(tweetUrl));
            tweetContent.getTweetBaseContent().setTweetContent(tweetContentText);
            tweetContent.getTweetBaseContent().setLatestViewNumber(tweetViewsNumber);
            tweetContent.getTweetBaseContent().setTweetUrl(tweetUrl);
            tweetContent.getTweetBaseContent().setTweetContentCreateTime(TimeUtil.convertToShanghaiTime(tweetContentCreateTime));
            tweetContent.getTweetBaseContent().setTweetContentCollectTime(new Date());
            tweetContent.getTweetBaseContent().setCreateTime(new Date());
            tweetContent.getTweetBaseContent().setModifyTime(new Date());

            tweetContent.setTweetMentions(TweetUtil.getTweetMentions(browser, index, tweetId));
            tweetContent.setTweetTopics(TweetUtil.getTweetTopics(browser, index, tweetId));

            List<TweetRelationPostView> postViews = new ArrayList<>();
            postViews.add(TweetRelationPostView.builder()
                    .tweetId(tweetId)
                    .collectDate(TimeUtil.getCurrentDateInShanghai())
                    .viewNumber(tweetViewsNumber)
                    .createTime(new Date())
                    .build());
            tweetContent.setPostViews(postViews);

            return tweetContent;
        } catch (Exception e) {
            log.error("获取tweet内容失败,{}", e);
        }
        return null;
    }

    @Override
    public List<String> searchLatestTweetUrls(String searchKey, int size, boolean searchPast24H) {
        if (StrUtil.isEmpty(searchKey)) {
            log.error("searchKey不允许为空！！！");
            return new ArrayList<>();
        }
        WebDriver browser = tweetLoginService.loginWithRandomAccount();
        if (ObjectUtil.isEmpty(browser)) {
            log.error("browser不允许为空！！！");
            return new ArrayList<>();
        }
        List<String> urls = new ArrayList<>();
        String searchUrl = StrUtil.format("https://twitter.com/search?q={}&src=typed_query&f=live", searchKey);
        try {
            browser.get(searchUrl);
            Thread.sleep(3000);
            int count = 0;
            while (true) {
                List<WebElement> cellInnerDivElements = browser.findElements(By.xpath("//section//div[@data-testid=\"cellInnerDiv\"]"));
                if (CollUtil.isEmpty(cellInnerDivElements)) {
                    continue;
                }
                for (WebElement cellInnerDivElement : cellInnerDivElements) {
                    String url = parsedTweetUrl(cellInnerDivElement);
                    if (StrUtil.isNotBlank(url)) {
                        count++;
                        log.info(StrUtil.format("解析获取到的url为{}", url));
                        urls.add(url);
                    }
                    if (searchPast24H && !isWithinPast24H(cellInnerDivElement)) {
                        count = size;
                        break;
                    }
                }
                if (count >= size) {
                    break;
                }
                String js_bottom = "window.scrollTo(0, document.body.scrollHeight);";
                JavascriptExecutor jsExecutor = (JavascriptExecutor) browser;
                jsExecutor.executeScript(js_bottom);
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            log.error("获取最新推特Urls时出现错误，请检查响应的代码！！！");
        } finally {
            if (ObjectUtil.isNotEmpty(browser)) {
                browser.quit();
            }
        }
        log.info(StrUtil.format("搜索关键词{}，一共获取{}条url", searchKey, urls.size()));
        return urls;
    }

    private String parsedTweetUrl(WebElement cellInnerDivElement) {
        if (ObjectUtil.isEmpty(cellInnerDivElement)) {
            return "";
        }
        try {
            WebElement element = cellInnerDivElement.findElement(By.xpath(".//a[@class=\"css-4rbku5 css-18t94o4 css-1dbjc4n r-1loqt21 r-1777fci r-bt1l66 r-1ny4l3l r-bztko3 r-lrvibr\"]"));
            if (ObjectUtil.isNotEmpty(element) && StrUtil.isNotBlank(element.getAttribute("href"))) {
                return element.getAttribute("href").replace("/analytics", "");
            }
        } catch (Exception e) {
            log.error(StrUtil.format("解析获取URL时出错"));
        }
        return "";
    }

    private boolean isWithinPast24H(WebElement cellInnerDivElement) {
        if (ObjectUtil.isEmpty(cellInnerDivElement)) {
            return false;
        }
        int RETRY_LIMIT = 3;
        Pattern TIME_PATTERN = Pattern.compile("^\\d+[mh]$");
        for (int i = 0; i < RETRY_LIMIT; i++) {
            try {
                WebElement tweetContentCreateTimeDiv = cellInnerDivElement.findElement(By.xpath(".//time"));
                String timeSinceNow = tweetContentCreateTimeDiv.getText();
                log.info(StrUtil.format("解析获取到的时间为：{}", timeSinceNow));
                if (TIME_PATTERN.matcher(timeSinceNow).matches()) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                log.error("{}", e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    log.error("{}", e);
                }
            }
        }
        return true;
    }
}
