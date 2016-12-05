package com.mogujie.tt.imservice.manager;

import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.DB.entity.TravelEntity;
import com.mogujie.tt.imservice.event.TravelEvent;
import com.mogujie.tt.protobuf.IMTravel;
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
    private IMLoginManager loginManager = IMLoginManager.instance();

    private DBInterface dbInterface = DBInterface.instance();

    /**key=> sessionKey*/
    private List<TravelEntity> travelEntityList = new ArrayList<>();

    @Override
    public void doOnStart() {}


    // 未读消息控制器，本地是不存状态的
    public void onNormalLoginOk(){
        travelEntityList.clear();
        //reqTravelList();
    }

    public void onLocalNetOk(){
        travelEntityList.clear();
        //reqTravelList();
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
/*    private void reqTravelList() {
        logger.i("unread#1reqTravelList");
        int loginId = IMLoginManager.instance().getLoginId();
        IMTravel.IMTravelInfoReq  travelInfoReq  = IMTravel.IMTravelInfoReq
                .newBuilder()
                .setUserId(loginId)
                .build();
        int sid = IMBaseDefine.ServiceID.SID_TRAVEL_VALUE;
        int cid = IMBaseDefine.TravelCmdID.CID_TRAVEL_REQ_TRAVEL_LIST_VALUE;
        imSocketManager.sendRequest(travelInfoReq,sid,cid);
    }*/

    public void onRepTravelList(IMTravel.IMTravelInfoRsp travelInfoRsp) {
        logger.i("onRepTravelList");
        List<IMTravel.TravelInfo> travelInfoList =  travelInfoRsp.getTravelInfoList();
        logger.i("onRepTravelList#travelCnt:%d",travelInfoList.size());
        List<TravelEntity> needDb = new ArrayList<>();
        for(IMTravel.TravelInfo travelInfo:travelInfoList){
            TravelEntity travelEntity = ProtoBuf2JavaBean.getTravelEntity(travelInfo);
            travelEntityList.add(travelEntity);
            needDb.add(travelEntity);
        }
        dbInterface.batchInsertOrUpdateTravel(needDb);
        triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_OK));
    }

/*    private void reqTrainList() {
        logger.i("unread#reqTrainList");
        int loginId = IMLoginManager.instance().getLoginId();
        IMTravel.IMTravelTrainInfoReq  travelTrainInfoReq  = IMTravel.IMTravelTrainInfoReq
                .newBuilder()
                .setUserId(loginId)
                .build();
        int sid = IMBaseDefine.ServiceID.SID_TRAVEL_VALUE;
        int cid = IMBaseDefine.TravelCmdID.CID_TRAVEL_REQ_TRAIN_LIST_VALUE;
        imSocketManager.sendRequest(travelTrainInfoReq,sid,cid);
    }*/

    public void onRepTrainList(IMTravel.IMTravelTrainInfoRsp trainInfoRsp) {
        logger.i("onRepTrainList");
        List<IMTravel.TrainInfo> trainInfoList =  trainInfoRsp.getTrainInfoList();
        logger.i("onRepTrainList#trainCnt:%d",trainInfoList.size());
        List<TravelEntity> needDb = new ArrayList<>();
        for(IMTravel.TrainInfo trainInfo:trainInfoList){
/*            TravelEntity travelEntity = ProtoBuf2JavaBean.getTravelEntity(travelInfo);
            travelEntityList.add(travelEntity);
            needDb.add(travelEntity);*/
        }
        dbInterface.batchInsertOrUpdateTravel(needDb);
        triggerEvent(new TravelEvent(TravelEvent.Event.TRAVEL_LIST_OK));
    }

    /**----------------实体set/get-------------------------------*/
    public List<TravelEntity> getTravelEntityList() {
        return travelEntityList;
    }
}
