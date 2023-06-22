package com.daniel.ltc20.service;

import com.daniel.ltc20.model.TweetContent;

import java.util.List;

public interface TweetContentService {
    List<String> searchLatestTweetUrls(String searchKey, int size, boolean searchPast24H);

    void insertTweetContent(TweetContent tweetContent);
}
