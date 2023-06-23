package com.daniel.ltc20.controller;

import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.service.TweetContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/tweet")
public class TweetController {
    @Autowired
    private TweetContentService tweetContentService;

    @RequestMapping(value = "/collectData/searchPast24H", method = RequestMethod.GET)
    public String collectData(String searchKey) {
        log.info("开始收集关键词{}过去24小时所有的推文", searchKey);
        Long size = tweetContentService.searchStorePass24HTweet(searchKey);
        log.info("收集任务结束，关键词{}在过去24小时一共有{}条推文", searchKey, size);
        return StrUtil.format("收集任务结束，关键词{}在过去24小时一共有{}条推文", searchKey, size);
    }

    @RequestMapping(value = "/collectData/init", method = RequestMethod.GET)
    public String init(String searchKey) {
        log.info("新增一个searchKey：{}，初始化历史数据", searchKey);
        Long size = tweetContentService.initTweetContentByNewKeyword(searchKey);
        log.info("初始化历史数据任务结束，关键词{}一共收集到{}条推文", searchKey, size);
        return StrUtil.format("初始化历史数据任务结束，关键词{}一共收集到{}条推文", searchKey, size);
    }
}
