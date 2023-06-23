package com.daniel.ltc20.job;

import cn.hutool.core.util.ObjectUtil;
import com.daniel.ltc20.domain.TweetSearchKeyword;
import com.daniel.ltc20.service.TweetContentService;
import com.daniel.ltc20.service.TweetSearchKeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TweetContentJob {
    @Autowired
    private TweetSearchKeywordService tweetSearchKeywordService;

    @Autowired
    private TweetContentService tweetContentService;

    @Scheduled(cron = "0/5 * * * * ?") // 每小时执行一次
    public void executeHourlyTask() {
        log.info("{}",new Date());
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywordService.getRandomUnupdatedDataWithinInterval(2);
        if (ObjectUtil.isNotEmpty(tweetSearchKeyword)) {
            List<String> urls = tweetContentService.searchLatestTweetUrls(tweetSearchKeyword.getKeyword(), 5000, 5);
            for (String url : urls) {
                System.out.println(url);
            }
        }
    }
}
