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
public class TweetRelationPostView {
    private Integer id;
    private String tweetId;
    private Date collectDate;
    private Long viewNumber;
    private Date createTime;
    private Date modifyTime;
}