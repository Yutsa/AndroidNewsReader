package com.openclassroomapp.newsreader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Édouard WILLISSECK
 */
public class RSSArticle {
    private String title;
    private String link;
    private Date date;
    private String channelTitle;
    private final DateFormat formatter;

    public RSSArticle(String title, String link, String date) {
        formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        setTitle(title);
        setLink(link);
        setDate(parseDate(date));
    }

    public RSSArticle(String title, String link, String date, String channelTitle) {
        formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        setTitle(title);
        setLink(link);
        setDate(parseDate(date));
        setChannelTitle(channelTitle);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().equals(""))
            throw new IllegalArgumentException("Can't pass an empty string.");
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        if (link == null || link.trim().equals(""))
            throw new IllegalArgumentException("Can't pass an empty string.");
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        if (channelTitle == null || channelTitle.trim().equals(""))
            throw new IllegalArgumentException("Can't pass an empty string.");
        this.channelTitle = channelTitle;
    }

    public Date parseDate(String date) {
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateAsString() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return dateFormat.format(this.date);
    }
}
