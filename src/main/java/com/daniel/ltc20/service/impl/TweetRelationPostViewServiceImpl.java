package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetRelationPostViewDao;
import com.daniel.ltc20.domain.TweetRelationPostView;
import com.daniel.ltc20.service.TweetRelationPostViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetRelationPostViewServiceImpl implements TweetRelationPostViewService {
    @Autowired
    private TweetRelationPostViewDao tweetRelationPostViewDao;


    @Override
    public void insert(List<TweetRelationPostView> list, String tweetId) {
        if (CollUtil.isEmpty(list) || StrUtil.isBlank(tweetId)) {
            return;
        }
        List<TweetRelationPostView> tweetRelationPostViews = tweetRelationPostViewDao.queryTweetRelationPostViewByTweetId(tweetId);
        if (CollUtil.isEmpty(tweetRelationPostViews)) {
            return;
        }
        tweetRelationPostViewDao.insertTweetRelationPostViews(list);
    }
}
