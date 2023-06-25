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
public class TweetViewsDaily {
    private Integer id;
    private String keyword;
    private String label;
    private String tweetId;
    private Date viewDate;
    private Long viewCount;
    private Date createTime;
    private Date modifyTime;
}
