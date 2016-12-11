package com.mogujie.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mogujie.tt.R;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.activity.IntroduceCityActivity;
import com.mogujie.tt.ui.adapter.CityAdapter;
import com.mogujie.tt.ui.adapter.ProvinceAdapter;
import com.mogujie.tt.ui.adapter.SelectCityResultAdapter;
import com.mogujie.tt.ui.adapter.TravelHotAdapter;
import com.mogujie.tt.ui.base.TTBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 */
public class SelectPlaceFragment extends TTBaseFragment{
	private View curView = null;
    private Intent intent;

    private TextView selectHot;
    private TextView selectNation;

    private RecyclerView rvHot;
    private TravelHotAdapter travelHotAdapter;

    private List<String> citySelectedList = new ArrayList<>();
    private RecyclerView rvSelectCityResult;
    private SelectCityResultAdapter selectCityResultAdapter;

    private RecyclerView rvProvince;
    private ProvinceAdapter provinceAdapter;
    private RecyclerView rvCity;
    private CityAdapter cityAdapter;
    final static int ADD = 1;
    final static int DEL = 0;

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
		curView = inflater.inflate(R.layout.travel_fragment_select_place, topContentView);
        intent = getActivity().getIntent();
		initRes();
        initHot();
        initSelectResult();
        initProvince();
        initCity();
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
                    String city = data.getStringExtra("city");
                    if (data.getBooleanExtra("selectFlag", false)) {
                        cityResultDisp(city, ADD);
                    }
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
		setTopLeftButton(R.drawable.back_x);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (getFragmentManager().getBackStackEntryCount() == 0) {
                    intent.putExtra("city", "厦门");
					getActivity().setResult(101, intent);
					getActivity().finish();
					return;
				}
				getFragmentManager().popBackStack();
			}
		});

        selectHot = (TextView)curView.findViewById(R.id.select_place_hot);
        selectNation = (TextView)curView.findViewById(R.id.select_place_nation);

        rvHot = (RecyclerView)curView.findViewById(R.id.rv_hot);
		rvSelectCityResult = (RecyclerView)curView.findViewById(R.id.rv_select_place_result);
        rvProvince = (RecyclerView)curView.findViewById(R.id.rv_province);
        rvCity = (RecyclerView)curView.findViewById(R.id.rv_city);
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        View.OnClickListener selectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProvinceOrCity(v.getId());
            }
        };
        selectHot.setOnClickListener(selectListener);
        selectNation.setOnClickListener(selectListener);
    }

    private void jump2CityIntroduction(String city) {
        Intent citySelect = new Intent(getActivity(), IntroduceCityActivity.class);
        citySelect.putExtra("city", city);
        citySelect.putExtra("selectFlag", isSelected(city));
        startActivityForResult(citySelect, Activity.RESULT_FIRST_USER);
    }

    private Boolean isSelected(String city) {
        return citySelectedList.contains(city);
    }

    private void ProvinceOrCity(int id) {
        if (R.id.select_place_hot == id) {
            rvHot.setVisibility(View.VISIBLE);
            rvProvince.setVisibility(View.GONE);
            rvCity.setVisibility(View.GONE);
        } else {
            rvHot.setVisibility(View.GONE);
            rvProvince.setVisibility(View.VISIBLE);
            rvCity.setVisibility(View.VISIBLE);
        }
    }

    private void initHot() {
        rvHot.setHasFixedSize(true);
        GridLayoutManager layoutManagerHot = new GridLayoutManager(getActivity(), 3);
        layoutManagerHot.setOrientation(LinearLayoutManager.VERTICAL);
        rvHot.setLayoutManager(layoutManagerHot);
        List<Integer> hotlist = new ArrayList<Integer>();
        hotlist.add(R.drawable.hot_xiamen);
        hotlist.add(R.drawable.hot_guangzhou);
        hotlist.add(R.drawable.hot_beijing);
        hotlist.add(R.drawable.hot_daocheng);
        hotlist.add(R.drawable.hot_lijiang);
        hotlist.add(R.drawable.hot_neimeng);
        hotlist.add(R.drawable.hot_songhuajiang);
        hotlist.add(R.drawable.hot_xianggang);
        hotlist.add(R.drawable.hot_dongji);
        hotlist.add(R.drawable.hot_suzhou);
        hotlist.add(R.drawable.hot_lijiang);
        hotlist.add(R.drawable.hot_dunhuang);

        TravelHotAdapter.OnRecyclerViewListener hotRVListener = new TravelHotAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    jump2CityIntroduction("厦门");
                }
                Toast.makeText(getActivity(), "pos"+position, Toast.LENGTH_SHORT).show();
            }
        };
        travelHotAdapter = new TravelHotAdapter(hotlist);
        travelHotAdapter.setOnRecyclerViewListener(hotRVListener);
        rvHot.setAdapter(travelHotAdapter);
    }

    private void initSelectResult() {
        rvSelectCityResult.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSelectCityResult.setLayoutManager(layoutManagerResult);
        SelectCityResultAdapter.OnRecyclerViewListener resultRVListener = new SelectCityResultAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                selectCityResultAdapter.notifyItemRemoved(position);
                citySelectedList.remove(position);
                Toast.makeText(getActivity(), "pos"+position, Toast.LENGTH_SHORT).show();
            }
        };
        selectCityResultAdapter = new SelectCityResultAdapter(citySelectedList);
        selectCityResultAdapter.setOnRecyclerViewListener(resultRVListener);
        rvSelectCityResult.setAdapter(selectCityResultAdapter);
    }

    private void initProvince() {
        List<String> provinceList = new ArrayList<>();
        provinceList.add("福建省");
        provinceList.add("广东省");
        provinceList.add("浙江省");
        provinceList.add("江苏省");
        provinceList.add("北京市");
        rvProvince.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvProvince.setLayoutManager(layoutManagerResult);
        ProvinceAdapter.OnRecyclerViewListener provinceRVListener = new ProvinceAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "pos"+position, Toast.LENGTH_SHORT).show();
                provinceAdapter.setProvincePos(position);
                provinceAdapter.notifyDataSetChanged();
            }
        };
        provinceAdapter = new ProvinceAdapter(provinceList);
        provinceAdapter.setOnRecyclerViewListener(provinceRVListener);
        rvProvince.setAdapter(provinceAdapter);
    }

    private void initCity() {
        final List<String> cityList = new ArrayList<>();
        cityList.add("厦门");
        rvCity.setHasFixedSize(true);
        LinearLayoutManager layoutManagerCity = new LinearLayoutManager(getActivity());
        layoutManagerCity.setOrientation(LinearLayoutManager.VERTICAL);
        rvCity.setLayoutManager(layoutManagerCity);
        CityAdapter.OnRecyclerViewListener cityRVListener = new CityAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                jump2CityIntroduction(cityList.get(position));
            }

            @Override
            public void onItemBtnClick(View view, int position) {
                String city = cityList.get(position);
                if (false == isSelected(city)) {
                    cityResultDisp(city, ADD);
                    view.setBackgroundResource(R.drawable.city_add);
                } else {
                    cityResultDisp(city, DEL);
                    view.setBackgroundResource(R.drawable.city_del);
                }
            }
        };
        cityAdapter = new CityAdapter(cityList);
        cityAdapter.setOnRecyclerViewListener(cityRVListener);
        rvCity.setAdapter(cityAdapter);
    }

    private void cityResultDisp(String city, int opt) {
        if (opt == ADD) {
            if (false == isSelected(city)) {
                citySelectedList.add(city);
                selectCityResultAdapter.notifyDataSetChanged();
            }
        } else {
            if (true == isSelected(city)) {
                citySelectedList.remove(city);
                selectCityResultAdapter.notifyDataSetChanged();
            }
        }

    }
}
