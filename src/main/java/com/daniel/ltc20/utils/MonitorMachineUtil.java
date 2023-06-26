package com.daniel.ltc20.utils;

import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;

@Slf4j
public class MonitorMachineUtil {
    private static OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static void monitorSystemLoadAverage() {
        try {
            double systemLoadAverage = osBean.getSystemLoadAverage();
            int numProcessors = Runtime.getRuntime().availableProcessors();
            if (systemLoadAverage > numProcessors) {
                do {
                    log.info("服务器的平均负载为{},服务器的核数为{}，服务器压力过大，程序停止执行3分钟", systemLoadAverage, numProcessors);
                    Thread.sleep(3 * 60 * 1000);
                    systemLoadAverage = osBean.getSystemLoadAverage();
                } while ((systemLoadAverage + 1) > numProcessors);
            }
        } catch (Exception e) {
            log.error("监控服务器平均负载出错，{}", e);
        }
    }
}
