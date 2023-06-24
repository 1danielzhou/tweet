package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetSearchKeyword;

import java.util.List;

public interface TweetSearchKeywordService {
    void insertKeyword(String keyword);
    void insert(TweetSearchKeyword tweetSearchKeyword);

    List<TweetSearchKeyword> queryAllTweetSearchKeyword();

    void deleteById(Integer id);

    void updateInfo(String searchKey, Long size);

    boolean setInitStatus(String searchKey);

    boolean setRefreshDataFlag(String searchKey);

    void finishRefresh(String searchKey, Long size);

    TweetSearchKeyword getRandomUnupdatedDataWithinInterval(int i);

    void update(TweetSearchKeyword build);
}
