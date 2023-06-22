package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetAccount;

import java.util.List;

public interface TweetAccountService {
    public List<TweetAccount> getAllTweetAccount();

    public TweetAccount getRandomTweetAccount();
}
