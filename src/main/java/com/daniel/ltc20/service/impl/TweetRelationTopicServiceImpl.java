package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetRelationTopicDao;
import com.daniel.ltc20.domain.TweetRelationTopic;
import com.daniel.ltc20.service.TweetRelationTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetRelationTopicServiceImpl implements TweetRelationTopicService {
    @Autowired
    private TweetRelationTopicDao tweetRelationTopicDao;


    @Override
    public void insert(List<TweetRelationTopic> list, String tweetId) {
        if (CollUtil.isEmpty(list) || StrUtil.isBlank(tweetId)) {
            return;
        }
        List<TweetRelationTopic> tweetRelationTopics = tweetRelationTopicDao.queryTweetRelationTopicByTweetId(tweetId);
        if (CollUtil.isNotEmpty(tweetRelationTopics)) {
            return;
        }
        tweetRelationTopicDao.insertTweetRelationTopics(list);
    }
}
