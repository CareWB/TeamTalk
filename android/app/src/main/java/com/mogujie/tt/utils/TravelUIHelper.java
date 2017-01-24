package com.mogujie.tt.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mogujie.tt.R;
import com.mogujie.tt.config.IntentConstant;
import com.mogujie.tt.ui.activity.Calculating;
import com.mogujie.tt.ui.activity.CreateTravelActivity;
import com.mogujie.tt.ui.activity.DetailDispActivity;
import com.mogujie.tt.ui.activity.IntroduceHotelActivity;
import com.mogujie.tt.ui.activity.IntroduceSightActivity;
import com.mogujie.tt.ui.activity.PlayBehaviorActivity;
import com.mogujie.tt.ui.activity.SelectSightActivity;
import com.mogujie.tt.ui.activity.TrafficListActivity;
import com.mogujie.tt.ui.activity.TravelDetailActivity;

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

	//跳转到景点介绍页面
	public static void openIntroduceHotelActivity(Context ctx, int hotelID) {
		Intent intent = new Intent(ctx, IntroduceHotelActivity.class);
		intent.putExtra(IntentConstant.KEY_PEERID, hotelID);
		ctx.startActivity(intent);
	}

	//跳转到详细行程页面
	public static void openTravelDetailActivity(Context ctx) {
		Intent intent = new Intent(ctx, TravelDetailActivity.class);
		//Intent intent = new Intent(ctx, Calculating.class);
		ctx.startActivity(intent);
	}

	//跳转到细节展示页面
	public static void openDetailDispActivity(Context ctx) {
		Intent intent = new Intent(ctx, DetailDispActivity.class);
		ctx.startActivity(intent);
	}

    //跳转到游玩喜好页面
    public static void openPlayBehaviorActivity(Context ctx) {
        Intent intent = new Intent(ctx, PlayBehaviorActivity.class);
        ctx.startActivity(intent);
    }
}
