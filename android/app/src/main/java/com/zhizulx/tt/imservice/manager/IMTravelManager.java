package com.zhizulx.tt.imservice.manager;

import android.util.Log;
import android.widget.Toast;

import com.zhizulx.tt.DB.DBInterface;
import com.zhizulx.tt.DB.String2Entity;
import com.zhizulx.tt.DB.entity.CityEntity;
import com.zhizulx.tt.DB.entity.HotelEntity;
import com.zhizulx.tt.DB.entity.PlayConfigEntity;
import com.zhizulx.tt.DB.entity.RouteEntity;
import com.zhizulx.tt.DB.entity.SightEntity;
import com.zhizulx.tt.DB.entity.TrafficEntity;
import com.zhizulx.tt.DB.entity.TravelCityEntity;
import com.zhizulx.tt.DB.entity.TravelEntity;
import com.zhizulx.tt.DB.sp.SystemConfigSp;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.event.TravelEvent;
import com.zhizulx.tt.protobuf.IMBaseDefine;
import com.zhizulx.tt.protobuf.IMBuddy;
import com.zhizulx.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.zhizulx.tt.utils.CsvUtil;
import com.zhizulx.tt.utils.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class IMTravelManager extends IMManager {
    private Logger logger = Logger.getLogger(IMTravelManager.class);
	private static IMTravelManager inst = new IMTravelManager();
	public static IMTravelManager instance() {
			return inst;
	}

    private IMSocketManager imSocketManager = IMSocketManager.instance();
    private DBInterface dbInterface = DBInterface.instance();
    private Boolean dBInitFin = false;

    /**key=> sessionKey*/
    private List<TravelEntity> travelEntityList = new ArrayList<>();
    private List<TrafficEntity> goTrafficEntityRspList = new ArrayList<>();
    private List<TrafficEntity> backTrafficEntityRspList = new ArrayList<>();
    private List<TrafficEntity> goTrafficEntityList = new ArrayList<>();
    private List<TrafficEntity> backTrafficEntityList = new ArrayList<>();
    private TravelEntity mtTravel = new TravelEntity();
    private List <TravelCityEntity> mtCity = new ArrayList<>();
    private Map<String, String> cityCodeName = new HashMap<>();
    private Map<String, String> cityNameCode = new HashMap<>();
    private static final int TRAFFIC_CITY_DIVDER = 0xf1;
    private static final int TRAFFIC_TYPE_DIVDER = 0xf2;

    private Map<Integer, HotelEntity> hotelEntityMap = new HashMap<>();
    private Map<Integer, SightEntity> sightEntityMap = new HashMap<>();
    private List<RouteEntity> routeEntityList = new ArrayList<>();
    private RouteEntity routeEntity = new RouteEntity();
    private String startCity;
    private String endCity;
    private String startDate;
    private String endDate;
    private List<String> emotionTags = new ArrayList<>();
    private String sentence = "";
    private List<CityEntity> cityEntityList = new ArrayList<>();

    @Override
    public void doOnStart() {
        String[] name = ctx.getResources().getStringArray(R.array.city_name);
        String[] code = ctx.getResources().getStringArray(R.array.city_code);
        for (int index=0; index<name.length; index ++) {
            cityCodeName.put(code[index], name[index]);
            cityNameCode.put(name[index], code[index]);
        }
        if (cityEntityList.size() == 0) {
            initCityEntity();
        }
    }


    // 未读消息控制器，本地是不存状态的
    public void onNormalLoginOk(){
        onLocalLoginOk();
        onLocalNetOk();
    }

    public void onLocalNetOk(){
        //reqTravelList();
    }

    public void onLocalLoginOk(){
        logger.i("group#loadFromDb");

/*        List<HotelEntity> localHotelEntityList = dbInterface.loadAllHotel();
        for(HotelEntity hotelEntity:localHotelEntityList){
            this.hotelEntityList.add(hotelEntity);
        }*/
        new Thread() {
            @Override
            public void run() {
                initSightHotel();
            }
        }.start();
        triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_OK));
    }

    @Override
    public void reset() {
        travelEntityList.clear();
    }

    private void initSightHotel() {
        if (SystemConfigSp.instance().getIntConfig(SystemConfigSp.SysCfgDimension.DBInit) == 1) {
            List<SightEntity> sightEntityList = dbInterface.loadAllSight();
            for (SightEntity sightEntity : sightEntityList) {
                sightEntityMap.put(sightEntity.getPeerId(), sightEntity);
            }

            List<HotelEntity> hotelEntityList = dbInterface.loadAllHotel();
            for (HotelEntity hotelEntity : hotelEntityList) {
                hotelEntityMap.put(hotelEntity.getPeerId(), hotelEntity);
            }
            dBInitFin = true;
            return;
        }
        // 加载本地hotel
        List<List<String>> csvHotel = new ArrayList<List<String>>();
        List<List<String>> csvSight = new ArrayList<List<String>>();
        List<HotelEntity> hotelEntityList = new ArrayList<>();
        List<SightEntity> sightEntityList = new ArrayList<>();
        try {
            CsvUtil csvUtilHotel = new CsvUtil(ctx, "hotel.csv");
            csvUtilHotel.run();
            csvHotel = csvUtilHotel.getCsv();
            CsvUtil csvUtilSight = new CsvUtil(ctx, "sight.csv");
            csvUtilSight.run();
            csvSight = csvUtilSight.getCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (List<String> hotelStringList : csvHotel) {
            HotelEntity hotelEntity = String2Entity.getHotelEntity(hotelStringList);
            hotelEntityList.add(hotelEntity);
            if (hotelEntityMap.containsKey(hotelEntity.getPeerId())) {
                continue;
            }
            hotelEntityMap.put(hotelEntity.getPeerId(), hotelEntity);
        }
        dbInterface.batchInsertOrUpdateHotel(hotelEntityList);

        for (List<String> sightStringList : csvSight) {
            SightEntity sightEntity = String2Entity.getSightEntity(sightStringList);
            sightEntityList.add(sightEntity);
            if (sightEntityMap.containsKey(sightEntity.getPeerId())) {
                continue;
            }
            sightEntityMap.put(sightEntity.getPeerId(), sightEntity);
        }
        dbInterface.batchInsertOrUpdateSight(sightEntityList);
        SystemConfigSp.instance().setIntConfig(SystemConfigSp.SysCfgDimension.DBInit, 1);
        dBInitFin = true;
    }

    /**
     * 继承该方法实现自身的事件驱动
     * @param event
     */
    public synchronized void triggerEvent(TravelEvent event) {
        EventBus.getDefault().post(event);
    }

    /**-------------------------------分割线----------------------------------*/
    public void reqTravelList() {
        Log.e("yuki", "reqTravelList");
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.QueryMyTravelReq queryMyTravelReq = IMBuddy.QueryMyTravelReq.newBuilder()
                .setUserId(loginId).build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_TRAVEL_QUERY_REQUEST_VALUE;
        imSocketManager.sendRequest(queryMyTravelReq,sid,cid);
    }

    public void onRspTravelList(IMBuddy.QueryMyTravelRsp queryMyTravelRsp) {
        Log.e("yuki", "onRspTravelList");
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

/*    public void reqCreateTravel() {
        int loginId = IMLoginManager.instance().getLoginId();

        IMBuddy.BasicInfo basicInfo = IMBuddy.BasicInfo
                .newBuilder()
                .setPersonNum(mtTravel.getPersonNum())
                .setPlaceFromCode("SZX")
                .setPlaceBackCode("SZX")
                .setPlaceToCode("XMN")
                .setDateFrom(mtTravel.getStartDate())
                .setDateTo(mtTravel.getEndDate())
                .build();

        IMBuddy.TransportConfig transportConfig = IMBuddy.TransportConfig
                .newBuilder()
                .setToolType(mtTravel.getTrafficWay())
                .setTimeFrom(mtTravel.getTrafficStartTime())
                .setTimeTo(mtTravel.getTrafficEndTime())
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

        List<IMBuddy.DayHotel> dayHotelList = new ArrayList<>();
        for (HotelEntity hotelEntity : travelCityEntity.getHotelList()) {
            IMBuddy.HotelInfo hotelInfo = IMBuddy.HotelInfo
                    .newBuilder()
                    .setId(hotelEntity.getPeerId())
                    .setCityCode(travelCityEntity.getCityName())
                    .setName(hotelEntity.getName())
                    .setScore(hotelEntity.getStar())
                    .setTags(hotelEntity.getTag())
                    .setMustSee(0)
                    .setUrl(hotelEntity.getUrl())
                    .setClass_("aaa")
                    .setPrice(hotelEntity.getPrice())
                    .setDistance(hotelEntity.getDistance())
                    .build();
            IMBuddy.DayHotel dayHotel = IMBuddy.DayHotel
                    .newBuilder()
                    .setDayTimeFrom(hotelEntity.getStartTime())
                    .setDayTimeTo(hotelEntity.getEndTime())
                    .setHotelInfo(hotelInfo)
                    .build();
            dayHotelList.add(dayHotel);
        }

        List<IMBuddy.DayScenic> dayScenicList = new ArrayList<>();
        for (SightEntity sightEntity : travelCityEntity.getSightList()) {
            IMBuddy.ScenicInfo scenicInfo = IMBuddy.ScenicInfo
                    .newBuilder()
                    .setId(sightEntity.getPeerId())
                    .setCityCode(travelCityEntity.getCityName())
                    .setName(sightEntity.getName())
                    .setScore(sightEntity.getStar())
                    .setTags(sightEntity.getTag())
                    .setFree(sightEntity.getFree())
                    .setMustSee(sightEntity.getMustGo())
                    .setUrl("")
                    .setClass_("abc")
                    .setPlayTime(sightEntity.getPlayTime())
                    .setPrice(123)
                    .setBestTimeFrom("0:0")
                    .setBestTimeTo("0:0")
                    .build();
            IMBuddy.DayScenic dayScenic = IMBuddy.DayScenic
                    .newBuilder()
                    .setDayTimeFrom(sightEntity.getStartTime())
                    .setDayTimeTo(sightEntity.getEndTime())
                    .setScenicInfo(scenicInfo)
                    .build();
            dayScenicList.add(dayScenic);
        }

        IMBuddy.PlayDetail playDetail = IMBuddy.PlayDetail
                .newBuilder()
                .setPlayConfig(playConfig)
                .addAllDayHotel(dayHotelList)
                .addAllDayScenic(dayScenicList)
                .build();

        TrafficEntity cityGo = travelCityEntity.getGo();
        IMBuddy.TravelToolInfo go = IMBuddy.TravelToolInfo
                .newBuilder()
                .setId(1)
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
                .setId(1)
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
                .setCost(mtTravel.getCost())
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
        imSocketManager.sendRequest(createMyTravelReq, sid, cid);
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
    }*/

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

/*    public void reqTravelRoute() {
        logger.i("reqTravelRoute");
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
                .setTimeFrom(mtTravel.getTrafficStartTime())
                .setTimeTo(mtTravel.getTrafficEndTime())
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
        logger.i("onRspTravelRoute");
        Log.e("yuki", "onRspTravelRoute");
        List<TrafficEntity> trafficEntitiesRsp = new ArrayList<>();
        if (getTransportToolRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", getTransportToolRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.REQ_TRAVEL_ROUTE_FAIL));
        } else {
            for (IMBuddy.TravelToolInfo travelToolInfo:getTransportToolRsp.getTravelToolInfoList()) {
                trafficEntitiesRsp.add(ProtoBuf2JavaBean.getTrafficEntity(travelToolInfo));
            }
            GoORBack(trafficEntitiesRsp, mtTravel.getDestination());
            triggerEvent(new TravelEvent(TravelEvent.Event.REQ_TRAVEL_ROUTE_OK));
        }
    }

    private void GoORBack(List<TrafficEntity> trafficEntities, String city) {
        for (TrafficEntity index:trafficEntities) {
            if (index.getEndCityCode().equals(city)) {
                goTrafficEntityList.add(index);
            }

            if (index.getStartCityCode().equals(city)) {
                backTrafficEntityList.add(index);
            }
        }
    }

    private void trafficTypePreProcess(List<TrafficEntity> trafficEntities, List<TrafficEntity> result) {
        String start;
        int type = 0;
        result.clear();
        for (TrafficEntity trafficEntity:trafficEntities) {
            int tt = trafficEntity.getType();
            if (tt != type) {
                type = tt;
                TrafficEntity typeIndex = new TrafficEntity();
                typeIndex.setType(tt+0xf0);
                typeIndex.setStatus(1);
                result.add(typeIndex);
            }

            result.add(trafficEntity);
        }
    }*/

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

/*    public List<TrafficEntity> getGoTrafficEntityList() {
        if (goTrafficEntityRspList.isEmpty()) {
            TrafficEntity plane1 = new TrafficEntity();
            plane1.setType(1);
            plane1.setStartTime("07:45");
            plane1.setEndTime("08:55");
            plane1.setStartStation("宝安T3");
            plane1.setEndStation("高崎T4");
            plane1.setStartCityCode("baoan");
            plane1.setEndCityCode("gaoqi");
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
            plane2.setStartCityCode("baoan");
            plane2.setEndCityCode("gaoqi");
            plane2.setNo("海航HU7065");
            plane2.setSeatClass("头等舱");
            plane2.setPrice(800);
            goTrafficEntityRspList.add(plane1);
            goTrafficEntityRspList.add(plane2);

            TrafficEntity train1 = new TrafficEntity();
            train1.setType(2);
            train1.setStartTime("07:45");
            train1.setEndTime("08:55");
            train1.setStartStation("深圳北站");
            train1.setEndStation("厦门东站");
            train1.setStartCityCode("shenzhebei");
            train1.setEndCityCode("xiamendong");
            train1.setNo("D4354");
            train1.setSeatClass("二等座");
            train1.setPrice(199);

            TrafficEntity train2 = new TrafficEntity();
            train2.setType(2);
            train2.setStartTime("07:45");
            train2.setEndTime("08:55");
            train2.setStartStation("深圳北站");
            train2.setEndStation("厦门东站");
            train2.setStartCityCode("shenzhebei");
            train2.setEndCityCode("xiamendong");
            train2.setNo("K845");
            train2.setSeatClass("无座");
            train2.setPrice(88);
            goTrafficEntityRspList.add(train1);
            goTrafficEntityRspList.add(train2);

            TrafficEntity bus = new TrafficEntity();
            bus.setType(3);
            bus.setStartTime("07:45");
            bus.setEndTime("08:55");
            bus.setStartStation("深圳客运站");
            bus.setEndStation("厦门客运站");
            bus.setStartCityCode("shenzhekeyunzhan");
            bus.setEndCityCode("xiamenkeyunzhan");
            bus.setNo("K845");
            bus.setSeatClass("无座");
            bus.setPrice(88);
            goTrafficEntityRspList.add(bus);
        }
        trafficTypePreProcess(goTrafficEntityRspList, goTrafficEntityList);
        return goTrafficEntityList;
    }

    public List<TrafficEntity> getBackTrafficEntityList() {
        if (backTrafficEntityRspList.isEmpty()) {
            TrafficEntity plane1 = new TrafficEntity();
            plane1.setType(1);
            plane1.setStartTime("07:45");
            plane1.setEndTime("08:55");
            plane1.setStartStation("宝安T3");
            plane1.setEndStation("高崎T4");
            plane1.setStartCityCode("baoan");
            plane1.setEndCityCode("gaoqi");
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
            plane2.setStartCityCode("baoan");
            plane2.setEndCityCode("gaoqi");
            plane2.setNo("海航HU7065");
            plane2.setSeatClass("头等舱");
            plane2.setPrice(800);
            backTrafficEntityRspList.add(plane1);
            backTrafficEntityRspList.add(plane2);

            TrafficEntity train1 = new TrafficEntity();
            train1.setType(2);
            train1.setStartTime("07:45");
            train1.setEndTime("08:55");
            train1.setStartStation("深圳北站");
            train1.setEndStation("厦门东站");
            train1.setStartCityCode("shenzhebei");
            train1.setEndCityCode("xiamendong");
            train1.setNo("D4354");
            train1.setSeatClass("二等座");
            train1.setPrice(199);

            TrafficEntity train2 = new TrafficEntity();
            train2.setType(2);
            train2.setStartTime("07:45");
            train2.setEndTime("08:55");
            train2.setStartStation("深圳北站");
            train2.setEndStation("厦门东站");
            train2.setStartCityCode("shenzhebei");
            train2.setEndCityCode("xiamendong");
            train2.setNo("K845");
            train2.setSeatClass("无座");
            train2.setPrice(88);
            backTrafficEntityRspList.add(train1);
            backTrafficEntityRspList.add(train2);

            TrafficEntity bus = new TrafficEntity();
            bus.setType(3);
            bus.setStartTime("07:45");
            bus.setEndTime("08:55");
            bus.setStartStation("深圳客运站");
            bus.setEndStation("厦门客运站");
            bus.setStartCityCode("shenzhekeyunzhan");
            bus.setEndCityCode("xiamenkeyunzhan");
            bus.setNo("K845");
            bus.setSeatClass("无座");
            bus.setPrice(88);
            backTrafficEntityRspList.add(bus);
        }
        trafficTypePreProcess(backTrafficEntityRspList, backTrafficEntityList);
        return backTrafficEntityList;
    }*/

    public List <TravelCityEntity> getMtCity() {
        if (mtCity.isEmpty()) {
            TravelCityEntity travelCityEntity = new TravelCityEntity();
            mtCity.add(travelCityEntity);
        }
        return mtCity;
    }

    public List<String> getEmotionTags() {
        return emotionTags;
    }

    public void setEmotionTags(List<String> emotionTags) {
        this.emotionTags.clear();
        this.emotionTags.addAll(emotionTags);
    }
    public Boolean getdBInitFin() {
        return dBInitFin;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCityNameByCode(String code) {
        return cityCodeName.get(code);
    }

    public String getCityCodeByName(String name) {
        return cityNameCode.get(name);
    }

    public List<HotelEntity> getHotelList() {
        List<HotelEntity> hotelEntityList = new ArrayList<>(hotelEntityMap.values());
        return hotelEntityList;
    }

    public List<SightEntity> getSightList() {
        List<SightEntity> sightEntityList = new ArrayList<>(sightEntityMap.values());
        return sightEntityList;
    }

    public HotelEntity getHotelByID(int id) {
        return hotelEntityMap.get(id);
    }

    public SightEntity getSightByID(int id) {
        return sightEntityMap.get(id);
    }

    public RouteEntity getRouteEntity() {
        return routeEntity;
    }

    public void setRouteEntity(RouteEntity routeEntity) {
        this.routeEntity = routeEntity;
    }

    public List<RouteEntity> getRouteEntityList() {
        return routeEntityList;
    }

    public void reqGetRandomRoute(String sentence) {
        Log.e("yuki", "reqGetRandomRoute");
        this.sentence = sentence;
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.NewQueryRadomRouteReq newQueryRadomRouteReq = IMBuddy.NewQueryRadomRouteReq.newBuilder()
                .setUserId(loginId)
                .addAllTags(tagProcess(routeEntity.getTags()))
                .setSentence(sentence).build();
        Log.e("yuki", newQueryRadomRouteReq.toString());

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_RADOM_ROUTE_QUERY_REQUEST_VALUE;
        imSocketManager.sendRequest(newQueryRadomRouteReq,sid,cid);
    }

    public void onRspGetRandomRoute(IMBuddy.NewQueryRadomRouteRsp newQueryRadomRouteRsp) throws ParseException {
        Log.e("yuki", "onRspGetRandomRoute");
        if (newQueryRadomRouteRsp.getResultCode() != 0) {
            Log.e("yuki", "onRepTravelList fail" + newQueryRadomRouteRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.QUERY_RANDOM_ROUTE_FAIL));
        } else {
            if (newQueryRadomRouteRsp.getRoutesList().size() == 0) {
                Log.e("yuki", "onRspGetRandomRoute no route");
            } else {
                if (sentence.equals("")) {
                    routeEntityList.clear();
                    for (IMBuddy.Route route : newQueryRadomRouteRsp.getRoutesList()) {
                        routeEntityList.add(ProtoBuf2JavaBean.getRouteEntity(route));
                    }
                    triggerEvent(new TravelEvent(TravelEvent.Event.QUERY_RANDOM_ROUTE_TAG_OK));
                } else {
                    routeEntity = ProtoBuf2JavaBean.getRouteEntity(newQueryRadomRouteRsp.getRoutesList().get(0));
                    triggerEvent(new TravelEvent(TravelEvent.Event.QUERY_RANDOM_ROUTE_SENTENCE_OK));
                }
            }
        }
    }

    public void reqUpdateRandomRoute(List<Integer> sightIdList) {
        Log.e("yuki", "reqUpdateRandomRoute");
        int loginId = IMLoginManager.instance().getLoginId();
        String startTime = String.format("%02d:00", routeEntity.getStartTime());
        String endTime = String.format("%02d:00", routeEntity.getEndTime());
        IMBuddy.NewUpdateRadomRouteReq newUpdateRadomRouteReq = IMBuddy.NewUpdateRadomRouteReq.newBuilder()
                .setUserId(loginId)
                .setDayCount(routeEntity.getDay())
                .setCityCode(routeEntity.getCityCode())
                .setStartTransportTool(IMBuddy.TransportToolType.valueOf(routeEntity.getStartTrafficTool()))
                .setEndTransportTool(IMBuddy.TransportToolType.valueOf(routeEntity.getEndTrafficTool()))
                .setStartTime(startTime)
                .setEndTime(endTime)
                .addAllScenicIds(sightIdList)
                .setTag(routeEntity.getRouteType()).build();

        Log.e("yuki", newUpdateRadomRouteReq.toString());

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_RADOM_ROUTE_UPDATE_REQUEST_VALUE;
        imSocketManager.sendRequest(newUpdateRadomRouteReq,sid,cid);
    }

    public void onRspUpdateRandomRoute(IMBuddy.NewUpdateRadomRouteRsp newUpdateRadomRouteRsp) throws ParseException {
        Log.e("yuki", "onRspUpdateRandomRoute");
        if (newUpdateRadomRouteRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", newUpdateRadomRouteRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.UPDATE_RANDOM_ROUTE_FAIL));
        } else {
            List<String> tags = new ArrayList<>();
            tags.addAll(routeEntity.getTags());
            routeEntity = ProtoBuf2JavaBean.getRouteEntity(newUpdateRadomRouteRsp.getRoute());
            routeEntity.setTags(tags);
            triggerEvent(new TravelEvent(TravelEvent.Event.UPDATE_RANDOM_ROUTE_OK));
        }
    }

    public void reqCreateRoute() {
        Log.e("yuki", "reqCreateRoute");
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.NewCreateMyTravelReq newUpdateRadomRouteReq = IMBuddy.NewCreateMyTravelReq.newBuilder()
                .setUserId(loginId)
                .setDayCount(routeEntity.getDay())
                .setCityCode(routeEntity.getCityCode())
                .addAllTags(tagProcess(routeEntity.getTags()))
                .build();
        Log.e("yuki", newUpdateRadomRouteReq.toString());
        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_NEW_TRAVEL_CREATE_REQUEST_VALUE;
        imSocketManager.sendRequest(newUpdateRadomRouteReq,sid,cid);
    }

    public void onRspCreateRoute(IMBuddy.NewCreateMyTravelRsp newCreateMyTravelRsp) throws ParseException {
        Log.e("yuki", "onRspCreateRoute");
        if (newCreateMyTravelRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", newCreateMyTravelRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_ROUTE_Fail));
        } else {
            List<String> tags = new ArrayList<>();
            tags.addAll(routeEntity.getTags());
            routeEntity = ProtoBuf2JavaBean.getRouteEntity(newCreateMyTravelRsp.getRoute());
            routeEntity.setTags(tags);
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_ROUTE_OK));
        }
    }

    public void initalRoute() {
        for(Map.Entry<Integer, SightEntity> entry : sightEntityMap.entrySet()) {
            entry.getValue().setSelect(0);
        }

        for(Map.Entry<Integer, HotelEntity> entry : hotelEntityMap.entrySet()) {
            entry.getValue().setSelect(0);
        }
    }

    private void initCityEntity() {
        List<String> xiamenPic = new ArrayList<>();
        xiamenPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/574e9258d109b3de3cafd4a4cdbf6c81810a4ced.jpg");
        xiamenPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/1ad5ad6eddc451da671ac9e0b4fd5266d11632c9.jpg");
        xiamenPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/f7246b600c33874494b2d1c6510fd9f9d72aa011.jpg");
        String xiamenDescription = "        厦门是一个秀丽清新的城市，空气和阳光都很好。厦门大学的校园整洁优美，鼓浪屿有很多特色店铺，都是可以去看看的。厦门的街道很干净，慢节奏的生活很惬意。海鲜不错，当地小吃很棒。";

        List<String> guangzhouPic = new ArrayList<>();
        guangzhouPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/b812c8fcc3cec3fda532f9b3d188d43f869427e3.jpg");
        guangzhouPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/9a504fc2d56285358b18fe8f90ef76c6a6ef63a7.jpg");
        guangzhouPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/5bafa40f4bfbfbeddfe47e237af0f736afc31f28.jpg");
        String guangzhouDescription = "        广州作为中国最发达的城市之一，交通还算方便。好玩的地方很多，珠江夜景很漂亮，上下九步行街十分美丽，有空的时候还是值得看看的。广州的小吃众多，早茶和粤菜都很有特色。";

        List<String> shenzhenPic = new ArrayList<>();
        shenzhenPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/9922720e0cf3d7caa3c963d0f21fbe096b63a95d.jpg");
        shenzhenPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/3c6d55fbb2fb431613dbf8ab22a4462308f7d31f.jpg");
        shenzhenPic.add("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/lvpics/pic/item/78310a55b319ebc42c081e578626cffc1f17168f.jpg");
        String shenzhenDescription = "        深圳人的生活节奏很快，非常美丽的城市。深圳的道路比较宽阔，交通方便，规划很好。这里可以感受大都市的繁华和繁忙。世界之窗是不错的地方，气候也很适合居住。经济高度发达。";

        CityEntity xiamen = new CityEntity("XMN", "厦门", R.drawable.xiamen_icon, xiamenPic, xiamenDescription);
        CityEntity guangzhou = new CityEntity("CAN", "广州", R.drawable.guangzhou_icon, guangzhouPic, guangzhouDescription);
        CityEntity shenzhen = new CityEntity("SZX", "深圳", R.drawable.shenzhen_icon, shenzhenPic, shenzhenDescription);

        cityEntityList.add(xiamen);
        cityEntityList.add(guangzhou);
        cityEntityList.add(shenzhen);
    }

    public List<CityEntity> getCityEntityList() {
        return cityEntityList;
    }

    public CityEntity getCityEntitybyCityCode(String cityCode) {
        for (CityEntity cityEntity : cityEntityList) {
            if (cityEntity.getCityCode().equals(cityCode)) {
                return cityEntity;
            }
        }
        return null;
    }

    private List<String> tagProcess(List<String> inputTag) {
        List<String> tags = new ArrayList<>();
        tags.addAll(inputTag);
        int tagSize = tags.size();
        if (tagSize < 3) {
            for (int i = 0; i < 3 - tagSize; i ++) {
                tags.add(emotionTags.get(i));
            }
        }
        return tags;
    }
}
