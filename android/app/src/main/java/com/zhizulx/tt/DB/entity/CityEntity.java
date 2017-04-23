package com.zhizulx.tt.DB.entity;

import java.util.ArrayList;
import java.util.List;

public class CityEntity {
    private String cityCode;
    private String name;
    private int icon;
    private List<String> picList = new ArrayList<>();
    private String discription;
    private int select;

    public CityEntity() {
    }

    public CityEntity(String cityCode, String name, int icon, List<String> picList, String discription) {
        this.cityCode = cityCode;
        this.name = name;
        this.icon = icon;
        this.picList.addAll(picList);
        this.discription = discription;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setPicList(List<String> picList) {
        this.picList.clear();
        this.picList.addAll(picList);
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDiscription() {
        return discription;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getSelect() {
        return select;
    }
}
