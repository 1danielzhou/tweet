package com.daniel.tweet.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TweetBaseContentDao {
    List<String> findAllUniqueUserIds();
}
