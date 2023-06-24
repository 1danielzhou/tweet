package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.domain.TweetRelationPostView;
import com.daniel.ltc20.domain.TweetUrl;
import com.daniel.ltc20.model.TweetContent;
import com.daniel.ltc20.service.*;
import com.daniel.ltc20.utils.CollectionUtil;
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

@Slf4j
@Service
public class TweetContentServiceImpl implements TweetContentService {
    @Autowired
    private TweetBaseContentService tweetBaseContentService;

    @Autowired
    private TweetRelationMentionService tweetRelationMentionService;

    @Autowired
    private TweetRelationPostViewService tweetRelationPostViewService;

    @Autowired
    private TweetRelationTopicService tweetRelationTopicService;

    @Autowired
    private TweetLoginService tweetLoginService;

    @Override
    public void insertTweetContent(TweetContent tweetContent) {
        if (ObjectUtil.isEmpty(tweetContent)) {
            return;
        }
        if (ObjectUtil.isNotEmpty(tweetContent.getTweetBaseContent())) {
            tweetBaseContentService.insert(tweetContent.getTweetBaseContent());
        }
        if (CollUtil.isNotEmpty(tweetContent.getTweetMentions())) {
            tweetRelationMentionService.insert(tweetContent.getTweetMentions(), tweetContent.getTweetBaseContent().getTweetId());
        }
        if (CollUtil.isNotEmpty(tweetContent.getPostViews())) {
            tweetRelationPostViewService.insert(tweetContent.getPostViews(), tweetContent.getTweetBaseContent().getTweetId());
        }
        if (CollUtil.isNotEmpty(tweetContent.getTweetTopics())) {
            tweetRelationTopicService.insert(tweetContent.getTweetTopics(), tweetContent.getTweetBaseContent().getTweetId());
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
    public List<TweetUrl> searchLatestTweetUrls(String searchKey, int size, int interval) {
        if (StrUtil.isEmpty(searchKey)) {
            log.error("searchKey不允许为空！！！");
            return new ArrayList<>();
        }
        WebDriver browser = tweetLoginService.loginWithRandomAccount();
        if (ObjectUtil.isEmpty(browser)) {
            log.error("browser不允许为空！！！");
            return new ArrayList<>();
        }
        List<TweetUrl> tweetUrls = new ArrayList<>();
        String searchUrl = StrUtil.format("https://twitter.com/search?q={}&src=typed_query&f=live", searchKey);
        try {
            browser.get(searchUrl);
            Thread.sleep(3000);
            int count = 0;

            String js_bottom = "window.scrollTo(0, document.body.scrollHeight);";
            JavascriptExecutor jsExecutor = (JavascriptExecutor) browser;

            long prevScrollHeight = 0;
            long currScrollHeight = (long) jsExecutor.executeScript("return document.body.scrollHeight;");

            while (true) {
                List<WebElement> cellInnerDivElements = browser.findElements(By.xpath("//section//div[@data-testid=\"cellInnerDiv\"]"));
                if (CollUtil.isEmpty(cellInnerDivElements)) {
                    break;
                }
                for (WebElement cellInnerDivElement : cellInnerDivElements) {
                    String url = parsedTweetUrl(cellInnerDivElement);
                    Date tweetCreateTime = TweetUtil.getTweetCreateTime(cellInnerDivElement);
                    if (StrUtil.isNotBlank(url) && ObjectUtil.isNotEmpty(tweetCreateTime)) {
                        count++;
                        log.info(StrUtil.format("解析获取到的url为{}", url));
                        String tweetId = url.substring(url.lastIndexOf('/') + 1);
                        tweetUrls.add(TweetUrl.builder()
                                .keyword(searchKey)
                                .tweetId(tweetId)
                                .tweetUrl(url)
                                .tweetCreateTime(tweetCreateTime)
                                .createTime(new Date())
                                .build());
                    }
                    if (!TweetUtil.isWithinIntervalHour(cellInnerDivElement, interval)) {
                        count = size;
                        break;
                    }
                }
                if (count >= size) {
                    break;
                }
                jsExecutor.executeScript(js_bottom);
                Thread.sleep(3000);
                prevScrollHeight = currScrollHeight;
                currScrollHeight = (long) jsExecutor.executeScript("return document.body.scrollHeight;");
                if (currScrollHeight == prevScrollHeight) {
                    log.info("滚到底了");
                    break;
                }
            }
        } catch (Exception e) {
            log.error("获取最新推特Urls时出现错误，请检查响应的代码！！！");
        } finally {
            if (ObjectUtil.isNotEmpty(browser)) {
                browser.quit();
            }
        }
        List<TweetUrl> uniqueTweetUrls = CollectionUtil.removeDuplicates(tweetUrls);
        log.info(StrUtil.format("搜索关键词{}，一共获取{}条url。去重后还有{}条url", searchKey, tweetUrls.size(), uniqueTweetUrls.size()));
        return uniqueTweetUrls;
    }

    @Override
    public Long searchStorePass24HTweet(String searchKey) {
        return searchStoreTweet(searchKey, true);
    }

    @Override
    public Long initTweetContentByNewKeyword(String searchKey) {
        return searchStoreTweet(searchKey, false);
    }

    private Long searchStoreTweet(String searchKey, boolean searchPast24H) {
        WebDriver webDriver = null;
        List<String> urls = new ArrayList<>();
        try {
            urls = this.getLatestTweetUrls();
            if (CollUtil.isEmpty(urls)) {
                return 0L;
            }
            webDriver = tweetLoginService.loginWithRandomAccount();
            for (int i = 0; i < urls.size(); i++) {
                if (StrUtil.isBlank(urls.get(i))) {
                    continue;
                }
                TweetContent tweetContent = this.queryTweetContentByUrl(webDriver, urls.get(i));
                if (ObjectUtil.isEmpty(tweetContent)) {
                    continue;
                }
                tweetContent.getTweetBaseContent().setSearchKey(searchKey);
                tweetContent.getTweetBaseContent().setLabel("latest");
                log.info("一共有{}条数据，目前收集到第{}条数据，收集到的数据为{}", urls.size(), i, tweetContent);
                this.insertTweetContent(tweetContent);
            }
        } catch (Exception e) {
            log.error("收集tweet信息的时候出错，{}", e);
        } finally {
            if (ObjectUtil.isNotEmpty(webDriver)) {
                webDriver.quit();
            }
        }
        if (CollUtil.isEmpty(urls)) {
            return 0L;
        } else {
            return (long) urls.size();
        }
    }

    private List<String> getLatestTweetUrls() {
        return null;
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
}
