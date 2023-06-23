package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetRelationMention;

import java.util.List;

public interface TweetRelationMentionService {
    void insert(List<TweetRelationMention> list,String tweetId);
}
