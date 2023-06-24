package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetRelationMentionDao;
import com.daniel.ltc20.domain.TweetRelationMention;
import com.daniel.ltc20.service.TweetRelationMentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetRelationMentionServiceImpl implements TweetRelationMentionService {
    @Autowired
    private TweetRelationMentionDao tweetRelationMentionDao;

    @Override
    public void insertOrUpdate(List<TweetRelationMention> list, String tweetId) {
        if (CollUtil.isEmpty(list) || StrUtil.isBlank(tweetId)) {
            return;
        }
        List<TweetRelationMention> tweetRelationMentions = tweetRelationMentionDao.queryTweetRelationMentionByTweetId(tweetId);
        if (CollUtil.isNotEmpty(tweetRelationMentions)) {
            return;
        }
        tweetRelationMentionDao.insertTweetRelationMentions(list);
    }
}
