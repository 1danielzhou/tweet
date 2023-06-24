package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetUrl;
import com.daniel.ltc20.model.TweetContent;
import org.openqa.selenium.WebDriver;

import java.util.List;

public interface TweetContentService {
//    List<String> searchLatestTweetUrls(String searchKey, int size, boolean searchPast24H);

    boolean searchStoreYesterdayTweet(String searchKey);

    void insertTweetContent(TweetContent tweetContent);

    TweetContent queryTweetContentByUrl(WebDriver browser, String tweetUrl);

    List<TweetUrl> searchLatestTweetUrls(String searchKey, int size, int interval);
}
