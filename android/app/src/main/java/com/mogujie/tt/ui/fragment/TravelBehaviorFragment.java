package com.mogujie.tt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mogujie.tt.R;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.base.TTBaseFragment;

/**
 * 设置页面
 */
public class TravelBehaviorFragment extends TTBaseFragment{
	private View curView = null;
    private Button ride_feel_cheap;
    private Button ride_feel_moderate;
    private Button ride_feel_expensive;
    private Button go_time_gold;
    private Button go_time_early;
    private Button go_time_custom;
    private Button vehicle_train;
    private Button vehicle_plane;
    private Button vehicle_bus;
    private Button transit_times_through;
    private Button transit_times_once;
    private Button transit_times_more;
    private int behavior = 0x4112;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {

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
		setTopTitle(getActivity().getString(R.string.create_behavior));
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
        go_time_gold = (Button)curView.findViewById(R.id.go_time_gold);
        go_time_early = (Button)curView.findViewById(R.id.go_time_early);
        go_time_custom = (Button)curView.findViewById(R.id.go_time_custom);
        vehicle_train = (Button)curView.findViewById(R.id.vehicle_train);
        vehicle_plane = (Button)curView.findViewById(R.id.vehicle_plane);
        vehicle_bus = (Button)curView.findViewById(R.id.vehicle_bus);
        transit_times_through = (Button)curView.findViewById(R.id.transit_times_through);
        transit_times_once = (Button)curView.findViewById(R.id.transit_times_once);
        transit_times_more = (Button)curView.findViewById(R.id.transit_times_more);
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

                    case R.id.go_time_gold:
                    case R.id.go_time_early:
                    case R.id.go_time_custom:
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
                }
                Log.e("LLTest", ""+behavior);
            }
        };

        ride_feel_cheap.setOnClickListener(behaviorListener);
        ride_feel_moderate.setOnClickListener(behaviorListener);
        ride_feel_expensive.setOnClickListener(behaviorListener);
        go_time_gold.setOnClickListener(behaviorListener);
        go_time_early.setOnClickListener(behaviorListener);
        go_time_custom.setOnClickListener(behaviorListener);
        vehicle_train.setOnClickListener(behaviorListener);
        vehicle_plane.setOnClickListener(behaviorListener);
        vehicle_bus.setOnClickListener(behaviorListener);
        transit_times_through.setOnClickListener(behaviorListener);
        transit_times_once.setOnClickListener(behaviorListener);
        transit_times_more.setOnClickListener(behaviorListener);
    }

    private void rideFeel(int id) {
        behavior &= 0xfff0;
        ride_feel_cheap.setTextColor(getResources().getColor(R.color.default_black_color));
        ride_feel_cheap.setBackgroundResource(R.drawable.travel_behavior_not_click);
        ride_feel_moderate.setTextColor(getResources().getColor(R.color.default_black_color));
        ride_feel_moderate.setBackgroundResource(R.drawable.travel_behavior_not_click);
        ride_feel_expensive.setTextColor(getResources().getColor(R.color.default_black_color));
        ride_feel_expensive.setBackgroundResource(R.drawable.travel_behavior_not_click);

        switch (id) {
            case R.id.ride_feel_cheap:
                behavior |= 0x0001;
                ride_feel_cheap.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                ride_feel_cheap.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.ride_feel_moderate:
                behavior |= 0x0002;
                ride_feel_moderate.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                ride_feel_moderate.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.ride_feel_expensive:
                behavior |= 0x0004;
                ride_feel_expensive.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                ride_feel_expensive.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
        }
    }

    private void goTime(int id) {
        behavior &= 0xff0f;
        go_time_gold.setTextColor(getResources().getColor(R.color.default_black_color));
        go_time_gold.setBackgroundResource(R.drawable.travel_behavior_not_click);
        go_time_early.setTextColor(getResources().getColor(R.color.default_black_color));
        go_time_early.setBackgroundResource(R.drawable.travel_behavior_not_click);
        go_time_custom.setTextColor(getResources().getColor(R.color.default_black_color));
        go_time_custom.setBackgroundResource(R.drawable.travel_behavior_not_click);

        switch (id) {
            case R.id.go_time_gold:
                behavior |= 0x0010;
                go_time_gold.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                go_time_gold.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.go_time_early:
                behavior |= 0x0020;
                go_time_early.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                go_time_early.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.go_time_custom:
                behavior |= 0x0040;
                go_time_custom.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                go_time_custom.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
        }
    }

    private void vehicle(int id) {
        switch (id) {
            case R.id.vehicle_train:
                if ((behavior&0x0100) != 0) {
                    behavior^=0x0100;
                    vehicle_train.setTextColor(getResources().getColor(R.color.default_black_color));
                    vehicle_train.setBackgroundResource(R.drawable.travel_behavior_not_click);
                    vehicle_train.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.train_black), null, null, null);
                } else {
                    behavior|=0x0100;
                    vehicle_train.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                    vehicle_train.setBackgroundResource(R.drawable.travel_behavior_click);
                    vehicle_train.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.train_white), null, null, null);
                }
                break;
            case R.id.vehicle_plane:
                if ((behavior&0x0200) != 0) {
                    behavior^=0x0200;
                    vehicle_plane.setTextColor(getResources().getColor(R.color.default_black_color));
                    vehicle_plane.setBackgroundResource(R.drawable.travel_behavior_not_click);
                    vehicle_plane.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_black), null, null, null);
                } else {
                    behavior|=0x0200;
                    vehicle_plane.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                    vehicle_plane.setBackgroundResource(R.drawable.travel_behavior_click);
                    vehicle_plane.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_white), null, null, null);
                }
                break;
            case R.id.vehicle_bus:
                if ((behavior&0x0400) != 0) {
                    behavior^=0x0400;
                    vehicle_bus.setTextColor(getResources().getColor(R.color.default_black_color));
                    vehicle_bus.setBackgroundResource(R.drawable.travel_behavior_not_click);
                    vehicle_bus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_black), null, null, null);
                } else {
                    behavior|=0x0400;
                    vehicle_bus.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                    vehicle_bus.setBackgroundResource(R.drawable.travel_behavior_click);
                    vehicle_bus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plane_white), null, null, null);
                }
                break;
        }
    }

    private void transitTimes(int id) {
        behavior &= 0x0fff;
        transit_times_through.setTextColor(getResources().getColor(R.color.default_black_color));
        transit_times_through.setBackgroundResource(R.drawable.travel_behavior_not_click);
        transit_times_once.setTextColor(getResources().getColor(R.color.default_black_color));
        transit_times_once.setBackgroundResource(R.drawable.travel_behavior_not_click);
        transit_times_more.setTextColor(getResources().getColor(R.color.default_black_color));
        transit_times_more.setBackgroundResource(R.drawable.travel_behavior_not_click);

        switch (id) {
            case R.id.transit_times_through:
                behavior |= 0x1000;
                transit_times_through.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                transit_times_through.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.transit_times_once:
                behavior |= 0x2000;
                transit_times_once.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                transit_times_once.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
            case R.id.transit_times_more:
                behavior |= 0x4000;
                transit_times_more.setTextColor(getResources().getColor(R.color.travel_menu_bk));
                transit_times_more.setBackgroundResource(R.drawable.travel_behavior_click);
                break;
        }
    }

    private boolean checkBehavior() {
        if ((behavior&0x0700) == 0) {
            showMsg("交通工具未选择");
            return false;
        }

        return true;
    }

    private void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
