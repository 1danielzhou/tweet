package com.daniel.ltc20.job;

import com.daniel.ltc20.utils.BrowserUtils;
import com.daniel.ltc20.utils.FileUtils;
import com.daniel.ltc20.utils.NumberFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TweetContentJob {
    @EventListener(ApplicationReadyEvent.class)
    public void queryXNumberDomain() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    for (int j = 1; j < 7; j++) {
                        String number_str = NumberFormatUtils.formatNumber(i, j);
                        if (!BrowserUtils.judgeExisted(number_str, "x")) {
                            FileUtils.writeToFile("x_domains.txt", number_str + "." + "x");
                        }
                    }
                }
            }
        }).start();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void queryXCharCharDomain() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    String number_str = NumberFormatUtils.convertTo26Base(i);
                    if (!BrowserUtils.judgeExisted(number_str, "x")) {
                        FileUtils.writeToFile("x_domains.txt", number_str + "." + "x");
                    }
                }
            }
        }).start();
    }
}
