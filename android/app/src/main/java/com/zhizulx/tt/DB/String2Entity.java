package com.zhizulx.tt.DB;

import android.util.Log;

import com.zhizulx.tt.DB.entity.HotelEntity;
import com.zhizulx.tt.DB.entity.SightEntity;
import com.zhizulx.tt.config.DBConstant;
import com.zhizulx.tt.protobuf.IMBaseDefine;

import java.util.List;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 */
public class String2Entity {
    /**----enum 转化接口--*/
    public static HotelEntity getHotelEntity(List<String> listString){
        HotelEntity hotelEntity = new HotelEntity();
        hotelEntity.setPeerId(Integer.valueOf(listString.get(0)));
        hotelEntity.setCityCode(listString.get(1));
        hotelEntity.setName(listString.get(2));
        hotelEntity.setPic(listString.get(3));
        hotelEntity.setStar(Integer.valueOf(listString.get(4)));
        hotelEntity.setTag(listString.get(5));
        hotelEntity.setUrl(listString.get(6));
        hotelEntity.setLongitude(Double.valueOf(listString.get(7)));
        hotelEntity.setLatitude(Double.valueOf(listString.get(8)));
        hotelEntity.setStartTime("00:00");
        hotelEntity.setEndTime("00:00");
        hotelEntity.setDistance(0);
        hotelEntity.setSelect(0);
        hotelEntity.setVersion(0);
        hotelEntity.setStatus(0);
        hotelEntity.setCreated(0);
        hotelEntity.setUpdated(0);
        return hotelEntity;
    }

    public static SightEntity getSightEntity(List<String> listString){
        SightEntity sightEntity = new SightEntity();
        sightEntity.setPeerId(Integer.valueOf(listString.get(0)));
        sightEntity.setCityCode(listString.get(1));
        sightEntity.setName(listString.get(2));
        sightEntity.setPic(listString.get(3));
        sightEntity.setStar(Integer.valueOf(listString.get(4)));
        sightEntity.setTag(listString.get(5));
        sightEntity.setFree(Integer.valueOf(listString.get(6)));
        sightEntity.setMustGo(Integer.valueOf(listString.get(7)));
        sightEntity.setOpenTime(listString.get(8));
        sightEntity.setPlayTime(Integer.valueOf(listString.get(9)));
        sightEntity.setIntroduction(listString.get(10));
        sightEntity.setAddress(listString.get(11));
        sightEntity.setLongitude(Double.valueOf(listString.get(12)));
        sightEntity.setLatitude(Double.valueOf(listString.get(13)));
        sightEntity.setStartTime("00:00");
        sightEntity.setEndTime("00:00");
        sightEntity.setSelect(0);
        sightEntity.setVersion(0);
        sightEntity.setStatus(0);
        sightEntity.setCreated(0);
        sightEntity.setUpdated(0);
        return sightEntity;
    }
}
