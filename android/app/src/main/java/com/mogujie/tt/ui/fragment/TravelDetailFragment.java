package com.mogujie.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.TrafficEntity;
import com.mogujie.tt.DB.entity.TravelEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.event.TravelEvent;
import com.mogujie.tt.imservice.manager.IMTravelManager;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.protobuf.IMBuddy;
import com.mogujie.tt.ui.activity.PlayBehaviorActivity;
import com.mogujie.tt.ui.activity.SelectSightActivity;
import com.mogujie.tt.ui.activity.TrafficListActivity;
import com.mogujie.tt.ui.adapter.TravelDetailAdapter;
import com.mogujie.tt.ui.base.TTBaseFragment;
import com.mogujie.tt.utils.TravelUIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 设置页面
 */
public class TravelDetailFragment extends TTBaseFragment{
	private View curView = null;
    private IMService imService;
	private RecyclerView rvTravelDetail;
    private TravelDetailAdapter travelDetailAdapter;
    private List<TrafficEntity> trafficEntityList = new ArrayList<>();
    private TravelEntity travelEntity;
    private Button payDetail;
    private IMTravelManager travelManager;
    private TextView travelDetailTime;
    private TextView travelDetailType;
    private Map<Integer, String> travelTypeStringMap = new HashMap();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
                travelManager.reqTravelRoute();

                travelEntity.setStartDate(travelManager.getMtTravel().getStartDate());
                travelEntity.setEndDate(travelManager.getMtTravel().getEndDate());
                travelEntity.setDestination(travelManager.getMtTravel().getDestination());
                travelDetailAdapter.notifyDataSetChanged();
                travelDetailTime.setText(String.valueOf(travelManager.getMtTravel().getDuration()+"天"));
                travelDetailType.setText(travelTypeStringMap.get(1));

                trafficEntityList.add(imService.getTravelManager().getTrafficEntityList().get(1));//yuki 逼不得已
                travelDetailAdapter.notifyDataSetChanged();
/*                trafficEntityList.clear();
                trafficEntityList.add(imService.getTravelManager().getTrafficEntityList().get(0));
                travelDetailAdapter.notifyDataSetChanged();*/
            }
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_FIRST_USER){
            switch (resultCode) {
                case 100:
                    trafficEntityList.clear();
                    trafficEntityList.add(imService.getTravelManager().getTrafficEntityList().get(data.getIntExtra("trafficIndex", 0)));
                    travelDetailAdapter.notifyDataSetChanged();
                    break;
                case 101:
                    //travelEntity.setSightSelect(data.getIntExtra("selectSight", 0));
                    travelDetailAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        imServiceConnector.connect(this.getActivity());
        EventBus.getDefault().register(TravelDetailFragment.this);
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.travel_fragment_travel_detail, topContentView);
        initTravelTypeMap();
		initRes();
        initBtn();
        initTravelDetail();
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
        EventBus.getDefault().unregister(TravelDetailFragment.this);
    }

	@Override
	public void onResume() {
		super.onResume();
	}

    /**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
		setTopTitle(getActivity().getString(R.string.travel_detail));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});

        rvTravelDetail = (RecyclerView)curView.findViewById(R.id.rv_travel_detail);
        payDetail = (Button)curView.findViewById(R.id.pay_detail);

        travelDetailTime = (TextView)curView.findViewById(R.id.travel_detail_time);
        travelDetailType = (TextView)curView.findViewById(R.id.travel_detail_type);
	}

    private void initTravelDetail() {
        rvTravelDetail.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvTravelDetail.setLayoutManager(layoutManagerResult);
        TravelDetailAdapter.OnRecyclerViewListener detailRVListener = new TravelDetailAdapter.OnRecyclerViewListener() {
            @Override
            public void onAddClick(int position, View v) {
                Intent playBehaviorIntent = new Intent(getActivity(), PlayBehaviorActivity.class);
                startActivityForResult(playBehaviorIntent, Activity.RESULT_FIRST_USER);
            }

            @Override
            public void onSelectClick(int position) {
                Intent trafficListIntent = new Intent(getActivity(), TrafficListActivity.class);
                startActivityForResult(trafficListIntent, Activity.RESULT_FIRST_USER);
            }
        };

        travelEntity = new TravelEntity();
        travelDetailAdapter = new TravelDetailAdapter(getActivity(), travelEntity, trafficEntityList);
        travelDetailAdapter.setOnRecyclerViewListener(detailRVListener);
        rvTravelDetail.setAdapter(travelDetailAdapter);
    }

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        payDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelUIHelper.openDetailDispActivity(getActivity());
            }
        });
    }

    public void onEventMainThread(TravelEvent event){
        switch (event.event){
            case REQ_TRAVEL_ROUTE_OK:
                trafficEntityList.clear();
                if (imService.getTravelManager().getTrafficEntityList().size()>1) {
                    trafficEntityList.add(imService.getTravelManager().getTrafficEntityList().get(1));
                    travelDetailAdapter.notifyDataSetChanged();
                }

                break;
        }
    }

    private void initTravelTypeMap() {
        travelTypeStringMap.put(IMBuddy.QualityType.QUALITY_LOW_VALUE, getString(R.string.economical_efficiency));
        travelTypeStringMap.put(IMBuddy.QualityType.QUALITY_MID_VALUE, getString(R.string.economical_comfort));
        travelTypeStringMap.put(IMBuddy.QualityType.QUALITY_HIGH_VALUE, getString(R.string.luxury_quality));
    }

}
