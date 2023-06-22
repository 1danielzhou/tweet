package com.daniel.ltc20.service;

import org.openqa.selenium.WebDriver;

import java.util.List;

public interface TweetContentService {
    public List<String> searchLatestTweetUrls(WebDriver browser, String searchKey, int size, boolean searchPast24H);

}
