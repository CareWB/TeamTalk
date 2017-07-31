package com.zhizulx.tt.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.FileUtil;
import com.zhizulx.tt.utils.MonitorActivityBehavior;
import com.zhizulx.tt.utils.MonitorClickListener;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectTagFragment extends TTBaseFragment {
    private View curView = null;
    private MonitorActivityBehavior monitorActivityBehavior;
    private IMService imService;
    private IMTravelManager travelManager;
    private RelativeLayout high;
    private RelativeLayout medium;
    private RelativeLayout low;
    private ImageView highCircle;
    private ImageView mediumCircle;
    private ImageView lowCircle;
    private List<String> highTags = new ArrayList<>();
    private List<String> mediumTags = new ArrayList<>();
    private List<String> lowTags = new ArrayList<>();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
                initTravelInfo();
                trace("010100", "home page in");
            }
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imServiceConnector.connect(getActivity());
        if (null != curView) {
            ((ViewGroup) curView.getParent()).removeView(curView);
            return curView;
        }
        curView = inflater.inflate(R.layout.travel_fragment_select_tag,
                topContentView);

        initTags();
        initRes();
        initBtn();
        return curView;
    }

    @Override
    public void onResume() {
        super.onResume();
        monitorActivityBehavior = new MonitorActivityBehavior(getActivity());
        monitorActivityBehavior.storeBehavior(monitorActivityBehavior.START);
        circleAnimation(highCircle, 3000);
        circleAnimation(mediumCircle, 6000);
        circleAnimation(lowCircle, 9000);
    }

    @Override
    public void onPause() {
        super.onPause();
        monitorActivityBehavior.storeBehavior(monitorActivityBehavior.END);
    }

    private void initTags() {
        highTags.add(getString(R.string.route_comfort));
        highTags.add(getString(R.string.route_exploration));
        highTags.add(getString(R.string.route_excite));
        mediumTags.add(getString(R.string.route_exploration));
        mediumTags.add(getString(R.string.route_comfort));
        mediumTags.add(getString(R.string.route_encounter));
        lowTags.add(getString(R.string.route_comfort));
        lowTags.add(getString(R.string.route_excite));
        lowTags.add(getString(R.string.route_encounter));
    }

    private void initRes() {
        // 设置顶部标题栏
        hideTopBar();
        high = (RelativeLayout)curView.findViewById(R.id.select_tag_high);
        medium = (RelativeLayout)curView.findViewById(R.id.select_tag_medium);
        low = (RelativeLayout)curView.findViewById(R.id.select_tag_low);
        highCircle = (ImageView)curView.findViewById(R.id.select_tag_high_circle);
        mediumCircle = (ImageView)curView.findViewById(R.id.select_tag_medium_circle);
        lowCircle = (ImageView)curView.findViewById(R.id.select_tag_low_circle);
        circleAnimation(highCircle, 3000);
        circleAnimation(mediumCircle, 6000);
        circleAnimation(lowCircle, 9000);
    }

    private void initBtn() {
        MonitorClickListener selectTagListener = new MonitorClickListener(getActivity()) {
            @Override
            public void onMonitorClick(View v) {
                if (travelManager.getdBInitFin() == false) {
                    Toast.makeText(getActivity(), "数据加载中...", Toast.LENGTH_SHORT).show();
                    return;
                }
                final List<String> tags = new ArrayList<>();
                switch (v.getId()) {
                    case R.id.select_tag_high:
                        tags.addAll(highTags);
                        circleAnimation(highCircle, 500);
                        trace("010301", "select_tag_high");
                        break;
                    case R.id.select_tag_medium:
                        tags.addAll(mediumTags);
                        circleAnimation(mediumCircle, 500);
                        trace("010302", "select_tag_medium");
                        break;
                    case R.id.select_tag_low:
                        tags.addAll(lowTags);
                        circleAnimation(lowCircle, 500);
                        trace("010303", "select_tag_low");
                        break;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        travelManager.getConfigEntity().setTags(tags);
                        TravelUIHelper.openSelectDesignWayActivity(getActivity());
                    }
                },500);
            }
        };
        high.setOnClickListener(selectTagListener);
        medium.setOnClickListener(selectTagListener);
        low.setOnClickListener(selectTagListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imServiceConnector.disconnect(getActivity());
    }

    @Override
    protected void initHandler() {
    }

    private void initTravelInfo() {
        travelManager.initDatePlace();
    }

    private void circleAnimation(ImageView icon, long time) {
        ObjectAnimator oaAnimator = ObjectAnimator.ofFloat(icon, "rotation", 0.0F, 360.0F);
        oaAnimator.setDuration(time);
        oaAnimator.setInterpolator(null);
        oaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        oaAnimator.setRepeatMode(ValueAnimator.RESTART);
        oaAnimator.start();
    }

    private void trace(String code, String msg) {
        if (travelManager != null) {
            String myMsg = "[SelectTagFragment] " + msg;
            travelManager.AppTrace(code, myMsg);
        }
    }
}
