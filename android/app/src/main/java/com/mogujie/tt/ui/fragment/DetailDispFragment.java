package com.mogujie.tt.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.DetailDispEntity;
import com.mogujie.tt.DB.entity.SightEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.adapter.DetailDispAdapter;
import com.mogujie.tt.ui.adapter.DetailDispMenuAdapter;
import com.mogujie.tt.ui.adapter.DetailDispSightAdapter;
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
    private RecyclerView rvMenu;
    private RecyclerView rvSight;
    private DetailDispMenuAdapter detailDispMenuAdapter;
    private TextView detailDispMenuDisp;
    private LinearLayout lyDetailDispMenu;
    private LinearLayout lyDetailDispSightDisp;
    private DetailDispSightAdapter detailDispSightAdapter;
    private List<SightEntity> sightEntityList = new ArrayList<>();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                sightEntityList.clear();
                sightEntityList.addAll(imService.getTravelManager().getMtCity().get(0).getSightList());
                detailDispSightAdapter.notifyDataSetChanged();
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
        setTopTitle(getString(R.string.detail_disp));
        setTopLeftButton(R.drawable.tt_top_back);
        topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getActivity().finish();
                return;
            }
        });
        setTopRightButton(R.drawable.detail_disp_sight_disp);
        topRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lyDetailDispSightDisp.getVisibility() == View.GONE) {
                    lyDetailDispSightDisp.setVisibility(View.VISIBLE);
                    detailDispMenuDisp.setClickable(false);
                    lyDetailDispMenu.setVisibility(View.GONE);
                } else {
                    detailDispMenuDisp.setClickable(true);
                    lyDetailDispSightDisp.setVisibility(View.GONE);
                }
            }
        });
        rvDetailDisp = (RecyclerView)curView.findViewById(R.id.rv_detail_disp);
        rvMenu = (RecyclerView)curView.findViewById(R.id.rv_detail_disp_menu);
        detailDispMenuDisp = (TextView)curView.findViewById(R.id.detail_disp_menu);
        lyDetailDispMenu = (LinearLayout)curView.findViewById(R.id.ly_detail_disp_menu);
        lyDetailDispSightDisp = (LinearLayout)curView.findViewById(R.id.ly_detail_disp_sight_disp);
        rvSight = (RecyclerView)curView.findViewById(R.id.rv_detail_disp_sight_disp);
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        detailDispMenuDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lyDetailDispMenu.getVisibility() == View.GONE) {
                    lyDetailDispMenu.setVisibility(View.VISIBLE);
                } else {
                    lyDetailDispMenu.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initDetailDisp() {
        rvDetailDisp.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetailDisp.setLayoutManager(layoutManagerResult);

        DetailDispAdapter.OnRecyclerViewListener detailDispRVListener = new DetailDispAdapter.OnRecyclerViewListener() {

            @Override
            public void onTrafficClick(int position) {

            }

            @Override
            public void onSightClick(int position) {

            }
        };

        DetailDispEntity one = new DetailDispEntity();
        one.setType(1);
        one.setTime("Day1");
        DetailDispEntity two = new DetailDispEntity();
        two.setType(2);
        two.setTitle("飞机");
        two.setTime("30分钟");
        two.setContent("点击查看飞机路线");
        DetailDispEntity three = new DetailDispEntity();
        three.setType(3);
        three.setTitle("鼓浪屿");
        three.setContent("点击查看景点详情");
        detailDispEntityList.add(one);
        detailDispEntityList.add(two);
        detailDispEntityList.add(three);
        detailDispEntityList.add(one);
        detailDispEntityList.add(two);
        detailDispEntityList.add(three);
        detailDispEntityList.add(one);
        detailDispEntityList.add(two);
        detailDispEntityList.add(three);

        detailDispAdapter = new DetailDispAdapter(getActivity(), detailDispEntityList);
        detailDispAdapter.setOnRecyclerViewListener(detailDispRVListener);
        rvDetailDisp.setAdapter(detailDispAdapter);

        List<String> day = new ArrayList<>();
        day.add("Day1");
        day.add("Day2");
        day.add("Day3");
        day.add("Day4");
        day.add("Day5");
        day.add("Day6");
        day.add("Day7");

        rvMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManagerMenu = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvMenu.setLayoutManager(layoutManagerMenu);
        detailDispMenuAdapter = new DetailDispMenuAdapter(getActivity(), day);
        rvMenu.setAdapter(detailDispMenuAdapter);

        rvSight.setHasFixedSize(true);
        LinearLayoutManager layoutManagerSight = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvSight.setLayoutManager(layoutManagerSight);

        detailDispSightAdapter = new DetailDispSightAdapter(getActivity(), sightEntityList);
        rvSight.setAdapter(detailDispSightAdapter);
    }
}
