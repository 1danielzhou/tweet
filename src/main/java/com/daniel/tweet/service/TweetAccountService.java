package com.daniel.tweet.service;

import com.daniel.tweet.domain.TweetAccount;

import java.util.List;

public interface TweetAccountService {
    public List<TweetAccount> getAllTweetAccount();

    public TweetAccount getRandomTweetAccount();
}
