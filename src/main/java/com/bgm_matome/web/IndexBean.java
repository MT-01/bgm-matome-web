package com.bgm_matome.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author 佐藤 猛
 */
@Named
@RequestScoped
public class IndexBean {

    private String url;

    private List<String> videoIdList;

    private String playNow;

    /**
     * Creates a new instance of IndexBean
     */
    public IndexBean() {
    }

    @PostConstruct
    public void init() {
    }

    public void searchIdList() {
        try {
            URL u = new URL(getUrl());
            URLConnection conn = u.openConnection();
            String charset = Arrays.asList(conn.getContentType().split(";")).get(1);
            String encoding = Arrays.asList(charset.split("=")).get(1);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            StringBuffer sb = new StringBuffer();
            String s;
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            String html = sb.toString();
            List<String> list = null;
            String regex = "v=([-\\w]{11})[&\\?]?\"";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(html);
            while (m.find()) {
                if (list == null) {
                    list = new ArrayList<>();
                    this.setPlayNow(m.group(1));
                } else {
                    list.add(m.group(1));
                }
            }
            
            if (list == null) {
                return;
            }

            Set<String> set = new HashSet<String>();
            set.addAll(list);
            List<String> uniqueList = new ArrayList<String>();
            uniqueList.addAll(set);

            this.setVideoIdList(uniqueList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String play() {
        //return "play.xhtml?faces-redirect=true";

        return "play.xhtml";
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the videoIdList
     */
    public List<String> getVideoIdList() {
        return videoIdList;
    }

    /**
     * @param videoIdList the videoIdList to set
     */
    public void setVideoIdList(List<String> videoIdList) {
        this.videoIdList = videoIdList;
    }

    /**
     * @return the playNow
     */
    public String getPlayNow() {
        return playNow;
    }

    /**
     * @param playNow the playNow to set
     */
    public void setPlayNow(String playNow) {
        this.playNow = playNow;
    }

}
