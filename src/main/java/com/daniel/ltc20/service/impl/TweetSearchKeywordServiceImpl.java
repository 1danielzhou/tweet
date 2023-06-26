package com.daniel.ltc20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.dao.TweetSearchKeywordDao;
import com.daniel.ltc20.domain.TweetSearchKeyword;
import com.daniel.ltc20.service.TweetSearchKeywordService;
import com.daniel.ltc20.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
                .refreshDataFlag(0)
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
        if (ObjectUtil.isEmpty(id)) {
            return;
        }
        tweetSearchKeywordDao.deleteById(id);
    }

    @Override
    public void updateInfo(String searchKey, Long size) {
        if (ObjectUtil.isEmpty(searchKey) || ObjectUtil.isEmpty(size)) {
            return;
        }
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywordsByKeyword(searchKey);
        if (CollUtil.isEmpty(tweetSearchKeywords)) {
            return;
        }
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywords.get(0);
        Integer initDataNumber = tweetSearchKeyword.getInitDataNumber();
        if (ObjectUtil.isEmpty(initDataNumber)) {
            initDataNumber = 0;
        }
        tweetSearchKeywordDao.update(TweetSearchKeyword
                .builder()
                .id(tweetSearchKeyword.getId())
                .initDataNumber(size.intValue() + initDataNumber)
                .initStatus(2)
                .modifyTime(new Date())
                .build());
    }

    @Override
    public boolean setInitStatus(String searchKey) {
        if (StrUtil.isBlank(searchKey)) {
            return false;
        }
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywordsByKeyword(searchKey);
        if (CollUtil.isEmpty(tweetSearchKeywords)) {
            return false;
        }
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywords.get(0);
        if (tweetSearchKeyword.getInitStatus() != 0) {
            return false;
        }
        tweetSearchKeywordDao.update(TweetSearchKeyword
                .builder()
                .id(tweetSearchKeyword.getId())
                .initStatus(1)
                .modifyTime(new Date())
                .build());
        return true;
    }

    @Override
    public boolean setRefreshDataFlag(String searchKey) {
        if (StrUtil.isBlank(searchKey)) {
            return false;
        }
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywordsByKeyword(searchKey);
        if (CollUtil.isEmpty(tweetSearchKeywords)) {
            return false;
        }
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywords.get(0);
        if (tweetSearchKeyword.getRefreshDataFlag() != 0) {
            return false;
        }
        tweetSearchKeywordDao.update(TweetSearchKeyword
                .builder()
                .id(tweetSearchKeyword.getId())
                .refreshDataFlag(1)
                .modifyTime(new Date())
                .build());
        return true;
    }

    @Override
    public void finishRefresh(String searchKey, Long size) {
        if (ObjectUtil.isEmpty(searchKey) || ObjectUtil.isEmpty(size)) {
            return;
        }
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywordsByKeyword(searchKey);
        if (CollUtil.isEmpty(tweetSearchKeywords)) {
            return;
        }
        TweetSearchKeyword tweetSearchKeyword = tweetSearchKeywords.get(0);
        Integer initDataNumber = tweetSearchKeyword.getInitDataNumber();
        if (ObjectUtil.isEmpty(initDataNumber)) {
            initDataNumber = 0;
        }
        tweetSearchKeywordDao.update(TweetSearchKeyword
                .builder()
                .id(tweetSearchKeyword.getId())
                .initDataNumber(size.intValue() + initDataNumber)
                .refreshDataFlag(2)
                .modifyTime(new Date())
                .build());
    }

    @Override
    public TweetSearchKeyword getRandomUnupdatedDataWithinInterval(int interval) {
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywords();
        if (CollUtil.isEmpty(tweetSearchKeywords)) {
            return null;
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        List<TweetSearchKeyword> unupdatedKeywords = new ArrayList<>();

        for (TweetSearchKeyword keyword : tweetSearchKeywords) {
            try {
                LocalDateTime modifyDateTime = keyword.getLastUrlUpdateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                Duration duration = Duration.between(modifyDateTime, currentDateTime);
                long secondsElapsed = duration.getSeconds();

                if (secondsElapsed > interval * 3600) { // 将interval转换为秒
                    unupdatedKeywords.add(keyword);
                }
            } catch (Exception e) {
                unupdatedKeywords.add(keyword);
            }
        }

        if (CollUtil.isEmpty(unupdatedKeywords)) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(unupdatedKeywords.size());
        TweetSearchKeyword tweetSearchKeyword = unupdatedKeywords.get(randomIndex);

        tweetSearchKeywordDao.update(
                TweetSearchKeyword
                        .builder()
                        .id(tweetSearchKeyword.getId())
                        .lastUrlUpdateTime(new Date())
                        .modifyTime(new Date())
                        .build()
        );
        return tweetSearchKeyword;
    }

    @Override
    public void update(TweetSearchKeyword tweetSearchKeyword) {
        tweetSearchKeywordDao.update(tweetSearchKeyword);
    }

    @Override
    public TweetSearchKeyword getLastCollectedRandomDataFromYesterday() {
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywords();
        if (CollUtil.isEmpty(tweetSearchKeywords)) {
            return null;
        }

        Pair<Date, Date> yesterdayTimeRange = TimeUtil.getYesterdayTimeRange();
        List<TweetSearchKeyword> unupdatedKeywords = new ArrayList<>();
        for (TweetSearchKeyword keyword : tweetSearchKeywords) {
            try {
                if (keyword.getLastCollectDataTime().before(yesterdayTimeRange.getValue())) { // 将interval转换为秒
                    unupdatedKeywords.add(keyword);
                }
            } catch (Exception e) {
                unupdatedKeywords.add(keyword);
            }
        }

        if (CollUtil.isEmpty(unupdatedKeywords)) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(unupdatedKeywords.size());
        TweetSearchKeyword tweetSearchKeyword = unupdatedKeywords.get(randomIndex);

        tweetSearchKeywordDao.update(
                TweetSearchKeyword
                        .builder()
                        .id(tweetSearchKeyword.getId())
                        .lastCollectDataTime(TimeUtil.extractDate(new Date()))
                        .modifyTime(new Date())
                        .build()
        );
        return tweetSearchKeyword;
    }

    @Override
    public TweetSearchKeyword getLastRefreshHistoricalDataFromYesterday() {
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywords();
        if (CollUtil.isEmpty(tweetSearchKeywords)) {
            return null;
        }

        Pair<Date, Date> yesterdayTimeRange = TimeUtil.getYesterdayTimeRange();
        List<TweetSearchKeyword> unupdatedKeywords = new ArrayList<>();
        for (TweetSearchKeyword keyword : tweetSearchKeywords) {
            try {
                if (keyword.getLastRefreshHistoricalDataTime().before(yesterdayTimeRange.getValue())) { // 将interval转换为秒
                    unupdatedKeywords.add(keyword);
                }
            } catch (Exception e) {
                unupdatedKeywords.add(keyword);
            }
        }

        if (CollUtil.isEmpty(unupdatedKeywords)) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(unupdatedKeywords.size());
        TweetSearchKeyword tweetSearchKeyword = unupdatedKeywords.get(randomIndex);

        tweetSearchKeywordDao.update(
                TweetSearchKeyword
                        .builder()
                        .id(tweetSearchKeyword.getId())
                        .lastRefreshHistoricalDataTime(TimeUtil.extractDate(new Date()))
                        .modifyTime(new Date())
                        .build()
        );
        return tweetSearchKeyword;
    }

    @Override
    public TweetSearchKeyword queryUnPostProcessKeyword() {
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordDao.queryTweetSearchKeywords();
        if (CollUtil.isEmpty(tweetSearchKeywords)) {
            return null;
        }

        Pair<Date, Date> todayTimeRange = TimeUtil.getTodayTimeRange();
        List<TweetSearchKeyword> unupdatedKeywords = new ArrayList<>();
        for (TweetSearchKeyword keyword : tweetSearchKeywords) {
            try {
                if (keyword.getLastCollectDataEndTime().after(todayTimeRange.getKey()) &&
                        keyword.getLastRefreshHistoricalDataEndTime().after(todayTimeRange.getKey()) &&
                        keyword.getLastPostProcessTime().before(todayTimeRange.getKey())) {
                    unupdatedKeywords.add(keyword);
                }
            } catch (Exception e) {
                unupdatedKeywords.add(keyword);
            }
        }

        if (CollUtil.isEmpty(unupdatedKeywords)) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(unupdatedKeywords.size());
        TweetSearchKeyword tweetSearchKeyword = unupdatedKeywords.get(randomIndex);

        tweetSearchKeywordDao.update(
                TweetSearchKeyword
                        .builder()
                        .id(tweetSearchKeyword.getId())
                        .lastPostProcessTime(new Date())
                        .modifyTime(new Date())
                        .build()
        );
        return tweetSearchKeyword;
    }
}
