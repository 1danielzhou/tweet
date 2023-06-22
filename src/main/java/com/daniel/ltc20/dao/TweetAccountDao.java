package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.TweetAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TweetAccountDao {
    List<TweetAccount> getTweetAccountList();
}
