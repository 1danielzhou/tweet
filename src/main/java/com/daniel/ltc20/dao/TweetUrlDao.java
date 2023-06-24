package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.TweetUrl;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface TweetUrlDao {
    void insertTweetUrls(List<TweetUrl> list);

    void insertTweetUrl(TweetUrl tweetUrl);

    List<TweetUrl> queryTweetUrlsByTweetId(String tweetId);

    List<TweetUrl> queryTweetUrlsByTimeRange(String searchKey, Date from, Date to);
}
