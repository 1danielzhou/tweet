package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetRelationMention;

import java.util.List;

public interface TweetRelationMentionService {
    void insertOrUpdate(List<TweetRelationMention> list, String tweetId);
}
