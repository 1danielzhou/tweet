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
public class TweetRelationMention {
    private Integer id;
    private String tweetId;
    private String tweetMention;
    private Date createTime;
}