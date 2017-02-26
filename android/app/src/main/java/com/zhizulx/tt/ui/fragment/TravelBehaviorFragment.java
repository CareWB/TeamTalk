package com.zhizulx.tt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.TravelUIHelper;

/**
 * 设置页面
 */
public class TravelBehaviorFragment extends TTBaseFragment{
	private View curView = null;
    private Button ride_feel_cheap;
    private Button ride_feel_moderate;
    private Button ride_feel_expensive;
    private Button go_time_morning;
    private Button go_time_gold;
    private Button go_time_evening;
    private Button vehicle_train;
    private Button vehicle_plane;
    private Button vehicle_bus;
    private Button transit_times_through;
    private Button transit_times_once;
    private Button transit_times_more;
    private Button next;
    private IMService imService;

    private String startTime;
    private String endTime;
    private int quality = 2;
    private int go_time = 2;
    private int way = 3;
    private int transitTimes = 3;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
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
		curView = inflater.inflate(R.layout.travel_fragment_travel_behavior, topContentView);
		initRes();
        initBtn();
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
		// 设置标题栏
		setTopTitle(getActivity().getString(R.string.travel_behavior));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});

        ride_feel_cheap = (Button)curView.findViewById(R.id.ride_feel_cheap);
        ride_feel_moderate = (Button)curView.findViewById(R.id.ride_feel_moderate);
        ride_feel_expensive = (Button)curView.findViewById(R.id.ride_feel_expensive);
        go_time_morning = (Button)curView.findViewById(R.id.go_time_morning);
        go_time_gold = (Button)curView.findViewById(R.id.go_time_gold);
        go_time_evening = (Button)curView.findViewById(R.id.go_time_evening);
        vehicle_train = (Button)curView.findViewById(R.id.vehicle_train);
        vehicle_plane = (Button)curView.findViewById(R.id.vehicle_plane);
        vehicle_bus = (Button)curView.findViewById(R.id.vehicle_bus);
        transit_times_through = (Button)curView.findViewById(R.id.transit_times_through);
        transit_times_once = (Button)curView.findViewById(R.id.transit_times_once);
        transit_times_more = (Button)curView.findViewById(R.id.transit_times_more);
        next = (Button)curView.findViewById(R.id.travel_behavior_next_step);
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        View.OnClickListener behaviorListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.ride_feel_cheap:
                    case R.id.ride_feel_moderate:
                    case R.id.ride_feel_expensive:
                        rideFeel(id);
                        break;

                    case R.id.go_time_morning:
                    case R.id.go_time_gold:
                    case R.id.go_time_evening:
                        goTime(id);
                        break;

                    case R.id.vehicle_train:
                    case R.id.vehicle_plane:
                    case R.id.vehicle_bus:
                        vehicle(id);
                        break;

                    case R.id.transit_times_through:
                    case R.id.transit_times_once:
                    case R.id.transit_times_more:
                        transitTimes(id);
                        break;
                    case R.id.travel_behavior_next_step:
                        if (checkBehavior()) {
                            storeTravelEntity();
                            TravelUIHelper.openTravelDetailActivity(getActivity());
                        }
                        break;
                }
            }
        };

        ride_feel_cheap.setOnClickListener(behaviorListener);
        ride_feel_moderate.setOnClickListener(behaviorListener);
        ride_feel_expensive.setOnClickListener(behaviorListener);
        go_time_morning.setOnClickListener(behaviorListener);
        go_time_gold.setOnClickListener(behaviorListener);
        go_time_evening.setOnClickListener(behaviorListener);
        vehicle_train.setOnClickListener(behaviorListener);
        vehicle_plane.setOnClickListener(behaviorListener);
        vehicle_bus.setOnClickListener(behaviorListener);
        transit_times_through.setOnClickListener(behaviorListener);
        transit_times_once.setOnClickListener(behaviorListener);
        transit_times_more.setOnClickListener(behaviorListener);
        next.setOnClickListener(behaviorListener);
    }

    private void rideFeel(int id) {
        quality = 0;
        ride_feel_cheap.setTextColor(getResources().getColor(R.color.not_clicked));
        ride_feel_cheap.setBackgroundResource(R.drawable.travel_behavior_not_click);
        ride_feel_moderate.setTextColor(getResources().getColor(R.color.not_clicked));
        ride_feel_moderate.setBackgroundResource(R.drawable.travel_behavior_not_click);
        ride_feel_expensive.setTextColor(getResources().getColor(R.color.not_clicked));
        ride_feel_expensive.setBackgroundResource(R.drawable.travel_behavior_not_click);

        switch (id) {
            case R.id.ride_feel_cheap:
                quality = 1;
                ride_feel_cheap.setTextColor(getResources().getColor(R.color.clicked));
                ride_feel_cheap.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.ride_feel_moderate:
                quality = 2;
                ride_feel_moderate.setTextColor(getResources().getColor(R.color.clicked));
                ride_feel_moderate.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.ride_feel_expensive:
                quality = 3;
                ride_feel_expensive.setTextColor(getResources().getColor(R.color.clicked));
                ride_feel_expensive.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
        }
    }

    private void goTime(int id) {
        go_time = 0;
        go_time_morning.setTextColor(getResources().getColor(R.color.not_clicked));
        go_time_morning.setBackgroundResource(R.drawable.travel_behavior_not_click);
        go_time_gold.setTextColor(getResources().getColor(R.color.not_clicked));
        go_time_gold.setBackgroundResource(R.drawable.travel_behavior_not_click);
        go_time_evening.setTextColor(getResources().getColor(R.color.not_clicked));
        go_time_evening.setBackgroundResource(R.drawable.travel_behavior_not_click);

        switch (id) {
            case R.id.go_time_morning:
                go_time = 1;
                go_time_morning.setTextColor(getResources().getColor(R.color.clicked));
                go_time_morning.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.go_time_gold:
                go_time = 2;
                go_time_gold.setTextColor(getResources().getColor(R.color.clicked));
                go_time_gold.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.go_time_evening:
                go_time = 3;
                go_time_evening.setTextColor(getResources().getColor(R.color.clicked));
                go_time_evening.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
        }
    }

    private void vehicle(int id) {
        switch (id) {
            case R.id.vehicle_train:
                if ((way&0x0001) != 0) {
                    way^=0x0001;
                    vehicle_train.setTextColor(getResources().getColor(R.color.not_clicked));
                    vehicle_train.setBackgroundResource(R.drawable.travel_behavior_not_click);
                    vehicle_train.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.train_black), null, null, null);
                } else {
                    way|=0x0001;
                    vehicle_train.setTextColor(getResources().getColor(R.color.clicked));
                    vehicle_train.setBackgroundResource(R.drawable.travel_behavior_click);
                    vehicle_train.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.train_white), null, null, null);
                }
                break;
            case R.id.vehicle_plane:
                if ((way&0x0002) != 0) {
                    way^=0x0002;
                    vehicle_plane.setTextColor(getResources().getColor(R.color.not_clicked));
                    vehicle_plane.setBackgroundResource(R.drawable.travel_behavior_not_click);
                    vehicle_plane.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_black), null, null, null);
                } else {
                    way|=0x0002;
                    vehicle_plane.setTextColor(getResources().getColor(R.color.clicked));
                    vehicle_plane.setBackgroundResource(R.drawable.travel_behavior_click);
                    vehicle_plane.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_white), null, null, null);
                }
                break;
            case R.id.vehicle_bus:
                if ((way&0x0004) != 0) {
                    way^=0x0004;
                    vehicle_bus.setTextColor(getResources().getColor(R.color.not_clicked));
                    vehicle_bus.setBackgroundResource(R.drawable.travel_behavior_not_click);
                    vehicle_bus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bus_black), null, null, null);
                } else {
                    way|=0x0004;
                    vehicle_bus.setTextColor(getResources().getColor(R.color.clicked));
                    vehicle_bus.setBackgroundResource(R.drawable.travel_behavior_click);
                    vehicle_bus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.bus_white), null, null, null);
                }
                break;
        }
    }

    private void transitTimes(int id) {
        transitTimes = 0;
        transit_times_through.setTextColor(getResources().getColor(R.color.not_clicked));
        transit_times_through.setBackgroundResource(R.drawable.travel_behavior_not_click);
        transit_times_once.setTextColor(getResources().getColor(R.color.not_clicked));
        transit_times_once.setBackgroundResource(R.drawable.travel_behavior_not_click);
        transit_times_more.setTextColor(getResources().getColor(R.color.not_clicked));
        transit_times_more.setBackgroundResource(R.drawable.travel_behavior_not_click);

        switch (id) {
            case R.id.transit_times_through:
                transitTimes = 1;
                transit_times_through.setTextColor(getResources().getColor(R.color.clicked));
                transit_times_through.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.transit_times_once:
                transitTimes = 2;
                transit_times_once.setTextColor(getResources().getColor(R.color.clicked));
                transit_times_once.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.transit_times_more:
                transitTimes = 3;
                transit_times_more.setTextColor(getResources().getColor(R.color.clicked));
                transit_times_more.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
        }
    }

    private boolean checkBehavior() {
        if ((way&0x0007) == 0) {
            showMsg("交通工具未选择");
            return false;
        }

        return true;
    }

    private void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void storeTravelEntity() {
        if (imService != null) {
            switch (go_time) {
                case 1:
                    imService.getTravelManager().getMtTravel().setTrafficStartTime("00:00");
                    imService.getTravelManager().getMtTravel().setTrafficEndTime("08:00");
                    break;
                case 2:
                    imService.getTravelManager().getMtTravel().setTrafficStartTime("08:00");
                    imService.getTravelManager().getMtTravel().setTrafficEndTime("16:00");
                    break;
                case 3:
                    imService.getTravelManager().getMtTravel().setTrafficStartTime("16:00");
                    imService.getTravelManager().getMtTravel().setTrafficEndTime("24:00");
                    break;

            }
            imService.getTravelManager().getMtTravel().setTrafficQuality((quality));
            imService.getTravelManager().getMtTravel().setTrafficWay((way&0x0007));
        }
    }

}
