package com.mogujie.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.SightEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.config.UrlConstant;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.adapter.SightAdapter;
import com.mogujie.tt.ui.base.TTBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 */
public class SelectSightFragment extends TTBaseFragment{
	private View curView = null;
    private Spinner spinner;
    private int spinner_select = 0;
    private TextView total;
    private TextView nature;
    private TextView history;
    private TextView entertainment;
    private TextView building;
    private RecyclerView rvSight;
    private SightAdapter sightAdapter;
    private List<SightEntity> sightEntityList = new ArrayList<>();

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
		curView = inflater.inflate(R.layout.travel_fragment_select_sight, topContentView);

		initRes();
        initBtn();
        initSight();
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
                    break;
            }
        }
    }

	/**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
		setTopTitle(getString(R.string.select_destination));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
                getActivity().finish();
			}
		});

        total = (TextView)curView.findViewById(R.id.select_all);
        nature = (TextView)curView.findViewById(R.id.select_nature);
        history = (TextView)curView.findViewById(R.id.select_history);
        entertainment = (TextView)curView.findViewById(R.id.select_entertainment);
        building = (TextView)curView.findViewById(R.id.select_building);

        spinner = (Spinner)curView.findViewById(R.id.spinner_sight);
        String[] mItems = getResources().getStringArray(R.array.sight_menu);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinner_select = pos;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        rvSight = (RecyclerView)curView.findViewById(R.id.rv_sight);

	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.select_all:
                        break;
                    case R.id.select_nature:
                        break;
                    case R.id.select_history:
                        break;
                    case R.id.select_entertainment:
                        break;
                    case R.id.select_building:
                        break;
                }
            }
        };
        total.setOnClickListener(listener);
        nature.setOnClickListener(listener);
        history.setOnClickListener(listener);
        entertainment.setOnClickListener(listener);
        building.setOnClickListener(listener);
    }

    private void initSight() {
        String pre = UrlConstant.PIC_URL_PREFIX;
        SightEntity xiada = new SightEntity();
        xiada.setName("厦门大学");
        xiada.setPic(pre+"xiamendaxue.png");
        xiada.setStar(9);
        xiada.setFocus(2345);
        xiada.setTag("建筑 文化");
        xiada.setFree(1);
        xiada.setMustGo(1);
        xiada.setSelect(1);

        SightEntity gulangyu = new SightEntity();
        gulangyu.setName("鼓浪屿");
        gulangyu.setPic(pre+"gulangyu.png");
        gulangyu.setStar(8);
        gulangyu.setFocus(5678);
        gulangyu.setTag("自然");
        gulangyu.setFree(0);
        gulangyu.setMustGo(0);
        gulangyu.setSelect(1);

        sightEntityList.add(xiada);
        sightEntityList.add(gulangyu);

        rvSight.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvSight.setLayoutManager(layoutManagerResult);
        SightAdapter.OnRecyclerViewListener sightRVListener = new SightAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Log.e("yuki", " "+position);
            }

            @Override
            public void onSelectClick(int position, View v) {
                SightEntity sightEntity = sightEntityList.get(position);
                if (sightEntity.getSelect() == 1) {
                    sightEntity.setSelect(0);
                } else {
                    sightEntity.setSelect(1);
                }
                sightAdapter.notifyDataSetChanged();
            }
        };
        sightAdapter = new SightAdapter(getActivity(), sightEntityList);
        sightAdapter.setOnRecyclerViewListener(sightRVListener);
        rvSight.setAdapter(sightAdapter);
    }
}
