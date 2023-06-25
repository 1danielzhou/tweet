package com.daniel.ltc20.controller;

import com.daniel.ltc20.domain.TweetSearchKeyword;
import com.daniel.ltc20.service.TweetSearchKeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tweet/keyword")
public class TweetSearchKeywordController {
    @Autowired
    private TweetSearchKeywordService tweetSearchKeywordService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(String searchKey) {
        log.info("新增一个searchKey,{}", searchKey);
        tweetSearchKeywordService.insertKeyword(searchKey);
        return "插入成功";
    }

    @RequestMapping(value = "/select/all", method = RequestMethod.GET)
    public List<TweetSearchKeyword> collectData() {
        return tweetSearchKeywordService.queryAllTweetSearchKeyword();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String add(Integer id) {
        tweetSearchKeywordService.deleteById(id);
        return "删除成功";
    }
}
