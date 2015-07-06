package com.upwork.core.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by jsarafajr on 02.07.15.
 */
public class Job implements Serializable {
    private String link;
    private String name;
    private String desc;
    private Timestamp date;

    public Job() {
    }

    public Job(String link, String name, String desc) {
        this.link = link;
        this.name = name;
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        return link.equals(job.link);

    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }

    @Override
    public String toString() {
        return "Work{" +
                "link='" + link + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
