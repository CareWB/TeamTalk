package com.zhizulx.tt.ui.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhizulx.tt.DB.entity.RouteEntity;
import com.zhizulx.tt.DB.sp.SystemConfigSp;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.CsvUtil;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SelectTagFragment extends TTBaseFragment {
    private View curView = null;
    private IMService imService;
    private IMTravelManager travelManager;
    private ImageView high;
    private ImageView medium;
    private ImageView low;
    private List<String> highTags = new ArrayList<>();
    private List<String> mediumTags = new ArrayList<>();
    private List<String> lowTags = new ArrayList<>();

    private String trafficTitle;
    private String trafficUrl;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
                //travelManager.onLocalLoginOk();
                initTravelInfo();
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
        high = (ImageView)curView.findViewById(R.id.select_tag_high);
        medium = (ImageView)curView.findViewById(R.id.select_tag_medium);
        low = (ImageView)curView.findViewById(R.id.select_tag_low);
    }

    private void initBtn() {
        View.OnClickListener selectTagListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tags = new ArrayList<>();
                switch (v.getId()) {
                    case R.id.select_tag_high:
                        tags.addAll(highTags);
                        break;
                    case R.id.select_tag_medium:
                        tags.addAll(mediumTags);
                        break;
                    case R.id.select_tag_low:
                        tags.addAll(lowTags);
                        break;
                }
                travelManager.getRouteEntity().setTags(tags);
                travelManager.setEmotionTags(tags);
                TravelUIHelper.openSelectDesignWayActivity(getActivity());
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
        Calendar cal = Calendar.getInstance();
        travelManager.setStartCity(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOCAL_CITY));
        travelManager.setEndCity(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOCAL_CITY));
        cal.add(Calendar.DATE, 2);
        travelManager.setStartDate((new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime()));
        cal.add(Calendar.DATE, 3);
        travelManager.setEndDate((new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime()));
    }
}
