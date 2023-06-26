package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetRelationPostViewDao;
import com.daniel.ltc20.domain.TweetRelationPostView;
import com.daniel.ltc20.domain.TweetViewsDaily;
import com.daniel.ltc20.service.TweetRelationPostViewService;
import com.daniel.ltc20.service.TweetViewDailyService;
import com.daniel.ltc20.utils.MathUtil;
import com.daniel.ltc20.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TweetRelationPostViewServiceImpl implements TweetRelationPostViewService {
    @Autowired
    private TweetRelationPostViewDao tweetRelationPostViewDao;
    @Autowired
    private TweetViewDailyService tweetViewDailyService;

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
                    log.info("{}的PostView是新纪录，插入成功",tweetId);
                } else {
                    tweetRelationPostViewDao.update(TweetRelationPostView.builder()
                            .id(relationPostView.getId())
                            .viewNumber(tweetRelationPostView.getViewNumber())
                            .modifyTime(new Date())
                            .build());
                    log.info("{}的PostView已经存在，更新成功",tweetId);
                }
            }
        } else {
            tweetRelationPostViewDao.insertTweetRelationPostViews(list);
        }
    }

    @Override
    public void postProcess(String keyword) {
        Pair<Date, Date> todayTimeRange = TimeUtil.getTodayTimeRange();
        Pair<Date, Date> yesterdayTimeRange = TimeUtil.getYesterdayTimeRange();
        List<TweetRelationPostView> todayDataList = tweetRelationPostViewDao.selectByKeywordAndCollectDate(keyword, todayTimeRange.getKey());
        List<TweetRelationPostView> yesterdayDataList = tweetRelationPostViewDao.selectByKeywordAndCollectDate(keyword, yesterdayTimeRange.getKey());
        Map<String, TweetRelationPostView> map = covertToMap(yesterdayDataList);
        if (CollUtil.isNotEmpty(todayDataList)) {
            for (TweetRelationPostView todayPostView : todayDataList) {
                TweetRelationPostView yesterdayPostView = map.get(todayPostView.getTweetId());
                TweetViewsDaily tweetViewsDaily = TweetViewsDaily.builder()
                        .keyword(todayPostView.getKeyword())
                        .label("n")
                        .tweetId(todayPostView.getTweetId())
                        .viewDate(yesterdayTimeRange.getKey())
                        .viewCount(todayPostView.getViewNumber())
                        .createTime(new Date())
                        .modifyTime(new Date())
                        .build();
                if (ObjectUtil.isNotEmpty(yesterdayPostView)) {
                    tweetViewsDaily.setViewCount(MathUtil.subtract(todayPostView.getViewNumber(), yesterdayPostView.getViewNumber()));
                    tweetViewsDaily.setLabel("o");
                }
                tweetViewDailyService.insert(tweetViewsDaily);
            }
        }
    }

    private Map<String, TweetRelationPostView> covertToMap(List<TweetRelationPostView> yesterdayDataList) {
        Map<String, TweetRelationPostView> map = new HashMap<>();
        if (CollUtil.isEmpty(yesterdayDataList)) {
            return map;
        }
        for (TweetRelationPostView relationPostView : yesterdayDataList) {
            map.put(relationPostView.getTweetId(), relationPostView);
        }
        return map;
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
