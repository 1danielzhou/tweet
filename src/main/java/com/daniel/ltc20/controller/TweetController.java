package com.daniel.ltc20.controller;

import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.service.TweetContentService;
import com.daniel.ltc20.service.TweetSearchKeywordService;
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
    @Autowired
    private TweetSearchKeywordService tweetSearchKeywordService;

    @RequestMapping(value = "/collectData/searchPast24H", method = RequestMethod.GET)
    public String collectData(String searchKey) {
        if (!tweetSearchKeywordService.setRefreshDataFlag(searchKey)) {
            log.info("设置标志位失败，{}正在进行刷新操作，或者刷新操作已经结束", searchKey);
            return StrUtil.format("{}正在进行刷新操作，或者刷新操作已经结束", searchKey);
        }
//        log.info("开始收集关键词{}过去24小时所有的推文", searchKey);
//        Long size = tweetContentService(searchKey);
//        log.info("收集任务结束，关键词{}在过去24小时一共有{}条推文", searchKey, size);
//        tweetSearchKeywordService.finishRefresh(searchKey, size);
//        return StrUtil.format("收集任务结束，关键词{}在过去24小时一共有{}条推文", searchKey, size);
        return "";
    }
}
