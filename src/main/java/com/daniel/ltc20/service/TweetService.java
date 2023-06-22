package com.daniel.ltc20.service;

import com.daniel.ltc20.model.Tweet;
import org.openqa.selenium.WebDriver;

public interface TweetService {
    public WebDriver login(Tweet tweet);
}
