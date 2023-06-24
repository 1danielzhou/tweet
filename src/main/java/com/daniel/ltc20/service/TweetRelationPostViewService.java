package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetRelationPostView;

import java.util.List;

public interface TweetRelationPostViewService {
    void insertOrUpdate(List<TweetRelationPostView> list, String tweetId);
}
