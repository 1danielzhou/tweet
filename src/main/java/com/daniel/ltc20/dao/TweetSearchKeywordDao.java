package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.TweetSearchKeyword;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TweetSearchKeywordDao {
    void insertTweetSearchKeyword(TweetSearchKeyword tweetSearchKeyword);

    void insertTweetSearchKeywords(List<TweetSearchKeyword> list);

    List<TweetSearchKeyword> queryTweetSearchKeywords();

    List<TweetSearchKeyword> queryTweetSearchKeywordsByKeyword(String keyword);
}
