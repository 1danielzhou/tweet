package com.daniel.ltc20.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.model.Tweet;
import com.daniel.ltc20.service.TweetService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class TweetServiceImpl implements TweetService {
    public static final String START_URL = "https://twitter.com/search?q=ltc20&src=typed_query&f=live";

    @EventListener(ApplicationReadyEvent.class)
    public void test(){
        login(Tweet.builder().number("+1(213) 464 60 59").password("24Happiness31.").userId("@btcdatasci85020").build());
    }
    @Override
    public WebDriver login(Tweet tweet) {
        if (ObjectUtil.isEmpty(tweet) || StrUtil.isEmpty(tweet.getNumber()) || StrUtil.isEmpty(tweet.getPassword()) || StrUtil.isEmpty(tweet.getUserId())) {
            log.debug("{}的参数不合法，不支持登录操作", tweet);
            return null;
        }
        WebDriver browser = getBrowser();
        if(ObjectUtil.isEmpty(browser)){
            return null;
        }
        try {
            assert browser != null;
            browser.get(START_URL);
            Thread.sleep(5000);

            if(isUserLoggedIn(browser)){
                log.debug("已经登录了，不需要重复登录");
                return browser;
            }

        }catch (Exception e){
            log.error("登录失败");
        }
        return browser;
    }

    public boolean isUserLoggedIn(WebDriver browser) {
        WebElement username_input = browser.findElement(By.xpath("//input[@autocomplete=\"username\"]"));
        if(ObjectUtil.isEmpty(username_input)){
            return true;
        }
        return false;
    }

    private static WebDriver getBrowser() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--start-minimized");
//        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        ArrayList<String> list = new ArrayList<>();
        list.add("enable-automation");
        chromeOptions.setExperimentalOption("excludeSwitches", list);

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            System.setProperty("webdriver.chrome.driver", "E:\\chromdrive\\chromedriver_win32\\chromedriver.exe");
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        } else {
            log.error("Unsupported operating system,当前系统是{}", osName);
            return null;
        }
        WebDriver driver = new ChromeDriver(chromeOptions);
        return driver;
    }
}
