package com.mogujie.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.HotelEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.config.UrlConstant;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.adapter.HotelAdapter;
import com.mogujie.tt.ui.base.TTBaseFragment;
import com.mogujie.tt.utils.TravelUIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 设置页面
 */
public class SelectHotelFragment extends TTBaseFragment{
	private View curView = null;
    private Spinner spinner;
    private int spinner_select = 0;
    private TextView recommend;
    private TextView economical;
    private TextView luxurious;
    private TextView youth;
    private TextView home;
    private RecyclerView rvHotel;
    private HotelAdapter hotelAdapter;
    private List<HotelEntity> hotelEntityArrayList = new ArrayList<>();
    private List<HotelEntity> selectHotelList = new ArrayList<>();
    String Tag = "推荐";

    private Map<Integer, String> selectFlag = new HashMap<>();

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
		curView = inflater.inflate(R.layout.travel_fragment_select_hotel, topContentView);

		initRes();
        initBtn();
        testCase();
        initHotel();
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
		setTopTitle(getString(R.string.select_hotel));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
                getActivity().finish();
			}
		});

        recommend = (TextView)curView.findViewById(R.id.select_recommend);
        economical = (TextView)curView.findViewById(R.id.select_economical);
        luxurious = (TextView)curView.findViewById(R.id.select_luxurious);
        youth = (TextView)curView.findViewById(R.id.select_youth);
        home = (TextView)curView.findViewById(R.id.select_home);
        selectFlag.put(R.id.select_recommend, "推荐");
        selectFlag.put(R.id.select_economical, "经济型");
        selectFlag.put(R.id.select_luxurious, "豪华型");
        selectFlag.put(R.id.select_youth, "青旅");
        selectFlag.put(R.id.select_home, "民宿");

        final String[] mItems = getResources().getStringArray(R.array.hotel_menu);
        spinner = (Spinner)curView.findViewById(R.id.spinner_hotel);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_checked_text, mItems) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = curView.inflate(getContext(), R.layout.spinner_item_layout,
                        null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);

                label.setText(mItems[position]);
                if (spinner.getSelectedItemPosition() == position) {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.travel_alert_dialog_title));
                } else {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.travel_menu_bk));
                }
                return view;
            }

        };
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinner_select = pos;
                tagProcess();
                menuProcess();
                hotelAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        rvHotel = (RecyclerView)curView.findViewById(R.id.rv_hotel);
        
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Tag = selectFlag.get(id);
                tagProcess();
                menuProcess();
                hotelAdapter.notifyDataSetChanged();
            }
        };
        recommend.setOnClickListener(listener);
        economical.setOnClickListener(listener);
        luxurious.setOnClickListener(listener);
        youth.setOnClickListener(listener);
        home.setOnClickListener(listener);
    }

    private void testCase() {
        String pre = UrlConstant.PIC_URL_PREFIX;

        HotelEntity qitian = new HotelEntity();
        qitian.setName("7天快捷酒店");
        qitian.setPic(pre+"qitiankuaijiejiudian.png");
        qitian.setStar(9);
        qitian.setFocus(2345);
        qitian.setTag("经济型");
        qitian.setSelect(0);
        qitian.setOptimize(0);

        HotelEntity rihang = new HotelEntity();
        rihang.setName("厦门日航酒店");
        rihang.setPic(pre+"rihangjiudian.png");
        rihang.setStar(10);
        rihang.setFocus(5678);
        rihang.setTag("豪华型");
        rihang.setSelect(0);
        qitian.setOptimize(1);

        hotelEntityArrayList.add(qitian);
        hotelEntityArrayList.add(rihang);

        selectHotelList.addAll(hotelEntityArrayList);
    }

    private void initHotel() {
        rvHotel.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvHotel.setLayoutManager(layoutManagerResult);
        HotelAdapter.OnRecyclerViewListener hotelRVListener = new HotelAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                TravelUIHelper.openIntroduceHotelActivity(getActivity(), selectHotelList.get(position).getPeerId());
            }

            @Override
            public void onSelectClick(int position, View v) {
                HotelEntity hotelEntity = selectHotelList.get(position);
                if (hotelEntity.getSelect() == 1) {
                    hotelEntity.setSelect(0);
                } else {
                    hotelEntity.setSelect(1);
                }
                hotelAdapter.notifyDataSetChanged();
            }
        };
        hotelAdapter = new HotelAdapter(getActivity(), selectHotelList);
        hotelAdapter.setOnRecyclerViewListener(hotelRVListener);
        rvHotel.setAdapter(hotelAdapter);
    }

    private void menuProcess() {
        Iterator<HotelEntity> iHotelEntity = selectHotelList.iterator();
        while (iHotelEntity.hasNext()) {
            if (iHotelEntity.next().getOptimize() != spinner_select) {
                iHotelEntity.remove();
            }
        }
    }

    private void tagProcess() {
        selectHotelList.clear();
        if (Tag.equals("推荐")) {
            selectHotelList.addAll(hotelEntityArrayList);
        } else {
            for (HotelEntity hotelEntity:hotelEntityArrayList) {
                if (hotelEntity.getTag().contains(Tag)) {
                    selectHotelList.add(hotelEntity);
                }
            }
        }
    }
}
