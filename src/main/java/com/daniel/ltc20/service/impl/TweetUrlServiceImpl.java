package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.daniel.ltc20.dao.TweetUrlDao;
import com.daniel.ltc20.domain.TweetUrl;
import com.daniel.ltc20.service.TweetUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetUrlServiceImpl implements TweetUrlService {
    @Autowired
    private TweetUrlDao tweetUrlDao;

    @Override
    public void insertTweetUrls(List<TweetUrl> list) {
        for (TweetUrl tweetUrl : list) {
            List<TweetUrl> tweetUrls = tweetUrlDao.queryTweetUrlsByTweetId(tweetUrl.getTweetId());
            if (CollUtil.isNotEmpty(tweetUrls)) {
                continue;
            }
            tweetUrlDao.insertTweetUrl(tweetUrl);
        }
    }
}
