package com.daniel.ltc20.job;

import com.daniel.ltc20.utils.BrowserUtils;
import com.daniel.ltc20.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;

@Slf4j
@Service
public class TweetContentJob {
    @EventListener(ApplicationReadyEvent.class)
    public void queryXCharCharDomain() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String filePath = "all_words.txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        if (!BrowserUtils.judgeExisted(line.trim()+".ord", "txt")) {
                            FileUtils.writeToFile("ord_domains.txt", line.trim() + "." + "ord");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
