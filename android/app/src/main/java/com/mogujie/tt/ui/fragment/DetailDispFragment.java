package com.mogujie.tt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mogujie.tt.DB.entity.DetailDispEntity;
import com.mogujie.tt.DB.entity.TrafficEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.adapter.DetailDispAdapter;
import com.mogujie.tt.ui.adapter.TrafficDetailAdapter;
import com.mogujie.tt.ui.base.TTBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 */
public class DetailDispFragment extends TTBaseFragment{
	private View curView = null;
    private RecyclerView rvDetailDisp;
    private DetailDispAdapter detailDispAdapter;
    private List<DetailDispEntity> detailDispEntityList = new ArrayList<>();

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

	/**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
        setTopTitle("细节显示");
        setTopLeftButton(R.drawable.tt_top_back);
        topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getActivity().finish();
                return;
            }
        });
        rvDetailDisp = (RecyclerView)curView.findViewById(R.id.rv_detail_disp);

	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {

    }

    private void initDetailDisp() {
        rvDetailDisp.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetailDisp.setLayoutManager(layoutManagerResult);

        DetailDispEntity gulangyu = new DetailDispEntity();
        gulangyu.setType(0);
        DetailDispEntity xiamendaxue = new DetailDispEntity();
        xiamendaxue.setType(1);
        detailDispEntityList.add(gulangyu);
        detailDispEntityList.add(xiamendaxue);

        detailDispAdapter = new DetailDispAdapter(getActivity(), detailDispEntityList);
        rvDetailDisp.setAdapter(detailDispAdapter);
    }
}
