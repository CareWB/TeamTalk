package com.zhizulx.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhizulx.tt.DB.entity.TravelCityEntity;
import com.zhizulx.tt.R;

import java.util.List;

//import com.bumptech.glide.Glide;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class TravelRouteAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<TravelCityEntity> mList;
    private Context ctx;

    public TravelRouteAdapter(Context ctx, List<TravelCityEntity> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_travel_route, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TravelRouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TravelCityEntity travelCityEntity = mList.get(i);
        TravelRouteViewHolder holder = (TravelRouteViewHolder) viewHolder;
        holder.days.setText(travelCityEntity.getCityName());
        holder.city.setText(travelCityEntity.getCityName());
        holder.topic.setText(travelCityEntity.getCityName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TravelRouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout lyTravelRoute;
        public TextView days;
        public TextView city;
        public TextView topic;

        public TravelRouteViewHolder(View itemView) {
            super(itemView);
            lyTravelRoute = (LinearLayout) itemView.findViewById(R.id.ly_travel_route);
            days = (TextView) itemView.findViewById(R.id.travel_route_days);
            city = (TextView) itemView.findViewById(R.id.travel_route_city);
            topic = (TextView) itemView.findViewById(R.id.travel_route_topic);
            lyTravelRoute.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(this.getPosition());
            }
        }
    }

}
