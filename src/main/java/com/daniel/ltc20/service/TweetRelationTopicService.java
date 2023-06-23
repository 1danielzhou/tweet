package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetRelationTopic;

import java.util.List;

public interface TweetRelationTopicService {
    void insert(List<TweetRelationTopic> list, String tweetId);
}
