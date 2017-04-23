package com.zhizulx.tt.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhizulx.tt.DB.entity.SightEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.config.UrlConstant;
import com.zhizulx.tt.imservice.event.TravelEvent;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.adapter.SightAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 设置页面
 */
public class SelectSightFragment extends TTBaseFragment{
	private View curView = null;
    private Intent intent;
    private IMTravelManager travelManager;
    private TextView total;
    private TextView literature;
    private TextView comfort;
    private TextView exploration;
    private TextView excite;
    private TextView encounter;
    private RecyclerView rvSight;
    private SightAdapter sightAdapter;
    private List<SightEntity> sightEntityList = new ArrayList<>();
    private List<SightEntity> tagSightEntityList = new ArrayList<>();
    String Tag = "全部";
    private PopupWindow mPopupWindow;
    private LinearLayout pop;
    private TextView notScreen;
    private TextView free;
    private LinearLayout lyPop;
    private Dialog dialog;
    static final int ALL = 0;
    static final int FREE = 1;
    private int spinner_select = ALL;
    private TextView selectSightDropText;
    private List<Integer> origin = new ArrayList<>();

    private Map<Integer, String> selectFlag = new HashMap<>();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
                initSightList();
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
        EventBus.getDefault().register(this);
        intent = getActivity().getIntent();
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.travel_fragment_select_sight, topContentView);

		initRes();
        initBtn();
        //testCase();
        initPopupWindow();
        initSight();
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
        EventBus.getDefault().unregister(this);
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
		setTopTitle(getString(R.string.select_sight));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
                updateRoute();
                dialog = TravelUIHelper.showCalculateDialog(getActivity());
                mHandler.postDelayed(runnable, 10000);
			}
		});

        total = (TextView)curView.findViewById(R.id.select_total);
        literature = (TextView)curView.findViewById(R.id.select_literature);
        comfort = (TextView)curView.findViewById(R.id.select_comfort);
        exploration = (TextView)curView.findViewById(R.id.select_exploration);
        excite = (TextView)curView.findViewById(R.id.select_excite);
        encounter = (TextView)curView.findViewById(R.id.select_encounter);
        selectFlag.put(R.id.select_total, "全部");
        selectFlag.put(R.id.select_literature, "文艺");
        selectFlag.put(R.id.select_comfort, "舒适");
        selectFlag.put(R.id.select_exploration, "探险");
        selectFlag.put(R.id.select_excite, "刺激");
        selectFlag.put(R.id.select_encounter, "艳遇");

        pop = (LinearLayout)curView.findViewById(R.id.select_sight_drop);
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.showAsDropDown(curView.findViewById(R.id.select_sight_drop));
            }
        });

        rvSight = (RecyclerView)curView.findViewById(R.id.rv_sight);
        selectSightDropText = (TextView)curView.findViewById(R.id.select_sight_drop_text);
    }

	@Override
	protected void initHandler() {
	}

    private void initBtn() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Tag = selectFlag.get(id);
                tagProcess();
                freeProcess();
                buttonDisp(id);
                sightAdapter.notifyDataSetChanged();
            }
        };
        total.setOnClickListener(listener);
        literature.setOnClickListener(listener);
        comfort.setOnClickListener(listener);
        exploration.setOnClickListener(listener);
        excite.setOnClickListener(listener);
        encounter.setOnClickListener(listener);
    }

    private void buttonDisp(int id) {
        total.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        total.setTextColor(getResources().getColor(R.color.not_clicked));
        literature.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        literature.setTextColor(getResources().getColor(R.color.not_clicked));
        comfort.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        comfort.setTextColor(getResources().getColor(R.color.not_clicked));
        exploration.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        exploration.setTextColor(getResources().getColor(R.color.not_clicked));
        excite.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        excite.setTextColor(getResources().getColor(R.color.not_clicked));
        encounter.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        encounter.setTextColor(getResources().getColor(R.color.not_clicked));

        switch (id) {
            case R.id.select_total:
                total.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                total.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_literature:
                literature.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                literature.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_comfort:
                comfort.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                comfort.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_exploration:
                exploration.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                exploration.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_excite:
                excite.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                excite.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_encounter:
                encounter.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                encounter.setTextColor(getResources().getColor(R.color.clicked));
                break;
        }
    }

    private void initSightList() {
        sightEntityList.clear();
        String cityCode = travelManager.getRouteEntity().getCityCode();
        for (SightEntity sightEntity : travelManager.getSightList()) {
            if (sightEntity.getCityCode().equals(cityCode)) {
                sightEntityList.add(sightEntity);
                if (sightEntity.getSelect() == 1) {
                    origin.add(sightEntity.getPeerId());
                }
            }
        }

        tagSightEntityList.addAll(sightEntityList);
        sightAdapter.notifyDataSetChanged();
    }

/*    private void testCase() {
        String pre = UrlConstant.PIC_URL_PREFIX;

        SightEntity gulangyu = new SightEntity();
        gulangyu.setPeerId(1);
        gulangyu.setName("鼓浪屿");
        gulangyu.setPic(pre+"gulangyu.png");
        gulangyu.setStar(8);
        gulangyu.setTag("文艺");
        gulangyu.setStartTime("12:00");
        gulangyu.setEndTime("15:00");
        gulangyu.setFree(0);
        gulangyu.setMustGo(1);

        SightEntity xiada = new SightEntity();
        xiada.setPeerId(1);
        xiada.setName("厦门大学");
        xiada.setPic(pre+"xiamendaxue.png");
        xiada.setStar(9);
        xiada.setTag("刺激 艳遇");
        xiada.setStartTime("12:00");
        xiada.setEndTime("15:00");
        xiada.setFree(1);
        xiada.setMustGo(1);

        SightEntity nanputuosi = new SightEntity();
        nanputuosi.setPeerId(1);
        nanputuosi.setName("南普陀寺");
        nanputuosi.setPic(pre+"nanputuosi.png");
        nanputuosi.setStar(8);
        nanputuosi.setTag("探险");
        nanputuosi.setStartTime("12:00");
        nanputuosi.setEndTime("15:00");
        nanputuosi.setFree(0);
        nanputuosi.setMustGo(0);

        SightEntity huandaolu = new SightEntity();
        huandaolu.setPeerId(1);
        huandaolu.setName("环岛路");
        huandaolu.setPic(pre+"huandaolu.png");
        huandaolu.setStar(8);
        huandaolu.setTag("舒适");
        huandaolu.setStartTime("12:00");
        huandaolu.setEndTime("15:00");
        huandaolu.setFree(1);
        huandaolu.setMustGo(0);

        SightEntity riguangyan = new SightEntity();
        riguangyan.setPeerId(1);
        riguangyan.setName("日光岩");
        riguangyan.setPic(pre+"riguangyan.png");
        riguangyan.setStar(8);
        riguangyan.setTag("探险");
        riguangyan.setStartTime("12:00");
        riguangyan.setEndTime("15:00");
        riguangyan.setFree(0);
        riguangyan.setMustGo(1);

        SightEntity zengcuoan = new SightEntity();
        zengcuoan.setPeerId(1);
        zengcuoan.setName("曾厝垵");
        zengcuoan.setPic(pre+"zengcuoan.png");
        zengcuoan.setStar(8);
        zengcuoan.setTag("刺激");
        zengcuoan.setStartTime("12:00");
        zengcuoan.setEndTime("15:00");
        zengcuoan.setFree(0);
        zengcuoan.setMustGo(0);

        SightEntity zhongshanlu = new SightEntity();
        zhongshanlu.setPeerId(1);
        zhongshanlu.setName("中山路");
        zhongshanlu.setPic(pre+"zhongshanlu.png");
        zhongshanlu.setStar(8);
        zhongshanlu.setTag("艳遇");
        zhongshanlu.setStartTime("12:00");
        zhongshanlu.setEndTime("15:00");
        zhongshanlu.setFree(1);
        zhongshanlu.setMustGo(1);

        SightEntity xiamenhaidishijie = new SightEntity();
        xiamenhaidishijie.setPeerId(1);
        xiamenhaidishijie.setName("厦门海底世界");
        xiamenhaidishijie.setPic(pre+"xiamenhaidishijie.png");
        xiamenhaidishijie.setStar(8);
        xiamenhaidishijie.setTag("舒适");
        xiamenhaidishijie.setStartTime("12:00");
        xiamenhaidishijie.setEndTime("15:00");
        xiamenhaidishijie.setFree(0);
        xiamenhaidishijie.setMustGo(0);

        SightEntity shuzhuanghuayuan = new SightEntity();
        shuzhuanghuayuan.setPeerId(1);
        shuzhuanghuayuan.setName("菽庄花园");
        shuzhuanghuayuan.setPic(pre+"shuzhuanghuayuan.png");
        shuzhuanghuayuan.setStar(8);
        shuzhuanghuayuan.setTag("文艺");
        shuzhuanghuayuan.setStartTime("12:00");
        shuzhuanghuayuan.setEndTime("15:00");
        shuzhuanghuayuan.setFree(1);
        shuzhuanghuayuan.setMustGo(0);

        SightEntity shadiaowenhuayuan = new SightEntity();
        shadiaowenhuayuan.setPeerId(1);
        shadiaowenhuayuan.setName("沙雕文化园");
        shadiaowenhuayuan.setPic(pre+"shadiaowenhuayuan.png");
        shadiaowenhuayuan.setStar(8);
        shadiaowenhuayuan.setTag("文艺");
        shadiaowenhuayuan.setStartTime("12:00");
        shadiaowenhuayuan.setEndTime("15:00");
        shadiaowenhuayuan.setFree(0);
        shadiaowenhuayuan.setMustGo(1);

        sightEntityList.add(gulangyu);
        sightEntityList.add(xiada);
        sightEntityList.add(nanputuosi);
        sightEntityList.add(huandaolu);
        sightEntityList.add(riguangyan);
        sightEntityList.add(zengcuoan);
        sightEntityList.add(zhongshanlu);
        sightEntityList.add(xiamenhaidishijie);
        sightEntityList.add(shuzhuanghuayuan);
        sightEntityList.add(shadiaowenhuayuan);

        for (SightEntity sightEntity:sightEntityList) {
            if (sightEntity.getMustGo() == 1) {
                sightEntity.setSelect(1);
            }
        }

        tagSightEntityList.addAll(sightEntityList);
    }*/

    private void initSight() {
        rvSight.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvSight.setLayoutManager(layoutManagerResult);
        SightAdapter.OnRecyclerViewListener sightRVListener = new SightAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                TravelUIHelper.openIntroduceSightActivity(getActivity(), tagSightEntityList.get(position).getPeerId());
            }

            @Override
            public void onSelectClick(int position, View v) {
                SightEntity sightEntity = tagSightEntityList.get(position);
                if (sightEntity.getSelect() == 1) {
                    sightEntity.setSelect(0);
                } else {
                    sightEntity.setSelect(1);
                }
                sightAdapter.notifyDataSetChanged();
            }
        };
        sightAdapter = new SightAdapter(getActivity(), tagSightEntityList);
        sightAdapter.setOnRecyclerViewListener(sightRVListener);
        rvSight.setAdapter(sightAdapter);
    }

    private void freeProcess() {
        if (spinner_select == ALL) {
            selectSightDropText.setText(getString(R.string.select_sight_recommend));
            return;
        }

        if (spinner_select == FREE) {
            selectSightDropText.setText(getString(R.string.select_sight_free));
            Iterator<SightEntity> iSightEntity = tagSightEntityList.iterator();
            while (iSightEntity.hasNext()) {
                if (iSightEntity.next().getPrice() != 0) {
                    iSightEntity.remove();
                }
            }
        }
    }

    private void tagProcess() {
        tagSightEntityList.clear();
        if (Tag.equals("全部")) {
            tagSightEntityList.addAll(sightEntityList);
        } else {
            for (SightEntity sightEntity : sightEntityList) {
                if (sightEntity.getTag().contains(Tag)) {
                    tagSightEntityList.add(sightEntity);
                }
            }
        }
    }

    private void initPopupWindow() {
        View popupView = curView.inflate(getActivity(), R.layout.select_sight_popup_window, null);
        notScreen = (TextView) popupView.findViewById(R.id.select_sight_pop_not_screen);
        free = (TextView) popupView.findViewById(R.id.select_sight_pop_free);
        lyPop = (LinearLayout) popupView.findViewById(R.id.ly_select_sight_pop);
        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        //mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);

        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });

        View.OnClickListener popupListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.select_sight_pop_not_screen:
                        spinner_select = 0;
                        tagProcess();
                        freeProcess();
                        sightAdapter.notifyDataSetChanged();
                        mPopupWindow.dismiss();
                        break;

                    case R.id.select_sight_pop_free:
                        spinner_select = 1;
                        tagProcess();
                        freeProcess();
                        sightAdapter.notifyDataSetChanged();
                        mPopupWindow.dismiss();
                        break;

                    case R.id.ly_select_sight_pop:
                        mPopupWindow.dismiss();
                        break;
                }
            }
        };
        lyPop.setOnClickListener(popupListener);
        notScreen.setOnClickListener(popupListener);
        free.setOnClickListener(popupListener);
    }

    private void updateRoute() {
        List<Integer> newSightIDList = new ArrayList<>();
        for (SightEntity sightEntity : sightEntityList) {
            if (sightEntity.getSelect() == 1) {
                newSightIDList.add(sightEntity.getPeerId());
            }
        }
        if (newSightIDList.equals(origin)) {
            getActivity().finish();
            return;
        }
        travelManager.reqUpdateRandomRoute(newSightIDList);
    }

    public void onEventMainThread(TravelEvent event){
        switch (event.getEvent()){
            case UPDATE_RANDOM_ROUTE_OK:
                mHandler.removeCallbacks(runnable);
                dialog.dismiss();
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    intent.putExtra("result", true);
                    getActivity().setResult(102, intent);
                    getActivity().finish();
                    return;
                }
                break;
            case UPDATE_RANDOM_ROUTE_FAIL:
                Log.e("yuki", "UPDATE_RANDOM_ROUTE_FAIL");
                mHandler.removeCallbacks(runnable);
                dialog.dismiss();
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    intent.putExtra("result", false);
                    getActivity().setResult(102, intent);
                    getActivity().finish();
                    return;
                }
                break;
        }
    }

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    };
}
