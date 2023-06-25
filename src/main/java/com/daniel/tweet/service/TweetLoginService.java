package com.daniel.tweet.service;

import com.daniel.tweet.domain.TweetAccount;
import org.openqa.selenium.WebDriver;

public interface TweetLoginService {
    public WebDriver login(TweetAccount tweetAccount);

    public WebDriver loginWithRandomAccount();
}
