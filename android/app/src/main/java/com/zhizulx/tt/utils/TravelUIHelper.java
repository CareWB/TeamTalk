package com.zhizulx.tt.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhizulx.tt.DB.Serializable.MapRoute;
import com.zhizulx.tt.R;
import com.zhizulx.tt.config.IntentConstant;
import com.zhizulx.tt.ui.activity.CreateTravelActivity;
import com.zhizulx.tt.ui.activity.DetailDispActivity;
import com.zhizulx.tt.ui.activity.ExpenseDetailActivity;
import com.zhizulx.tt.ui.activity.IntroduceSightActivity;
import com.zhizulx.tt.ui.activity.SelectDesignWayActivity;
import com.zhizulx.tt.ui.activity.SelectHotelActivity;
import com.zhizulx.tt.ui.activity.SelectSightActivity;
import com.zhizulx.tt.ui.activity.SelectTravelRouteActivity;
import com.zhizulx.tt.ui.activity.TrafficListActivity;
import com.zhizulx.tt.ui.activity.HotelWebViewActivity;
import com.zhizulx.tt.ui.route.RouteActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	//跳转到酒店介绍页面
	public static void openIntroduceHotelActivity(Context ctx, String name, String url) {
/*		Intent intent = new Intent(ctx, IntroduceHotelActivity.class);
		intent.putExtra(IntentConstant.KEY_PEERID, hotelID);
		ctx.startActivity(intent);*/
		Intent intent = new Intent(ctx, HotelWebViewActivity.class);
        intent.putExtra(IntentConstant.NAME, name);
        intent.putExtra(IntentConstant.WEBVIEW_URL, url);
		ctx.startActivity(intent);
	}

	//跳转到细节展示页面
	public static void openDetailDispActivity(Context ctx) {
		Intent intent = new Intent(ctx, DetailDispActivity.class);
		ctx.startActivity(intent);
	}

	//跳转到游玩喜好页面
	public static void openExpenseDetailActivity(Context ctx) {
		Intent intent = new Intent(ctx, ExpenseDetailActivity.class);
		ctx.startActivity(intent);
	}

	//跳转到游玩喜好页面
	public static void openTrafficListActivity(Context ctx) {
		Intent intent = new Intent(ctx, TrafficListActivity.class);
		ctx.startActivity(intent);
	}

	//跳转到设计方式选择页面
	public static void openSelectDesignWayActivity(Context ctx) {
		Intent intent = new Intent(ctx, SelectDesignWayActivity.class);
		ctx.startActivity(intent);
	}

	//跳转到路线选择细节页面
	public static void openSelectTravelRouteActivity(Context ctx) {
		Intent intent = new Intent(ctx, SelectTravelRouteActivity.class);
		ctx.startActivity(intent);
	}

	//跳转到景点选择细节页面
	public static void openSelectSightActivity(Context ctx) {
		Intent intent = new Intent(ctx, SelectSightActivity.class);
		ctx.startActivity(intent);
	}

	//跳转到路径规划页面
	public static void openMapRouteActivity(Context ctx, MapRoute mapRoute) {
		Intent intent = new Intent(ctx, RouteActivity.class);
        intent.putExtra("map_point", mapRoute);
        ctx.startActivity(intent);
	}
}
