package com.daniel.ltc20.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetBaseContentDao;
import com.daniel.ltc20.domain.TweetBaseContent;
import com.daniel.ltc20.service.TweetBaseContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class TweetBaseContentServiceImpl implements TweetBaseContentService {
    @Autowired
    private TweetBaseContentDao tweetBaseContentDao;

    @Override
    public void insertOrUpdate(TweetBaseContent tweetBaseContent) {
        if (ObjectUtil.isEmpty(tweetBaseContent) || StrUtil.isBlank(tweetBaseContent.getTweetId())) {
            return;
        }
        try {
            TweetBaseContent tweet = tweetBaseContentDao.queryTweetBaseContentByTweetId(tweetBaseContent.getTweetId());
            if (ObjectUtil.isNotEmpty(tweet)) {
                tweetBaseContentDao.update(TweetBaseContent
                        .builder()
                        .id(tweet.getId())
                        .latestViewNumber(tweetBaseContent.getLatestViewNumber())
                        .modifyTime(new Date())
                        .build());
                log.info("{}的tweetBaseContent已经存在，更新latestViewNumber：{}->{}", tweet.getTweetId(), tweet.getLatestViewNumber(), tweetBaseContent.getLatestViewNumber());
            } else {
                tweetBaseContentDao.insertTweetBaseContent(tweetBaseContent);
                log.info("{}的tweetBaseContent是新纪录，插入成功", tweetBaseContent.getTweetId());
            }
        } catch (Exception e) {
            log.error("插入数据失败，{}", tweetBaseContent);
        }
    }
}