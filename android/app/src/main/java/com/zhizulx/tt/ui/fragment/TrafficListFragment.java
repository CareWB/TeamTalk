package com.zhizulx.tt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhizulx.tt.DB.entity.TrafficEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.adapter.TrafficDetailAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 */
public class TrafficListFragment extends TTBaseFragment{
	private View curView = null;
    private Intent intent;
    private int trafficID = 0;
    private String date;
    private RecyclerView rvTrafficList;
    private List<TrafficEntity> trafficEntityListDB = new ArrayList<>();
    private List<TrafficEntity> trafficEntityList = new ArrayList<>();
    private TrafficDetailAdapter trafficDetailAdapter;
    private static final int GO = 1;
    private static final int BACK = 0;
    private int direction = GO;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                trafficEntityListDB.clear();
                if (direction == GO) {
                    setTopTitle(imService.getTravelManager().getMtTravel().getStartPlace()+"-"+
                            imService.getTravelManager().getMtTravel().getDestination());
                    trafficEntityListDB.addAll(imService.getTravelManager().getGoTrafficEntityList());
                    date = imService.getTravelManager().getMtTravel().getStartDate();
                } else {
                    setTopTitle(imService.getTravelManager().getMtTravel().getDestination()+"-"+
                            imService.getTravelManager().getMtTravel().getEndPlace());
                    trafficEntityListDB.addAll(imService.getTravelManager().getBackTrafficEntityList());
                    date = imService.getTravelManager().getMtTravel().getEndDate();
                }
                trafficEntityList.addAll(trafficEntityListDB);
                initTrafficList();
                //trafficDetailAdapter.notifyDataSetChanged();
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
		curView = inflater.inflate(R.layout.travel_fragment_traffic_list, topContentView);
        intent = getActivity().getIntent();
        if (intent.getStringExtra("direction").equals("go")) {
            direction = GO;
        } else {
            direction = BACK;
        }
		initRes();
        initBtn();
        initTrafficList();
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

	/**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
        setTopLeftButton(R.drawable.tt_top_back);
        topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intent = new Intent();
                intent.putExtra("trafficIndex", trafficID);
                if (direction == GO) {
                    getActivity().setResult(100, intent);
                } else {
                    getActivity().setResult(102, intent);
                }

                getActivity().finish();
                return;
            }
        });
        rvTrafficList = (RecyclerView)curView.findViewById(R.id.rv_traffic_list);

	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {

    }

    private void initTrafficList() {
        rvTrafficList.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvTrafficList.setLayoutManager(layoutManagerResult);
        TrafficDetailAdapter.OnRecyclerViewListener detailRVListener = new TrafficDetailAdapter.OnRecyclerViewListener() {
            @Override
            public void onSelectClick(int position) {
                trafficID = position;
                for (TrafficEntity trafficEntity : trafficEntityList) {
                    trafficEntity.setSelect(0);
                }
                trafficEntityList.get(position).setSelect(1);
            }

            @Override
            public void onPullClick(int position) {
                if (trafficEntityList.get(position).getStatus() == 0) {
                    trafficEntityList.get(position).setStatus(1);
                } else {
                    trafficEntityList.get(position).setStatus(0);
                }
                pullProcess();
            }
        };

        trafficDetailAdapter = new TrafficDetailAdapter(getActivity(), date, trafficEntityList);
        trafficDetailAdapter.setOnRecyclerViewListener(detailRVListener);
        rvTrafficList.setAdapter(trafficDetailAdapter);
    }

    private void pullProcess() {
        trafficEntityList.clear();
        int type = 0;
        int status = 0;
        for (TrafficEntity trafficEntity : trafficEntityListDB) {
            if (trafficEntity.getType() > 0xf0) {
                type = trafficEntity.getType()-0xf0;
                status = trafficEntity.getStatus();
                trafficEntityList.add(trafficEntity);
            }

            if (type == trafficEntity.getType()) {
                if (status == 1) {
                    trafficEntityList.add(trafficEntity);
                }
            }
        }
        trafficDetailAdapter.notifyDataSetChanged();
    }
}
