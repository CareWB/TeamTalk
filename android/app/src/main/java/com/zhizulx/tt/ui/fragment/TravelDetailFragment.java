package com.zhizulx.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizulx.tt.DB.entity.TrafficEntity;
import com.zhizulx.tt.DB.entity.TravelEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.event.TravelEvent;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.protobuf.IMBuddy;
import com.zhizulx.tt.ui.activity.PlayBehaviorActivity;
import com.zhizulx.tt.ui.activity.TrafficListActivity;
import com.zhizulx.tt.ui.adapter.TravelDetailAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.TravelUIHelper;

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
    private List<TrafficEntity> goTrafficEntityList = new ArrayList<>();
    private List<TrafficEntity> backTrafficEntityList = new ArrayList<>();
    private TravelEntity travelEntity;
    private ImageView expenseDetail;
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
                travelEntity.setStartPlace(travelManager.getMtTravel().getStartPlace());
                travelEntity.setEndPlace(travelManager.getMtTravel().getEndPlace());
                travelEntity.setDestination(travelManager.getMtTravel().getDestination());
                travelDetailAdapter.notifyDataSetChanged();
                travelDetailTime.setText(String.valueOf(travelManager.getMtTravel().getPersonNum()+"人"));
                travelDetailType.setText(travelTypeStringMap.get(1));

                goTrafficEntityList.add(imService.getTravelManager().getGoTrafficEntityList().get(1));//yuki 逼不得已
                backTrafficEntityList.add(imService.getTravelManager().getBackTrafficEntityList().get(1));//yuki 逼不得已
                travelDetailAdapter.notifyDataSetChanged();
/*                goTrafficEntityList.clear();
                goTrafficEntityList.add(imService.getTravelManager().getTrafficEntityList().get(0));
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
                    goTrafficEntityList.clear();
                    goTrafficEntityList.add(imService.getTravelManager().getGoTrafficEntityList().get(data.getIntExtra("trafficIndex", 0)));
                    travelDetailAdapter.notifyDataSetChanged();
                    break;
                case 101:
                    //travelEntity.setSightSelect(data.getIntExtra("selectSight", 0));
                    travelDetailAdapter.notifyDataSetChanged();
                    break;
                case 102:
                    backTrafficEntityList.clear();
                    backTrafficEntityList.add(imService.getTravelManager().getBackTrafficEntityList().get(data.getIntExtra("trafficIndex", 0)));
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
        expenseDetail = (ImageView)curView.findViewById(R.id.pay_detail);

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
                storeTraffic();
                Intent playBehaviorIntent = new Intent(getActivity(), PlayBehaviorActivity.class);
                startActivityForResult(playBehaviorIntent, Activity.RESULT_FIRST_USER);
            }

            @Override
            public void onSelectGo(int position) {
                Intent goIntent = new Intent(getActivity(), TrafficListActivity.class);
                goIntent.putExtra("direction", "go");
                startActivityForResult(goIntent, Activity.RESULT_FIRST_USER);
            }

            @Override
            public void onSelectBack(int position) {
                Intent backIntent = new Intent(getActivity(), TrafficListActivity.class);
                backIntent.putExtra("direction", "back");
                startActivityForResult(backIntent, Activity.RESULT_FIRST_USER);
            }
        };

        travelEntity = new TravelEntity();
        travelDetailAdapter = new TravelDetailAdapter(getActivity(), travelEntity, goTrafficEntityList, backTrafficEntityList);
        travelDetailAdapter.setOnRecyclerViewListener(detailRVListener);
        rvTravelDetail.setAdapter(travelDetailAdapter);
    }

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        expenseDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelUIHelper.openExpenseDetailActivity(getActivity());
            }
        });
    }

    public void onEventMainThread(TravelEvent event){
        switch (event.event){
            case REQ_TRAVEL_ROUTE_OK:
                goTrafficEntityList.clear();
                backTrafficEntityList.clear();
                if (imService.getTravelManager().getGoTrafficEntityList().size()>1 &&
                        imService.getTravelManager().getBackTrafficEntityList().size()>1) {
                    goTrafficEntityList.add(imService.getTravelManager().getGoTrafficEntityList().get(1));
                    backTrafficEntityList.add(imService.getTravelManager().getBackTrafficEntityList().get(1));
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

    private void storeTraffic() {
        if (imService != null) {
            imService.getTravelManager().getMtCity().get(0).setGo(goTrafficEntityList.get(0));
            imService.getTravelManager().getMtCity().get(0).setBack(backTrafficEntityList.get(0));
        }
    }

}
