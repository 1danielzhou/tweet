package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.TweetBaseContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TweetBaseContentDao {
    List<TweetBaseContent> getTweetBaseContentList();

    void insertTweetBaseContent(TweetBaseContent tweetBaseContent);
}