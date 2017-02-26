package com.mogujie.tt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.UserEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.manager.IMLoginManager;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.base.TTBaseFragment;
import com.mogujie.tt.utils.TravelUIHelper;

/**
 * 设置页面
 */
public class MineInfoFragment extends TTBaseFragment{
	private View curView = null;
	private ImageView avatar;
	private UserEntity currentUser;
	private TextView nickName;
	private TextView sex;
	private TextView marital;
	private TextView homeland;
	private RelativeLayout rlSignature;
	private ImageView logout;

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
				int logid = imService.getLoginManager().getLoginId();
				currentUser = imService.getContactManager().findContact(logid);
				nickName.setText(currentUser.getMainName());
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
        super.onActivityResult(requestCode, resultCode, data);
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

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
		View.OnClickListener mineInfoListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.mine_info_avatar:
						break;
					case R.id.mine_info_name:
						break;
					case R.id.mine_info_sex:
						break;
					case R.id.mine_info_marital:
						break;
					case R.id.mine_info_homeland:
						break;
					case R.id.rl_personalized_signature:
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
}
