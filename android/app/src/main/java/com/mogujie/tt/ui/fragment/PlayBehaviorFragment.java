package com.mogujie.tt.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.manager.IMTravelManager;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.activity.SelectSightActivity;
import com.mogujie.tt.ui.base.TTBaseFragment;

/**
 * 设置页面
 */
public class PlayBehaviorFragment extends TTBaseFragment{
	private View curView = null;
    private Button economical_efficiency;
    private Button economical_comfort;
    private Button luxury_quality;
    private Button near_downtown;
    private Button near_scenic_spot;
    private Button easy_communication;
    private Button traffic_walk;
    private Button traffic_bus;
    private Button traffic_taxi;
    private ImageButton next;
    private IMTravelManager imTravelManager;

    private String strStartTime = "9:00";
    private String strEndTime = "20:00";;
    private int play_quality = 2;
    private int hotel_Position = 3;
    private int city_traffic = 3;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                imTravelManager = imService.getTravelManager();
                setTopTitle(imTravelManager.getMtTravel().getDestination());
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
		curView = inflater.inflate(R.layout.travel_fragment_play_behavior, topContentView);
		initRes();
        initBtn();
        setGoTime();
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
    }

    /**
	 * @Description 初始化资源
	 */
	private void initRes() {
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});

        economical_efficiency = (Button)curView.findViewById(R.id.economical_efficiency);
        economical_comfort = (Button)curView.findViewById(R.id.economical_comfort);
        luxury_quality = (Button)curView.findViewById(R.id.luxury_quality);
        near_downtown = (Button)curView.findViewById(R.id.near_downtown);
        near_scenic_spot = (Button)curView.findViewById(R.id.near_scenic_spot);
        easy_communication = (Button)curView.findViewById(R.id.easy_communication);
        traffic_walk = (Button)curView.findViewById(R.id.traffic_walk);
        traffic_walk.setClickable(false);
        traffic_bus = (Button)curView.findViewById(R.id.traffic_bus);
        traffic_taxi = (Button)curView.findViewById(R.id.traffic_taxi);
        next = (ImageButton)curView.findViewById(R.id.play_behavior_next_step);
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        final View.OnClickListener behaviorListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.economical_efficiency:
                    case R.id.economical_comfort:
                    case R.id.luxury_quality:
                        playQuality(id);
                        break;

                    case R.id.near_downtown:
                    case R.id.near_scenic_spot:
                    case R.id.easy_communication:
                        hotelPosition(id);
                        break;

                    case R.id.traffic_bus:
                    case R.id.traffic_taxi:
                        cityTraffic(id);
                        break;

                    case R.id.play_behavior_next_step:
                        storeTravelEntity();
                        imTravelManager.reqCreateTravel();
                        jump2SelectSight();
                        break;
                }
            }
        };

        economical_efficiency.setOnClickListener(behaviorListener);
        economical_comfort.setOnClickListener(behaviorListener);
        luxury_quality.setOnClickListener(behaviorListener);
        traffic_bus.setOnClickListener(behaviorListener);
        traffic_taxi.setOnClickListener(behaviorListener);
        near_downtown.setOnClickListener(behaviorListener);
        near_scenic_spot.setOnClickListener(behaviorListener);
        easy_communication.setOnClickListener(behaviorListener);
        next.setOnClickListener(behaviorListener);
    }

    private void playQuality(int id) {
        play_quality = 0;
        economical_efficiency.setTextColor(getResources().getColor(R.color.default_black_color));
        economical_efficiency.setBackgroundResource(R.drawable.travel_behavior_not_click);
        economical_comfort.setTextColor(getResources().getColor(R.color.default_black_color));
        economical_comfort.setBackgroundResource(R.drawable.travel_behavior_not_click);
        luxury_quality.setTextColor(getResources().getColor(R.color.default_black_color));
        luxury_quality.setBackgroundResource(R.drawable.travel_behavior_not_click);

        switch (id) {
            case R.id.economical_efficiency:
                play_quality = 1;
                economical_efficiency.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                economical_efficiency.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.economical_comfort:
                play_quality = 2;
                economical_comfort.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                economical_comfort.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.luxury_quality:
                play_quality = 3;
                luxury_quality.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                luxury_quality.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
        }
    }

    private void setGoTime(){

        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) curView.findViewById(R.id.play_time);

        // get min and max text view
        final TextView tvMin = (TextView)curView.findViewById(R.id.play_time_start);
        final TextView tvMax = (TextView)curView.findViewById(R.id.play_time_end);

        // set properties
        rangeSeekbar
                .setCornerRadius(10f)
                .setBarColor(Color.parseColor("#93F9B5"))
                .setBarHighlightColor(Color.parseColor("#16E059"))
                .setMinValue(0)
                .setMaxValue(24)
                .setMinStartValue(9)
                .setMaxStartValue(20)
                .setSteps(1)
                .setLeftThumbDrawable(R.drawable.play_time_min)
                .setLeftThumbHighlightDrawable(R.drawable.play_time_min)
                .setRightThumbDrawable(R.drawable.play_time_max)
                .setRightThumbHighlightDrawable(R.drawable.play_time_max)
                .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                .apply();

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                strStartTime = String.valueOf(minValue)+":00";
                strEndTime = String.valueOf(maxValue)+":00";
                tvMin.setText(strStartTime);
                tvMax.setText(strEndTime);
            }
        });
    }

    private void cityTraffic(int id) {
        switch (id) {
            case R.id.traffic_bus:
                if ((city_traffic&0x0002) != 0) {
                    city_traffic^=0x0002;
                    traffic_bus.setTextColor(getResources().getColor(R.color.default_black_color));
                    traffic_bus.setBackgroundResource(R.drawable.travel_behavior_not_click);
                    traffic_bus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_black), null, null, null);
                } else {
                    city_traffic|=0x0002;
                    traffic_bus.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                    traffic_bus.setBackgroundResource(R.drawable.travel_behavior_click);
                    traffic_bus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_white), null, null, null);
                }
                break;
            case R.id.traffic_taxi:
                if ((city_traffic&0x0004) != 0) {
                    city_traffic^=0x0004;
                    traffic_taxi.setTextColor(getResources().getColor(R.color.default_black_color));
                    traffic_taxi.setBackgroundResource(R.drawable.travel_behavior_not_click);
                    traffic_taxi.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_black), null, null, null);
                } else {
                    city_traffic|=0x0004;
                    traffic_taxi.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                    traffic_taxi.setBackgroundResource(R.drawable.travel_behavior_click);
                    traffic_taxi.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_white), null, null, null);
                }
                break;
        }
    }

    private void hotelPosition(int id) {
        hotel_Position = 0;
        near_downtown.setTextColor(getResources().getColor(R.color.default_black_color));
        near_downtown.setBackgroundResource(R.drawable.travel_behavior_not_click);
        near_scenic_spot.setTextColor(getResources().getColor(R.color.default_black_color));
        near_scenic_spot.setBackgroundResource(R.drawable.travel_behavior_not_click);
        easy_communication.setTextColor(getResources().getColor(R.color.default_black_color));
        easy_communication.setBackgroundResource(R.drawable.travel_behavior_not_click);

        switch (id) {
            case R.id.near_downtown:
                hotel_Position = 1;
                near_downtown.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                near_downtown.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.near_scenic_spot:
                hotel_Position = 2;
                near_scenic_spot.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                near_scenic_spot.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.easy_communication:
                hotel_Position = 3;
                easy_communication.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                easy_communication.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
        }
    }

    private boolean checkBehavior() {
        return true;
    }

    private void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void storeTravelEntity() {
        if (imTravelManager != null) {
            imTravelManager.getMtTravel().setPlayQuality(play_quality);
            imTravelManager.getMtTravel().setPlayStartTime(strStartTime);
            imTravelManager.getMtTravel().setPlayEndTime(strEndTime);
            imTravelManager.getMtTravel().setHotelPosition(hotel_Position);
            imTravelManager.getMtTravel().setCityTraffic(city_traffic);
        }
    }

    private void jump2SelectSight() {
        Intent playBehavior = new Intent(getActivity(), SelectSightActivity.class);
        startActivity(playBehavior);
    }

}
