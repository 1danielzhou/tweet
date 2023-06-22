package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.TweetRelationMention;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TweetRelationMentionDao {
    List<TweetRelationMention> queryTweetRelationMentionByTweetId(String tweetId);

    void insertTweetRelationMention(TweetRelationMention tweetRelationMention);

    void insertTweetRelationMentions(List<TweetRelationMention> list);
}