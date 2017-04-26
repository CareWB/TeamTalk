package com.zhizulx.tt.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhizulx.tt.DB.entity.RouteEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.event.TravelEvent;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.activity.SelectDateActivity;
import com.zhizulx.tt.ui.activity.SelectPlaceActivity;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.ui.widget.city.CityActivity;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 设置页面
 */
public class CreateTravelFragment extends TTBaseFragment{
	private View curView = null;
    private RelativeLayout bnStart;
    private RelativeLayout bnEnd;
    private RelativeLayout time;
    private TextView duration;
    private TextView betweenTime;
    private RelativeLayout place;
    private TextView startCity;
    private TextView endCity;
    private IMTravelManager travelManager;

/*    private ImageView economicComfort;
    private ImageView economicEfficiency;
    private ImageView luxuryQuality;
    private ImageView experienceCancel;
    private LinearLayout lyExperiencePop;
    private TextView createTravelExperience;
    private PopupWindow mExperiencePopupWindow;
    RelativeLayout experience;*/

    private ImageView literature;
    private ImageView comfort;
    private ImageView exploration;
    private ImageView excite;
    private ImageView encounter;
    private ImageView routeCancel;
    private LinearLayout lyRoutePop;
    private TextView createTravelRoute;
    private PopupWindow mRoutePopupWindow;
    private RelativeLayout route;
    private Button next;
    private RouteEntity routeEntity;
    private Dialog dialog;

    private final static int START_CITY = 0;
    private final static int END_CITY = 1;

    private Date startDate;
    private Date endDate;
    private String strStartPlace = "";
    private String strEndPlace = "";;
    private String strStartDate = "";
    private String strEndDate = "";
    private String destination = "";
    private int iduration = 0;
    private IMService imService;
    //private Map<Integer, String> mapExperience = new HashMap<>();
    private Map<Integer, String> mapRoute = new HashMap<>();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            travelManager = imService.getTravelManager();
        }

        @Override
        public void onServiceDisconnected() {
        }
    };


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imServiceConnector.connect(this.getActivity());
        EventBus.getDefault().register(this);
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.travel_fragment_create_travel, topContentView);
		initRes();
        initBtn();
        //initExperiencePopupWindow();
        initRoutePopupWindow();
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
        EventBus.getDefault().unregister(this);
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
                    if (data.getIntExtra("type", START_CITY) == START_CITY) {
                        startCity.setText(data.getStringExtra("city"));
                        strStartPlace = data.getStringExtra("city");
                    } else {
                        endCity.setText(data.getStringExtra("city"));
                        strEndPlace = data.getStringExtra("city");
                    }
                    if (imService != null) {
                        imService.getTravelManager().getConfigEntity().setStartCity(strStartPlace);
                        imService.getTravelManager().getConfigEntity().setEndCity(strEndPlace);
                    }
                    break;
                case 101:
                    TextView place = (TextView)curView.findViewById(R.id.create_travel_place);
                    place.setText(data.getStringExtra("city"));
                    destination = data.getStringExtra("city");
                    if (imService != null) {
                        imService.getTravelManager().getConfigEntity().setDestination(destination);
                    }
                    break;
                case 102:
                    java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd");
                    try {
                        startDate =  formatter.parse(data.getStringExtra("startDate"));
                        endDate =  formatter.parse(data.getStringExtra("endDate"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateProcess();
                    if (imService != null) {
                        imService.getTravelManager().getConfigEntity().setStartDate(strStartDate);
                        imService.getTravelManager().getConfigEntity().setEndDate(strEndDate);
                        imService.getTravelManager().getConfigEntity().setDuration(iduration);
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
		setTopTitle(getActivity().getString(R.string.create_route));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});

        bnStart = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_start);
        bnEnd = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_end);
        startCity = (TextView)curView.findViewById(R.id.create_travel_start);
        endCity = (TextView)curView.findViewById(R.id.create_travel_end);
        time = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_time);
        duration = (TextView)curView.findViewById(R.id.tcreate_travel_time);
        betweenTime = (TextView)curView.findViewById(R.id.create_travel_time);
        place = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_place);
        //experience = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_experience);
        route = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_route);
        next = (Button)curView.findViewById(R.id.create_travel_next_step);

/*        mapExperience.put(R.id.create_travel_experience_economic_comfort, getString(R.string.economical_comfort));
        mapExperience.put(R.id.create_travel_experience_economic_efficiency, getString(R.string.economical_efficiency));
        mapExperience.put(R.id.create_travel_experience_luxury_quality, getString(R.string.luxury_quality));
        createTravelExperience = (TextView)curView.findViewById(R.id.create_travel_experience);
        createTravelExperience.setText(mapExperience.get(R.id.create_travel_experience_economic_comfort));*/

        mapRoute.put(R.id.create_travel_route_literature, getString(R.string.route_literature));
        mapRoute.put(R.id.create_travel_route_comfort, getString(R.string.route_comfort));
        mapRoute.put(R.id.create_travel_route_exploration, getString(R.string.route_exploration));
        mapRoute.put(R.id.create_travel_route_excite, getString(R.string.route_excite));
        mapRoute.put(R.id.create_travel_route_encounter, getString(R.string.route_encounter));;
        createTravelRoute = (TextView)curView.findViewById(R.id.create_travel_route);
        createTravelRoute.setText(mapRoute.get(R.id.create_travel_route_literature));
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        View.OnClickListener createTravelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (v.getId()) {
                    case R.id.rlcreate_travel_start:
                    case R.id.rlcreate_travel_end:
                        jump2CitySelect(id);
                        break;

                    case R.id.rlcreate_travel_time:
                        jump2TimeSelect();
                        break;

                    case R.id.rlcreate_travel_place:
                        jump2PlaceSelect();
                        break;

/*                    case R.id.rlcreate_travel_experience:
                        mExperiencePopupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.travel_fragment_create_travel, null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;*/

                    case R.id.rlcreate_travel_route:
                        mRoutePopupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.travel_fragment_create_travel, null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;

                    case R.id.create_travel_next_step:
                        if (checkResult()) {
                            storeRoute();
                            travelManager.reqCreateRoute();
                            mHandler.postDelayed(runnable, 10000);
                            dialog = TravelUIHelper.showCalculateDialog(getActivity());
                        }
                        break;
                }
            }
        };

        bnStart.setOnClickListener(createTravelListener);
        bnEnd.setOnClickListener(createTravelListener);
        time.setOnClickListener(createTravelListener);
        place.setOnClickListener(createTravelListener);
        //experience.setOnClickListener(createTravelListener);
        route.setOnClickListener(createTravelListener);
        next.setOnClickListener(createTravelListener);
    }

    private boolean checkResult() {
        if (strStartDate.equals("")) {
            errorHint(getString(R.string.create_travel_not_select_start_date));
            return false;
        }

        if (strEndDate.equals("")) {
            errorHint(getString(R.string.create_travel_not_select_end_date));
            return false;
        }

        if (destination.equals("")) {
            errorHint(getString(R.string.create_travel_not_select_destination));
            return false;
        }

        if (strStartPlace.equals("")) {
            errorHint(getString(R.string.create_travel_not_select_start_place));
            return false;
        }

        if (strEndPlace.equals("")) {
            errorHint(getString(R.string.create_travel_not_select_end_place));
            return false;
        }

        return true;
    }

    private void errorHint(String hint) {
        Toast.makeText(getActivity(), hint, Toast.LENGTH_SHORT).show();
    }

    private void jump2CitySelect(int opt) {
        Intent citySelect = new Intent(getActivity(), CityActivity.class);
        if (opt == R.id.rlcreate_travel_start) {
            citySelect.putExtra("title", "出发城市");
            citySelect.putExtra("type", START_CITY);
        } else {
            citySelect.putExtra("title", "返回城市");
            citySelect.putExtra("type", END_CITY);
        }
        startActivityForResult(citySelect, Activity.RESULT_FIRST_USER);
    }

    private void jump2PlaceSelect() {
        Intent placeSelect = new Intent(getActivity(), SelectPlaceActivity.class);
        startActivityForResult(placeSelect, Activity.RESULT_FIRST_USER);
    }

    private void jump2TimeSelect() {
        Intent dateSelect = new Intent(getActivity(), SelectDateActivity.class);
        startActivityForResult(dateSelect, Activity.RESULT_FIRST_USER);
    }

    private void dateProcess() {
        if (endDate == null || startDate == null) {
            return;
        }
        long duration = (endDate.getTime()-startDate.getTime())/(1000*60*60*24);
        duration ++;
        java.text.SimpleDateFormat formatter = new SimpleDateFormat( "MM.dd");

        strStartDate = formatter.format(startDate);
        strEndDate = formatter.format(endDate);

        String date = strStartDate+" - "+strEndDate;//格式化数据
        iduration = (int)duration;
        this.duration.setText(duration+"天");
        this.betweenTime.setText(date);
    }

/*    private void initExperiencePopupWindow() {
        View popupView = curView.inflate(getActivity(), R.layout.travel_select_experience_popup_window, null);
        economicComfort = (ImageView) popupView.findViewById(R.id.create_travel_experience_economic_comfort);
        economicEfficiency = (ImageView) popupView.findViewById(R.id.create_travel_experience_economic_efficiency);
        luxuryQuality = (ImageView) popupView.findViewById(R.id.create_travel_experience_luxury_quality);
        experienceCancel = (ImageView) popupView.findViewById(R.id.create_travel_experience_cancel);
        lyExperiencePop = (LinearLayout) popupView.findViewById(R.id.ly_create_travel_experience_pop);
        mExperiencePopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mExperiencePopupWindow.setTouchable(true);
        mExperiencePopupWindow.setOutsideTouchable(true);
        mExperiencePopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        //mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);

        mExperiencePopupWindow.getContentView().setFocusableInTouchMode(true);
        mExperiencePopupWindow.getContentView().setFocusable(true);
        mExperiencePopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mExperiencePopupWindow != null && mExperiencePopupWindow.isShowing()) {
                        mExperiencePopupWindow.dismiss();
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
                    case R.id.create_travel_experience_economic_comfort:
                    case R.id.create_travel_experience_economic_efficiency:
                    case R.id.create_travel_experience_luxury_quality:
                        createTravelExperience.setText(mapExperience.get(v.getId()));
                        mExperiencePopupWindow.dismiss();
                        break;

                    case R.id.ly_create_travel_experience_pop:
                    case R.id.create_travel_experience_cancel:
                        mExperiencePopupWindow.dismiss();
                        break;
                }
            }
        };
        lyExperiencePop.setOnClickListener(popupListener);
        economicComfort.setOnClickListener(popupListener);
        economicEfficiency.setOnClickListener(popupListener);
        luxuryQuality.setOnClickListener(popupListener);
        experienceCancel.setOnClickListener(popupListener);
    }*/

    private void initRoutePopupWindow() {
        View popupView = curView.inflate(getActivity(), R.layout.travel_select_route_popup_window, null);
        literature = (ImageView) popupView.findViewById(R.id.create_travel_route_literature);
        comfort = (ImageView) popupView.findViewById(R.id.create_travel_route_comfort);
        exploration = (ImageView) popupView.findViewById(R.id.create_travel_route_exploration);
        excite = (ImageView) popupView.findViewById(R.id.create_travel_route_excite);
        encounter = (ImageView) popupView.findViewById(R.id.create_travel_route_encounter);
        routeCancel = (ImageView) popupView.findViewById(R.id.create_travel_route_cancel);
        lyRoutePop = (LinearLayout) popupView.findViewById(R.id.ly_create_travel_route_pop);
        mRoutePopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mRoutePopupWindow.setTouchable(true);
        mRoutePopupWindow.setOutsideTouchable(true);
        mRoutePopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        //mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);

        mRoutePopupWindow.getContentView().setFocusableInTouchMode(true);
        mRoutePopupWindow.getContentView().setFocusable(true);
        mRoutePopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mRoutePopupWindow != null && mRoutePopupWindow.isShowing()) {
                        mRoutePopupWindow.dismiss();
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
                    case R.id.create_travel_route_literature:
                    case R.id.create_travel_route_comfort:
                    case R.id.create_travel_route_exploration:
                    case R.id.create_travel_route_excite:
                    case R.id.create_travel_route_encounter:
                        createTravelRoute.setText(mapRoute.get(v.getId()));
                        mRoutePopupWindow.dismiss();
                        break;

                    case R.id.ly_create_travel_route_pop:
                    case R.id.create_travel_route_cancel:
                        mRoutePopupWindow.dismiss();
                        break;
                }
            }
        };
        lyRoutePop.setOnClickListener(popupListener);
        literature.setOnClickListener(popupListener);
        comfort.setOnClickListener(popupListener);
        exploration.setOnClickListener(popupListener);
        excite.setOnClickListener(popupListener);
        encounter.setOnClickListener(popupListener);
        routeCancel.setOnClickListener(popupListener);
    }

    private void storeRoute() {
        routeEntity = travelManager.getRouteEntity();
        routeEntity.setDay(iduration);
        routeEntity.setCityCode(travelManager.getCityCodeByName(destination));
        List<String> tags = new ArrayList<>();
        tags.add("舒适");
        routeEntity.setTags(tags);
    }

    public void onEventMainThread(TravelEvent event){
        switch (event.getEvent()){
            case CREATE_ROUTE_OK:
                mHandler.removeCallbacks(runnable);
                dialog.dismiss();
                TravelUIHelper.openDetailDispActivity(getActivity());
                break;
            case CREATE_ROUTE_FAIL:
                Log.e("yuki", "CREATE_ROUTE_FAIL");
                break;
        }
    }

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    };
}
