package com.daniel.tweet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TweetAccount {
    private String id;
    private String number;
    private String password;
    private String userId;
}
