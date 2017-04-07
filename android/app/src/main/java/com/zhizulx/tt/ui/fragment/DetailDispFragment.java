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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhizulx.tt.DB.Serializable.MapRoute;
import com.zhizulx.tt.DB.entity.DetailDispEntity;
import com.zhizulx.tt.DB.entity.HotelEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.event.LocationEvent;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.activity.SelectHotelActivity;
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
    private Boolean editing = false;
    private static final int DAY = 1;
    private static final int SIGHT = 2;
    private static final int HOTEL = 3;
    private static final int TRAFFIC = 4;
    private LinearLayout lyTimeSelectWheel;
    private WheelPicker timeWheel;
    private DetailDispEntity timeSelect;
    private List<Integer> hotellist = new ArrayList<>();
    private int hotelEditPos = 0;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
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
                    String sss = java.net.URLDecoder.decode("http://m.ctrip.com/webapp/train/v2/index?from=http%3A%2F%2Fm.ctrip.com%2Fhtml5%2F#!/list");
                    Log.e("yukiurl", sss);
                }
            }

            @Override
            public void onHotelClick(View v, int position) {
                switch (v.getId()) {
                    case R.id.detail_disp_hotel_info:
                        TravelUIHelper.openIntroduceHotelActivity(getActivity(), "test", "http://m.ctrip.com/webapp/hotel/hoteldetail/890106.html");
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
                        hotellist.add(1);
                        hotellist.add(2);
                        hotellist.add(3);
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
                        TravelUIHelper.openTrafficListActivity(getActivity());
                        break;
                    case R.id.detail_disp_traffic_plane:
                        detailDispEntityList.get(position).setTitle("飞机");
                        break;
                    case R.id.detail_disp_traffic_train:
                        detailDispEntityList.get(position).setTitle("火车");
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
        DetailDispEntity start = new DetailDispEntity();
        start.setType(4);
        start.setEdited(1);
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
        two.setEdited(1);
        DetailDispEntity three = new DetailDispEntity();
        three.setType(3);
        three.setTitle("七天");
        three.setEdited(1);
        DetailDispEntity end = new DetailDispEntity();
        end.setType(4);
        end.setTitle("火车");
        end.setTime("21:00");
        end.setEdited(1);
        DetailDispEntity blank = new DetailDispEntity();
        blank.setType(0);
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
        detailDispEntityList.add(blank);

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
            if (index.getType() == 1) {
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
        TravelUIHelper.openSelectSightActivity(getActivity());
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
                index.setEdited(2);
            } else {
                index.setEdited(0);
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
                index.setEdited(1);
            } else {
                index.setEdited(0);
            }
        }
        setTopRightButton(R.drawable.detail_disp_adjust_finish);
        lyDetailDispAdjust.setVisibility(View.GONE);
        detailDispAdapter.notifyDataSetChanged();
    }

    private void adjustFinish() {
        editing = false;
        for (DetailDispEntity index : detailDispEntityList) {
            index.setEdited(1);
        }
        detailDispAdapter.notifyDataSetChanged();
        mIndex = 0;
        moveToPosition(0);
    }
}
