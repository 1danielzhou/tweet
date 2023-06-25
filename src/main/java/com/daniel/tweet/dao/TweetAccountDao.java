package com.daniel.tweet.dao;

import com.daniel.tweet.domain.TweetAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TweetAccountDao {
    List<TweetAccount> getTweetAccountList();
}
