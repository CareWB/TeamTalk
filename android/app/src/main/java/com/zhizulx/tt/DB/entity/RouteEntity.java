package com.zhizulx.tt.DB.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import java.util.ArrayList;
import java.util.List;

/**
 * Entity mapped to table cityInfo.
 */
public class RouteEntity {
    private int day;
    private String cityCode;
    private String routeType;
    private List<String> tags = new ArrayList<>();
    private int startTrafficTool;
    private int endTrafficTool;
    private int startTime;
    private int endTime;
    private List<DayRouteEntity> dayRouteEntityList = new ArrayList<>();

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public RouteEntity() {
    }

    public RouteEntity(int day, String cityCode, String routeType, List<String> tags,
                       int startTrafficTool, int endTrafficTool, int startTime, int endTime,
                       List<DayRouteEntity> dayRouteEntityList) {
        this.day = day;
        this.cityCode = cityCode;
        this.routeType = routeType;
        this.tags.addAll(tags);
        this.startTrafficTool = startTrafficTool;
        this.endTrafficTool = endTrafficTool;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayRouteEntityList.addAll(dayRouteEntityList);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getCityCode() {
        return cityCode;
    }
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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

    public int getStartTrafficTool() {
        return startTrafficTool;
    }

    public void setStartTrafficTool(int startTrafficTool) {
        this.startTrafficTool = startTrafficTool;
    }

    public int getEndTrafficTool() {
        return endTrafficTool;
    }

    public void setEndTrafficTool(int endTrafficTool) {
        this.endTrafficTool = endTrafficTool;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public List<DayRouteEntity> getDayRouteEntityList() {
        return dayRouteEntityList;
    }

    public void setDayRouteEntityList(List<DayRouteEntity> dayRouteEntityList) {
        this.dayRouteEntityList.clear();
        this.dayRouteEntityList.addAll(dayRouteEntityList);
    }
}