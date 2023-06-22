package com.daniel.ltc20.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.daniel.ltc20.domain.TweetAccount;
import com.daniel.ltc20.service.TweetLoginService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class TweetLoginServiceImpl implements TweetLoginService {
    public static final String START_URL = "https://twitter.com/search?q=ltc20&src=typed_query&f=live";


    @Override
    public WebDriver login(TweetAccount tweetAccount) {
        if (ObjectUtil.isEmpty(tweetAccount) || StrUtil.isEmpty(tweetAccount.getNumber()) || StrUtil.isEmpty(tweetAccount.getPassword()) || StrUtil.isEmpty(tweetAccount.getUserId())) {
            log.info("{}的参数不合法，不支持登录操作", tweetAccount);
            return null;
        }
        WebDriver browser = getBrowser();
        if (ObjectUtil.isEmpty(browser)) {
            return null;
        }
        try {
            assert browser != null;
            browser.get(START_URL);
            Thread.sleep(5000);

            if (isUserLoggedIn(browser)) {
                log.info("已经登录了，不需要重复登录");
                return browser;
            }
            if (!inputNumber(browser, tweetAccount.getNumber()) || !inputPassword(browser, tweetAccount.getPassword()) || !inputUserId(browser, tweetAccount.getUserId())) {
                log.info("{}，登录失败", tweetAccount);
                return null;
            }
            log.info("{}，登录成功", tweetAccount);
            return browser;
        } catch (Exception e) {
            log.error("登录失败");
        }
        return browser;
    }

    private boolean inputUserId(WebDriver browser, String userId) {
        if (StrUtil.isEmpty(userId)) {
            log.info(StrUtil.format("userId为空，不支持登录操作"));
            return false;
        }
        try {
            WebElement usernameIdInput = browser.findElement(By.xpath("//input[@autocomplete='on']"));
            usernameIdInput.sendKeys(userId);
            WebElement loginButton = browser.findElement(By.xpath("//div[@role='button' and @data-testid='ocfEnterTextNextButton']"));
            loginButton.click();
            Thread.sleep(3000);
        } catch (Exception e) {
            log.info("不需要进行认证，登录成功了");
        }
        return true;
    }

    private boolean inputPassword(WebDriver browser, String password) {
        if (StrUtil.isEmpty(password)) {
            log.info(StrUtil.format("密码为空，不支持登录操作"));
            return false;
        }
        try {
            WebElement passwordInput = browser.findElement(By.xpath("//input[@autocomplete='current-password']"));
            passwordInput.sendKeys(password);
            WebElement nextButton = browser.findElement(By.xpath("//div[@role=\"button\"][@data-testid=\"LoginForm_Login_Button\"]"));
            nextButton.click();
            Thread.sleep(3000);
            return true;
        } catch (Exception e) {
            log.error("出现异常，{}", e);
        }
        return false;
    }

    private boolean inputNumber(WebDriver browser, String number) {
        if (StrUtil.isEmpty(number)) {
            log.info(StrUtil.format("number为空，不支持登录操作"));
            return false;
        }
        try {
            WebElement usernameInput = browser.findElement(By.xpath("//input[@autocomplete='username']"));
            usernameInput.sendKeys(number);
            WebElement nextButton = browser.findElement(By.xpath("//div[@role='button' and @class='css-18t94o4 css-1dbjc4n r-sdzlij r-1phboty r-rs99b7 r-ywje51 r-usiww2 r-2yi16 r-1qi8awa r-1ny4l3l r-ymttw5 r-o7ynqc r-6416eg r-lrvibr r-13qz1uu']"));
            nextButton.click();
            Thread.sleep(3000);
            return true;
        } catch (Exception e) {
            log.error("出现异常，{}", e);
        }
        return false;
    }

    public boolean isUserLoggedIn(WebDriver browser) {
        WebElement username_input = browser.findElement(By.xpath("//input[@autocomplete=\"username\"]"));
        if (ObjectUtil.isEmpty(username_input)) {
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
