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
    public void querySatsDomain() {
        String sats_str = "https://unisat.io/brc20-api-v2/inscriptions/category/sats/search/v2?name={}.sats&limit=32&start=0";
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    for (int j = 1; j < 7; j++) {
                        String number_str = NumberFormatUtils.formatNumber(i, j);
                        if (!BrowserUtils.judgeDomainExist("sats", sats_str, number_str)) {
                            FileUtils.writeToFile("domains.txt", number_str + "." + "sats");
                        }
                    }
                }
            }
        }).start();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void queryUniSatsDomain() {
        String unisats_str = "https://unisat.io/brc20-api-v2/inscriptions/category/unisat/search/v2?name={}.unisat&limit=32&start=0";
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 14; i < 1000000; i++) {
                    for (int j = 0; j < 7; j++) {
                        String number_str = NumberFormatUtils.formatNumber(i, j);
                        if (!BrowserUtils.judgeDomainExist("unisat", unisats_str, number_str)) {
                            System.out.println(number_str);
                            FileUtils.writeToFile("domains.txt", number_str + "." + "unisat");
                        }
                    }
                }
            }
        }).start();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void querySatsCharDomain() {
        String sats_str = "https://unisat.io/brc20-api-v2/inscriptions/category/sats/search/v2?name={}.sats&limit=32&start=0";
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    String number_str = NumberFormatUtils.convertTo26Base(i);
                    if (!BrowserUtils.judgeDomainExist("sats", sats_str, number_str)) {
                        FileUtils.writeToFile("domains.txt", number_str + "." + "sats");
                    }
                }
            }
        }).start();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void queryUniSatsCharDomain() {
        String unisats_str = "https://unisat.io/brc20-api-v2/inscriptions/category/unisat/search/v2?name={}.unisat&limit=32&start=0";
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    String number_str = NumberFormatUtils.convertTo26Base(i);
                    if (!BrowserUtils.judgeDomainExist("unisat", unisats_str, number_str)) {
                        System.out.println(number_str);
                        FileUtils.writeToFile("domains.txt", number_str + "." + "unisat");
                    }
                }
            }
        }).start();
    }
}
