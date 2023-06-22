package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.TweetRelationTopic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TweetRelationTopicDao {
    List<TweetRelationTopic> queryTweetRelationTopicByTweetId(String tweetId);

    void insertTweetRelationTopic(TweetRelationTopic tweetRelationTopic);

    void insertTweetRelationTopics(List<TweetRelationTopic> list);
}