package com.daniel.ltc20.controller;

import com.daniel.ltc20.service.TweetContentService;
import com.daniel.ltc20.service.TweetLoginService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TweetLoginService tweetLoginService;
    @Autowired
    private TweetContentService tweetContentService;

    @RequestMapping(value = "/testLog", method = RequestMethod.GET)
    public void testLog() throws InterruptedException {
        WebDriver browser = tweetLoginService.loginWithRandomAccount();

        List<String> ltc20 = tweetContentService.searchLatestTweetUrls(browser, "ltc20", 1000, true);
        Thread.sleep(10000);
        browser.quit();
        System.out.println();
    }
}
