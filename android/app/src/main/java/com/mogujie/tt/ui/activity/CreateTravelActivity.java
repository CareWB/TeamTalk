package com.mogujie.tt.ui.activity;

import android.os.Bundle;

import com.mogujie.tt.R;
import com.mogujie.tt.ui.base.TTBaseFragmentActivity;

public class CreateTravelActivity extends  TTBaseFragmentActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_fragment_activity_create_travel);
    }
}