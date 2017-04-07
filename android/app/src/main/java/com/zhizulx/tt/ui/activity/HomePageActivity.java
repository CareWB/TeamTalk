package com.zhizulx.tt.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhizulx.tt.DB.sp.SystemConfigSp;
import com.zhizulx.tt.R;
import com.zhizulx.tt.config.IntentConstant;
import com.zhizulx.tt.imservice.event.LoginEvent;
import com.zhizulx.tt.imservice.event.UserInfoEvent;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.service.LocationService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.utils.FileUtil;
import com.zhizulx.tt.utils.ImageEffect;
import com.zhizulx.tt.utils.ImageUtil;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class HomePageActivity extends FragmentActivity {
    private ImageView mineIcon;
    private PopupWindow popupWindow;
    private RelativeLayout mine;
    private ImageView mineAvatar;
    private TextView commonInfo;
    private TextView order;
    private TextView aboutUs;
    private TextView clearCache;
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private IMService imService;

    private IMServiceConnector imServiceConnector = new IMServiceConnector() {
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
        EventBus.getDefault().register(this);
        imServiceConnector.connect(this);
        setContentView(R.layout.activity_homepage);
        initView();
        initButton();
        startLocation();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        imServiceConnector.disconnect(this);
        super.onDestroy();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mineIcon = (ImageView)findViewById(R.id.mine_icon);
    }

    private void initButton() {
        View.OnClickListener homePageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mine_icon:
                        initPopupWindow();
                        break;
                }
            }
        };
        mineIcon.setOnClickListener(homePageListener);
    }

    class popupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    protected void initPopupWindow(){
        View popupWindowView = getLayoutInflater().inflate(R.layout.travel_popup_mine, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationLeftFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示位置
        popupWindow.showAtLocation(mineIcon, Gravity.LEFT, 0, 500);
        //设置背景半透明
        backgroundAlpha(0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*if( popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow=null;
                }*/
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        mine = (RelativeLayout)popupWindowView.findViewById(R.id.mine_info);
        mineAvatar = (ImageView) popupWindowView.findViewById(R.id.mine_avatar);
        ImageUtil.GlideRoundAvatar(HomePageActivity.this, "http://i3.sinaimg.cn/blog/2014/1029/S129809T1414550868715.jpg", mineAvatar);
        commonInfo = (TextView)popupWindowView.findViewById(R.id.mine_common_info);
        order = (TextView)popupWindowView.findViewById(R.id.mine_order);
        aboutUs = (TextView)popupWindowView.findViewById(R.id.mine_about_us);
        clearCache = (TextView)popupWindowView.findViewById(R.id.mine_clear_cache);

        View.OnClickListener popListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mine_info:
                        Intent intentMine = new Intent(HomePageActivity.this, MineInfoActivity.class);
                        startActivity(intentMine);
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_common_info:
                        Intent intentMineCommon = new Intent(HomePageActivity.this, MineCommonActivity.class);
                        startActivity(intentMineCommon);
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_order:
                        Intent intentMineOrder = new Intent(HomePageActivity.this, MineOrderActivity.class);
                        startActivity(intentMineOrder);
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_about_us:
                        Intent intentMineAboutUs = new Intent(HomePageActivity.this, MineAboutUsActivity.class);
                        startActivity(intentMineAboutUs);
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_clear_cache:
                        ClearCache();
                        popupWindow.dismiss();
                        break;
                }
            }
        };
        mine.setOnClickListener(popListener);
        commonInfo.setOnClickListener(popListener);
        order.setOnClickListener(popListener);
        aboutUs.setOnClickListener(popListener);
        clearCache.setOnClickListener(popListener);
    }

    private void ClearCache() {
        TravelUIHelper.dialogCallback callback = new TravelUIHelper.dialogCallback() {
            @Override
            public void callback() {
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiskCache();
                Glide.get(HomePageActivity.this).clearMemory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.deleteHistoryFiles(new File(FileUtil.getAppPath() + File.separator), System.currentTimeMillis());
                        Glide.get(HomePageActivity.this).clearDiskCache();
                        Toast toast = Toast.makeText(HomePageActivity.this,R.string.thumb_remove_finish,Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                },500);
            }
        };
        TravelUIHelper.showAlertDialog(HomePageActivity.this, getString(R.string.clear_cache_tip), callback);
    }

    public void onEventMainThread(UserInfoEvent event){
        switch (event){
            case USER_INFO_OK:
                break;
        }
    }

    public void onEventMainThread(LoginEvent event){
        switch (event){
            case LOGIN_OUT:
                handleOnLogout();
                break;
        }
    }

    private void handleOnLogout() {
        finish();
        jumpToLoginPage();

    }

    private void jumpToLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(IntentConstant.KEY_LOGIN_NOT_AUTO, true);
        startActivity(intent);
    }

    private void startLocation() {
        Intent intent = new Intent();
        intent.setClass(this, LocationService.class);
        startService(intent);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
