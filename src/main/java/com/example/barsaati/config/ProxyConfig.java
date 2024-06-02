package com.example.barsaati.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {
    private String proxyAddress;

    public String getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }
}
