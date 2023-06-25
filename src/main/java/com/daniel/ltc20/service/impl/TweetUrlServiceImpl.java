package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.daniel.ltc20.dao.TweetUrlDao;
import com.daniel.ltc20.domain.TweetUrl;
import com.daniel.ltc20.service.TweetUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public List<TweetUrl> getTweetUrlsByTimeRange(String searchKey, Date from, Date to) {
        if(ObjectUtil.isEmpty(from)||ObjectUtil.isEmpty(to)){
            return new ArrayList<>();
        }
        return tweetUrlDao.queryTweetUrlsByTimeRange(searchKey,from,to);
    }
}
