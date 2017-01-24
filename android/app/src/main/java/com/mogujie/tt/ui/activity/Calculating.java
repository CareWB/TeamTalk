package com.mogujie.tt.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.event.LoginEvent;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;

import de.greenrobot.event.EventBus;


public class Calculating extends FragmentActivity{
    private ImageView calculating;
    private IMService imService;
	private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            imService = imServiceConnector.getIMService();
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 在这个地方加可能会有问题吧
        EventBus.getDefault().register(this);
		imServiceConnector.connect(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.travel_activity_calculate);

        calculating = (ImageView)findViewById(R.id.calculating);
        Glide.with(this).load(R.drawable.testgif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(calculating);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		imServiceConnector.disconnect(this);
        super.onDestroy();
	}

	public void onEventMainThread(LoginEvent event){
        switch (event){
            case LOGIN_OUT:
                break;
        }
    }

}
