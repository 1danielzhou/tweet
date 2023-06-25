package com.daniel.tweet.service.impl;

import com.daniel.tweet.dao.TweetAccountDao;
import com.daniel.tweet.domain.TweetAccount;
import com.daniel.tweet.service.TweetAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.List;

@Service
public class TweetAccountServiceImpl implements TweetAccountService {
    @Autowired
    private TweetAccountDao tweetAccountDao;

    @Override
    public List<TweetAccount> getAllTweetAccount() {
        List<TweetAccount> tweetAccountList = tweetAccountDao.getTweetAccountList();
        return tweetAccountList;
    }

    @Override
    public TweetAccount getRandomTweetAccount() {
        List<TweetAccount> tweetAccountList = tweetAccountDao.getTweetAccountList();
        if (tweetAccountList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(tweetAccountList.size());
        return tweetAccountList.get(randomIndex);
    }
}
