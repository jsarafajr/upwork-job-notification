package com.upwork.core.logic;

import com.upwork.core.domain.Job;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Created by jsarafajr on 02.07.15.
 */
@Component
@EnableScheduling
public class UpWorkBot {
    private final Logger LOG = LogManager.getLogger(this.getClass());

    @Autowired
    private UpWorkParser upWorkParser;
    @Autowired
    private EmailService emailService;

    private WebDriver browser = new FirefoxDriver();

    private DB db = DBMaker.newFileDB(new File("map_storage")).make();

    private String userEmail;

    @Autowired
    public UpWorkBot(@Value("${user.login}") String login,
                     @Value("${user.password}") String password,
                     @Value("${user.email}") String email,
                     UpWorkParser upWorkParser, EmailService emailService) {

        this.upWorkParser = upWorkParser;
        this.emailService = emailService;
        this.userEmail = email;

        authorize(login, password);
        parseJobFeed();
    }

    public void authorize(String login, String password) {
        browser.get("https://www.upwork.com/login");
        browser.findElement(By.id("username")).sendKeys(login);
        browser.findElement(By.id("password")).sendKeys(password);
        browser.findElement(By.id("submit")).click();
    }

    // every minute
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void parseJobFeed() {
        LOG.info("Parsing job...");
        int newJobCount = 0;

        browser.get("https://www.upwork.com/find-work-home/");

        String source = browser.getPageSource();

        List<Job> jobList = upWorkParser.parseJobs(source);

        ConcurrentNavigableMap<String, Job> map = db.getTreeMap("jobs");

        for (Job job : jobList) {
            if (!map.containsKey(job.getLink())) {
                LOG.info(job);

                StringBuilder emailText = new StringBuilder();

                emailText.append("<a href='https://www.upwork.com" + job.getLink() + "'>");
                emailText.append("<b>" + job.getName() + "</b>");
                emailText.append("</a>");
                emailText.append("<br><br>");
                emailText.append(job.getDesc());
                emailText.append("<br><br>");
                emailText.append(job.getDate());

                emailService.sendHTML("UpWork New Job", emailText.toString(), userEmail);
                map.put(job.getLink(), job);
                newJobCount++;
            }
        }

        db.commit();
        LOG.info("Found " + newJobCount + " new jobs");
    }
}
