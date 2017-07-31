package com.zhizulx.tt.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhizulx.tt.R;
import com.zhizulx.tt.config.SysConstant;
import com.zhizulx.tt.imservice.event.TravelEvent;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.MonitorActivityBehavior;
import com.zhizulx.tt.utils.MonitorClickListener;
import com.zhizulx.tt.utils.TravelUIHelper;

import de.greenrobot.event.EventBus;

public class SelectDesignWayFragment extends TTBaseFragment {
    private View curView = null;
    private MonitorActivityBehavior monitorActivityBehavior;
    private LinearLayout introduct;
    private LinearLayout custom;
    private IMTravelManager travelManager;
    private Dialog dialog;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
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
        curView = inflater.inflate(R.layout.travel_fragment_select_design_way,
                topContentView);

        initRes();
        return curView;
    }

    private void initRes() {
        // 设置顶部标题栏
        setTopTitle(getString(R.string.select_design_way));
        setTopLeftButton(R.drawable.tt_top_back);
        topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        introduct = (LinearLayout) curView.findViewById(R.id.select_design_way_introduct);
        custom = (LinearLayout)curView.findViewById(R.id.select_design_way_custom);
        MonitorClickListener designWayListener = new MonitorClickListener(getActivity()) {
            @Override
            public void onMonitorClick(View v) {
                switch (v.getId()) {
                    case R.id.select_design_way_introduct:
                        if (travelManager != null) {
                            travelManager.reqGetRandomRoute(travelManager.GET_ROUTE_BY_TAG);
                            dialog = TravelUIHelper.showCalculateDialog(getActivity());
                            mHandler.postDelayed(runnable, SysConstant.CALCULATE_OVERTIME);
                        }
                        break;
                    case R.id.select_design_way_custom:
                        TravelUIHelper.openCreateTravelActivity(getActivity());
                        break;
                }
            }
        };
        introduct.setOnClickListener(designWayListener);
        custom.setOnClickListener(designWayListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imServiceConnector.disconnect(getActivity());
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
            case QUERY_RANDOM_ROUTE_TAG_OK:
                mHandler.removeCallbacks(runnable);
                dialog.dismiss();
                TravelUIHelper.openSelectTravelRouteActivity(getActivity());
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
}
