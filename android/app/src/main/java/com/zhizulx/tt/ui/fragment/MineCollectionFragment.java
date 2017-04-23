package com.zhizulx.tt.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhizulx.tt.DB.entity.RouteEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.imservice.manager.IMTravelManager;
import com.zhizulx.tt.imservice.service.IMService;
import com.zhizulx.tt.imservice.support.IMServiceConnector;
import com.zhizulx.tt.ui.adapter.CollectionAdapter;
import com.zhizulx.tt.ui.base.TTBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 */
public class MineCollectionFragment extends TTBaseFragment{
	private View curView = null;
    private IMTravelManager travelManager;
	private RecyclerView rvCollection;
	private CollectionAdapter collectionAdapter;
    private List<RouteEntity> routeEntityList = new ArrayList<>();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                travelManager = imService.getTravelManager();
                initCollectionList();
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
		curView = inflater.inflate(R.layout.travel_fragment_mine_collection, topContentView);
		initRes();
        initBtn();
        initCollection();
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
		setTopTitle(getString(R.string.mine_collection));
		setTopLeftButton(R.drawable.tt_top_back);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});
        rvCollection = (RecyclerView)curView.findViewById(R.id.rv_collection_disp);
	}

	@Override
	protected void initHandler() {
	}

    private void initBtn() {

    }

    private void initCollectionList() {
        routeEntityList.clear();
        RouteEntity routeEntity1 = new RouteEntity();
        routeEntity1.setCityCode("111");
        RouteEntity routeEntity2 = new RouteEntity();
        routeEntity2.setCityCode("222");
        RouteEntity routeEntity3 = new RouteEntity();
        routeEntity3.setCityCode("333");
        RouteEntity routeEntity4 = new RouteEntity();
        routeEntity4.setCityCode("444");
        RouteEntity routeEntity5 = new RouteEntity();
        routeEntity5.setCityCode("555");
        routeEntityList.add(routeEntity1);
        routeEntityList.add(routeEntity2);
        routeEntityList.add(routeEntity3);
        routeEntityList.add(routeEntity4);
        routeEntityList.add(routeEntity5);
        collectionAdapter.notifyDataSetChanged();
    }

    private void initCollection() {
        rvCollection.setHasFixedSize(true);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(getActivity());
        layoutManagerResult.setOrientation(LinearLayoutManager.VERTICAL);
        rvCollection.setLayoutManager(layoutManagerResult);
        CollectionAdapter.OnRecyclerViewListener collectionListenser = new CollectionAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Log.e("swip", "onItemClick");
                collectionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTopClick(int position) {
                Log.e("swip", "onTopClick");
                collectionAdapter.notifyItemMoved(position, 0);
            }

            @Override
            public void onDelClick(int position) {
                Log.e("swip", "onDelClick");
                routeEntityList.remove(position);
                collectionAdapter.notifyItemRemoved(position);
                collectionAdapter.notifyDataSetChanged();
            }
        };
        collectionAdapter = new CollectionAdapter(getActivity(), routeEntityList);
        collectionAdapter.setOnRecyclerViewListener(collectionListenser);
        rvCollection.setAdapter(collectionAdapter);
    }
}
