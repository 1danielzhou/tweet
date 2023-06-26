package com.daniel.ltc20.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.daniel.ltc20.domain.TweetSearchKeyword;
import com.daniel.ltc20.domain.TweetUrl;
import com.daniel.ltc20.service.TweetContentService;
import com.daniel.ltc20.service.TweetRelationPostViewService;
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

    @Autowired
    private TweetRelationPostViewService tweetRelationPostViewService;

//    @Scheduled(cron = "0 0/1 4-23 * * ?") // 每分钟执行一次
    public void getTweetUrl() {
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywordService.getRandomUnupdatedDataWithinInterval(INTERVAL);
        if (ObjectUtil.isNotEmpty(tweetSearchKeyword)) {
            log.info("关键词{}的Urls在{}小时内未更新，下面尝试进行更新操作,{}", tweetSearchKeyword.getKeyword(), INTERVAL, tweetSearchKeyword);
            List<TweetUrl> tweetUrls = tweetContentService.searchLatestTweetUrls(tweetSearchKeyword.getKeyword(), 5000, 10);
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
                tweetSearchKeywordService.update(TweetSearchKeyword.builder()
                        .id(tweetSearchKeyword.getId())
                        .lastUrlUpdateEndTime(new Date())
                        .modifyTime(new Date())
                        .build());
            }
        } else {
            log.info("没有查询到在过去{}小时未更新的关键词", INTERVAL);
        }
    }

//    @Scheduled(cron = "0 0/1 * * * ?")
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
            } else {
                tweetSearchKeywordService.update(TweetSearchKeyword.builder()
                        .id(tweetSearchKeyword.getId())
                        .lastCollectDataEndTime(new Date())
                        .modifyTime(new Date())
                        .build());
                log.info("收集昨天新发的tweet结束，更新结束的时间");
            }
        } else {
            log.info("没有查询到在过去{}小时未更新前一天数据的关键词", 24);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
    public void refreshHistoricalTweet() {
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywordService.getLastRefreshHistoricalDataFromYesterday();
        if (ObjectUtil.isNotEmpty(tweetSearchKeyword)) {
            log.info("关键词{}的还未更新过去7天的历史数据，下面尝试进行收集,{}", tweetSearchKeyword.getKeyword(), tweetSearchKeyword);
            boolean result = tweetContentService.refreshPast7DaysTweet(tweetSearchKeyword.getKeyword());
            if (!result) {
                log.info("关键词{}更新过去7天的Tweet失败，将last_refresh_historical_data_time还原回：{},以便再次获取", tweetSearchKeyword.getKeyword(), tweetSearchKeyword.getLastRefreshHistoricalDataTime());
                tweetSearchKeywordService.update(TweetSearchKeyword.builder()
                        .id(tweetSearchKeyword.getId())
                        .lastRefreshHistoricalDataTime(tweetSearchKeyword.getLastCollectDataTime())
                        .modifyTime(new Date())
                        .build());
            } else {
                tweetSearchKeywordService.update(TweetSearchKeyword.builder()
                        .id(tweetSearchKeyword.getId())
                        .lastRefreshHistoricalDataEndTime(new Date())
                        .modifyTime(new Date())
                        .build());
            }
        } else {
            log.info("没有查询到在过去{}小时未更新过去7天历史数据的关键词", 24);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
    public void postProcessTweet() {
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywordService.queryUnPostProcessKeyword();
        if (ObjectUtil.isNotEmpty(tweetSearchKeyword)) {
            log.info("查询到一个还没有进行后处理的keyword:{}，下面进行后处理,{}", tweetSearchKeyword.getKeyword(), tweetSearchKeyword);
            try {
                tweetRelationPostViewService.postProcess(tweetSearchKeyword.getKeyword());
                log.info("keyword:{}的post process 结束，", tweetSearchKeyword.getKeyword());
            } catch (Exception e) {
                log.info("post process失败将时间进行还原，{}", tweetSearchKeyword);
                tweetSearchKeywordService.update(
                        TweetSearchKeyword
                                .builder()
                                .id(tweetSearchKeyword.getId())
                                .lastPostProcessTime(tweetSearchKeyword.getLastPostProcessTime())
                                .modifyTime(new Date())
                                .build()
                );
            }
        } else {
            log.info("没有查询到需要post process的关键词");
        }
    }
}
