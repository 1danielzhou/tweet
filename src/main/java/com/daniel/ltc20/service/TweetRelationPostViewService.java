package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetRelationPostView;

import java.util.List;

public interface TweetRelationPostViewService {
    void insert(List<TweetRelationPostView> list,String tweetId);
}
