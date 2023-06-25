package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.TweetViewsDaily;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface TweetViewsDailyDao {
    void insertTweetViewsDaily(TweetViewsDaily tweetViewsDaily);

    void deleteTweetViewsDailyById(int id);

    void updateTweetViewsDaily(TweetViewsDaily tweetViewsDaily);

    TweetViewsDaily getTweetViewsDailyById(int id);

    TweetViewsDaily getTweetViewsDailyByDate(Date viewDate);

    List<TweetViewsDaily> getTweetViewsDailyByConditions(TweetViewsDaily tweetViewsDaily);
}
