package com.upwork.core.logic;

import com.upwork.core.domain.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsarafajr on 02.07.15.
 */
@Component
public class UpWorkParser {

    public List<Job> parseJobs(String html) {
        Document page = Jsoup.parse(html);

        Elements elements = page.getElementById("jsJobResults").getElementsByTag("article");

        List<Job> jobs = new ArrayList<>(elements.size());

        for (Element element : elements) {
            Element jobTitle = element.getElementsByClass("oRowTitle").get(0).getElementsByTag("a").get(0);
            String timestampLong = element.getElementsByClass("oSupportInfo").get(0)
                    .getElementsByClass("jsPosted").get(0).getElementsByTag("span").get(1).attr("data-timestamp");
            timestampLong = timestampLong.substring(0, timestampLong.length() - 1);

            String jobDescription = "";

            try {
                // trying to get full description
                 jobDescription = element.getElementsByClass("oDescription").get(0)
                        .getElementsByClass("jsFull").get(0).getElementsByTag("div").get(0).text();
            } catch (Exception e) {
                jobDescription = element.getElementsByClass("oDescription").get(0).html();
            }

            Job currentJob = new Job();
            currentJob.setLink(jobTitle.attr("href"));
            currentJob.setName(jobTitle.text());
            currentJob.setDesc(jobDescription);
            currentJob.setDate(new Timestamp(Long.valueOf(timestampLong)));
            jobs.add(currentJob);
        }

        return jobs;
    }
}
