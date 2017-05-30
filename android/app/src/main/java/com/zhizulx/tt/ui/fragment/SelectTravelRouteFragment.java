package com.zhizulx.tt.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhizulx.tt.DB.entity.RouteEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.config.SysConstant;
import com.zhizulx.tt.imservice.event.TravelEvent;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.adapter.TravelRouteAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.MonitorActivityBehavior;
import com.zhizulx.tt.utils.MonitorClickListener;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SelectTravelRouteFragment extends TTBaseFragment {
    private View curView = null;
    private MonitorActivityBehavior monitorActivityBehavior;
    private IMTravelManager travelManager;
    private ImageView reset;
    private TextView createTravelRoute;
    private ImageView noSelectedRouteHint;
    private RecyclerView rvTravelRoute;
    private EditText travelRouteUserWord;
    private TravelRouteAdapter travelRouteAdapter;
    private RelativeLayout lySelectRouteCondition;
    private List<RouteEntity> routeEntityList = new ArrayList<>();
    private List<RouteEntity> routeEntityListServer = new ArrayList<>();
    private int firstPageRouteNum = 0;
    private final static int ONE_PAGE_NUM = 3;
    private Dialog dialog;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
                travelManager.initDatePlace();
                routeEntityListServer.addAll(travelManager.getRouteEntityList());
                DaySort sort = new DaySort();
                Collections.sort(routeEntityListServer,sort);
                if (routeEntityListServer.size() > ONE_PAGE_NUM) {
                    firstPageRouteNum = ONE_PAGE_NUM;
                    for (int i = 0; i < ONE_PAGE_NUM; i ++) {
                        routeEntityList.add(routeEntityListServer.get(i));
                    }
                } else {
                    firstPageRouteNum = routeEntityListServer.size();
                    routeEntityList.addAll(routeEntityListServer);
                    reset.setVisibility(View.GONE);
                    lySelectRouteCondition.setVisibility(View.VISIBLE);
                    noSelectedRouteHint.setBackground(getResources().getDrawable(R.drawable.no_selected_route_black));
                }
                initTravelRoute();
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
        EventBus.getDefault().register(this);
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
        MonitorClickListener designWayListener = new MonitorClickListener(getActivity()) {
            @Override
            public void onMonitorClick(View v) {
                switch (v.getId()) {
                    case R.id.select_travel_route_reset:
                        routeEntityList.clear();
                        for (int i = ONE_PAGE_NUM; i < routeEntityListServer.size(); i ++) {
                            routeEntityList.add(routeEntityListServer.get(i));
                        }
                        travelRouteAdapter.notifyDataSetChanged();
                        reset.setVisibility(View.GONE);
                        lySelectRouteCondition.setVisibility(View.VISIBLE);
                        noSelectedRouteHint.setBackground(getResources().getDrawable(R.drawable.no_selected_route_black));
                        noSelectedRouteHint.setVisibility(View.VISIBLE);
                        /** 设置缩放动画 */
                        final ScaleAnimation animation =new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(2000);//设置动画持续时间
                        noSelectedRouteHint.setAnimation(animation);
                        /** 开始动画 */
                        animation.startNow();
                        break;
                    case R.id.create_travel_route:
                        if (travelRouteUserWord.getText().toString().length() > 0) {
                            travelManager.getConfigEntity().setSentence(travelRouteUserWord.getText().toString());
                            travelManager.reqGetRandomRoute(travelManager.GET_ROUTE_BY_SENTENCE);
                            mHandler.postDelayed(runnable, SysConstant.CALCULATE_OVERTIME);
                            dialog = TravelUIHelper.showCalculateDialog(getActivity());
                        } else {
                            Toast.makeText(getActivity(), "不要惜字如金嘛", Toast.LENGTH_SHORT).show();
                        }
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
        rvTravelRoute.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvTravelRoute.setLayoutManager(layoutManagerResult);
        TravelRouteAdapter.OnRecyclerViewListener hotelRVListener = new TravelRouteAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                travelManager.setRouteEntity(routeEntityList.get(position));
                travelManager.getRouteEntity().setTags(travelManager.getConfigEntity().getTags());
                travelManager.getRouteEntity().setRouteType(travelManager.getConfigEntity().getTags().get(0));
                TravelUIHelper.openDetailDispActivity(getActivity());
            }
        };
        travelRouteAdapter = new TravelRouteAdapter(getActivity(), travelManager, routeEntityList);
        travelRouteAdapter.setOnRecyclerViewListener(hotelRVListener);
        rvTravelRoute.setAdapter(travelRouteAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imServiceConnector.disconnect(this.getActivity());
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        monitorActivityBehavior = new MonitorActivityBehavior(getActivity());
        monitorActivityBehavior.storeBehavior(monitorActivityBehavior.START);
    }

    @Override
    public void onPause() {
        super.onPause();
        monitorActivityBehavior.storeBehavior(monitorActivityBehavior.END);
    }

    @Override
    protected void initHandler() {
    }

    public void onEventMainThread(TravelEvent event){
        switch (event.getEvent()){
            case QUERY_RANDOM_ROUTE_SENTENCE_OK:
                mHandler.removeCallbacks(runnable);
                dialog.dismiss();
                TravelUIHelper.openDetailDispActivity(getActivity());
                break;
            case QUERY_RANDOM_ROUTE_FAIL:
                Log.e("yuki", "QUERY_RANDOM_ROUTE_FAIL");
                break;
        }
    }

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    };

    public class DaySort implements Comparator{
        @Override
        public int compare(Object arg0, Object arg1) {
            // TODO Auto-generated method stub
            RouteEntity route0 = (RouteEntity) arg0;
            RouteEntity route1 = (RouteEntity) arg1;
            int d0 = route0.getDay();
            int d1 = route1.getDay();
            return d0 > d1 ? 1 : -1; //按照时间的由小到大排列
        }
    }
}
