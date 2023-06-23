package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetSearchKeyword;

import java.util.List;

public interface TweetSearchKeywordService {
    void insertKeyword(String keyword);
    void insert(TweetSearchKeyword tweetSearchKeyword);

    List<TweetSearchKeyword> queryAllTweetSearchKeyword();

    void deleteById(Integer id);
}
