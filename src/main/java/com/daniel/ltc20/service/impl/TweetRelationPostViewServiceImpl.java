package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetRelationPostViewDao;
import com.daniel.ltc20.domain.TweetRelationPostView;
import com.daniel.ltc20.service.TweetRelationPostViewService;
import com.daniel.ltc20.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TweetRelationPostViewServiceImpl implements TweetRelationPostViewService {
    @Autowired
    private TweetRelationPostViewDao tweetRelationPostViewDao;


    @Override
    public void insertOrUpdate(List<TweetRelationPostView> list, String tweetId) {
        if (CollUtil.isEmpty(list) || StrUtil.isBlank(tweetId)) {
            return;
        }
        List<TweetRelationPostView> tweetRelationPostViews = tweetRelationPostViewDao.queryTweetRelationPostViewByTweetId(tweetId);
        if (CollUtil.isNotEmpty(tweetRelationPostViews)) {
            for (TweetRelationPostView tweetRelationPostView : list) {
                TweetRelationPostView relationPostView = findByCollectDate(tweetRelationPostViews, tweetRelationPostView.getCollectDate());
                if (ObjectUtil.isEmpty(relationPostView)) {
                    tweetRelationPostViewDao.insertTweetRelationPostView(tweetRelationPostView);
                } else {
                    tweetRelationPostViewDao.update(TweetRelationPostView.builder()
                            .id(relationPostView.getId())
                            .viewNumber(tweetRelationPostView.getViewNumber())
                            .modifyTime(new Date())
                            .build());
                }
            }
        } else {
            tweetRelationPostViewDao.insertTweetRelationPostViews(list);
        }
    }

    private TweetRelationPostView findByCollectDate(List<TweetRelationPostView> tweetRelationPostViews, Date collectDate) {
        if (CollUtil.isEmpty(tweetRelationPostViews) || ObjectUtil.isEmpty(collectDate)) {
            return null;
        }
        for (TweetRelationPostView tweetRelationPostView : tweetRelationPostViews) {
            if (TimeUtil.extractDate(collectDate).equals(tweetRelationPostView.getCollectDate())) {
                return tweetRelationPostView;
            }
        }
        return null;
    }
}
