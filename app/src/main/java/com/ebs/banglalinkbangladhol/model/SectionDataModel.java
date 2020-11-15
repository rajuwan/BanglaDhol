package com.ebs.banglalinkbangladhol.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pratap.kesaboyina on 30-11-2015.
 */
public class SectionDataModel {

    private String catCode;
    private String headerTitle;
    private int contentType;
    private int contentViewType;
    ArrayList<HashMap<String, String>> menuItems;

    public SectionDataModel() {

    }

    public SectionDataModel(String headerTitle, ArrayList<HashMap<String, String>> menuItems) {
        this.headerTitle = headerTitle;
        this.menuItems = menuItems;
    }

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getContentViewType() {
        return contentViewType;
    }

    public void setContentViewType(int contentViewType) {
        this.contentViewType = contentViewType;
    }

    public ArrayList<HashMap<String, String>> getSectionData() {
        return menuItems;
    }

    public void setSectionData(ArrayList<HashMap<String, String>> menuItems) {
        this.menuItems = menuItems;
    }


}
