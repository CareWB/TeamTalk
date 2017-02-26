package com.zhizulx.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhizulx.tt.DB.entity.SightEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.config.UrlConstant;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.activity.SelectHotelActivity;
import com.zhizulx.tt.ui.adapter.SightAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 设置页面
 */
public class SelectSightFragment extends TTBaseFragment{
	private View curView = null;
    private IMTravelManager travelManager;
    private TextView total;
    private TextView nature;
    private TextView history;
    private TextView entertainment;
    private TextView building;
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
    static final int ALL = 0;
    static final int FREE = 1;
    private int spinner_select = ALL;
    private TextView selectSightDropText;

    private Button next;
    
    private Map<Integer, String> selectFlag = new HashMap<>();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
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
		curView = inflater.inflate(R.layout.travel_fragment_select_sight, topContentView);

		initRes();
        initBtn();
        testCase();
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
    }

	@Override
	public void onResume() {
		super.onResume();
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_FIRST_USER){
            switch (resultCode) {
                case 100:
                    break;
            }
        }
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
                Intent intent = new Intent();
                intent.putExtra("selectSight", 1);
                getActivity().setResult(101, intent);
                getActivity().finish();
                return;
			}
		});

        total = (TextView)curView.findViewById(R.id.select_total);
        nature = (TextView)curView.findViewById(R.id.select_nature);
        history = (TextView)curView.findViewById(R.id.select_history);
        entertainment = (TextView)curView.findViewById(R.id.select_entertainment);
        building = (TextView)curView.findViewById(R.id.select_building);
        selectFlag.put(R.id.select_total, "全部");
        selectFlag.put(R.id.select_nature, "自然");
        selectFlag.put(R.id.select_history, "历史");
        selectFlag.put(R.id.select_entertainment, "文娱");
        selectFlag.put(R.id.select_building, "建筑");

        pop = (LinearLayout)curView.findViewById(R.id.select_sight_drop);
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.showAsDropDown(curView.findViewById(R.id.select_sight_drop));
            }
        });

        next = (Button)curView.findViewById(R.id.select_sight_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageSight();
                jump2SelectHotel();
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
        nature.setOnClickListener(listener);
        history.setOnClickListener(listener);
        entertainment.setOnClickListener(listener);
        building.setOnClickListener(listener);
    }

    private void buttonDisp(int id) {
        total.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        total.setTextColor(getResources().getColor(R.color.not_clicked));
        nature.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        nature.setTextColor(getResources().getColor(R.color.not_clicked));
        history.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        history.setTextColor(getResources().getColor(R.color.not_clicked));
        entertainment.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        entertainment.setTextColor(getResources().getColor(R.color.not_clicked));
        building.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_not_click));
        building.setTextColor(getResources().getColor(R.color.not_clicked));

        switch (id) {
            case R.id.select_total:
                total.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                total.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_nature:
                nature.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                nature.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_history:
                history.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                history.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_entertainment:
                entertainment.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                entertainment.setTextColor(getResources().getColor(R.color.clicked));
                break;
            case R.id.select_building:
                building.setBackground(getResources().getDrawable(R.drawable.select_sight_tag_click));
                building.setTextColor(getResources().getColor(R.color.clicked));
                break;
        }
    }

    private void testCase() {
        String pre = UrlConstant.PIC_URL_PREFIX;

        SightEntity gulangyu = new SightEntity();
        gulangyu.setName("鼓浪屿");
        gulangyu.setPic(pre+"gulangyu.png");
        gulangyu.setStar(8);
        gulangyu.setUrl("http://www.baidu.com");
        gulangyu.setTag("自然");
        gulangyu.setBestStartTime("08:00");
        gulangyu.setBestEndTime("20:00");
        gulangyu.setStartTime("12:00");
        gulangyu.setEndTime("15:00");
        gulangyu.setFree(0);
        gulangyu.setMustGo(1);

        SightEntity xiada = new SightEntity();
        xiada.setName("厦门大学");
        xiada.setPic(pre+"xiamendaxue.png");
        xiada.setStar(9);
        xiada.setUrl("http://www.baidu.com");
        xiada.setTag("建筑 文娱");
        xiada.setBestStartTime("08:00");
        xiada.setBestEndTime("20:00");
        xiada.setStartTime("12:00");
        xiada.setEndTime("15:00");
        xiada.setFree(1);
        xiada.setMustGo(1);

        SightEntity nanputuosi = new SightEntity();
        nanputuosi.setName("南普陀寺");
        nanputuosi.setPic(pre+"nanputuosi.png");
        nanputuosi.setStar(8);
        nanputuosi.setUrl("http://www.baidu.com");
        nanputuosi.setTag("文娱");
        nanputuosi.setBestStartTime("08:00");
        nanputuosi.setBestEndTime("20:00");
        nanputuosi.setStartTime("12:00");
        nanputuosi.setEndTime("15:00");
        nanputuosi.setFree(0);
        nanputuosi.setMustGo(0);

        SightEntity huandaolu = new SightEntity();
        huandaolu.setName("环岛路");
        huandaolu.setPic(pre+"huandaolu.png");
        huandaolu.setStar(8);
        huandaolu.setUrl("http://www.baidu.com");
        huandaolu.setTag("建筑");
        huandaolu.setBestStartTime("08:00");
        huandaolu.setBestEndTime("20:00");
        huandaolu.setStartTime("12:00");
        huandaolu.setEndTime("15:00");
        huandaolu.setFree(1);
        huandaolu.setMustGo(0);

        SightEntity riguangyan = new SightEntity();
        riguangyan.setName("日光岩");
        riguangyan.setPic(pre+"riguangyan.png");
        riguangyan.setStar(8);
        riguangyan.setUrl("http://www.baidu.com");
        riguangyan.setTag("自然");
        riguangyan.setBestStartTime("08:00");
        riguangyan.setBestEndTime("20:00");
        riguangyan.setStartTime("12:00");
        riguangyan.setEndTime("15:00");
        riguangyan.setFree(0);
        riguangyan.setMustGo(1);

        SightEntity zengcuoan = new SightEntity();
        zengcuoan.setName("曾厝垵");
        zengcuoan.setPic(pre+"zengcuoan.png");
        zengcuoan.setStar(8);
        zengcuoan.setUrl("http://www.baidu.com");
        zengcuoan.setTag("历史");
        zengcuoan.setBestStartTime("08:00");
        zengcuoan.setBestEndTime("20:00");
        zengcuoan.setStartTime("12:00");
        zengcuoan.setEndTime("15:00");
        zengcuoan.setFree(0);
        zengcuoan.setMustGo(0);

        SightEntity zhongshanlu = new SightEntity();
        zhongshanlu.setName("中山路");
        zhongshanlu.setPic(pre+"zhongshanlu.png");
        zhongshanlu.setStar(8);
        zhongshanlu.setUrl("http://www.baidu.com");
        zhongshanlu.setTag("建筑");
        zhongshanlu.setBestStartTime("08:00");
        zhongshanlu.setBestEndTime("20:00");
        zhongshanlu.setStartTime("12:00");
        zhongshanlu.setEndTime("15:00");
        zhongshanlu.setFree(1);
        zhongshanlu.setMustGo(1);

        SightEntity xiamenhaidishijie = new SightEntity();
        xiamenhaidishijie.setName("厦门海底世界");
        xiamenhaidishijie.setPic(pre+"xiamenhaidishijie.png");
        xiamenhaidishijie.setStar(8);
        xiamenhaidishijie.setUrl("http://www.baidu.com");
        xiamenhaidishijie.setTag("自然");
        xiamenhaidishijie.setBestStartTime("08:00");
        xiamenhaidishijie.setBestEndTime("20:00");
        xiamenhaidishijie.setStartTime("12:00");
        xiamenhaidishijie.setEndTime("15:00");
        xiamenhaidishijie.setFree(0);
        xiamenhaidishijie.setMustGo(0);

        SightEntity shuzhuanghuayuan = new SightEntity();
        shuzhuanghuayuan.setName("菽庄花园");
        shuzhuanghuayuan.setPic(pre+"shuzhuanghuayuan.png");
        shuzhuanghuayuan.setStar(8);
        shuzhuanghuayuan.setUrl("http://www.baidu.com");
        shuzhuanghuayuan.setTag("文娱");
        shuzhuanghuayuan.setBestStartTime("08:00");
        shuzhuanghuayuan.setBestEndTime("20:00");
        shuzhuanghuayuan.setStartTime("12:00");
        shuzhuanghuayuan.setEndTime("15:00");
        shuzhuanghuayuan.setFree(1);
        shuzhuanghuayuan.setMustGo(0);

        SightEntity shadiaowenhuayuan = new SightEntity();
        shadiaowenhuayuan.setName("沙雕文化园");
        shadiaowenhuayuan.setPic(pre+"shadiaowenhuayuan.png");
        shadiaowenhuayuan.setStar(8);
        shadiaowenhuayuan.setUrl("http://www.baidu.com");
        shadiaowenhuayuan.setTag("文娱");
        shadiaowenhuayuan.setBestStartTime("08:00");
        shadiaowenhuayuan.setBestEndTime("20:00");
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
    }

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
            selectSightDropText.setText(getString(R.string.select_sight_not_screen));
            return;
        }

        if (spinner_select == FREE) {
            selectSightDropText.setText(getString(R.string.select_sight_free));
            Iterator<SightEntity> iSightEntity = tagSightEntityList.iterator();
            while (iSightEntity.hasNext()) {
                if (iSightEntity.next().getFree() == 0) {
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

    private void jump2SelectHotel() {
        Intent selectHotel = new Intent(getActivity(), SelectHotelActivity.class);
        startActivity(selectHotel);
    }

    private void storageSight() {
        if (travelManager != null) {
            List<SightEntity> sightEntityList = travelManager.getMtCity().get(0).getSightList();
            sightEntityList.clear();
            for (SightEntity sightEntity:tagSightEntityList) {
                if (sightEntity.getSelect() == 0) {
                    continue;
                }
                sightEntityList.add(sightEntity);
            }
        }
    }
}
