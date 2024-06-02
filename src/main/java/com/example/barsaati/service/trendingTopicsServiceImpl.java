package com.example.barsaati.service;

import com.example.barsaati.config.ProxyConfig;
import com.example.barsaati.exception.LoginException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.barsaati.repo.trendingTopicsRepository;
import com.example.barsaati.entity.trendingTopics;
import com.example.barsaati.config.webDriverLibrary;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.time.Duration;

@Service
public class trendingTopicsServiceImpl implements trendingTopicsService{

    @Value("${app.url}")
    private String appUrl;

    @Value("${app.username}")
    private String username;

    @Value(("${app.password}"))
    private String password;

    @Autowired
    private trendingTopicsRepository trendingTopicsRepository;

    @Autowired
    private ProxyConfig proxyConfig;

    @Autowired
    private webDriverLibrary webDriverLibrary;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public trendingTopics login() throws Exception {

        WebDriver webDriver = webDriverLibrary.getChromeDriver();
        try {
            webDriver.navigate().to(appUrl);

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            // Sign in
            WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='text'][type='text']")));
            usernameInput.sendKeys(username, Keys.ENTER);

            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password'][type='password']")));
            passwordInput.sendKeys(password,Keys.ENTER);

            wait.until(ExpectedConditions.urlContains("home"));

            return scrapeTrends(webDriver);
        }catch (Exception e){
            throw new LoginException("Failed to login",e);
        }finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }

    }


    private trendingTopics scrapeTrends(WebDriver webDriver) throws Exception {
        List<String> trends = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        try{

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div[data-testid='trend']")));

            List<WebElement> webElementList = webDriver.findElements(By.cssSelector("div[data-testid='trend']"));

            for (WebElement element : webElementList) {
                try {
                    // Find all 'div' elements with 'dir=ltr'
                    List<WebElement> ltrDivs = element.findElements(By.cssSelector("div[dir='ltr']"));

                    // The second 'div[dir=ltr]' contains the trend topic
                    if (ltrDivs.size() > 1) {
                        WebElement trendDiv = ltrDivs.get(1);
                        String trendingTopic = trendDiv.findElement(By.cssSelector("span")).getText();

                        trends.add(trendingTopic);
                    }
                } catch (Exception e) {
                    System.err.println("Error retrieving text from trend element: " + e.getMessage());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        trendingTopics trendingTopics = new trendingTopics();

        trendingTopics.setTrends(trends);
        trendingTopics.setLocalDateTime(LocalDateTime.now());
        trendingTopics.setIpAddress(proxyConfig.getProxyAddress());


        trendingTopicsRepository.save(trendingTopics);


        try{
            WebElement accMenu = webDriver.findElement(By.cssSelector("[aria-label = 'Account menu']"));
            accMenu.click();


            WebElement logoutButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href='/logout']")));
            logoutButton.click();

            Actions actions = new Actions(webDriver);
            actions.sendKeys(Keys.ENTER).perform();
        }catch (Exception e){
            throw new LoginException("Failed to logout",e);
        }

        return trendingTopics;
    }


    @Override
    public String getLatestTrendingTopicsAsJson() throws JsonProcessingException {
        trendingTopics latestTrendingTopics = getLatestTrendingTopics();
        return objectMapper.writeValueAsString(latestTrendingTopics);
    }

    private trendingTopics getLatestTrendingTopics() {
        return trendingTopicsRepository.findTopByOrderByLocalDateTimeDesc();
    }

}
