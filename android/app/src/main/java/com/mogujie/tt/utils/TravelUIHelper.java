package com.mogujie.tt.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.SightEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.config.IntentConstant;
import com.mogujie.tt.ui.activity.CreateTravelActivity;
import com.mogujie.tt.ui.activity.IntroduceSightActivity;

public class TravelUIHelper {

	// 对话框回调函数
	public interface dialogCallback{
		public void callback();
	}

	public static void showAlertDialog(Context context, String title, final dialogCallback callback) {
		final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialog_view = inflater.inflate(R.layout.travel_alert_dialog, null);
		TextView textText = (TextView)dialog_view.findViewById(R.id.alert_dialog_title);
		textText.setText(title);
		dialog_view.findViewById(R.id.bn_alert_dialog_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog_view.findViewById(R.id.bn_alert_dialog_confirm).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.callback();
				dialog.dismiss();
			}
		});

		dialog.setContentView(dialog_view);
		dialog.show();
	}

    //跳转到新建行程页面
    public static void openCreateTravelActivity(Context ctx) {
        Intent intent = new Intent(ctx, CreateTravelActivity.class);
        ctx.startActivity(intent);
    }

	//跳转到景点介绍页面
	public static void openIntroduceSightActivity(Context ctx, int sightID) {
        Intent intent = new Intent(ctx, IntroduceSightActivity.class);
        intent.putExtra(IntentConstant.KEY_PEERID, sightID);
        ctx.startActivity(intent);
	}

}
