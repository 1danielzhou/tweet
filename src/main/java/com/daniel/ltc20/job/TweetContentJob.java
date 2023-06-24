package com.daniel.ltc20.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.daniel.ltc20.domain.TweetSearchKeyword;
import com.daniel.ltc20.domain.TweetUrl;
import com.daniel.ltc20.service.TweetContentService;
import com.daniel.ltc20.service.TweetSearchKeywordService;
import com.daniel.ltc20.service.TweetUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TweetContentJob {
    private static final int INTERVAL = 2;
    @Autowired
    private TweetSearchKeywordService tweetSearchKeywordService;

    @Autowired
    private TweetContentService tweetContentService;

    @Autowired
    private TweetUrlService tweetUrlService;

    @Scheduled(cron = "0 0/1 * * * ?") // 每小时执行一次
    public void getTweetUrl() {
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywordService.getRandomUnupdatedDataWithinInterval(INTERVAL);
        if (ObjectUtil.isNotEmpty(tweetSearchKeyword)) {
            log.info("关键词{}的Urls在{}小时内未更新，下面尝试进行更新操作,{}", tweetSearchKeyword.getKeyword(), INTERVAL, tweetSearchKeyword);
            List<TweetUrl> tweetUrls = tweetContentService.searchLatestTweetUrls(tweetSearchKeyword.getKeyword(), 5000, 100);
            if (CollUtil.isEmpty(tweetUrls)) {
                log.info("获取Url失败或者获取为空，将lastUrlUpdateTime还原回：{},以便再次获取", tweetSearchKeyword.getLastUrlUpdateTime());
                tweetSearchKeywordService.update(TweetSearchKeyword.builder()
                        .id(tweetSearchKeyword.getId())
                        .lastUrlUpdateTime(tweetSearchKeyword.getLastUrlUpdateTime())
                        .modifyTime(new Date())
                        .build());
            } else {
                log.info("一共获取到{}个链接，将其插入数据库中", tweetUrls.size());
                tweetUrlService.insertTweetUrls(tweetUrls);
            }
        } else {
            log.info("没有查询到在过去{}小时未更新的关键词", INTERVAL);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?") // 每小时执行一次
    public void searchStoreYesterdayTweet() {
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywordService.getLastCollectedRandomDataFromYesterday();
        if (ObjectUtil.isNotEmpty(tweetSearchKeyword)) {
            log.info("关键词{}的Urls在24小时内未收集最新的Tweet，下面尝试进行收集,{}", tweetSearchKeyword.getKeyword(), tweetSearchKeyword);
            boolean result = tweetContentService.searchStoreYesterdayTweet(tweetSearchKeyword.getKeyword());
            if (!result) {
                log.info("关键词{}收集昨天的Tweet失败，将last_collect_data_time还原回：{},以便再次获取", tweetSearchKeyword.getKeyword(), tweetSearchKeyword.getLastCollectDataTime());
                tweetSearchKeywordService.update(TweetSearchKeyword.builder()
                        .id(tweetSearchKeyword.getId())
                        .lastCollectDataTime(tweetSearchKeyword.getLastCollectDataTime())
                        .modifyTime(new Date())
                        .build());
            }
        } else {
            log.info("没有查询到在过去{}小时未更新的关键词", INTERVAL);
        }
    }
}
