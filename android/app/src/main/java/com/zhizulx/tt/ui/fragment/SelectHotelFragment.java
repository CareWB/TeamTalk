package com.zhizulx.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhizulx.tt.DB.entity.HotelEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.config.UrlConstant;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.adapter.HotelAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置页面
 */
public class SelectHotelFragment extends TTBaseFragment{
	private View curView = null;
    private IMService imService;
    private IMTravelManager travelManager;
    private TextView total;
    private TextView economical;
    private TextView luxurious;
    private TextView youth;
    private TextView home;
    private RecyclerView rvHotel;
    private HotelAdapter hotelAdapter;
    private List<HotelEntity> hotelEntityArrayList = new ArrayList<>();
    private List<HotelEntity> selectHotelList = new ArrayList<>();
    String Tag = "全部";

    private PopupWindow mPopupWindow;
    private LinearLayout pop;
    private TextView comprehensive;
    private TextView distance;
    private TextView price;
    private LinearLayout lyPop;
    static final int COMPREHENSIVE = 0;
    static final int DISTANCE = 1;
    static final int PRICE = 2;
    private int spinner_select = COMPREHENSIVE;
    private TextView selectHotelDropText;

    private int roomNum = 1;
    private final static int MAX_ROOM_SUM = 6;
    private final static int MIN_ROOM_SUM = 1;
    private ImageButton room_num_add;
    private ImageButton room_num_sub;
    private TextView room_num;

    private Button createRoute;

    private Map<Integer, String> selectFlag = new HashMap<>();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
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
		curView = inflater.inflate(R.layout.travel_fragment_select_hotel, topContentView);

		initRes();
        initBtn();
        testCase();
        initPopupWindow();
        initHotel();
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
                    break;
            }
        }
    }

	/**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
		setTopTitle(getString(R.string.select_hotel));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
                getActivity().finish();
			}
		});

        createRoute = (Button)curView.findViewById(R.id.create_route);

        total = (TextView)curView.findViewById(R.id.select_total);
        economical = (TextView)curView.findViewById(R.id.select_economical);
        luxurious = (TextView)curView.findViewById(R.id.select_luxurious);
        youth = (TextView)curView.findViewById(R.id.select_youth);
        home = (TextView)curView.findViewById(R.id.select_home);
        selectFlag.put(R.id.select_total, "全部");
        selectFlag.put(R.id.select_economical, "经济型");
        selectFlag.put(R.id.select_luxurious, "豪华型");
        selectFlag.put(R.id.select_youth, "青旅");
        selectFlag.put(R.id.select_home, "民宿");

        pop = (LinearLayout)curView.findViewById(R.id.select_hotel_drop);
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.showAsDropDown(curView.findViewById(R.id.select_hotel_drop));
            }
        });

        rvHotel = (RecyclerView)curView.findViewById(R.id.rv_hotel);

        room_num_add = (ImageButton)curView.findViewById(R.id.select_hotel_room_num_add);
        room_num_sub = (ImageButton)curView.findViewById(R.id.select_hotel_room_num_sub);
        room_num = (TextView)curView.findViewById(R.id.select_hotel_room_num);
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Tag = selectFlag.get(id);
                tagProcess();
                menuProcess();
                buttonDisp(id);
                hotelAdapter.notifyDataSetChanged();
            }
        };
        total.setOnClickListener(listener);
        economical.setOnClickListener(listener);
        luxurious.setOnClickListener(listener);
        youth.setOnClickListener(listener);
        home.setOnClickListener(listener);

        View.OnClickListener roomNumListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clacRoomNum(v.getId());
            }
        };
        room_num_add.setOnClickListener(roomNumListener);
        room_num_sub.setOnClickListener(roomNumListener);

        createRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageHotel();
                travelManager.reqCreateTravel();
                TravelUIHelper.openDetailDispActivity(getActivity());
            }
        });
    }

    private void buttonDisp(int id) {
        total.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        total.setTextColor(getResources().getColor(R.color.not_clicked));
        economical.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        economical.setTextColor(getResources().getColor(R.color.not_clicked));
        luxurious.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        luxurious.setTextColor(getResources().getColor(R.color.not_clicked));
        youth.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        youth.setTextColor(getResources().getColor(R.color.not_clicked));
        home.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        home.setTextColor(getResources().getColor(R.color.not_clicked));

        switch (id) {
            case R.id.select_total:
                total.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                total.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_economical:
                economical.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                economical.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_luxurious:
                luxurious.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                luxurious.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_youth:
                youth.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                youth.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_home:
                home.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                home.setTextColor(getResources().getColor(R.color.clicked));
                break;
        }
    }

    private void testCase() {
        String pre = UrlConstant.PIC_URL_PREFIX;

        HotelEntity qitian = new HotelEntity();
        qitian.setName("7天快捷酒店");
        qitian.setPic(pre+"qitiankuaijiejiudian.png");
        qitian.setStar(9);
        qitian.setUrl("http://www.baidu.com");
        qitian.setTag("经济型");
        qitian.setMustGo(1);
        qitian.setPrice(654);
        qitian.setDistance(123);
        qitian.setStartTime("12:00");
        qitian.setEndTime("14:00");

        HotelEntity rihang = new HotelEntity();
        rihang.setName("厦门日航酒店");
        rihang.setPic(pre+"rihangjiudian.png");
        rihang.setStar(10);
        rihang.setUrl("http://www.baidu.com");
        rihang.setTag("豪华型");
        rihang.setPrice(321);
        rihang.setDistance(456);
        rihang.setStartTime("12:00");
        rihang.setEndTime("14:00");

        hotelEntityArrayList.add(qitian);
        hotelEntityArrayList.add(rihang);

        selectHotelList.addAll(hotelEntityArrayList);
        for (HotelEntity hotelEntity:selectHotelList) {
            if (hotelEntity.getMustGo() == 1) {
                hotelEntity.setSelect(1);
            }
        }
    }

    private void initHotel() {
        rvHotel.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvHotel.setLayoutManager(layoutManagerResult);
        HotelAdapter.OnRecyclerViewListener hotelRVListener = new HotelAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                TravelUIHelper.openIntroduceHotelActivity(getActivity(), selectHotelList.get(position).getPeerId());
            }

            @Override
            public void onSelectClick(int position, View v) {
                HotelEntity hotelEntity = selectHotelList.get(position);
                if (hotelEntity.getSelect() == 1) {
                    hotelEntity.setSelect(0);
                } else {
                    hotelEntity.setSelect(1);
                }
                hotelAdapter.notifyDataSetChanged();
            }
        };
        hotelAdapter = new HotelAdapter(getActivity(), selectHotelList);
        hotelAdapter.setOnRecyclerViewListener(hotelRVListener);
        rvHotel.setAdapter(hotelAdapter);
    }

    private void menuProcess() {
        switch (spinner_select) {
            case COMPREHENSIVE:
                break;
            case DISTANCE:
                ComparatorDistance distance = new ComparatorDistance();
                Collections.sort(selectHotelList, distance);
                break;
            case PRICE:
                ComparatorPrice price = new ComparatorPrice();
                Collections.sort(selectHotelList, price);
                break;
        }
    }

    private void tagProcess() {
        selectHotelList.clear();
        if (Tag.equals("全部")) {
            selectHotelList.addAll(hotelEntityArrayList);
        } else {
            for (HotelEntity hotelEntity:hotelEntityArrayList) {
                if (hotelEntity.getTag().contains(Tag)) {
                    selectHotelList.add(hotelEntity);
                }
            }
        }
    }

    private void initPopupWindow() {
        View popupView = curView.inflate(getActivity(), R.layout.select_hotel_popup_window, null);
        comprehensive = (TextView) popupView.findViewById(R.id.select_hotel_pop_comprehensive);
        distance = (TextView) popupView.findViewById(R.id.select_hotel_pop_distance);
        price = (TextView) popupView.findViewById(R.id.select_hotel_pop_price);
        lyPop = (LinearLayout) popupView.findViewById(R.id.ly_select_hotel_pop);
        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        //mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);

        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });

        View.OnClickListener popupListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.select_hotel_pop_comprehensive:
                        spinner_select = COMPREHENSIVE;
                        tagProcess();
                        menuProcess();
                        hotelAdapter.notifyDataSetChanged();
                        mPopupWindow.dismiss();
                        break;

                    case R.id.select_hotel_pop_distance:
                        spinner_select = DISTANCE;
                        tagProcess();
                        menuProcess();
                        hotelAdapter.notifyDataSetChanged();
                        mPopupWindow.dismiss();
                        break;

                    case R.id.select_hotel_pop_price:
                        spinner_select = PRICE;
                        tagProcess();
                        menuProcess();
                        hotelAdapter.notifyDataSetChanged();
                        mPopupWindow.dismiss();
                        break;

                    case R.id.ly_select_hotel_pop:
                        mPopupWindow.dismiss();
                        break;
                }
            }
        };
        lyPop.setOnClickListener(popupListener);
        comprehensive.setOnClickListener(popupListener);
        distance.setOnClickListener(popupListener);
        price.setOnClickListener(popupListener);
    }

    private void clacRoomNum(int opt) {
        roomNum = Integer.parseInt(room_num.getText().toString());

        if (opt == R.id.select_hotel_room_num_add) {
            roomNum ++;
            if (roomNum > MAX_ROOM_SUM) {
                roomNum = MAX_ROOM_SUM;
            }
        } else {
            roomNum --;
            if (roomNum < MIN_ROOM_SUM) {
                roomNum = MIN_ROOM_SUM;
            }
        }

        room_num_add.setBackgroundResource(R.drawable.create_travel_add);
        room_num_sub.setBackgroundResource(R.drawable.create_travel_sub);
        if (roomNum == MAX_ROOM_SUM) {
            room_num_add.setBackgroundResource(R.drawable.create_travel_add_grey);
            room_num_add.setClickable(false);
        } else {
            room_num_add.setClickable(true);
        }

        if (roomNum == MIN_ROOM_SUM) {
            room_num_sub.setBackgroundResource(R.drawable.create_travel_sub_grey);
            room_num_sub.setClickable(false);
        } else {
            room_num_sub.setClickable(true);
        }

        room_num.setText(String.valueOf(roomNum));
    }

    private void storageHotel() {
        if (travelManager != null) {
            List<HotelEntity> hotelEntityList = travelManager.getMtCity().get(0).getHotelList();
            hotelEntityList.clear();
            for (HotelEntity hotelEntity:selectHotelList) {
                if (hotelEntity.getSelect() == 0) {
                    continue;
                }
                hotelEntityList.add(hotelEntity);
            }
        }
    }

    public class ComparatorDistance implements Comparator {
        public int compare(Object arg0, Object arg1) {

            HotelEntity left = (HotelEntity)arg0;
            HotelEntity right = (HotelEntity)arg1;

            //首先比较出现次数，如果相同，则比较名字
            Integer num = left.getDistance();
            Integer num2 = right.getDistance();
            int flag = num.compareTo(num2);
            if(flag == 0){
                return (left.getName()).compareTo(right.getName());
            }else{
                return flag;
            }
        }
    }

    public class ComparatorPrice implements Comparator {
        public int compare(Object arg0, Object arg1) {

            HotelEntity left = (HotelEntity)arg0;
            HotelEntity right = (HotelEntity)arg1;

            //首先比较出现次数，如果相同，则比较名字
            Integer num = left.getPrice();
            Integer num2 = right.getPrice();
            int flag = num.compareTo(num2);
            if(flag == 0){
                return (left.getName()).compareTo(right.getName());
            }else{
                return flag;
            }
        }
    }

}
