package com.mogujie.tt.imservice.manager;

import android.util.Log;

import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.DB.entity.HotelEntity;
import com.mogujie.tt.DB.entity.PlayConfigEntity;
import com.mogujie.tt.DB.entity.SightEntity;
import com.mogujie.tt.DB.entity.TrafficEntity;
import com.mogujie.tt.DB.entity.TravelCityEntity;
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
    private List<TrafficEntity> trafficEntityRspList = new ArrayList<>();
    private List<TrafficEntity> trafficEntityList = new ArrayList<>();
    private TravelEntity mtTravel = new TravelEntity();
    private List <TravelCityEntity> mtCity = new ArrayList<>();
    private static final int TRAFFIC_CITY_DIVDER = 0xf1;
    private static final int TRAFFIC_TYPE_DIVDER = 0xf2;

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
        logger.i("reqTravelList");
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.QueryMyTravelReq queryMyTravelReq = IMBuddy.QueryMyTravelReq.newBuilder()
                .setUserId(loginId).build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_TRAVEL_QUERY_REQUEST_VALUE;
        imSocketManager.sendRequest(queryMyTravelReq,sid,cid);
    }

    public void onRspTravelList(IMBuddy.QueryMyTravelRsp queryMyTravelRsp) {
        logger.i("onRepTravelList");
        if (queryMyTravelRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", queryMyTravelRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_FAIL));
            return;
        }

        clearTravelList();
        List<IMBuddy.MyTravel> myTravelList =  queryMyTravelRsp.getMyTravelList();
        if (myTravelList.size() == 0) {
            logger.e("onRepTravelList#travelCnt:%d",myTravelList.size());
            triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_OK));
            return;
        }

        List<TravelEntity> needDb = new ArrayList<>();
        for(IMBuddy.MyTravel myTravel:myTravelList){
            TravelEntity travelEntity = ProtoBuf2JavaBean.getTravelEntity(myTravel);
            travelEntity.setDestinationBK("abc");
            travelEntityList.add(travelEntity);
            needDb.add(travelEntity);
        }
        dbInterface.batchInsertOrUpdateTravel(needDb);
        triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_OK));
    }

    public void reqCreateTravel() {
        int loginId = IMLoginManager.instance().getLoginId();

        IMBuddy.BasicInfo basicInfo = IMBuddy.BasicInfo
                .newBuilder()
                .setPersonNum(mtTravel.getPersonNum())
                .setPlaceFromCode(mtTravel.getStartPlace())
                .setPlaceBackCode(mtTravel.getEndPlace())
                .setPlaceToCode(mtTravel.getDestination())
                .setDateFrom(mtTravel.getStartDate())
                .setDateTo(mtTravel.getEndDate())
                .build();

        IMBuddy.TransportConfig transportConfig = IMBuddy.TransportConfig
                .newBuilder()
                .setToolType(mtTravel.getTrafficWay())
                .setTimeStart(mtTravel.getTrafficStartTime())
                .setTimeEnd(mtTravel.getTrafficEndTime())
                .setQuality(getQuality(mtTravel.getTrafficWay()))
                .setTransit(mtTravel.getTransit())
                .build();

        TravelCityEntity travelCityEntity = mtCity.get(0);
        PlayConfigEntity playConfigEntity = travelCityEntity.getPlayConfig();
        IMBuddy.PlayConfig playConfig = IMBuddy.PlayConfig
                .newBuilder()
                .setQuality(getQuality(playConfigEntity.getQuality()))
                .setTimeFrom(playConfigEntity.getStartTime())
                .setTimeTo(playConfigEntity.getEndTime())
                .setTransportToolType(playConfigEntity.getTransportToolType())
                .setPosition(getPosition(playConfigEntity.getPosition()))
                .build();

        HotelEntity hotelEntity = travelCityEntity.getHotelEntity();
        IMBuddy.HotelInfo hotelInfo = IMBuddy.HotelInfo
                .newBuilder()
                .setCityCode(travelCityEntity.getCityName())
                .setName(hotelEntity.getName())
                .setScore(hotelEntity.getStar())
                .setTags(hotelEntity.getTag())
                .setMustSee(hotelEntity.getMustGo())
                .setUrl(hotelEntity.getUrl())
                .setPrice(hotelEntity.getPrice())
                .setDistance(hotelEntity.getDistance())
                .build();

        List<IMBuddy.ScenicInfo> scenicInfoList = new ArrayList<>();
        for (SightEntity sightEntity : travelCityEntity.getSightList()) {
            IMBuddy.ScenicInfo scenicInfo = IMBuddy.ScenicInfo
                    .newBuilder()
                    .setCityCode(travelCityEntity.getCityName())
                    .setName(sightEntity.getName())
                    .setScore(sightEntity.getStar())
                    .setTags(sightEntity.getTag())
                    .setMustSee(sightEntity.getMustGo())
                    .setUrl(sightEntity.getUrl())
                    .setPlayTime(sightEntity.getPlayTime())
                    .setPrice(sightEntity.getPrice())
                    .setBestTimeFrom(sightEntity.getBestStartTime())
                    .setBestTimeTo(sightEntity.getBestEndTime())
                    .build();
            scenicInfoList.add(scenicInfo);
        }

        IMBuddy.PlayDetail playDetail = IMBuddy.PlayDetail
                .newBuilder()
                .setPlayConfig(playConfig)
                .setHotelInfo(hotelInfo)
                .addAllScenicInfo(scenicInfoList)
                .build();

        TrafficEntity cityGo = travelCityEntity.getGo();
        IMBuddy.TravelToolInfo go = IMBuddy.TravelToolInfo
                .newBuilder()
                .setTransportToolType(cityGo.getType())
                .setNo(cityGo.getNo())
                .setPlaceFromCode(cityGo.getStartCityCode())
                .setPlaceFrom(cityGo.getStartStation())
                .setPlaceToCode(cityGo.getEndCityCode())
                .setPlaceTo(cityGo.getEndStation())
                .setTimeFrom(cityGo.getStartTime())
                .setTimeTo(cityGo.getEndTime())
                .setClass_(cityGo.getSeatClass())
                .setPrice(cityGo.getPrice())
                .build();

        TrafficEntity cityBack = travelCityEntity.getBack();
        IMBuddy.TravelToolInfo back = IMBuddy.TravelToolInfo
                .newBuilder()
                .setTransportToolType(cityBack.getType())
                .setNo(cityBack.getNo())
                .setPlaceFromCode(cityBack.getStartCityCode())
                .setPlaceFrom(cityBack.getStartStation())
                .setPlaceToCode(cityBack.getEndCityCode())
                .setPlaceTo(cityBack.getEndStation())
                .setTimeFrom(cityBack.getStartTime())
                .setTimeTo(cityBack.getEndTime())
                .setClass_(cityBack.getSeatClass())
                .setPrice(cityBack.getPrice())
                .build();

        IMBuddy.TransportTool transportTool = IMBuddy.TransportTool
                .newBuilder()
                .setFromInfo(go)
                .setBackInfo(back)
                .build();

        IMBuddy.TravelDetail travelDetail = IMBuddy.TravelDetail
                .newBuilder()
                .setTransportTool(transportTool)
                .setPlayDetail(playDetail)
                .build();

        IMBuddy.MyTravel myTravel = IMBuddy.MyTravel
                .newBuilder()
                .setDbIdx(0)
                .setBasicInfo(basicInfo)
                .setTravelDetail(travelDetail)
                .setTransportConfig(transportConfig)
                .build();


        IMBuddy.CreateMyTravelReq createMyTravelReq = IMBuddy.CreateMyTravelReq
                .newBuilder()
                .setUserId(loginId)
                .setMyTravel(myTravel)
                .build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_TRAVEL_CREATE_REQUEST_VALUE;
        //imSocketManager.sendRequest(createTravelReq,sid,cid);
    }

    public void onRspCreateTravel(IMBuddy.CreateMyTravelRsp createMyTravelRsp) {
        logger.i("onRspCreateTravel");
        Log.e("yuki", "onRspCreateTravel");
        if (createMyTravelRsp.getResultCode() != 0) {
            logger.e("onRspCreateTravel fail %d", createMyTravelRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_TRAVEL_FAIL));
        } else {
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_TRAVEL_OK));
        }
    }

    public void reqDelTravel(List<Integer> travelList) {
        logger.i("reqDelTravel");
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.DeleteMyTravelReq deleteMyTravelReq= IMBuddy.DeleteMyTravelReq.newBuilder()
                .setUserId(loginId)
                .addAllDbIdx(travelList).build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_TRAVEL_DELETE_REQUEST_VALUE;
        imSocketManager.sendRequest(deleteMyTravelReq,sid,cid);
    }

    public void onRspDelTravel(IMBuddy.DeleteMyTravelRsp deleteMyTravelRsp) {
        logger.i("onRspDelTravel");
        Log.e("yuki", "onRspDelTravel");
        if (deleteMyTravelRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", deleteMyTravelRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.DEL_TRAVEL_FAIL));
        } else {
            triggerEvent(new TravelEvent(TravelEvent.Event.DEL_TRAVEL_OK));
        }
    }

    private IMBuddy.QualityType getQuality(int i) {
        switch (i) {
            case 1:
                return IMBuddy.QualityType.QUALITY_LOW;
            case 2:
                return IMBuddy.QualityType.QUALITY_MID;
            case 3:
                return IMBuddy.QualityType.QUALITY_HIGH;
            default:
                return IMBuddy.QualityType.QUALITY_MID;
        }
    }

    private IMBuddy.PositionType getPosition(int i) {
        switch (i) {
            case 1:
                return IMBuddy.PositionType.CENTRAL;
            case 2:
                return IMBuddy.PositionType.SCENIC;
            case 3:
                return IMBuddy.PositionType.OTHER;
            default:
                return IMBuddy.PositionType.OTHER;
        }
    }

    public void reqTravelRoute() {
        logger.i("reqDelTravel");
        int loginId = IMLoginManager.instance().getLoginId();

        IMBuddy.BasicInfo basicInfo = IMBuddy.BasicInfo
                .newBuilder()
                .setPersonNum(mtTravel.getPersonNum())
                .setPlaceFromCode(mtTravel.getStartPlace())
                .setPlaceBackCode(mtTravel.getEndPlace())
                .setPlaceToCode(mtTravel.getDestination())
                .setDateFrom(mtTravel.getStartDate())
                .setDateTo(mtTravel.getEndDate())
                .build();

        IMBuddy.TransportConfig transportConfig = IMBuddy.TransportConfig
                .newBuilder()
                .setToolType(mtTravel.getTrafficWay())
                .setTimeStart(mtTravel.getTrafficStartTime())
                .setTimeEnd(mtTravel.getTrafficEndTime())
                .setQuality(getQuality(mtTravel.getTrafficWay()))
                .setTransit(mtTravel.getTransit())
                .build();

        IMBuddy.GetTransportToolReq getTransportToolReq = IMBuddy.GetTransportToolReq.newBuilder()
                .setUserId(loginId)
                .setBasicInfo(basicInfo)
                .setTransportConfig(transportConfig)
                .build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_TRAVEL_TRANSPORT_TOOL_REQUEST_VALUE;
        imSocketManager.sendRequest(getTransportToolReq,sid,cid);
    }

    public void onRspTravelRoute(IMBuddy.GetTransportToolRsp getTransportToolRsp) {
        logger.i("onRspDelTravel");
        Log.e("yuki", "onRspDelTravel");
        if (getTransportToolRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", getTransportToolRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.REQ_TRAVEL_ROUTE_FAIL));
        } else {
            for (IMBuddy.TravelToolInfo travelToolInfo:getTransportToolRsp.getTravelToolInfoList()) {
                trafficEntityList.add(ProtoBuf2JavaBean.getTrafficEntity(travelToolInfo));
            }
            trafficTypePreProcess();

            triggerEvent(new TravelEvent(TravelEvent.Event.REQ_TRAVEL_ROUTE_OK));
        }
    }

    private void trafficTypePreProcess() {
        String start;
        int type = 0;

        for (TrafficEntity trafficEntity:trafficEntityRspList) {
            int tt = trafficEntity.getType();
            if (tt != type) {
                type = tt;
                TrafficEntity typeIndex = new TrafficEntity();
                typeIndex.setType(tt+0xf0);
                trafficEntityList.add(typeIndex);
            }

            trafficEntityList.add(trafficEntity);
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

    public List<TrafficEntity> getTrafficEntityList() {
        if (trafficEntityRspList.isEmpty()) {
            TrafficEntity plane1 = new TrafficEntity();
            plane1.setType(1);
            plane1.setStartTime("07:45");
            plane1.setEndTime("08:55");
            plane1.setStartStation("宝安T3");
            plane1.setEndStation("高崎T4");
            plane1.setNo("海航HU7065");
            plane1.setSeatClass("经济舱");
            plane1.setPrice(800);
            plane1.setSelect(1);

            TrafficEntity plane2 = new TrafficEntity();
            plane2.setType(1);
            plane2.setStartTime("07:45");
            plane2.setEndTime("08:55");
            plane2.setStartStation("宝安T3");
            plane2.setEndStation("高崎T4");
            plane2.setNo("海航HU7065");
            plane2.setSeatClass("头等舱");
            plane2.setPrice(800);
            trafficEntityRspList.add(plane1);
            trafficEntityRspList.add(plane2);

            TrafficEntity train1 = new TrafficEntity();
            train1.setType(2);
            train1.setStartTime("07:45");
            train1.setEndTime("08:55");
            train1.setStartStation("深圳北站");
            train1.setEndStation("厦门东站");
            train1.setNo("D4354");
            train1.setSeatClass("二等座");
            train1.setPrice(199);

            TrafficEntity train2 = new TrafficEntity();
            train2.setType(2);
            train2.setStartTime("07:45");
            train2.setEndTime("08:55");
            train2.setStartStation("深圳北站");
            train2.setEndStation("厦门东站");
            train2.setNo("K845");
            train2.setSeatClass("无座");
            train2.setPrice(88);
            trafficEntityRspList.add(train1);
            trafficEntityRspList.add(train2);

            TrafficEntity bus = new TrafficEntity();
            bus.setType(3);
            bus.setStartTime("07:45");
            bus.setEndTime("08:55");
            bus.setStartStation("深圳客运站");
            bus.setEndStation("厦门客运站");
            bus.setNo("K845");
            bus.setSeatClass("无座");
            bus.setPrice(88);
            trafficEntityRspList.add(bus);
        }
        trafficTypePreProcess();
        return trafficEntityList;
    }

    public List <TravelCityEntity> getMtCity() {
        if (mtCity.isEmpty()) {
            TravelCityEntity travelCityEntity = new TravelCityEntity();
            mtCity.add(travelCityEntity);
        }
        return mtCity;
    }
}
