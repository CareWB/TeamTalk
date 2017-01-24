package com.mogujie.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
        void onPullClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<TrafficEntity> mList;
    private Context ctx;
    private static final int HEADER = 0;
    private static final int BODAY = 1;

    public TrafficDetailAdapter(Context ctx, List<TrafficEntity> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        TrafficEntity trafficEntity = mList.get(position);
        if (trafficEntity.getType() > 0xf0) {
            return HEADER;
        } else {
            return BODAY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (type){
            case HEADER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_traffic_header, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lph = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lph);
                holder = new HeadViewHolder(view);
                break;
            case BODAY:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_traffic_detail, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lpb);
                holder = new TrafficViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TrafficEntity trafficEntity = mList.get(i);

        switch (getItemViewType(i)){
            case HEADER:
                HeadViewHolder header = (HeadViewHolder) viewHolder;
                switch (trafficEntity.getType()-0xf0) {
                    case 1:
                        header.trafficType.setText("飞机票");
                        header.trafficType.setCompoundDrawablesWithIntrinsicBounds(ctx.getResources().getDrawable(R.drawable.plane_blue), null, null, null);
                        break;

                    case 2:
                        header.trafficType.setText("火车票");
                        header.trafficType.setCompoundDrawablesWithIntrinsicBounds(ctx.getResources().getDrawable(R.drawable.train_blue), null, null, null);
                        break;

                    case 3:
                        header.trafficType.setText("汽车票");
                        header.trafficType.setCompoundDrawablesWithIntrinsicBounds(ctx.getResources().getDrawable(R.drawable.bus_blue), null, null, null);
                        break;
                }
                break;
            case BODAY:
                TrafficViewHolder holder = (TrafficViewHolder) viewHolder;
                holder.startTime.setText(trafficEntity.getStartTime());
                holder.endTime.setText(trafficEntity.getEndTime());
                holder.startStation.setText(trafficEntity.getStartStation());
                holder.endStation.setText(trafficEntity.getEndStation());
                holder.no.setText(trafficEntity.getNo());
                holder.price.setText("￥"+trafficEntity.getPrice());
                holder.seatType.setText(trafficEntity.getSeatClass());
                break;
        }
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

    class HeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView trafficType;
        public ImageButton pull;

        public HeadViewHolder(View itemView) {
            super(itemView);
            trafficType = (TextView) itemView.findViewById(R.id.traffic_type);
            pull = (ImageButton) itemView.findViewById(R.id.traffic_select);
            pull.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewListener.onPullClick(this.getPosition());
        }
    }
}
