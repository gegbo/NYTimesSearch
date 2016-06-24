package com.example.gegbo.nytimessearch.Models;

import android.util.Log;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gegbo on 6/23/16.
 */

@Parcel
public class Filter {
    String beginDate;
    String endDate;
    String sort;

    HashMap<String,Boolean> newsDesk;

    public Filter() {
        beginDate = "20160608";
        endDate = "20160623";
        sort = "oldest";
        newsDesk = new HashMap<>();
        newsDesk.put("Arts",false);
        newsDesk.put("Fashion",false);
        newsDesk.put("Sports",false);
    }
    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getSort() {
        return sort;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public HashMap<String, Boolean> getNewsDesk() {
        return newsDesk;
    }

    public String getNewsDeskParams() {
        ArrayList<String> validNews = new ArrayList<>();

        for(String key: newsDesk.keySet()) {
            boolean valid = newsDesk.get(key);

            if(valid) {
                validNews.add("\""+key+"\"");
            }
        }

        String newsDeskParams = android.text.TextUtils.join(" ",validNews);
        Log.d("NewsDeskParams",String.format("news_desk:(%s)",newsDeskParams));
        return String.format("news_desk:(%s)",newsDeskParams);
    }
}
