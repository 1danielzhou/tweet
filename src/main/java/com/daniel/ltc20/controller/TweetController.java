package com.daniel.ltc20.controller;

import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.domain.TweetUrl;
import com.daniel.ltc20.service.TweetContentService;
import com.daniel.ltc20.service.TweetSearchKeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        List<TweetUrl> tweetUrls = tweetContentService.searchLatestTweetUrls(searchKey, 5000, 100);
        return "";
    }
}
