package com.zhizulx.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhizulx.tt.DB.Serializable.MapRoute;
import com.zhizulx.tt.DB.entity.DayRouteEntity;
import com.zhizulx.tt.DB.entity.DetailDispEntity;
import com.zhizulx.tt.DB.entity.HotelEntity;
import com.zhizulx.tt.DB.entity.RouteEntity;
import com.zhizulx.tt.DB.entity.SightEntity;
import com.zhizulx.tt.DB.sp.SystemConfigSp;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.event.LocationEvent;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.protobuf.IMBuddy;
import com.zhizulx.tt.ui.activity.CollectActivity;
import com.zhizulx.tt.ui.activity.SelectHotelActivity;
import com.zhizulx.tt.ui.activity.SelectSightActivity;
import com.zhizulx.tt.ui.adapter.DetailDispAdapter;
import com.zhizulx.tt.ui.adapter.DetailDispMenuAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.TravelUIHelper;
import com.zhizulx.tt.utils.WheelPicker;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.URLDecoder;

import de.greenrobot.event.EventBus;

import static com.zhizulx.tt.DB.dao.PlayConfigDao.Properties.TransportToolType;

/**
 * 设置页面
 */
public class DetailDispFragment extends TTBaseFragment{
	private View curView = null;
    private IMTravelManager travelManager;
    private RecyclerView rvDetailDisp;
    private DetailDispAdapter detailDispAdapter;
    private List<DetailDispEntity> detailDispEntityList = new ArrayList<>();
    private RecyclerView rvMenu;
    private DetailDispMenuAdapter detailDispMenuAdapter;
    private TextView detailDispMenuDisp;
    private LinearLayout lyDetailDispMenu;
    private List<String> day = new ArrayList<>();
    private boolean move = false;
    private LinearLayoutManager layoutManagerResult;
    private int mIndex = 0;
    private LinearLayout lyDetailDispAdjust;
    private TextView detailDispAdjustSight;
    private TextView detailDispAdjustHotel;
    private TextView detailDispAdjustTraffic;
    private ImageView routeCollection;
    private TextView routeStyle;
    private Boolean editing = false;
    private static final int NULL = 0;
    private static final int DAY = 1;
    private static final int SIGHT = 2;
    private static final int HOTEL = 3;
    private static final int TRAFFIC = 4;
    private static final int STATUS_DISP = 0;
    private static final int STATUS_EDIT = 1;
    private static final int STATUS_CANNOT_EDIT = 2;
    private LinearLayout lyTimeSelectWheel;
    private WheelPicker timeWheel;
    private DetailDispEntity timeSelect;
    private List<Integer> hotellist = new ArrayList<>();
    private int hotelEditPos = 0;
    private DetailDispEntity start;
    private DetailDispEntity end;
    private String trafficTitle;
    private String trafficUrl;
    private String startCity;
    private String endCity;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
                startCity = travelManager.getStartCity();
                endCity = travelManager.getEndCity();
                travelManager.initalRoute();
                showRoute();
                rvDayInit();
            }
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imServiceConnector.connect(this.getActivity());
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.travel_fragment_detail_disp, topContentView);
		initRes();
        initBtn();
        initDetailDisp();
		return curView;
	}

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        imServiceConnector.disconnect(getActivity());
    }

	@Override
	public void onResume() {
		super.onResume();
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_FIRST_USER){
            switch (resultCode) {
                case 100:
                    int hotelID = data.getIntExtra("result", 0);
                    HotelEntity hotelEntity = travelManager.getHotelByID(hotelID);
                    DetailDispEntity detailDispEntity = detailDispEntityList.get(hotelEditPos);
                    detailDispEntity.setTitle(hotelEntity.getName());
                    detailDispEntity.setImage(hotelEntity.getPic());
                    detailDispAdapter.notifyDataSetChanged();
                    break;
                case 101:
                    if(data.getIntExtra("collectStatus", 0) == 1) {
                        routeCollection.setBackgroundResource(R.drawable.collected);
                        hideTopRightButton();
                    }
                    break;
                case 102:
                    if(data.getBooleanExtra("result", false)) {
                        showRoute();
                    }
                    break;
            }
        }
    }

	/**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
        setTopTitle(getString(R.string.detail_disp));
        setTopLeftButton(R.drawable.tt_top_back);
        topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getActivity().finish();
                return;
            }
        });
        setTopRightButton(R.drawable.detail_disp_adjust);
        topRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing == true) {
                    setTopRightButton(R.drawable.detail_disp_adjust);
                    adjustFinish();
                    return;
                }

                if (lyDetailDispAdjust.getVisibility() == View.GONE) {
                    lyDetailDispAdjust.setVisibility(View.VISIBLE);
                    detailDispMenuDisp.setClickable(false);
                    lyDetailDispMenu.setVisibility(View.GONE);
                } else {
                    detailDispMenuDisp.setClickable(true);
                    lyDetailDispAdjust.setVisibility(View.GONE);
                }
            }
        });
        rvDetailDisp = (RecyclerView)curView.findViewById(R.id.rv_detail_disp);
        rvMenu = (RecyclerView)curView.findViewById(R.id.rv_detail_disp_menu);
        detailDispMenuDisp = (TextView)curView.findViewById(R.id.detail_disp_menu);
        lyDetailDispMenu = (LinearLayout)curView.findViewById(R.id.ly_detail_disp_menu);

        lyDetailDispAdjust = (LinearLayout)curView.findViewById(R.id.ly_detail_disp_adjust_disp);
        detailDispAdjustSight = (TextView)curView.findViewById(R.id.detail_disp_adjust_sight);
        detailDispAdjustHotel = (TextView)curView.findViewById(R.id.detail_disp_adjust_hotel);
        detailDispAdjustTraffic = (TextView)curView.findViewById(R.id.detail_disp_adjust_traffic);

        lyTimeSelectWheel = (LinearLayout)curView.findViewById(R.id.ly_time_select_wheel);
        timeWheel = (WheelPicker)curView.findViewById(R.id.time_select_wheel);

        routeCollection = (ImageView)curView.findViewById(R.id.route_collection);
        routeCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing == false) {
                    Intent collect = new Intent(getActivity(), CollectActivity.class);
                    startActivityForResult(collect, Activity.RESULT_FIRST_USER);
                } else {
                    Toast.makeText(getActivity(), "编辑中，请点击完成后再保存", Toast.LENGTH_SHORT).show();
                }
            }
        });

        routeStyle = (TextView)curView.findViewById(R.id.detail_disp_route_style);
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        detailDispMenuDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lyDetailDispMenu.getVisibility() == View.GONE) {
                    lyDetailDispMenu.setVisibility(View.VISIBLE);
                } else {
                    lyDetailDispMenu.setVisibility(View.GONE);
                }
            }
        });

        View.OnClickListener adjustListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.detail_disp_adjust_sight:
                        adjustSight();
                        lyDetailDispAdjust.setVisibility(View.GONE);
                        break;
                    case R.id.detail_disp_adjust_hotel:
                        adjustHotel();
                        break;
                    case R.id.detail_disp_adjust_traffic:
                        adjustTraffic();
                        break;
                    case R.id.ly_detail_disp_adjust_disp:
                        lyDetailDispAdjust.setVisibility(View.GONE);
                        break;
                }
            }
        };
        lyDetailDispAdjust.setOnClickListener(adjustListener);
        detailDispAdjustSight.setOnClickListener(adjustListener);
        detailDispAdjustHotel.setOnClickListener(adjustListener);
        detailDispAdjustTraffic.setOnClickListener(adjustListener);

        lyTimeSelectWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyTimeSelectWheel.setVisibility(View.GONE);
                topRightBtn.setClickable(true);
                timeSelect.setTime(timeWheel.getTimeData().replace("时", "") + ":00");
                detailDispAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initDetailDisp() {
        rvDetailDisp.setHasFixedSize(true);
        layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetailDisp.setLayoutManager(layoutManagerResult);

        DetailDispAdapter.OnRecyclerViewListener detailDispRVListener = new DetailDispAdapter.OnRecyclerViewListener() {
            @Override
            public void onSightClick(View v, int position) {
                if (v.getId() == R.id.detail_disp_sight_info) {
                    TravelUIHelper.openIntroduceSightActivity(getActivity(), detailDispEntityList.get(position).getDbID());
                } else {
                    Toast.makeText(getActivity(), "景点地图", Toast.LENGTH_SHORT).show();
                    //String sss = java.net.URLDecoder.decode("http://m.ctrip.com/webapp/train/v2/index?from=http%3A%2F%2Fm.ctrip.com%2Fhtml5%2F#!/list");
                    String sss = java.net.URLDecoder.decode("https://m.flight.qunar.com/ncs/page/flightlist?depCity=%E9%87%8D%E5%BA%86&arrCity=%E6%88%90%E9%83%BD&goDate=2017-04-11&sort=&airLine=&from=");
                    Log.e("yukiurl", sss);
                }
            }

            @Override
            public void onHotelClick(View v, int position) {
                switch (v.getId()) {
                    case R.id.detail_disp_hotel_info:
                        int dbID = detailDispEntityList.get(position).getDbID();
                        HotelEntity hotelEntity = travelManager.getHotelByID(dbID);
                        TravelUIHelper.openIntroduceHotelActivity(getActivity(), hotelEntity.getName(), hotelEntity.getUrl());
                        //TravelUIHelper.openIntroduceHotelActivity(getActivity(), "test", "https://m.ctrip.com/html5/flight/swift/domestic/BJS/SHA/2017-04-01");
                        //TravelUIHelper.openIntroduceHotelActivity(getActivity(), "test", "http://m.ctrip.com/webapp/train/v2/index?from=http%3A%2F%2Fm.ctrip.com%2Fhtml5%2F#!/list");
                        break;
                    case R.id.detail_disp_hotel_map:
                        Toast.makeText(getActivity(), "酒店地图", Toast.LENGTH_SHORT).show();
                        //EventBus.getDefault().postSticky(new LocationEvent(LocationEvent.Event.FRESH_EVENT));
                        MapRoute mapRoute = new MapRoute();
                        mapRoute.setCity("深圳");
                        mapRoute.setStartLongitude(113.937768);
                        mapRoute.setStartLatitude(22.57435);
                        mapRoute.setEndLongitude(113.930000);
                        mapRoute.setEndLatitude(22.570000);
                        TravelUIHelper.openMapRouteActivity(getActivity(), mapRoute);
                        break;
                    case R.id.detail_disp_hotel_select:
                        hotellist.clear();
                        hotellist.addAll(getOneDayHotel(detailDispEntityList.get(position).getDbID()));
                        hotelEditPos = position;
                        Intent hotelIntent = new Intent(getActivity(), SelectHotelActivity.class);
                        hotelIntent.putIntegerArrayListExtra("hotelList", new ArrayList(hotellist));
                        startActivityForResult(hotelIntent, Activity.RESULT_FIRST_USER);
                        break;
                }
            }

            @Override
            public void onTrafficClick(View v, int position) {
                switch (v.getId()) {
                    case R.id.detail_disp_traffic_select_result:
                        getTrafficContent(detailDispEntityList.get(position).getDbID(), detailDispEntityList.get(position).getTitle());
                        TravelUIHelper.openTrafficListActivity(getActivity(), trafficTitle, trafficUrl);
                        break;
                    case R.id.detail_disp_traffic_plane:
                        detailDispEntityList.get(position).setTitle("飞机");
                        detailDispAdapter.notifyDataSetChanged();
                        break;
                    case R.id.detail_disp_traffic_train:
                        detailDispEntityList.get(position).setTitle("火车");
                        detailDispAdapter.notifyDataSetChanged();
                        break;
                    case R.id.detail_disp_traffic_time:
                        lyTimeSelectWheel.setVisibility(View.VISIBLE);
                        timeSelect = detailDispEntityList.get(position);
                        topRightBtn.setClickable(false);
                        timeWheel.setTimeData(8);
                        break;
                }
            }
        };
        /*DetailDispEntity start = new DetailDispEntity();
        start.setType(4);
        start.setEdited(0);
        start.setTitle("飞机");
        start.setTime("09:00");
        DetailDispEntity one1 = new DetailDispEntity();
        one1.setType(1);
        one1.setTitle("Day1");
        DetailDispEntity one2 = new DetailDispEntity();
        one2.setType(1);
        one2.setTitle("Day2");
        DetailDispEntity one3 = new DetailDispEntity();
        one3.setType(1);
        one3.setTitle("Day3");
        DetailDispEntity two = new DetailDispEntity();
        two.setType(2);
        two.setTitle("鼓浪屿");
        two.setEdited(0);
        DetailDispEntity three = new DetailDispEntity();
        three.setType(3);
        three.setTitle("七天");
        three.setEdited(0);
        DetailDispEntity end = new DetailDispEntity();
        end.setType(4);
        end.setTitle("火车");
        end.setTime("21:00");
        end.setEdited(0);
        DetailDispEntity blank = new DetailDispEntity();
        blank.setType(NULL);
        detailDispEntityList.add(one1);
        detailDispEntityList.add(start);
        detailDispEntityList.add(two);
        detailDispEntityList.add(two);
        detailDispEntityList.add(two);
        detailDispEntityList.add(three);
        detailDispEntityList.add(one2);
        detailDispEntityList.add(two);
        detailDispEntityList.add(two);
        detailDispEntityList.add(three);
        detailDispEntityList.add(one3);
        detailDispEntityList.add(two);
        detailDispEntityList.add(end);
        detailDispEntityList.add(blank);*/

        detailDispAdapter = new DetailDispAdapter(getActivity(), detailDispEntityList);
        detailDispAdapter.setOnRecyclerViewListener(detailDispRVListener);
        rvDetailDisp.setAdapter(detailDispAdapter);
        rvDetailDisp.addOnScrollListener(new RecyclerViewListener());

        rvMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManagerMenu = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvMenu.setLayoutManager(layoutManagerMenu);
        detailDispMenuAdapter = new DetailDispMenuAdapter(getActivity(), rvDayInit());
        DetailDispMenuAdapter.OnRecyclerViewListener dayListener = new DetailDispMenuAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                roll2Day(day.get(position));
            }
        };
        detailDispMenuAdapter.setOnRecyclerViewListener(dayListener);
        rvMenu.setAdapter(detailDispMenuAdapter);
    }

    private List<String> rvDayInit() {
        day.clear();
        for (DetailDispEntity index : detailDispEntityList) {
            if (index.getType() == DAY) {
                day.add(index.getTitle());
            }
        }
        return day;
    }

    private void roll2Day(String targetDay) {
        int pos = 0;
        for (DetailDispEntity index : detailDispEntityList) {
            if (index.getType() == DAY && index.getTitle().equals(targetDay)) {
                mIndex = pos;
                moveToPosition(pos);
                return;
            }
            pos ++;
        }
    }

    private void smoothMoveToPosition(int n) {
        int firstItem = layoutManagerResult.findFirstVisibleItemPosition();
        int lastItem = layoutManagerResult.findLastVisibleItemPosition();
        if (n <= firstItem ){
            rvDetailDisp.smoothScrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = rvDetailDisp.getChildAt(n - firstItem).getTop();
            rvDetailDisp.smoothScrollBy(0, top);
        }else{
            rvDetailDisp.smoothScrollToPosition(n);
            move = true;
        }
    }

    private void moveToPosition(int n) {
        int firstItem = layoutManagerResult.findFirstVisibleItemPosition();
        int lastItem = layoutManagerResult.findLastVisibleItemPosition();
        if (n <= firstItem ){
            rvDetailDisp.scrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = rvDetailDisp.getChildAt(n - firstItem).getTop();
            rvDetailDisp.scrollBy(0, top);
        }else{
            rvDetailDisp.scrollToPosition(n);
            move = true;
        }
    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE){
                move = false;
                int n = mIndex - layoutManagerResult.findFirstVisibleItemPosition();
                if ( 0 <= n && n < rvDetailDisp.getChildCount()){
                    int top = rvDetailDisp.getChildAt(n).getTop();
                    rvDetailDisp.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move){
                move = false;
                int n = mIndex - layoutManagerResult.findFirstVisibleItemPosition();
                if ( 0 <= n && n < rvDetailDisp.getChildCount()){
                    int top = rvDetailDisp.getChildAt(n).getTop();
                    rvDetailDisp.scrollBy(0, top);
                }
            }
        }
    }

    private void adjustSight() {
        Intent intent = new Intent(getActivity(), SelectSightActivity.class);
        startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }

    private void adjustHotel() {
        editing = true;
        int firstHotel = 0xffffffff;
        int indexHotel = 0;
        for (DetailDispEntity index : detailDispEntityList) {
            if (index.getType() == HOTEL) {
                if (firstHotel == 0xffffffff) {
                    firstHotel = indexHotel;
                }
                index.setEdited(STATUS_EDIT);
            } else {
                index.setEdited(STATUS_CANNOT_EDIT);
            }
            indexHotel ++;
        }
        setTopRightButton(R.drawable.detail_disp_adjust_finish);
        lyDetailDispAdjust.setVisibility(View.GONE);
        detailDispAdapter.notifyDataSetChanged();
        mIndex = firstHotel;
        moveToPosition(firstHotel);
    }

    private void adjustTraffic() {
        editing = true;
        for (DetailDispEntity index : detailDispEntityList) {
            if (index.getType() == TRAFFIC) {
                index.setEdited(STATUS_EDIT);
            } else {
                index.setEdited(STATUS_CANNOT_EDIT);
            }
        }
        setTopRightButton(R.drawable.detail_disp_adjust_finish);
        lyDetailDispAdjust.setVisibility(View.GONE);
        detailDispAdapter.notifyDataSetChanged();
    }

    private void adjustFinish() {
        editing = false;
        for (DetailDispEntity index : detailDispEntityList) {
            index.setEdited(STATUS_DISP);
        }
        detailDispAdapter.notifyDataSetChanged();
        mIndex = 0;
        moveToPosition(0);
    }

    private void showRoute() {
        detailDispEntityList.clear();
        RouteEntity routeEntity = travelManager.getRouteEntity();
        routeStyle.setText(routeEntity.getRouteType());
        start = new DetailDispEntity();
        start.setType(TRAFFIC);
        start.setTitle(getTrafficType(routeEntity.getStartTrafficTool()));
        start.setTime(String.format("%02d:00", routeEntity.getStartTime()));
        detailDispEntityList.add(start);

        int day = 0;
        for (DayRouteEntity dayRouteEntity : routeEntity.getDayRouteEntityList()) {
            day ++;
            detailDispEntityList.addAll(getOneDayRoute(day, dayRouteEntity));
        }

        end = new DetailDispEntity();
        end.setType(TRAFFIC);
        end.setTitle(getTrafficType(routeEntity.getEndTrafficTool()));
        end.setTime(String.format("%02d:00", routeEntity.getEndTime()));
        detailDispEntityList.add(end);

        DetailDispEntity blank = new DetailDispEntity();
        blank.setType(NULL);
        detailDispEntityList.add(blank);

        detailDispAdapter.notifyDataSetChanged();
    }

    private String getTrafficType(int trafficTool) {
        String trafficType = getString(R.string.plane);
        switch (trafficTool) {
            case IMBuddy.TransportToolType.AIRPLANE_VALUE:
                trafficType = getString(R.string.plane);
                break;
            case IMBuddy.TransportToolType.TAXI_VALUE:
                trafficType = getString(R.string.train);
                break;
        }
        return trafficType;
    }

    private List<DetailDispEntity> getOneDayRoute(int day, DayRouteEntity dayRouteEntity) {
        List<DetailDispEntity> detailDispEntityList = new ArrayList<>();
        if (travelManager.getdBInitFin() == false) {
            return detailDispEntityList;
        }

        DetailDispEntity detailDispEntity;

        detailDispEntity = new DetailDispEntity();
        detailDispEntity.setType(DAY);
        detailDispEntity.setTitle("Day " + day);
        detailDispEntityList.add(detailDispEntity);

        for (int i : dayRouteEntity.getSightIDList()) {
            SightEntity sightEntity = travelManager.getSightByID(i);
            if (sightEntity == null) {
                Log.e("getOneDayRoute", "not find sightEntity " + i);
                continue;
            }
            sightEntity.setSelect(1);
            detailDispEntity = new DetailDispEntity();
            detailDispEntity.setDbID(sightEntity.getPeerId());
            detailDispEntity.setType(SIGHT);
            detailDispEntity.setTitle(sightEntity.getName());
            detailDispEntity.setImage(sightEntity.getPic());
            detailDispEntityList.add(detailDispEntity);
        }

        HotelEntity hotelEntity = travelManager.getHotelByID(dayRouteEntity.getHotelSelected());
        if (hotelEntity == null) {
            Log.e("getOneDayRoute", "not find hotelEntity " + hotelEntity.getPeerId());
            return detailDispEntityList;
        }
        hotelEntity.setSelect(1);
        detailDispEntity = new DetailDispEntity();
        detailDispEntity.setDbID(hotelEntity.getPeerId());
        detailDispEntity.setType(HOTEL);
        detailDispEntity.setTitle(hotelEntity.getName());
        detailDispEntity.setImage(hotelEntity.getPic());
        detailDispEntityList.add(detailDispEntity);

        return detailDispEntityList;
    }

    private List<Integer> getOneDayHotel(int dayHotelID) {
        List<Integer> hotelList = new ArrayList<>();
        for (DayRouteEntity dayRouteEntity : travelManager.getRouteEntity().getDayRouteEntityList()) {
            if (dayRouteEntity.getHotelIDList().contains(dayHotelID)) {
                hotelList.addAll(dayRouteEntity.getHotelIDList());
            }
        }
        return hotelList;
    }

    private void getTrafficContent(int id, String trafficType) {
        // https://m.flight.qunar.com/ncs/page/flightlist?depCity=重庆&arrCity=成都&goDate=2017-04-11&sort=&airLine=&from=
        // http://touch.train.qunar.com/trainList.html?startStation=杭州&endStation=西安&date=2017-04-11&searchType=stasta&bd_source=&filterTrainType=&filterTrainType=&filterTrainType=

        String planeFormat = "https://m.flight.qunar.com/ncs/page/flightlist?depCity=%s&arrCity=%s&goDate=%s&sort=&airLine=&from=";
        String trainFormat = "http://touch.train.qunar.com/trainList.html?startStation=%s&endStation=%s&date=%s&searchType=stasta&bd_source=&filterTrainType=&filterTrainType=&filterTrainType=";
        RouteEntity routeEntity = travelManager.getRouteEntity();
        String destination = travelManager.getCityNameByCode(routeEntity.getCityCode());
        String startCity = travelManager.getStartCity();
        String endCity = travelManager.getEndCity();
        String startDate = travelManager.getStartDate();
        String endDate = travelManager.getEndDate();
        if (id == start.getDbID()) {
            trafficTitle = startCity + "-" + destination;
            if (trafficType.equals(getString(R.string.plane))) {
                trafficUrl = String.format(planeFormat, startCity, destination, startDate);
            } else {
                trafficUrl = String.format(trainFormat, startCity, destination, startDate);
            }
        } else {
            trafficTitle = destination + "-" + endCity;
            if (trafficType.equals(getString(R.string.plane))) {
                trafficUrl = String.format(planeFormat, destination, endCity, endDate);
            } else {
                trafficUrl = String.format(trainFormat, destination, endCity, endDate);
            }
        }
    }
}
