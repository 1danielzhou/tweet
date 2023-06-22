package com.daniel.ltc20.controller;

import com.daniel.ltc20.domain.TweetAccount;
import com.daniel.ltc20.service.TweetAccountService;
import com.daniel.ltc20.service.TweetLoginService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TweetAccountService tweetAccountService;
    @Autowired
    private TweetLoginService tweetLoginService;

    @RequestMapping(value = "/testLog", method = RequestMethod.GET)
    public void testLog() {
        TweetAccount tweetAccount = tweetAccountService.getRandomTweetAccount();
        WebDriver browser = tweetLoginService.login(tweetAccount);
        browser.quit();
        System.out.println();
    }
}
