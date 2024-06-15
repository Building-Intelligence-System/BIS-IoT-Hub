/*
 * Copyright © 2017 ООО "Первая Мониторинговая Компания".
 * All rights reserved.
 */

package com.device.server.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {

    private String leakDetectionLevel = "DISABLED";

    public String getLeakDetectionLevel() {
        return leakDetectionLevel;
    }

    public void setLeakDetectionLevel(final String leakDetectionLevel) {
        this.leakDetectionLevel = leakDetectionLevel;
    }
}
