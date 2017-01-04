package com.mogujie.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.TrafficEntity;
import com.mogujie.tt.DB.entity.TravelEntity;
import com.mogujie.tt.R;

import java.util.List;

//import com.bumptech.glide.Glide;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class TrafficDetailAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onSelectClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<TrafficEntity> mList;
    private Context ctx;

    public TrafficDetailAdapter(Context ctx, List<TrafficEntity> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_traffic_detail, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TrafficViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TrafficEntity trafficEntity = mList.get(i);
        TrafficViewHolder holder = (TrafficViewHolder) viewHolder;

        holder.startTime.setText(trafficEntity.getStartTime());
        holder.endTime.setText(trafficEntity.getEndTime());
        holder.startStation.setText(trafficEntity.getStartStation());
        holder.endStation.setText(trafficEntity.getEndStation());
        holder.no.setText(trafficEntity.getNo());
        holder.price.setText("￥"+trafficEntity.getPrice());
        holder.seatType.setText(trafficEntity.getExtra());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TrafficViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout trafficList;
        public TextView startTime;
        public TextView endTime;
        public TextView startStation;
        public TextView endStation;
        public TextView no;
        public TextView price;
        public TextView seatType;

        public TrafficViewHolder(View itemView) {
            super(itemView);
            trafficList = (RelativeLayout) itemView.findViewById(R.id.rl_traffic_list);
            startTime = (TextView) itemView.findViewById(R.id.traffic_detail_start_time);
            endTime = (TextView) itemView.findViewById(R.id.traffic_detail_end_time);
            startStation = (TextView) itemView.findViewById(R.id.traffic_detail_start_station);
            endStation = (TextView) itemView.findViewById(R.id.traffic_detail_end_station);
            no = (TextView) itemView.findViewById(R.id.traffic_list_no);
            price = (TextView) itemView.findViewById(R.id.traffic_detail_price);
            seatType = (TextView) itemView.findViewById(R.id.traffic_detail_seat_type);

            trafficList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewListener.onSelectClick(this.getPosition());
        }
    }
}
