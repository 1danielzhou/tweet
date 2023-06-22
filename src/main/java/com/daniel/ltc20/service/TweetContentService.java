package com.daniel.ltc20.service;

import com.daniel.ltc20.model.TweetContent;
import org.openqa.selenium.WebDriver;

import java.util.List;

public interface TweetContentService {
    List<String> searchLatestTweetUrls(WebDriver browser, String searchKey, int size, boolean searchPast24H);

    void insertTweetContent(TweetContent tweetContent);
}
