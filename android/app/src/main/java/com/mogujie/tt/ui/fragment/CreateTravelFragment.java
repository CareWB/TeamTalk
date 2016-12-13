package com.mogujie.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogujie.tt.R;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.activity.SelectCityActivity;
import com.mogujie.tt.ui.activity.SelectPlaceActivity;
import com.mogujie.tt.ui.activity.SelectTimeActivity;
import com.mogujie.tt.ui.activity.TravelBehaviorActivity;
import com.mogujie.tt.ui.base.TTBaseFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设置页面
 */
public class CreateTravelFragment extends TTBaseFragment{
	private View curView = null;
    private ImageButton per_num_add;
    private ImageButton per_num_sub;
    private TextView per_num;
    private RelativeLayout bnStart;
    private RelativeLayout bnEnd;
    private RelativeLayout time;
    private TextView duration;
    private TextView betweenTime;
    private RelativeLayout place;
    private TextView startCity;
    private TextView endCity;
    private ImageButton next;

    private final static int MAX_PER_SUM = 6;
    private final static int MIN_PER_SUM = 1;
    private final static int START_CITY = 0;
    private final static int END_CITY = 1;

    private Date startDate;
    private Date endDate;

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
		curView = inflater.inflate(R.layout.travel_fragment_create_travel, topContentView);
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
        if(requestCode == Activity.RESULT_FIRST_USER){
            switch (resultCode) {
                case 100:
                    if (data.getIntExtra("type", START_CITY) == START_CITY) {
                        startCity.setText(data.getStringExtra("city"));
                    } else {
                        endCity.setText(data.getStringExtra("city"));
                    }
                    break;
                case 101:
                    TextView place = (TextView)curView.findViewById(R.id.create_travel_place);
                    place.setText(data.getStringExtra("city"));
                    break;
                case 102:
                    java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd ");
                    try {
                        startDate =  formatter.parse(data.getStringExtra("start"));
                        endDate =  formatter.parse(data.getStringExtra("end"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateProcess();
                    break;
            }
        }
    }

    /**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
		setTopTitle(getActivity().getString(R.string.travel_create));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});

        per_num_add = (ImageButton)curView.findViewById(R.id.create_travel_per_num_add);
        per_num_sub = (ImageButton)curView.findViewById(R.id.create_travel_per_num_sub);
        per_num = (TextView)curView.findViewById(R.id.create_travel_per_num);
        bnStart = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_start);
        bnEnd = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_end);
        startCity = (TextView)curView.findViewById(R.id.create_travel_start);
        endCity = (TextView)curView.findViewById(R.id.create_travel_end);
        time = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_time);
        duration = (TextView)curView.findViewById(R.id.tcreate_travel_time);
        betweenTime = (TextView)curView.findViewById(R.id.create_travel_time);
        place = (RelativeLayout)curView.findViewById(R.id.rlcreate_travel_place);
        next = (ImageButton)curView.findViewById(R.id.create_travel_next_step);
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
                    case R.id.create_travel_per_num_add:
                    case R.id.create_travel_per_num_sub:
                        clacPerNum(id);
                        break;

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

                    case R.id.create_travel_next_step:
                        jump2TravelBehavior();
                        break;
                }
            }
        };

        per_num_add.setOnClickListener(createTravelListener);
        per_num_sub.setOnClickListener(createTravelListener);
        bnStart.setOnClickListener(createTravelListener);
        bnEnd.setOnClickListener(createTravelListener);
        time.setOnClickListener(createTravelListener);
        place.setOnClickListener(createTravelListener);
        next.setOnClickListener(createTravelListener);
    }

    private void clacPerNum(int opt) {
        int perNum = Integer.parseInt(per_num.getText().toString());

        if (opt == R.id.create_travel_per_num_add) {
            perNum ++;
            if (perNum > MAX_PER_SUM) {
                perNum = MAX_PER_SUM;
            }
        } else {
            perNum --;
            if (perNum < MIN_PER_SUM) {
                perNum = MIN_PER_SUM;
            }
        }

        per_num.setText(String.valueOf(perNum));
    }

    private void jump2CitySelect(int opt) {
        Intent citySelect = new Intent(getActivity(), SelectCityActivity.class);
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
        Intent timeSelect = new Intent(getActivity(), SelectTimeActivity.class);
        startActivityForResult(timeSelect, Activity.RESULT_FIRST_USER);
    }

    private void jump2TravelBehavior() {
        Intent travelBehavior = new Intent(getActivity(), TravelBehaviorActivity.class);
        startActivity(travelBehavior);
    }

    private void dateProcess() {
        long duration = (endDate.getTime()-startDate.getTime())/(1000*60*60*24);
        duration ++;
        java.text.SimpleDateFormat formatter = new SimpleDateFormat( "MM.dd");
        String date = formatter.format(startDate)+"-"+formatter.format(endDate);//格式化数据
        this.duration.setText(duration+"天");
        this.betweenTime.setText(date);
    }
}
