package com.zhizulx.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhizulx.tt.DB.entity.CityEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.activity.IntroduceCityActivity;
import com.zhizulx.tt.ui.adapter.CityAdapter;
import com.zhizulx.tt.ui.adapter.ProvinceAdapter;
import com.zhizulx.tt.ui.adapter.SelectCityResultAdapter;
import com.zhizulx.tt.ui.adapter.TravelHotAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 */
public class SelectPlaceFragment extends TTBaseFragment{
	private View curView = null;
    private Intent intent;

    private LinearLayout selectSwitch;
    private LinearLayout selectPlaceDivider;
    private TextView selectHot;
    private TextView selectNation;
    private ImageView ibnSelectPlaceOk;

    private RecyclerView rvHot;
    private TravelHotAdapter travelHotAdapter;

    private List<CityEntity> citySelectedList = new ArrayList<>();
    private List<CityEntity> cityList = new ArrayList<>();
    private RecyclerView rvSelectCityResult;
    private SelectCityResultAdapter selectCityResultAdapter;

    private RecyclerView rvProvince;
    private ProvinceAdapter provinceAdapter;
    private RecyclerView rvCity;
    private CityAdapter cityAdapter;
    private CityEntity xiamen = new CityEntity();
    final static int ADD = 1;
    final static int DEL = 0;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                if (imService.getTravelManager().getMtTravel().getDestination() != null &&
                        !imService.getTravelManager().getMtTravel().getDestination().equals("")) {
                    CityEntity cityEntity = new CityEntity();
                    cityEntity.setName(imService.getTravelManager().getMtTravel().getDestination());
                    cityEntity.setSelect(1);
                    citySelectedList.add(cityEntity);
                    selectCityResultAdapter.notifyDataSetChanged();
                    ibnSelectPlaceOk.setBackground(getActivity().getResources().getDrawable(R.drawable.select_place_ok_click));
                    ibnSelectPlaceOk.setClickable(true);
                }
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
        xiamen.setName("厦门");
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
                        xiamen.setSelect(1);
                        cityResultDisp(xiamen, ADD);
                        cityAdapter.notifyDataSetChanged();
                    } else {
                        xiamen.setSelect(0);
                        cityResultDisp(xiamen, DEL);
                        cityAdapter.notifyDataSetChanged();
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
        View.OnClickListener chooseListener = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (citySelectedList.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.create_travel_not_select_destination), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    if (citySelectedList.isEmpty()) {
                        intent.putExtra("city", "");
                    } else {
                        intent.putExtra("city", citySelectedList.get(0).getName());
                    }

                    getActivity().setResult(101, intent);
                    getActivity().finish();
                    return;
                }
                getFragmentManager().popBackStack();
            }
        };
		topLeftContainerLayout.setOnClickListener(chooseListener);

        ibnSelectPlaceOk = (ImageView)curView.findViewById(R.id.ibn_select_place_ok);
        ibnSelectPlaceOk.setOnClickListener(chooseListener);
        ibnSelectPlaceOk.setClickable(false);

        selectSwitch = (LinearLayout)curView.findViewById(R.id.ly_select_switch);
        selectPlaceDivider = (LinearLayout)curView.findViewById(R.id.ly_select_place_divider);
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

    private void jump2CityIntroduction(CityEntity city) {
        Intent citySelect = new Intent(getActivity(), IntroduceCityActivity.class);
        citySelect.putExtra("city", city.getName());
        if (hasSelected(city)) {
            citySelect.putExtra("selectFlag", true);
        } else {
            citySelect.putExtra("selectFlag", false);
        }
        startActivityForResult(citySelect, Activity.RESULT_FIRST_USER);
    }

    private void ProvinceOrCity(int id) {
        if (R.id.select_place_hot == id) {
            selectSwitch.setBackgroundResource(R.drawable.select_place_left);
            selectHot.setTextColor(getResources().getColor(R.color.switch_on));
            selectNation.setTextColor(getResources().getColor(R.color.switch_off));
            rvHot.setVisibility(View.VISIBLE);
            rvProvince.setVisibility(View.GONE);
            rvCity.setVisibility(View.GONE);
            selectPlaceDivider.setVisibility(View.GONE);
        } else {
            selectSwitch.setBackgroundResource(R.drawable.select_place_right);
            selectHot.setTextColor(getResources().getColor(R.color.switch_off));
            selectNation.setTextColor(getResources().getColor(R.color.switch_on));
            rvHot.setVisibility(View.GONE);
            rvProvince.setVisibility(View.VISIBLE);
            rvCity.setVisibility(View.VISIBLE);
            selectPlaceDivider.setVisibility(View.VISIBLE);
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
                    jump2CityIntroduction(xiamen);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.under_constracted), Toast.LENGTH_SHORT).show();
                }

            }
        };
        travelHotAdapter = new TravelHotAdapter(hotlist);
        travelHotAdapter.setOnRecyclerViewListener(hotRVListener);
        rvHot.setAdapter(travelHotAdapter);
    }

    private void initSelectResult() {
        rvSelectCityResult.setHasFixedSize(true);
        GridLayoutManager layoutManagerResult = new GridLayoutManager(getActivity(), 3);
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvSelectCityResult.setLayoutManager(layoutManagerResult);
        SelectCityResultAdapter.OnRecyclerViewListener resultRVListener = new SelectCityResultAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                selectCityResultAdapter.notifyItemRemoved(position);
                citySelectedList.remove(position);
                Toast.makeText(getActivity(), "pos"+position, Toast.LENGTH_SHORT).show();
            }
        };

        selectCityResultAdapter = new SelectCityResultAdapter(getActivity(), citySelectedList);
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
                if (position != 0) {
                    Toast.makeText(getActivity(), getString(R.string.under_constracted), Toast.LENGTH_SHORT).show();
                }
                provinceAdapter.setProvincePos(position);
                provinceAdapter.notifyDataSetChanged();
            }
        };
        provinceAdapter = new ProvinceAdapter(getActivity(), provinceList);
        provinceAdapter.setOnRecyclerViewListener(provinceRVListener);
        rvProvince.setAdapter(provinceAdapter);
    }

    private void initCity() {
        cityList.add(xiamen);
        rvCity.setHasFixedSize(true);
        LinearLayoutManager layoutManagerCity = new LinearLayoutManager(getActivity());
        layoutManagerCity.setOrientation(LinearLayoutManager.VERTICAL);
        rvCity.setLayoutManager(layoutManagerCity);
        CityAdapter.OnRecyclerViewListener cityRVListener = new CityAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                jump2CityIntroduction(xiamen);
            }

            @Override
            public void onItemBtnClick(View view, int position) {
                CityEntity city = cityList.get(position);
                if (0 == city.getSelect()) {
                    city.setSelect(1);
                    cityResultDisp(city, ADD);
                    cityAdapter.notifyDataSetChanged();
                } else {
                    city.setSelect(0);
                    cityResultDisp(city, DEL);
                    cityAdapter.notifyDataSetChanged();
                }
            }
        };
        cityAdapter = new CityAdapter(getActivity(), cityList);
        cityAdapter.setOnRecyclerViewListener(cityRVListener);
        rvCity.setAdapter(cityAdapter);
    }

    private void cityResultDisp(CityEntity city, int opt) {
        if (opt == ADD) {
            if (false == hasSelected(city)) {
                citySelectedList.add(city);
                selectCityResultAdapter.notifyDataSetChanged();
            }
        } else {
            if (true == hasSelected(city)) {
                citySelectedList.remove(city);
                selectCityResultAdapter.notifyDataSetChanged();
            }
        }
        if (citySelectedList.isEmpty()) {
            ibnSelectPlaceOk.setBackground(getActivity().getResources().getDrawable(R.drawable.select_place_ok_click_not));
            ibnSelectPlaceOk.setClickable(false);
        } else {
            ibnSelectPlaceOk.setBackground(getActivity().getResources().getDrawable(R.drawable.select_place_ok_click));
            ibnSelectPlaceOk.setClickable(true);
        }
    }

    private boolean hasSelected(CityEntity cityEntity) {
        for (CityEntity index : citySelectedList) {
            if (cityEntity.getName().equals(index.getName())) {
                return true;
            }
        }
        return false;
    }
}
