package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.daniel.ltc20.dao.TweetViewsDailyDao;
import com.daniel.ltc20.domain.TweetViewsDaily;
import com.daniel.ltc20.service.TweetViewDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TweetViewDailyServiceImpl implements TweetViewDailyService {
    @Autowired
    private TweetViewsDailyDao tweetViewsDailyDao;

    @Override
    public void insert(TweetViewsDaily tweetViewsDaily) {
        if (ObjectUtil.isEmpty(tweetViewsDaily)) {
            return;
        }
        List<TweetViewsDaily> tweetViewsDailyByConditions = tweetViewsDailyDao.getTweetViewsDailyByConditions(TweetViewsDaily.builder()
                .tweetId(tweetViewsDaily.getTweetId())
                .viewDate(tweetViewsDaily.getViewDate())
                .build());
        if (CollUtil.isNotEmpty(tweetViewsDailyByConditions)) {
            log.info("数据已经插入,直接返回，{}", tweetViewsDailyByConditions);
            return;
        }
        log.info("塞入数据，{}", tweetViewsDaily);
        tweetViewsDailyDao.insertTweetViewsDaily(tweetViewsDaily);
    }
}
