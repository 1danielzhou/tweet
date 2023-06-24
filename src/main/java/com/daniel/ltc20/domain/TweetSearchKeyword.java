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
public class TweetSearchKeyword {
    private Integer id;
    private String keyword;
    private Integer initStatus;
    private Integer initDataNumber;
    private Integer isDeleted;
    private Integer refreshDataFlag;
    private Date lastUrlUpdateTime;
    private Date lastCollectDataTime;
    private Date lastRefreshHistoricalDataTime;
    private Date createTime;
    private Date modifyTime;
}
