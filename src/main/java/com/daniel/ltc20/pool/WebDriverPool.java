package com.daniel.ltc20.pool;

import com.daniel.ltc20.service.TweetLoginService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class WebDriverPool {
    @Autowired
    private TweetLoginService tweetLoginService;

    private static final int POOL_SIZE = 3;
    private List<WebDriver> webDriverPool;
    private boolean[] isWebDriverInUse;

    @PostConstruct
    private void init() {
        webDriverPool = new ArrayList<>();
        isWebDriverInUse = new boolean[POOL_SIZE];
        for (int i = 0; i < POOL_SIZE; i++) {
            WebDriver webDriver = tweetLoginService.loginWithRandomAccount();
            webDriverPool.add(webDriver);
            isWebDriverInUse[i] = false;
        }
    }

    public WebDriver getWebDriver() {
        int index = getRandomAvailableWebDriverIndex();
        if (index != -1) {
            WebDriver webDriver = webDriverPool.get(index);
            markWebDriverAsUsed(index);
            return webDriver;
        }
        return null; // No available WebDriver
    }

    public void releaseWebDriver(WebDriver webDriver) {
        int index = webDriverPool.indexOf(webDriver);
        if (index != -1) {
            markWebDriverAsUnused(index);
        }
    }

    private int getRandomAvailableWebDriverIndex() {
        List<Integer> availableIndexes = new ArrayList<>();
        for (int i = 0; i < isWebDriverInUse.length; i++) {
            if (!isWebDriverInUse[i]) {
                availableIndexes.add(i);
            }
        }
        if (availableIndexes.isEmpty()) {
            return -1; // No available WebDriver
        }
        int randomIndex = new Random().nextInt(availableIndexes.size());
        return availableIndexes.get(randomIndex);
    }

    private void markWebDriverAsUsed(int index) {
        isWebDriverInUse[index] = true;
    }

    private void markWebDriverAsUnused(int index) {
        isWebDriverInUse[index] = false;
    }
}
