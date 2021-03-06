package com.zhizulx.tt.DB.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity mapped to table cityInfo.
 */
public class ConfigEntity {
    private Date startDate;
    private Date endDate;
    private int duration;
    private String startCity;
    private String endCity;
    private String destination;
    private String routeType;
    private List<String> tags = new ArrayList<>();
    private String sentence;
    private String startTrafficNo;
    private String endTrafficNo;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ConfigEntity() {
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartCity() {
        return startCity;
    }
    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }
    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRouteType() {
        return routeType;
    }
    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getStartTrafficNo() {
        return startTrafficNo;
    }

    public void setStartTrafficNo(String startTrafficNo) {
        this.startTrafficNo = startTrafficNo;
    }

    public String getEndTrafficNo() {
        return endTrafficNo;
    }

    public void setEndTrafficNo(String endTrafficNo) {
        this.endTrafficNo = endTrafficNo;
    }
}
