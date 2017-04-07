package com.zhizulx.tt.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhizulx.tt.DB.entity.TravelCityEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.adapter.TravelRouteAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.util.ArrayList;
import java.util.List;

import static com.zhizulx.tt.R.id.create_travel_route;

public class SelectTravelRouteFragment extends TTBaseFragment {
    private View curView = null;
    private ImageView reset;
    private Boolean resetFlag = false;
    private TextView createTravelRoute;
    private ImageView noSelectedRouteHint;
    private RecyclerView rvTravelRoute;
    private EditText travelRouteUserWord;
    private TravelRouteAdapter travelRouteAdapter;
    private RelativeLayout lySelectRouteCondition;
    private List<TravelCityEntity> travelCityEntityList = new ArrayList<>();
    private List<TravelCityEntity> travelCityEntityListServer = new ArrayList<>();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelCityEntityList.add(travelCityEntityListServer.get(0));
                travelCityEntityList.add(travelCityEntityListServer.get(1));
                travelCityEntityList.add(travelCityEntityListServer.get(2));
                travelRouteAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != curView) {
            ((ViewGroup) curView.getParent()).removeView(curView);
            return curView;
        }
        curView = inflater.inflate(R.layout.travel_fragment_select_travel_route,
                topContentView);

        initRes();
        initTravelRoute();
        return curView;
    }

    private void initRes() {
        // 设置顶部标题栏
        setTopTitle(getString(R.string.select_travel_route));
        setTopLeftButton(R.drawable.tt_top_back);
        topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        reset = (ImageView)curView.findViewById(R.id.select_travel_route_reset);
        rvTravelRoute = (RecyclerView)curView.findViewById(R.id.rv_select_travel_route);
        lySelectRouteCondition = (RelativeLayout)curView.findViewById(R.id.select_route_condition);
        createTravelRoute = (TextView)curView.findViewById(R.id.create_travel_route);
        noSelectedRouteHint = (ImageView)curView.findViewById(R.id.no_selected_route_hint);
        View.OnClickListener designWayListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.select_travel_route_reset:
                        if (resetFlag == false) {
                            resetFlag = true;
                            travelCityEntityList.clear();
                            travelCityEntityList.add(travelCityEntityListServer.get(3));
                            travelCityEntityList.add(travelCityEntityListServer.get(4));
                            travelCityEntityList.add(travelCityEntityListServer.get(5));
                            travelRouteAdapter.notifyDataSetChanged();
                            reset.setVisibility(View.GONE);
                            lySelectRouteCondition.setVisibility(View.VISIBLE);
                            noSelectedRouteHint.setBackground(getResources().getDrawable(R.drawable.no_selected_route_black));
                        }
                        break;
                    case R.id.create_travel_route:
                        TravelUIHelper.openDetailDispActivity(getActivity());
                        break;
                }
            }
        };
        reset.setOnClickListener(designWayListener);
        createTravelRoute.setOnClickListener(designWayListener);

        travelRouteUserWord = (EditText)curView.findViewById(R.id.travel_route_user_word);
        travelRouteUserWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rvTravelRoute.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initTravelRoute() {
        TravelCityEntity xiamen = new TravelCityEntity();
        TravelCityEntity guangzhou = new TravelCityEntity();
        xiamen.setCityName("厦门");
        guangzhou.setCityName("广州");
        travelCityEntityListServer.add(xiamen);
        travelCityEntityListServer.add(xiamen);
        travelCityEntityListServer.add(xiamen);
        travelCityEntityListServer.add(guangzhou);
        travelCityEntityListServer.add(guangzhou);
        travelCityEntityListServer.add(guangzhou);
        travelCityEntityList.add(travelCityEntityListServer.get(0));
        travelCityEntityList.add(travelCityEntityListServer.get(1));
        travelCityEntityList.add(travelCityEntityListServer.get(2));
        rvTravelRoute.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvTravelRoute.setLayoutManager(layoutManagerResult);
        TravelRouteAdapter.OnRecyclerViewListener hotelRVListener = new TravelRouteAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                TravelUIHelper.openDetailDispActivity(getActivity());
            }
        };
        travelRouteAdapter = new TravelRouteAdapter(getActivity(), travelCityEntityList);
        travelRouteAdapter.setOnRecyclerViewListener(hotelRVListener);
        rvTravelRoute.setAdapter(travelRouteAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initHandler() {
    }

}
