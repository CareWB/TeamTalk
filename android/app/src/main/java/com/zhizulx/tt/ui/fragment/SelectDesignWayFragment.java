package com.zhizulx.tt.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhizulx.tt.R;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.TravelUIHelper;

public class SelectDesignWayFragment extends TTBaseFragment {
    private View curView = null;
    private ImageView introduct;
    private ImageView custom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        introduct = (ImageView)curView.findViewById(R.id.select_design_way_introduct);
        custom = (ImageView)curView.findViewById(R.id.select_design_way_custom);
        View.OnClickListener designWayListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.select_design_way_introduct:
                        TravelUIHelper.openSelectTravelRouteActivity(getActivity());
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
    }

    @Override
    protected void initHandler() {
    }
}
