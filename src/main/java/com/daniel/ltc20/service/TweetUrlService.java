package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.TweetUrl;

import java.util.Date;
import java.util.List;

public interface TweetUrlService {
    void insertTweetUrls(List<TweetUrl> list);

    List<TweetUrl> getTweetUrlsByTimeRange(String searchKey, Date date, Date date1);
}
