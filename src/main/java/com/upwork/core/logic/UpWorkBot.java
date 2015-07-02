package com.upwork.core.logic;

import com.upwork.core.domain.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jsarafajr on 02.07.15.
 */
@Component
public class UpWorkBot {

    @Autowired
    private UpWorkParser upWorkParser;

    private WebDriver browser = new FirefoxDriver();

    @Autowired
    public UpWorkBot(@Value("${user.login}") String login,
                     @Value("${user.password}") String password,
                     UpWorkParser upWorkParser) {
        this.upWorkParser = upWorkParser;

        authorize(login, password);
        parseJobFeed();
    }

    public void authorize(String login, String password) {
        browser.get("https://www.upwork.com/login");
        browser.findElement(By.id("username")).sendKeys(login);
        browser.findElement(By.id("password")).sendKeys(password);
        browser.findElement(By.id("submit")).click();
    }


    public void parseJobFeed() {
        browser.get("https://www.upwork.com/find-work-home/");

        String source = browser.getPageSource();

        List<Job> jobList = upWorkParser.parseJobs(source);
    }
}
