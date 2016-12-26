package com.mogujie.tt.imservice.manager;

import android.util.Log;

import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.DB.entity.TravelEntity;
import com.mogujie.tt.imservice.event.TravelEvent;
import com.mogujie.tt.protobuf.IMBaseDefine;
import com.mogujie.tt.protobuf.IMBuddy;
import com.mogujie.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.mogujie.tt.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class IMTravelManager extends IMManager {
    private Logger logger = Logger.getLogger(IMTravelManager.class);
	private static IMTravelManager inst = new IMTravelManager();
	public static IMTravelManager instance() {
			return inst;
	}

    private IMSocketManager imSocketManager = IMSocketManager.instance();
    private DBInterface dbInterface = DBInterface.instance();

    /**key=> sessionKey*/
    private List<TravelEntity> travelEntityList = new ArrayList<>();
    private TravelEntity mtTravel = new TravelEntity();

    @Override
    public void doOnStart() {}


    // 未读消息控制器，本地是不存状态的
    public void onNormalLoginOk(){
        onLocalLoginOk();
        onLocalNetOk();
    }

    public void onLocalNetOk(){
        reqTravelList();
    }

    public void onLocalLoginOk(){
        logger.i("group#loadFromDb");

/*        if(!EventBus.getDefault().isRegistered(inst)){
            EventBus.getDefault().registerSticky(inst);
        }*/

        // 加载本地group
        List<TravelEntity> localTravelDetailList = dbInterface.loadAllTravel();
        for(TravelEntity travelEntity:localTravelDetailList){
            travelEntityList.add(travelEntity);
        }

        triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_OK));
    }

    @Override
    public void reset() {
        travelEntityList.clear();
    }

    /**
     * 继承该方法实现自身的事件驱动
     * @param event
     */
    public synchronized void triggerEvent(TravelEvent event) {
        EventBus.getDefault().post(event);
    }

    /**-------------------------------分割线----------------------------------*/
    private void reqTravelList() {
        logger.i("1reqTravelList");
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.GetTravelListReq getTravelListReq = IMBuddy.GetTravelListReq
                .newBuilder()
                .setUserId(loginId)
                .build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_TRAVEL_LIST_REQUEST_VALUE;
        imSocketManager.sendRequest(getTravelListReq,sid,cid);
    }

    public void onRspTravelList(IMBuddy.GetTravelTripListRsp getTravelTripListRsp) {
        logger.i("onRepTravelList");
        if (getTravelTripListRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", getTravelTripListRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_FAIL));
            return;
        }

        clearTravelList();
        List<IMBuddy.TravelDetail> travelDetailList =  getTravelTripListRsp.getTravelDetailList();
        if (travelDetailList.size() == 0) {
            logger.e("onRepTravelList#travelCnt:%d",travelDetailList.size());
            triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_OK));
            return;
        }

        List<TravelEntity> needDb = new ArrayList<>();
        for(IMBuddy.TravelDetail travelDetail:travelDetailList){
            TravelEntity travelEntity = ProtoBuf2JavaBean.getTravelEntity(travelDetail);
            travelEntity.setDestinationBK("abc");
            travelEntityList.add(travelEntity);
            needDb.add(travelEntity);
        }
        dbInterface.batchInsertOrUpdateTravel(needDb);
        triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_OK));
    }

    public void reqCreateTravel() {
        int loginId = IMLoginManager.instance().getLoginId();

        IMBuddy.TravelInfo travelInfo = IMBuddy.TravelInfo
                .newBuilder()
                .setPersonNum(mtTravel.getPersonNum())
                .setPlaceFrom(mtTravel.getStartPlace())
                .setPlaceBack(mtTravel.getEndPlace())
                .setPlaceTo(mtTravel.getDestination())
                .setDateFrom(mtTravel.getStartDate())
                .setDateTo(mtTravel.getEndDate())
                .build();

        IMBuddy.TrafficInfo trafficInfo = IMBuddy.TrafficInfo
                .newBuilder()
                .setTravelType(mtTravel.getTrafficWay())
                .setTrafficTimeFrom(mtTravel.getTrafficStartTime())
                .setTrafficTimeTo(mtTravel.getTrafficEndTime())
                .build();

        IMBuddy.PlayInfo playInfo = IMBuddy.PlayInfo
                .newBuilder()
                .setPlayQuality(getPlayQuality(mtTravel.getPlayQuality()))
                .setPlayTimeFrom(mtTravel.getPlayStartTime())
                .setPlayTimeTo(mtTravel.getPlayEndTime())
                .setCityTraffic(mtTravel.getCityTraffic())
                .setHotelPosition(getHotelPosition(mtTravel.getHotelPosition()))
                .build();

        IMBuddy.TravelDetail travelDetail = IMBuddy.TravelDetail
                .newBuilder()
                .setDbIdx(52)
                .setTravelInfo(travelInfo)
                .setTrafficInfo(trafficInfo)
                .setPlayInfo(playInfo)
                .setCost(mtTravel.getCost())
                .build();

        IMBuddy.CreateTravelReq createTravelReq = IMBuddy.CreateTravelReq
                .newBuilder()
                .setUserId(loginId)
                .setTravelDetail(travelDetail)
                .build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_TRAVEL_CREATE_REQUEST_VALUE;
        imSocketManager.sendRequest(createTravelReq,sid,cid);
    }

    public void onRspCreateTravel(IMBuddy.CreateTravelRsp createTravelRsp) {
        logger.i("onRspCreateTravel");
        Log.e("yuki", "onRspCreateTravel");
        if (createTravelRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", createTravelRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_TRAVEL_FAIL));
        } else {
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_TRAVEL_OK));
        }
    }

    private IMBuddy.PlayQualityType getPlayQuality(int i) {
        switch (i) {
            case 1:
                return IMBuddy.PlayQualityType.FEEL_TYPE_ECONOMIC;
            case 2:
                return IMBuddy.PlayQualityType.FEEL_TYPE_GENERAL;
            case 3:
                return IMBuddy.PlayQualityType.FEEL_TYPE_COMFORTABLE;
            default:
                return IMBuddy.PlayQualityType.FEEL_TYPE_GENERAL;
        }
    }

    private IMBuddy.HotelPositionType getHotelPosition(int i) {
        switch (i) {
            case 1:
                return IMBuddy.HotelPositionType.HOTEL_NEAR_CITY;
            case 2:
                return IMBuddy.HotelPositionType.HOTEL_NEAR_VIEW_SPOT;
            case 4:
                return IMBuddy.HotelPositionType.HOTEL_TRAFFIC_FIT;
            default:
                return IMBuddy.HotelPositionType.HOTEL_NEAR_CITY;
        }
    }

    private void clearTravelList() {
        travelEntityList.clear();
        dbInterface.delAllTravel();
    }

    /**----------------实体set/get-------------------------------*/
    public List<TravelEntity> getTravelEntityList() {
        return travelEntityList;
    }

    public TravelEntity getMtTravel() {
        return mtTravel;
    }
}
