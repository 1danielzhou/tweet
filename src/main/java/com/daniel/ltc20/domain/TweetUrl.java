package com.daniel.ltc20.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TweetUrl {
    private Integer id;
    private String keyword;
    private String tweetId;
    private String tweetUrl;
    private Date tweetCreateTime;
    private Date createTime;
}
