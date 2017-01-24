package com.mogujie.tt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogujie.tt.R;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.base.TTBaseFragment;
import com.mogujie.tt.ui.widget.adver.CircleFlowIndicator;
import com.mogujie.tt.ui.widget.adver.ImagePagerAdapter;
import com.mogujie.tt.ui.widget.adver.ViewFlow;

import java.util.ArrayList;

/**
 * 设置页面
 */
public class IntroduceCityFragment extends TTBaseFragment{
	private View curView = null;
    private String city;
    private Intent intent;
    private ImageView back;
    private Boolean selectFlag;

    private ViewFlow mViewFlow;
    private CircleFlowIndicator mFlowIndicator;
    private ArrayList<Integer> imageUrlList = new ArrayList<Integer>();

    private TextView citySelect;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {

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
		curView = inflater.inflate(R.layout.travel_fragment_introduce_city, topContentView);
        intent = getActivity().getIntent();
        city = intent.getStringExtra("city");
        selectFlag = intent.getBooleanExtra("selectFlag", false);
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

	/**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
		hideTopBar();
        back = (ImageView)curView.findViewById(R.id.introduce_city_back);
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (getFragmentManager().getBackStackEntryCount() == 0) {
                    intent.putExtra("selectFlag", selectFlag);
					getActivity().setResult(100, intent);
					getActivity().finish();
					return;
				}
				getFragmentManager().popBackStack();
			}
		});

        mViewFlow = (ViewFlow) curView.findViewById(R.id.viewflow);
        mFlowIndicator = (CircleFlowIndicator) curView.findViewById(R.id.viewflowindic);

        imageUrlList.add(R.drawable.xiamen_1);
        imageUrlList.add(R.drawable.xiamen_2);
        imageUrlList.add(R.drawable.xiamen_3);
        imageUrlList.add(R.drawable.xiamen_4);
        imageUrlList.add(R.drawable.xiamen_5);
        initBanner(imageUrlList);

        citySelect = (TextView) curView.findViewById(R.id.bn_city_select);
        dispCitySelect();
        citySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFlag = selectFlag ? false : true;
                dispCitySelect();
            }
        });

	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {

    }

    private void initBanner(ArrayList<Integer> imageUrlList) {
        mViewFlow.setAdapter(new ImagePagerAdapter(getActivity(), imageUrlList).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
        // 我的ImageAdapter实际图片张数为3

        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(4500);
        mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
        if (imageUrlList.size() > 1)
        {
            mViewFlow.startAutoFlowTimer(); // 启动自动播放
        }
    }

    private void dispCitySelect() {
        if (selectFlag) {
            citySelect.setBackgroundResource(R.drawable.city_select);
        } else {
            citySelect.setBackgroundResource(R.drawable.city_not_select);
        }
    }

}
