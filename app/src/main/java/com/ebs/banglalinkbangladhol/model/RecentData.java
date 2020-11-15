package com.ebs.banglalinkbangladhol.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rajuwan on 15-Apr-17.
 */

public class RecentData {

    public RecentData() {

    }

    public static ArrayList<HashMap<String, String>> recentList = new ArrayList<HashMap<String, String>>();

    public ArrayList<HashMap<String, String>> getRecentList() {

        return recentList;
    }
}
