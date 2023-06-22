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
public class TweetRelationTopic {
    private Integer id;
    private String tweetId;
    private String tweetTopic;
    private Date createTime;
}