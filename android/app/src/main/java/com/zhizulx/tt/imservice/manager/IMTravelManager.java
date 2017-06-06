package com.zhizulx.tt.imservice.manager;

import android.util.Log;

import com.zhizulx.tt.DB.DBInterface;
import com.zhizulx.tt.DB.String2Entity;
import com.zhizulx.tt.DB.entity.CityEntity;
import com.zhizulx.tt.DB.entity.CollectRouteEntity;
import com.zhizulx.tt.DB.entity.ConfigEntity;
import com.zhizulx.tt.DB.entity.DayRouteEntity;
import com.zhizulx.tt.DB.entity.HotelEntity;
import com.zhizulx.tt.DB.entity.RouteEntity;
import com.zhizulx.tt.DB.entity.SightEntity;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
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
    private Map<String, String> cityCodeName = new HashMap<>();
    private Map<String, String> cityNameCode = new HashMap<>();

    private Map<Integer, HotelEntity> hotelEntityMap = new HashMap<>();
    private Map<Integer, SightEntity> sightEntityMap = new HashMap<>();
    private List<RouteEntity> routeEntityList = new ArrayList<>();
    private RouteEntity routeEntity = new RouteEntity();
    private ConfigEntity configEntity = new ConfigEntity();
    private List<CityEntity> cityEntityList = new ArrayList<>();
    private List<CollectRouteEntity> collectRouteEntityList = new ArrayList<>();
    private CollectRouteEntity collectRouteEntity = new CollectRouteEntity();
    public static final int GET_ROUTE_BY_TAG = 0;
    public static final int GET_ROUTE_BY_SENTENCE = 1;

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

    /**----------------实体set/get-------------------------------*/
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

    public CollectRouteEntity getCollectRouteEntity() {
        return collectRouteEntity;
    }

    public void setCollectRouteEntity(CollectRouteEntity collectRouteEntity) {
        this.collectRouteEntity = collectRouteEntity;
    }

    public List<CollectRouteEntity> getCollectRouteEntityList() {
        return collectRouteEntityList;
    }

    public void setCollectRouteEntityList(List<CollectRouteEntity> collectRouteEntityList) {
        this.collectRouteEntityList.clear();
        this.collectRouteEntityList.addAll(collectRouteEntityList);
    }

    public ConfigEntity getConfigEntity() {
        return configEntity;
    }

    public void setConfigEntity(ConfigEntity configEntity) {
        this.configEntity = configEntity;
    }

    public Boolean getdBInitFin() {
        return dBInitFin;
    }

    public void reqGetRandomRoute(int queryType) {
        Log.e("yuki", "reqGetRandomRoute");
        if (queryType == GET_ROUTE_BY_TAG) {
            configEntity.setSentence("");
        }
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.NewQueryRadomRouteReq newQueryRadomRouteReq = IMBuddy.NewQueryRadomRouteReq.newBuilder()
                .setUserId(loginId)
                .addAllTags(configEntity.getTags())
                .setSentence(configEntity.getSentence()).build();

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
                if (configEntity.getSentence().equals("")) {
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
        for (int index:sightIdList) {
            Log.e("yuki", String.valueOf(index));
        }
        HashSet h = new HashSet(sightIdList);
        sightIdList.clear();
        sightIdList.addAll(h);
        for (int index:sightIdList) {
            Log.e("yuki--", String.valueOf(index));
        }

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
                .setDayCount(configEntity.getDuration())
                .setCityCode(getCityCodeByName(configEntity.getDestination()))
                .addAllTags(tagProcess(configEntity.getRouteType()))
                .build();
        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_NEW_TRAVEL_CREATE_REQUEST_VALUE;
        imSocketManager.sendRequest(newUpdateRadomRouteReq,sid,cid);
    }

    public void onRspCreateRoute(IMBuddy.NewCreateMyTravelRsp newCreateMyTravelRsp) throws ParseException {
        Log.e("yuki", "onRspCreateRoute");
        if (newCreateMyTravelRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", newCreateMyTravelRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_ROUTE_FAIL));
        } else {
            routeEntity = ProtoBuf2JavaBean.getRouteEntity(newCreateMyTravelRsp.getRoute());
            routeEntity.setRouteType(configEntity.getRouteType());
            routeEntity.setTags(configEntity.getTags());
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

    private List<String> tagProcess(String routeType) {
        List<String> tags = new ArrayList<>();
        if (routeType != null && !routeType.isEmpty()) {
            tags.add(routeType);
        }

        for (int i = 0; i < 3 - tags.size(); i ++) {
            tags.add(configEntity.getTags().get(i));
        }
        return tags;
    }

    public void reqCreateCollectRoute() {
        Log.e("yuki", "CreateCollectRoute");
        int loginId = IMLoginManager.instance().getLoginId();

        RouteEntity routeEntity = collectRouteEntity.getRouteEntity();
        String startTime = String.format("%02d:00", routeEntity.getStartTime());
        String endTime = String.format("%02d:00", routeEntity.getEndTime());

        List<IMBuddy.DayRoute> dayRouteList = new ArrayList<>();
        for (DayRouteEntity dayRouteEntity : routeEntity.getDayRouteEntityList()) {
            IMBuddy.DayRoute dayRoute = IMBuddy.DayRoute.newBuilder()
                    .addAllScenics(dayRouteEntity.getSightIDList())
                    .addAllHotels(dayRouteEntity.getHotelIDList()).build();
            dayRouteList.add(dayRoute);
        }

        IMBuddy.Route route = IMBuddy.Route.newBuilder()
                .setId(routeEntity.getDbId())
                .setDayCount(routeEntity.getDay())
                .setCityCode(routeEntity.getCityCode())
                .addAllTag(routeEntity.getTags())
                .setStartTransportTool(IMBuddy.TransportToolType.valueOf(routeEntity.getStartTrafficTool()))
                .setEndTransportTool(IMBuddy.TransportToolType.valueOf(routeEntity.getEndTrafficTool()))
                .setStartTime(startTime)
                .setEndTime(endTime)
                .addAllDayRoutes(dayRouteList).build();

        IMBuddy.CollectionRoute collectionRoute = IMBuddy.CollectionRoute.newBuilder()
                .setId(collectRouteEntity.getDbId())
                .setStartDate(collectRouteEntity.getStartDate())
                .setEndDate(collectRouteEntity.getEndDate())
                .setStartTrafficNo(collectRouteEntity.getStartTrafficNo())
                .setEndTrafficNo(collectRouteEntity.getEndTrafficNo())
                .setRoute(route).build();

        IMBuddy.NewCreateCollectRouteReq newCreateCollectRouteReq = IMBuddy.NewCreateCollectRouteReq.newBuilder()
                .setUserId(loginId)
                .setCollect(collectionRoute).build();
        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_NEW_CREATE_COLLECT_ROUTE_REQUEST_VALUE;
        imSocketManager.sendRequest(newCreateCollectRouteReq,sid,cid);
    }

    public void onRspCreateCollectRoute(IMBuddy.NewCreateCollectRouteRsp newCreateCollectRouteRsp) throws ParseException {
        Log.e("yuki", "onRspCreateCollectRoute");
        if (newCreateCollectRouteRsp.getResultCode() != 0) {
            logger.e("onRspCreateCollectRoute fail %d", newCreateCollectRouteRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_COLLECT_ROUTE_FAIL));
        } else {
            triggerEvent(new TravelEvent(TravelEvent.Event.CREATE_COLLECT_ROUTE_OK));
        }
    }

    public void reqGetCollectRoute() {
        Log.e("yuki", "reqGetCollectRoute");
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.NewQueryCollectRouteReq newQueryCollectRouteReq = IMBuddy.NewQueryCollectRouteReq.newBuilder()
                .setUserId(loginId).build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_NEW_QUERY_COLLECT_ROUTE_REQUEST_VALUE;
        imSocketManager.sendRequest(newQueryCollectRouteReq,sid,cid);
    }

    public void onRspGetCollectRoute(IMBuddy.NewQueryCollectRouteRsp newQueryCollectRouteRsp) throws ParseException {
        Log.e("yuki", "onRspGetCollectRoute");
        if (newQueryCollectRouteRsp.getResultCode() != 0) {
            Log.e("yuki", "onRepTravelList fail" + newQueryCollectRouteRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.QUERY_COLLECT_ROUTE_FAIL));
        } else {
            if (newQueryCollectRouteRsp.getCollectionsList().size() == 0) {
                Log.e("yuki", "onRspGetCollectRoute no route");
            } else {
                List<CollectRouteEntity> collectRouteEntityList = new ArrayList<>();
                for (IMBuddy.CollectionRoute collectionRoute : newQueryCollectRouteRsp.getCollectionsList()) {
                    collectRouteEntityList.add(ProtoBuf2JavaBean.getCollectRouteEntity(collectionRoute));
                }
                setCollectRouteEntityList(collectRouteEntityList);
                triggerEvent(new TravelEvent(TravelEvent.Event.QUERY_COLLECT_ROUTE_OK));
            }
        }
    }

    public void reqDelCollectRoute(List<Integer> delIdList) {
        Log.e("yuki", "reqDelCollectRoute");
        int loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.NewDelCollectRouteReq newQueryCollectRouteReq = IMBuddy.NewDelCollectRouteReq.newBuilder()
                .setUserId(loginId)
                .addAllCollectId(delIdList).build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_NEW_DELETE_COLLECT_ROUTE_REQUEST_VALUE;
        imSocketManager.sendRequest(newQueryCollectRouteReq,sid,cid);
    }

    public void onRspDelCollectRoute(IMBuddy.NewDelCollectRouteRsp newDelCollectRouteRsp) throws ParseException {
        Log.e("yuki", "onRspDelCollectRoute");
        if (newDelCollectRouteRsp.getResultCode() != 0) {
            logger.e("onRepTravelList fail %d", newDelCollectRouteRsp.getResultCode());
            triggerEvent(new TravelEvent(TravelEvent.Event.DELETE_COLLECT_ROUTE_FAIL));
        } else {
            triggerEvent(new TravelEvent(TravelEvent.Event.DELETE_COLLECT_ROUTE_OK));
        }
    }

    public Boolean hasCollected(int routeId) {
        for (CollectRouteEntity collectRouteEntity: collectRouteEntityList) {
            if (routeId == collectRouteEntity.getRouteEntity().getDbId()) {
                return true;
            }
        }
        return false;
    }

    public void initDatePlace() {
        Calendar cal = Calendar.getInstance();
        configEntity.setStartCity(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOCAL_CITY));
        configEntity.setEndCity(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOCAL_CITY));
        cal.add(Calendar.DATE, 2);
        configEntity.setStartDate(cal.getTime());
        cal.add(Calendar.DATE, 3);
        configEntity.setEndDate(cal.getTime());
    }
}
