package com.zhizulx.tt.ui.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.CsvUtil;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SelectTagFragment extends TTBaseFragment {
    private View curView = null;
    private IMService imService;
    private IMTravelManager travelManager;
    private ImageView high;
    private ImageView medium;
    private ImageView low;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
                //travelManager.onLocalLoginOk();
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
        curView = inflater.inflate(R.layout.travel_fragment_select_tag,
                topContentView);

        initRes();
        initBtn();
        return curView;
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
                switch (v.getId()) {
                    case R.id.select_tag_high:
                    case R.id.select_tag_medium:
                    case R.id.select_tag_low:
                        TravelUIHelper.openSelectDesignWayActivity(getActivity());
                        break;
                }
            }
        };
        high.setOnClickListener(selectTagListener);
        medium.setOnClickListener(selectTagListener);
        low.setOnClickListener(selectTagListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initHandler() {
    }

    private void initDB() {

    }
}
