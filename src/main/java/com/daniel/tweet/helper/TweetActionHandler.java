package com.daniel.tweet.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.tweet.dao.TweetBaseContentDao;
import com.daniel.tweet.service.TweetLoginService;
import com.daniel.tweet.utils.TweetUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TweetActionHandler {
    @Autowired
    private TweetLoginService tweetLoginService;
    @Autowired
    private TweetBaseContentDao tweetBaseContentDao;


    @EventListener(ApplicationReadyEvent.class)
    public void tweetActions() {
        WebDriver browser = null;
        try {
            browser = tweetLoginService.loginWithRandomAccount();
//            String tweetUrl = "https://twitter.com/Sey_fiii/status/1672293411926228996";
//            allTweetActions(browser, tweetUrl);

            List<String> allUniqueUserIds = tweetBaseContentDao.findAllUniqueUserIds();
            for (String userId : allUniqueUserIds) {
                follow(browser, userId);
            }
        } catch (Exception e) {
            log.error("{}", e);
        } finally {
            if (ObjectUtil.isNotEmpty(browser)) {
                browser.quit();
            }
        }
    }

    private void allTweetActions(WebDriver browser, String tweetUrl) throws InterruptedException {
        try {
            browser.get(tweetUrl);
            Thread.sleep(3000);
            Integer index = TweetUtil.locateIndex(browser, tweetUrl);
            log.info("该消息位于这个页面的第{}个推文", index);
            replyTweet(browser, index);
            reTweet(browser, index);
            like(browser, index);
            bookmark(browser, index);
        } catch (Exception e) {
            log.info("tweet action出现了一点问题");
        }
    }

    private void follow(WebDriver browser, String userId) {
        try {
            String userUrl = StrUtil.format("https://twitter.com/{}", userId);
            browser.get(userUrl);
            Thread.sleep(3000);

            String followXPath = "//div[@role=\"button\"][@class=\"css-18t94o4 css-1dbjc4n r-42olwf r-sdzlij r-1phboty r-rs99b7 r-2yi16 r-1qi8awa r-1ny4l3l r-ymttw5 r-o7ynqc r-6416eg r-lrvibr\"]";
            WebElement followButton = browser.findElement(By.xpath(followXPath));
            followButton.click();
            Thread.sleep(1000);

            log.info("关注{}成功！！！", userId);
        } catch (Exception e) {
            log.info("关注{}失败！！！也许是已经关注了", userId);
        }
    }

    private void bookmark(WebDriver browser, Integer index) {
        try {
            String bookmarkXPath = StrUtil.format("//div[@data-testid=\"cellInnerDiv\"][{}]//div[@data-testid=\"bookmark\"]", index);
            WebElement likeButton = browser.findElement(By.xpath(bookmarkXPath));
            likeButton.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            log.info("已经点击bookmark了");
        }
    }

    private void like(WebDriver browser, Integer index) {
        try {
            String likeXPath = StrUtil.format("//div[@data-testid=\"cellInnerDiv\"][{}]//div[@data-testid=\"like\"]", index);
            WebElement likeButton = browser.findElement(By.xpath(likeXPath));
            likeButton.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            log.info("已经点击like了");
        }
    }

    private void reTweet(WebDriver browser, Integer index) {
        try {
            String xPath = StrUtil.format("//div[@data-testid=\"cellInnerDiv\"][{}]//div[@data-testid=\"retweet\"]", index);
            WebElement reTweetButton = browser.findElement(By.xpath(xPath));
            reTweetButton.click();
            Thread.sleep(1000);
            String retweetConfirmXPath = StrUtil.format("//div[@data-testid=\"retweetConfirm\"]");
            WebElement retweetConfirmButton = browser.findElement(By.xpath(retweetConfirmXPath));
            retweetConfirmButton.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            log.info("该推文已经点击retweet了");
        }
    }

    private void replyTweet(WebDriver browser, Integer index) {
        try {
            String xpath = StrUtil.format("//div[@data-testid=\"cellInnerDiv\"][{}]//div[@data-testid=\"reply\"]", index);
            WebElement button = browser.findElement(By.xpath(xpath));
            button.click();
            Thread.sleep(2000);
            String boxTextXPath = "//div[@data-testid=\"tweetTextarea_0\"]";
            WebElement commentBoxText = browser.findElement(By.xpath(boxTextXPath));
            commentBoxText.sendKeys("哈哈哈");
            Thread.sleep(1000);
            String replyButtonXPath = StrUtil.format("//div[@data-testid=\"tweetButton\"]");
            WebElement replyButton = browser.findElement(By.xpath(replyButtonXPath));
            replyButton.click();
            Thread.sleep(2000);

            checkReplyExist(browser);
        } catch (Exception e) {
            log.info("已经回复了");
        }
    }

    private static void checkReplyExist(WebDriver browser) {
        try {
            String alreadyExistXPath = StrUtil.format("//div[@role=\"status\"][@aria-live=\"assertive\"]");
            browser.findElement(By.xpath(alreadyExistXPath));

            String closeXPath = "//div[@data-testid=\"app-bar-close\"]";
            WebElement closeButton = browser.findElement(By.xpath(closeXPath));
            closeButton.click();
            Thread.sleep(2000);

            String confirmationSheetCancelXPath = "//div[@data-testid=\"confirmationSheetCancel\"]";
            WebElement confirmationSheetCancelButton = browser.findElement(By.xpath(confirmationSheetCancelXPath));
            confirmationSheetCancelButton.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            log.info("没有重复回复，回复成功");
        }
    }
}
