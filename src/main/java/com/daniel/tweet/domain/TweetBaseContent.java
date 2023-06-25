package com.daniel.tweet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TweetBaseContent {
    private int id;
    private String tweetId;
    private String searchKey;
    private String label;
    private String userId;
    private String tweetContent;
    private Long latestViewNumber;
    private String tweetUrl;
    private String screenshotUrl;
    private Date tweetContentCreateTime;
    private Date tweetContentCollectTime;
    private Date createTime;
    private Date modifyTime;
}
