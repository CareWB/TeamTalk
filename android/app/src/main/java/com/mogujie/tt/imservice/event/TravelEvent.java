package com.mogujie.tt.imservice.event;

import com.mogujie.tt.DB.entity.TravelEntity;

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
        TRAVEL_LIST_OK
    }
}
