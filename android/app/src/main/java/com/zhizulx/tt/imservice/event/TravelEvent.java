package com.zhizulx.tt.imservice.event;

import com.zhizulx.tt.DB.entity.TravelEntity;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 */
public class TravelEvent {

    public TravelEntity entity;
    public Event event;

    public TravelEvent(){}
    public TravelEvent(Event e){
        this.event = e;
    }

    public enum Event {
        TRAVEL_LIST_OK,
        TRAVEL_LIST_FAIL,
        CREATE_TRAVEL_OK,
        CREATE_TRAVEL_FAIL,
        DEL_TRAVEL_OK,
        DEL_TRAVEL_FAIL,
        REQ_TRAVEL_ROUTE_OK,
        REQ_TRAVEL_ROUTE_FAIL
    }
}
