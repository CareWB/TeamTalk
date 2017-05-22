package com.zhizulx.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhizulx.tt.DB.entity.UserEntity;
import com.zhizulx.tt.DB.sp.SystemConfigSp;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.event.UserInfoEvent;
import com.zhizulx.tt.imservice.manager.IMLoginManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.activity.MineTextChangeActivity;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.ui.helper.PhotoHelper;
import com.zhizulx.tt.ui.widget.city.CityActivity;
import com.zhizulx.tt.utils.FileUtil;
import com.zhizulx.tt.utils.ImageEffect;
import com.zhizulx.tt.utils.ImageUtil;
import com.zhizulx.tt.utils.MoGuHttpClient;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 设置页面
 */
public class MineInfoFragment extends TTBaseFragment{
	private View curView = null;
	private ImageView avatar;
	private IMService imService;
	private UserEntity currentUser;
	private TextView nickName;
	private TextView sex;
	private TextView marital;
	private TextView homeland;
	private RelativeLayout rlSignature;
    private StringBuffer signature = new StringBuffer();
	private ImageView logout;
    private PopupWindow popupWindow;
    private TextView phote;
    private TextView gallary;
    private ImageView avatarCancel;
    private TextView sexGusee;
    private TextView sexMale;
    private TextView sexFemale;
    private ImageView sexCancel;
    private TextView maritalGusee;
    private TextView maritalSingle;
    private TextView maritalCouple;
    private ImageView maritalCancel;

	private Uri imageUri;//to store the big bitmap
	private Uri imagePhoto;
	private String avatarUploadUrl;

	private static final int PHOTO_REQUEST_CAREMA = 0;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 1;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 2;// 结果
    private static final int NAME_CHANGE = 11;
    private static final int SIGN_CHANGE = 12;
    private static final int HOMELAND_CHANGE = 100;// 结果
    private static final int NAME_LIMIT = 6;// 结果
    private static final int SIGN_LIMIT = 32;// 结果
	private static final int Cancel = 123;
	private int iAvatar = Cancel;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            if (imService != null) {
				int logid = imService.getLoginManager().getLoginId();
				String avatarUriPath = String.format("file://%s", imService.getLoginManager().getUserAvatarPath());
				imageUri = Uri.parse(avatarUriPath);
				imagePhoto = Uri.parse("file://%s" + FileUtil.getAppPath() + File.separator + "photo.jpg");
				currentUser = imService.getLoginManager().getLoginInfo();
				nickName.setText(currentUser.getMainName());
                //ImageUtil.GlideRoundAvatar(getActivity(), "http://i3.sinaimg.cn/blog/2014/1029/S129809T1414550868715.jpg", avatar);
				ImageUtil.GlideRoundAvatar(getActivity(), currentUser.getAvatar(), avatar);
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
		curView = inflater.inflate(R.layout.travel_fragment_mine_info, topContentView);
		initRes();
        initBtn();
		return curView;
	}

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        imServiceConnector.disconnect(getActivity());
    }

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_GALLERY:
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    crop(uri);
                }
                break;
            case PHOTO_REQUEST_CAREMA:
                // 从相机返回的数据
                if (hasSdcard()) {
                    crop(imagePhoto);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.storecard_not_exit), Toast.LENGTH_SHORT).show();
                }
                break;
            case PHOTO_REQUEST_CUT:
                // 从剪切图片返回的数据
                if (data != null) {
                    Bitmap bitmap = decodeUriAsBitmap(imageUri);
                    avatar.setImageBitmap(ImageEffect.makeRoundCorner(bitmap));
                    new Thread(runnable).start();
                }
                break;
            case NAME_CHANGE:
                nickName.setText(data.getStringExtra("content"));
                break;
            case SIGN_CHANGE:
                signature.setLength(0);
                signature.append(data.getStringExtra("content"));
                break;
            case HOMELAND_CHANGE:
                homeland.setText(data.getStringExtra("city"));
                break;
        }

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onEventMainThread(UserInfoEvent event){
		switch (event){
			case MY_AVATAR_OK:

				break;

			case AVATAR_UPLOAD_URL_OK:
				avatarUploadUrl = SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.AVATAR_UPLOAD_URL);
				break;
		}
	}


	private String uploadAvatar() {
		Bitmap bitmap;
		String result = "";
		try {
			bitmap = PhotoHelper.revitionImage(imService.getLoginManager().getUserAvatarPath());
			if (null != bitmap) {
				MoGuHttpClient httpClient = new MoGuHttpClient();
				byte[] bytes = PhotoHelper.getBytes(bitmap);
				if (avatarUploadUrl != null && !avatarUploadUrl.isEmpty()) {
					result = httpClient.uploadAvatar(avatarUploadUrl+"/auth/terminal", bytes,
							String.valueOf(imService.getLoginManager().getLoginId()));
				}
			}
		} catch (IOException e) {
			logger.e(e.getMessage());
		}
		return result;
	}

    /**
	 * @Description 初始化资源
	 */
	private void initRes() {
		setTopTitle(getString(R.string.mine_info));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});

		avatar = (ImageView)curView.findViewById(R.id.mine_info_avatar);
		nickName = (TextView)curView.findViewById(R.id.mine_info_name);
		sex = (TextView)curView.findViewById(R.id.mine_info_sex);
		marital = (TextView)curView.findViewById(R.id.mine_info_marital);
		homeland = (TextView)curView.findViewById(R.id.mine_info_homeland);
		rlSignature = (RelativeLayout)curView.findViewById(R.id.rl_personalized_signature);
		logout = (ImageView)curView.findViewById(R.id.logout);
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String val = "null";
			if (msg.what == 1)
			{
				Bundle data = msg.getData();
				val = data.getString("avatarUrl");
			}

			Log.i("mylog", "请求结果为-->" + val);
		}
	};

	Runnable runnable = new Runnable(){
		@Override
		public void run() {
			//
			// TODO: http request.
			//
			Message msg = new Message();
			msg.what = 0;
			Bundle data = new Bundle();
			String avatarUrl = uploadAvatar();

			msg.what = 1;
			data.putString("avatarUrl", avatarUrl);
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

	private Bitmap decodeUriAsBitmap(Uri uri){
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
		View.OnClickListener mineInfoListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.mine_info_avatar:
						changeAvatar();
						break;
					case R.id.mine_info_name:
						changeName();
						break;
					case R.id.mine_info_sex:
                        changeSex();
						break;
					case R.id.mine_info_marital:
                        changeMarital();
						break;
					case R.id.mine_info_homeland:
                        changeHomeland();
						break;
					case R.id.rl_personalized_signature:
                        changeSign();
						break;
					case R.id.logout:
						TravelUIHelper.showAlertDialog(getActivity(), getString(R.string.exit_zhizulx_tip), new TravelUIHelper.dialogCallback() {
							@Override
							public void callback() {
								IMLoginManager.instance().setKickout(false);
								IMLoginManager.instance().logOut();
								getActivity().finish();
							}
						});
						break;
				}
			}
		};
		avatar.setOnClickListener(mineInfoListener);
		nickName.setOnClickListener(mineInfoListener);
		sex.setOnClickListener(mineInfoListener);
		marital.setOnClickListener(mineInfoListener);
		homeland.setOnClickListener(mineInfoListener);
		rlSignature.setOnClickListener(mineInfoListener);
		logout.setOnClickListener(mineInfoListener);
    }

	private void changeAvatar() {
        View popupWindowView = getActivity().getLayoutInflater().inflate(R.layout.travel_popup_avatar, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示位置
        popupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.travel_fragment_mine_info, null),
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
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

        phote = (TextView)popupWindowView.findViewById(R.id.mine_info_photo);
        gallary = (TextView)popupWindowView.findViewById(R.id.mine_info_gallary);
        avatarCancel = (ImageView)popupWindowView.findViewById(R.id.mine_info_avatar_cancel);

        View.OnClickListener popAvatar = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mine_info_photo:
						iAvatar = PHOTO_REQUEST_CAREMA;
						avatorGet();
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_info_gallary:
						iAvatar = PHOTO_REQUEST_GALLERY;
						avatorGet();
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_info_avatar_cancel:
                        popupWindow.dismiss();
                        break;
                }
            }
        };
        phote.setOnClickListener(popAvatar);
        gallary.setOnClickListener(popAvatar);
        avatarCancel.setOnClickListener(popAvatar);
	}

	public void avatorGet() {
		switch(iAvatar)
		{
			case PHOTO_REQUEST_CAREMA:
				camera();
				break;
			case PHOTO_REQUEST_GALLERY:
				gallery();
				break;
			case Cancel:
				break;
		}
	}

	/* 从相册获取 */
	public void gallery() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/* 从相机获取 */
	public void camera() {
		// 激活相机
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePhoto);
		}
        if (!FileUtil.isFileExist("photo.jpg", "ZZLX")) {
            FileUtil.creatSDDir("ZZLX");
            FileUtil.createFileInSDCard("photo.jpg", "ZZLX");
        }
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
	}

	/* 判断sdcard是否被挂载 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/* 剪切图片 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", false);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

    class popupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

	private void changeName() {
        Intent nameIntent = new Intent(getActivity(), MineTextChangeActivity.class);
        nameIntent.putExtra("title", getString(R.string.change_name));
        nameIntent.putExtra("content", nickName.getText().toString());
        nameIntent.putExtra("limit", NAME_LIMIT);
        startActivityForResult(nameIntent, NAME_CHANGE);
    }

    private void changeSign() {
        Intent nameIntent = new Intent(getActivity(), MineTextChangeActivity.class);
        nameIntent.putExtra("title", getString(R.string.change_sign));
        nameIntent.putExtra("content", signature.toString());
        nameIntent.putExtra("limit", SIGN_LIMIT);
        startActivityForResult(nameIntent, SIGN_CHANGE);
    }

    private void changeSex() {
        View popupWindowView = getActivity().getLayoutInflater().inflate(R.layout.travel_popup_sex, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示位置
        popupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.travel_fragment_mine_info, null),
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
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

        sexGusee = (TextView)popupWindowView.findViewById(R.id.mine_info_sex_guess);
        sexMale = (TextView)popupWindowView.findViewById(R.id.mine_info_sex_male);
        sexFemale = (TextView)popupWindowView.findViewById(R.id.mine_info_sex_female);
        sexCancel = (ImageView) popupWindowView.findViewById(R.id.mine_info_sex_cancel);

        View.OnClickListener popSex = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mine_info_sex_guess:
                        sex.setText("你猜");
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_info_sex_male:
                        sex.setText("帅哥");
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_info_sex_female:
                        sex.setText("美女");
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_info_sex_cancel:
                        popupWindow.dismiss();
                        break;
                }
            }
        };
        sexGusee.setOnClickListener(popSex);
        sexMale.setOnClickListener(popSex);
        sexFemale.setOnClickListener(popSex);
        sexCancel.setOnClickListener(popSex);
    }

    private void changeMarital() {
        View popupWindowView = getActivity().getLayoutInflater().inflate(R.layout.travel_popup_marital, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示位置
        popupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.travel_fragment_mine_info, null),
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
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

        maritalGusee = (TextView)popupWindowView.findViewById(R.id.mine_info_marital_guess);
        maritalSingle = (TextView)popupWindowView.findViewById(R.id.mine_info_marital_single);
        maritalCouple = (TextView)popupWindowView.findViewById(R.id.mine_info_marital_couple);
        maritalCancel = (ImageView) popupWindowView.findViewById(R.id.mine_info_marital_cancel);

        View.OnClickListener popMarital = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mine_info_marital_guess:
                        marital.setText("无所谓");
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_info_marital_single:
                        marital.setText("可约");
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_info_marital_couple:
                        marital.setText("拒邀");
                        popupWindow.dismiss();
                        break;
                    case R.id.mine_info_marital_cancel:
                        popupWindow.dismiss();
                        break;
                }
            }
        };
        maritalGusee.setOnClickListener(popMarital);
        maritalSingle.setOnClickListener(popMarital);
        maritalCouple.setOnClickListener(popMarital);
        maritalCancel.setOnClickListener(popMarital);
    }

    private void changeHomeland() {
        Intent citySelect = new Intent(getActivity(), CityActivity.class);
        citySelect.putExtra("title", getString(R.string.select_homeland));
        startActivityForResult(citySelect, HOMELAND_CHANGE);
    }
}
