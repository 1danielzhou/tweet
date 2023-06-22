package com.daniel.ltc20.controller;

import com.daniel.ltc20.domain.InscriptionMintInfo;
import com.daniel.ltc20.domain.TweetAccount;
import com.daniel.ltc20.service.InscriptionMintInfoService;
import com.daniel.ltc20.service.TweetAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class InscriptionMintInfoController {
    @Autowired
    private InscriptionMintInfoService inscriptionMintInfoService;
    @Autowired
    private TweetAccountService tweetAccountService;

    @RequestMapping(value = "/testLog", method = RequestMethod.GET)
    public void testLog() {
        List<TweetAccount> allTweetAccount = tweetAccountService.getAllTweetAccount();
        for (TweetAccount tweetAccount : allTweetAccount) {
            System.out.println(tweetAccount);
        }
        System.out.println("----------");
        System.out.println(tweetAccountService.getRandomTweetAccount());
    }

    @RequestMapping(value = "/queryByAddress", method = RequestMethod.GET)
    public List<InscriptionMintInfo> queryByAddress(@RequestParam("address") String address) {
        return inscriptionMintInfoService.queryByAddress(address);
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public InscriptionMintInfo queryById(@RequestParam("id") int id) {
        InscriptionMintInfo inscriptionMintInfo = inscriptionMintInfoService.queryById(id);
        return inscriptionMintInfo;
    }
}
