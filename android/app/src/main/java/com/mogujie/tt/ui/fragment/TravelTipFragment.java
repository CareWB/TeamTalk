package com.mogujie.tt.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mogujie.tt.DB.entity.TravelEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.event.TravelEvent;
import com.mogujie.tt.imservice.manager.IMTravelManager;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.adapter.TravelTipAdapter;
import com.mogujie.tt.ui.base.TTBaseFragment;
import com.mogujie.tt.utils.BlurBitmapUtils;
import com.mogujie.tt.utils.DateUtil;
import com.mogujie.tt.utils.ViewSwitchUtils;
import com.view.jameson.library.CardScaleHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;

/**
 * 设置页面
 */
public class TravelTipFragment extends TTBaseFragment {
	private View curView = null;
	private RecyclerView mRecyclerView;
	private ImageView mBlurView;
    private TextView tvToday;
    private TextView tvWeekday;
	private TextView mUp;
	private CardScaleHelper mCardScaleHelper = null;
	private Runnable mBlurRunnable;
	private int mLastPos = -1;
    private IMTravelManager imTravelManager;
    private List<TravelEntity> travelEntityList = new ArrayList<>();
    private TravelTipAdapter travelTipAdapter;
	private IMService imService;

	private IMServiceConnector imServiceConnector = new IMServiceConnector(){
		@Override
		public void onIMServiceConnected() {
			logger.d("config#onIMServiceConnected");
			imService = imServiceConnector.getIMService();
			if (imService != null) {
                imTravelManager = imService.getTravelManager();
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
		EventBus.getDefault().register(TravelTipFragment.this);
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.tt_fragment_travel_tip, container, false);
		init();
		//test();
        initTravel();
		return curView;
	}

	@Override
	protected void initHandler() {

	}

	/**
	 * Called when the fragment is no longer in use.  This is called
	 * after {@link #onStop()} and before {@link #onDetach()}.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		imServiceConnector.disconnect(getActivity());
        EventBus.getDefault().unregister(TravelTipFragment.this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void init() {
		mRecyclerView = (RecyclerView) curView.findViewById(R.id.recycler_view_travel_tip);
        mBlurView = (ImageView)curView.findViewById(R.id.blurView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange();
                }
            }
		});
        tvToday = (TextView)curView.findViewById(R.id.travel_tip_today);
        SimpleDateFormat dateFormater = new SimpleDateFormat("MM-dd");
        Date date=new Date();
        tvToday.setText(dateFormater.format(date));

        tvWeekday = (TextView)curView.findViewById(R.id.travel_tip_weekday);
        tvWeekday.setText(DateUtil.getWeekOfDate(date));
		initBtn();
	}

	private void initBtn() {
/*		mUp = (TextView)curView.findViewById(R.id.other_tip);
		mUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.activity_open,0);
			}
		});*/
	}

	private void notifyBackgroundChange() {
		if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
		mLastPos = mCardScaleHelper.getCurrentItemPos();
		final String url = travelEntityList.get(mCardScaleHelper.getCurrentItemPos()).getDestinationBK();
		mBlurView.removeCallbacks(mBlurRunnable);
		mBlurRunnable = new Runnable() {
			@Override
			public void run() {
  /*              if (mCardScaleHelper.getCurrentItemPos() == travelEntityList.size() - 1) {
                    Bitmap loadedImage= BitmapFactory.decodeResource(getResources(), R.drawable.pic4);
                    ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), loadedImage, 15));
                } else {
                    ImageLoader.getInstance().loadImage(url, null, null, new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), loadedImage, 15));
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                        }
                    });
                }*/
                Bitmap bmp;
                if (mLastPos == travelEntityList.size()-1) {
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.new_travel_bk);
                } else {
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.xiamen);
                }

				ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bmp, 15));
            }
		};

		mBlurView.postDelayed(mBlurRunnable, 500);
	}

    public void onEventMainThread(TravelEvent event){
        switch (event.event){
            case TRAVEL_LIST_OK:
                freshTravel();
                break;
        }
    }

    private void initTravel() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //travelEntityList.addAll(imTravelManager.getTravelEntityList());
        travelEntityList.add(new TravelEntity());
        travelTipAdapter = new TravelTipAdapter(getActivity(), imService, travelEntityList);
        TravelTipAdapter.OnRecyclerViewListener travelTipRVListener = new TravelTipAdapter.OnRecyclerViewListener() {
            @Override
            public void onDeleteClick(int position) {
                TravelEntity travelEntity = travelEntityList.get(position);
                List<Integer> delIdList = new ArrayList<>();
                delIdList.add(travelEntity.getDbId());
                imTravelManager.reqDelTravel(delIdList);

                travelEntityList.remove(position);
                travelTipAdapter.notifyItemRemoved(position);
                Toast.makeText(getActivity(),"delete ok",Toast.LENGTH_SHORT).show();
            }
        };
        travelTipAdapter.setOnRecyclerViewListener(travelTipRVListener);
        mRecyclerView.setAdapter(travelTipAdapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
        notifyBackgroundChange();
    }

    private void test(){
        TravelEntity xianmen = new TravelEntity();
        xianmen.setDestination("厦门");
        xianmen.setDestinationBK("http://i3.sinaimg.cn/blog/2014/1029/S129809T1414550868715.jpg");
        xianmen.setDuration(6);
        xianmen.setStartDate("10.18");
        xianmen.setEndDate("10.23");
        xianmen.setPersonNum(2);
        xianmen.setCost(3444);
        travelEntityList.add(xianmen);

        TravelEntity newTravel = new TravelEntity();
        travelEntityList.add(newTravel);
        initTravel();
    }

   private void freshTravel() {
        travelEntityList.clear();
        travelEntityList.addAll(imTravelManager.getTravelEntityList());
        travelEntityList.add(new TravelEntity());
        travelTipAdapter.notifyDataSetChanged();
    }
}
