package com.example.barsaati.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Random;

@Configuration
public class webDriverLibrary {

    @Autowired
    private ProxyConfig proxyConfig;

    private static final String[] PROXY_LIST = {
            "us-dc.proxymesh.com:31280",
            "us-ca.proxymesh.com:31280",
            "us-il.proxymesh.com:31280",
            "us-ny.proxymesh.com:31280",
            "us-tx.proxymesh.com:31280"
    };

    private String getRandomProxy() {
        Random random = new Random();
        String proxy = PROXY_LIST[random.nextInt(PROXY_LIST.length)];
        return proxy;
    }

    @Bean
    @Scope("prototype")
    public WebDriver getChromeDriver() {
        String proxyAddress = getRandomProxy();
        proxyConfig.setProxyAddress(proxyAddress);
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyAddress);
        ChromeOptions options = new ChromeOptions();
        options.setProxy(proxy);

        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }
}
