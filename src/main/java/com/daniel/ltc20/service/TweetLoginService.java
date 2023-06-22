package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetAccount;
import org.openqa.selenium.WebDriver;

public interface TweetLoginService {
    public WebDriver login(TweetAccount tweetAccount);

    public WebDriver loginWithRandomAccount();
}
