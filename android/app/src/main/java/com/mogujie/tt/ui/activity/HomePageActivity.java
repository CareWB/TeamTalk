package com.mogujie.tt.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mogujie.tt.R;

public class HomePageActivity extends FragmentActivity {
    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        initFragment();
        setFragmentIndicator(0);
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        TextView travel = (TextView)findViewById(R.id.tbn_travel);
        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentIndicator(0);
            }
        });

        TextView local = (TextView)findViewById(R.id.tbn_local);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentIndicator(1);
            }
        });

    }

    private void initFragment() {
        mFragments = new Fragment[2];
        mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_travel_tip);
        mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_local);
    }

    public void setFragmentIndicator(int which) {
        getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).show(mFragments[which]).commit();
    }

}
