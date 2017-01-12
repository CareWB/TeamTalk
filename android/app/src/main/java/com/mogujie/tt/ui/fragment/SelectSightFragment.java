package com.mogujie.tt.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.mogujie.tt.DB.entity.SightEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.config.UrlConstant;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.activity.SelectHotelActivity;
import com.mogujie.tt.ui.adapter.SelectSightIntellengenceAdapter;
import com.mogujie.tt.ui.adapter.SightAdapter;
import com.mogujie.tt.ui.adapter.TravelHotAdapter;
import com.mogujie.tt.ui.base.TTBaseFragment;
import com.mogujie.tt.utils.TravelUIHelper;

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
    private Spinner spinner;
    private int spinner_select = 0;
    private TextView total;
    private TextView nature;
    private TextView history;
    private TextView entertainment;
    private TextView building;
    private RecyclerView rvSight;
    private SightAdapter sightAdapter;
    private List<SightEntity> sightEntityList = new ArrayList<>();
    private List<SightEntity> selectSightEntityList = new ArrayList<>();
    private List<SightEntity> tagSightEntityList = new ArrayList<>();
    String Tag = "全部";
    private PopupWindow mPopupWindow;
    private Button sightIntelligenceSelect;

    private RecyclerView rvSelectSightIntellengence;
    private SelectSightIntellengenceAdapter selectSightIntellengenceAdapter;
    private Button popupOK;
    private Button popupCancel;
    private ImageView intelligenceSelectGif;

    private Button next;
    
    private Map<Integer, String> selectFlag = new HashMap<>();

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
		curView = inflater.inflate(R.layout.travel_fragment_select_sight, topContentView);

		initRes();
        initBtn();
        testCase();
        initSight();
        initPopupWindow();
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

        total = (TextView)curView.findViewById(R.id.select_all);
        nature = (TextView)curView.findViewById(R.id.select_nature);
        history = (TextView)curView.findViewById(R.id.select_history);
        entertainment = (TextView)curView.findViewById(R.id.select_entertainment);
        building = (TextView)curView.findViewById(R.id.select_building);
        selectFlag.put(R.id.select_all, "全部");
        selectFlag.put(R.id.select_nature, "自然");
        selectFlag.put(R.id.select_history, "历史");
        selectFlag.put(R.id.select_entertainment, "文化");
        selectFlag.put(R.id.select_building, "建筑");

        sightIntelligenceSelect = (Button)curView.findViewById(R.id.sight_intelligence_select);
        sightIntelligenceSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.showAtLocation(curView.findViewById(R.id.layout_select_sight), Gravity.BOTTOM, 0, 0);
                Glide.with(getActivity()).load(R.drawable.calculating)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(new GlideDrawableImageViewTarget(intelligenceSelectGif, 2));
            }
        });

        next = (Button)curView.findViewById(R.id.select_sight_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2SelectHotel();
            }
        });


        final String[] mItems = getResources().getStringArray(R.array.sight_menu);
        spinner = (Spinner)curView.findViewById(R.id.spinner_sight);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_checked_text, mItems) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = curView.inflate(getContext(), R.layout.spinner_item_layout,
                        null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);

                label.setText(mItems[position]);
                if (spinner.getSelectedItemPosition() == position) {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.travel_alert_dialog_title));
                } else {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.travel_menu_bk));
                }
                return view;
            }

        };
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinner_select = pos;
                tagProcess();
                freeProcess();
                sightAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        rvSight = (RecyclerView)curView.findViewById(R.id.rv_sight);
        
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
                sightAdapter.notifyDataSetChanged();
            }
        };
        total.setOnClickListener(listener);
        nature.setOnClickListener(listener);
        history.setOnClickListener(listener);
        entertainment.setOnClickListener(listener);
        building.setOnClickListener(listener);
    }

    private void testCase() {
        String pre = UrlConstant.PIC_URL_PREFIX;

        SightEntity gulangyu = new SightEntity();
        gulangyu.setName("鼓浪屿");
        gulangyu.setPic(pre+"gulangyu.png");
        gulangyu.setStar(8);
        gulangyu.setFocus(5678);
        gulangyu.setTag("自然");
        gulangyu.setFree(0);
        gulangyu.setMustGo(1);
        gulangyu.setSelect(0);

        SightEntity xiada = new SightEntity();
        xiada.setName("厦门大学");
        xiada.setPic(pre+"xiamendaxue.png");
        xiada.setStar(9);
        xiada.setFocus(2345);
        xiada.setTag("建筑 文化");
        xiada.setFree(1);
        xiada.setMustGo(1);
        xiada.setSelect(0);

        SightEntity nanputuosi = new SightEntity();
        nanputuosi.setName("南普陀寺");
        nanputuosi.setPic(pre+"nanputuosi.png");
        nanputuosi.setStar(8);
        nanputuosi.setFocus(5678);
        nanputuosi.setTag("文化");
        nanputuosi.setFree(0);
        nanputuosi.setMustGo(0);
        nanputuosi.setSelect(0);

        SightEntity huandaolu = new SightEntity();
        huandaolu.setName("环岛路");
        huandaolu.setPic(pre+"huandaolu.png");
        huandaolu.setStar(8);
        huandaolu.setFocus(5678);
        huandaolu.setTag("建筑");
        huandaolu.setFree(0);
        huandaolu.setMustGo(0);
        huandaolu.setSelect(0);

        SightEntity riguangyan = new SightEntity();
        riguangyan.setName("日光岩");
        riguangyan.setPic(pre+"riguangyan.png");
        riguangyan.setStar(8);
        riguangyan.setFocus(5678);
        riguangyan.setTag("自然");
        riguangyan.setFree(0);
        riguangyan.setMustGo(1);
        riguangyan.setSelect(0);

        SightEntity zengcuoan = new SightEntity();
        zengcuoan.setName("曾厝垵");
        zengcuoan.setPic(pre+"zengcuoan.png");
        zengcuoan.setStar(8);
        zengcuoan.setFocus(5678);
        zengcuoan.setTag("历史");
        zengcuoan.setFree(0);
        zengcuoan.setMustGo(0);
        zengcuoan.setSelect(0);

        SightEntity zhongshanlu = new SightEntity();
        zhongshanlu.setName("中山路");
        zhongshanlu.setPic(pre+"zhongshanlu.png");
        zhongshanlu.setStar(8);
        zhongshanlu.setFocus(5678);
        zhongshanlu.setTag("建筑");
        zhongshanlu.setFree(0);
        zhongshanlu.setMustGo(1);
        zhongshanlu.setSelect(0);

        SightEntity xiamenhaidishijie = new SightEntity();
        xiamenhaidishijie.setName("厦门海底世界");
        xiamenhaidishijie.setPic(pre+"xiamenhaidishijie.png");
        xiamenhaidishijie.setStar(8);
        xiamenhaidishijie.setFocus(5678);
        xiamenhaidishijie.setTag("自然");
        xiamenhaidishijie.setFree(0);
        xiamenhaidishijie.setMustGo(0);
        xiamenhaidishijie.setSelect(0);

        SightEntity shuzhuanghuayuan = new SightEntity();
        shuzhuanghuayuan.setName("菽庄花园");
        shuzhuanghuayuan.setPic(pre+"shuzhuanghuayuan.png");
        shuzhuanghuayuan.setStar(8);
        shuzhuanghuayuan.setFocus(5678);
        shuzhuanghuayuan.setTag("文化");
        shuzhuanghuayuan.setFree(0);
        shuzhuanghuayuan.setMustGo(0);
        shuzhuanghuayuan.setSelect(0);

        SightEntity shadiaowenhuayuan = new SightEntity();
        shadiaowenhuayuan.setName("沙雕文化园");
        shadiaowenhuayuan.setPic(pre+"shadiaowenhuayuan.png");
        shadiaowenhuayuan.setStar(8);
        shadiaowenhuayuan.setFocus(5678);
        shadiaowenhuayuan.setTag("文化");
        shadiaowenhuayuan.setFree(0);
        shadiaowenhuayuan.setMustGo(1);
        shadiaowenhuayuan.setSelect(0);

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
                SightEntity sightTemp = new SightEntity();
                sightTemp.setName(sightEntity.getName());
                sightTemp.setPic(sightEntity.getPic());
                sightTemp.setSelect(1);
                selectSightEntityList.add(sightTemp);
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

    private void initPopupWindow() {
        View popupView = curView.inflate(getActivity(), R.layout.select_sight_popup_window, null);
        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);

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

        rvSelectSightIntellengence = (RecyclerView) popupView.findViewById(R.id.select_sight_intellengence);
        rvSelectSightIntellengence.setHasFixedSize(true);
        GridLayoutManager layoutManagerHot = new GridLayoutManager(getActivity(), 3);
        layoutManagerHot.setOrientation(LinearLayoutManager.VERTICAL);
        rvSelectSightIntellengence.setLayoutManager(layoutManagerHot);

        SelectSightIntellengenceAdapter.OnRecyclerViewListener intellengenceRVListener = new SelectSightIntellengenceAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                if (selectSightEntityList.get(position).getSelect() == 1) {
                    selectSightEntityList.get(position).setSelect(0);
                } else {
                    selectSightEntityList.get(position).setSelect(1);
                }

                selectSightIntellengenceAdapter.notifyDataSetChanged();
            }
        };

        for (SightEntity selectSight:selectSightEntityList) {
            selectSight.setSelect(1);
        }
        selectSightIntellengenceAdapter = new SelectSightIntellengenceAdapter(getActivity(), selectSightEntityList);
        selectSightIntellengenceAdapter.setOnRecyclerViewListener(intellengenceRVListener);
        rvSelectSightIntellengence.setAdapter(selectSightIntellengenceAdapter);

        popupOK = (Button) popupView.findViewById(R.id.select_sight_pop_ok);
        popupCancel = (Button) popupView.findViewById(R.id.select_sight_pop_cancel);
        View.OnClickListener popupListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.select_sight_pop_ok:
                        popupSelectSightProcess();
                        mPopupWindow.dismiss();
                        break;

                    case R.id.select_sight_pop_cancel:
                        mPopupWindow.dismiss();
                        break;
                }
            }
        };
        popupOK.setOnClickListener(popupListener);
        popupCancel.setOnClickListener(popupListener);

        intelligenceSelectGif = (ImageView) popupView.findViewById(R.id.intelligence_select_gif);
    }

    private void popupSelectSightProcess() {
        for (SightEntity select:selectSightEntityList) {
            for (SightEntity sight:sightEntityList) {
                if (select.getName().equals(sight.getName())) {
                    sight.setSelect(select.getSelect());
                }
            }
            for (SightEntity tagSight:tagSightEntityList) {
                if (select.getName().equals(tagSight.getName())) {
                    tagSight.setSelect(select.getSelect());
                }
            }
        }
        sightAdapter.notifyDataSetChanged();
    }

    private void freeProcess() {
        if (spinner_select == 0) {
            return;
        }

        if (spinner_select == 1) {
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

    private void jump2SelectHotel() {
        Intent selectHotel = new Intent(getActivity(), SelectHotelActivity.class);
        startActivity(selectHotel);
    }
}
