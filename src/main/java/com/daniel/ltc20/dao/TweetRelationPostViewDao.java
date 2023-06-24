package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.TweetRelationPostView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TweetRelationPostViewDao {
    List<TweetRelationPostView> queryTweetRelationPostViewByTweetId(String tweetId);

    void insertTweetRelationPostView(TweetRelationPostView tweetRelationPostView);

    void insertTweetRelationPostViews(List<TweetRelationPostView> list);

    void update(TweetRelationPostView tweetRelationPostView);
}