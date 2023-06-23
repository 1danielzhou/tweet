package com.daniel.ltc20.controller;

import com.daniel.ltc20.domain.TweetSearchKeyword;
import com.daniel.ltc20.service.TweetSearchKeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ThymeleafController {

    @Autowired
    private TweetSearchKeywordService tweetSearchKeywordService;

    /*
     * http://localhost:9012/012-springboot-thymeleaf/springboot/thymeleaf/index
     * */
    @RequestMapping(value = "/springboot/thymeleaf/index")
    public String index(HttpServletRequest request, Model model) {
        List<TweetSearchKeyword> tweetSearchKeywords = tweetSearchKeywordService.queryAllTweetSearchKeyword();
        model.addAttribute("data", tweetSearchKeywords);
        return "index";
    }
}
