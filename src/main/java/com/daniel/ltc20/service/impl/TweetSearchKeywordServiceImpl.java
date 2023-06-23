package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetSearchKeywordDao;
import com.daniel.ltc20.domain.TweetSearchKeyword;
import com.daniel.ltc20.service.TweetSearchKeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TweetSearchKeywordServiceImpl implements TweetSearchKeywordService {
    @Autowired
    private TweetSearchKeywordDao tweetSearchKeywordDao;

    @Override
    public void insertKeyword(String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return;
        }
        TweetSearchKeyword searchKeyword = TweetSearchKeyword
                .builder()
                .keyword(keyword)
                .initStatus(0)
                .isDeleted(0)
                .createTime(new Date())
                .modifyTime(new Date())
                .build();
        this.insert(searchKeyword);
    }

    @Override
    public void insert(TweetSearchKeyword tweetSearchKeyword) {
        if (ObjectUtil.isEmpty(tweetSearchKeyword) || StrUtil.isBlank(tweetSearchKeyword.getKeyword())) {
            return;
        }
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywordsByKeyword(tweetSearchKeyword.getKeyword());
        if (CollUtil.isNotEmpty(tweetSearchKeywords)) {
            return;
        }
        tweetSearchKeywordDao.insertTweetSearchKeyword(tweetSearchKeyword);
    }

    @Override
    public List<TweetSearchKeyword> queryAllTweetSearchKeyword() {
        return tweetSearchKeywordDao.queryTweetSearchKeywords();
    }

    @Override
    public void deleteById(Integer id) {
        if(ObjectUtil.isEmpty(id)){
            return;
        }
        tweetSearchKeywordDao.deleteById(id);
    }
}
