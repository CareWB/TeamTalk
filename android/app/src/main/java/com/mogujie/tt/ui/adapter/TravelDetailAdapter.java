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
public class TravelDetailAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onAddClick(int position, View v);
        void onSelectGo(int position);
        void onSelectBack(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<TrafficEntity> mGoList;
    private List<TrafficEntity> mBackList;
    private Context ctx;
    private TravelEntity travelEntity;

    public TravelDetailAdapter(Context ctx, TravelEntity travelEntity, List<TrafficEntity> mGoList, List<TrafficEntity> mBackList) {
        this.ctx = ctx;
        this.travelEntity = travelEntity;
        this.mGoList = mGoList;
        this.mBackList = mBackList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_travel_detail, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TrafficViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TrafficEntity go = mGoList.get(i);
        TrafficEntity back = mBackList.get(i);
        TrafficViewHolder holder = (TrafficViewHolder) viewHolder;

        holder.tvTravel.setText(travelEntity.getDestination()+" : "+travelEntity.getStartDate()+"-"+travelEntity.getEndDate());

        holder.goTitle.setText(travelEntity.getStartPlace()+"-"+travelEntity.getDestination());
        holder.goTitleDate.setText(travelEntity.getStartDate());
        holder.backTitle.setText(travelEntity.getDestination()+"-"+travelEntity.getEndPlace());
        holder.backTitleDate.setText(travelEntity.getEndDate());
        holder.goStartTime.setText(go.getStartTime());
        holder.goEndTime.setText(go.getEndTime());
        holder.goStartStation.setText(go.getStartStation());
        holder.goEndStation.setText(go.getEndStation());
        holder.goNo.setText(go.getNo());
        holder.goPrice.setText("￥"+go.getPrice());
        holder.goSeatType.setText(go.getSeatClass());

        holder.backStartTime.setText(back.getStartTime());
        holder.backEndTime.setText(back.getEndTime());
        holder.backStartStation.setText(back.getStartStation());
        holder.backEndStation.setText(back.getEndStation());
        holder.backNo.setText(back.getNo());
        holder.backPrice.setText("￥"+back.getPrice());
        holder.backSeatType.setText(back.getSeatClass());

/*        if (travelEntity.get() == 0) {

        } else {
            holder.addSight.setBackground(ctx.getResources().getDrawable(R.drawable.pic4));
        }*/
    }

    @Override
    public int getItemCount() {
        return mGoList.size();
    }

    class TrafficViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout addSight;
        public RelativeLayout goTrafficList;
        public RelativeLayout backTrafficList;
        public TextView tvTravel;
        public TextView goTitle;
        public TextView goTitleDate;
        public TextView goStartTime;
        public TextView goEndTime;
        public TextView goStartStation;
        public TextView goEndStation;
        public TextView goNo;
        public TextView goPrice;
        public TextView goSeatType;
        public TextView backTitle;
        public TextView backTitleDate;
        public TextView backStartTime;
        public TextView backEndTime;
        public TextView backStartStation;
        public TextView backEndStation;
        public TextView backNo;
        public TextView backPrice;
        public TextView backSeatType;

        public TrafficViewHolder(View itemView) {
            super(itemView);
            addSight = (LinearLayout) itemView.findViewById(R.id.travel_detail_add_sight);
            tvTravel = (TextView) itemView.findViewById(R.id.tv_travel);

            goTrafficList = (RelativeLayout) itemView.findViewById(R.id.go_travel_detail_traffic_list);
            goTitle = (TextView) itemView.findViewById(R.id.go_travel_detail_title);
            goTitleDate = (TextView) itemView.findViewById(R.id.go_travel_detail_title_date);
            goStartTime = (TextView) itemView.findViewById(R.id.go_traffic_detail_start_time);
            goEndTime = (TextView) itemView.findViewById(R.id.go_traffic_detail_end_time);
            goStartStation = (TextView) itemView.findViewById(R.id.go_traffic_detail_start_station);
            goEndStation = (TextView) itemView.findViewById(R.id.go_traffic_detail_end_station);
            goNo = (TextView) itemView.findViewById(R.id.go_traffic_list_no);
            goPrice = (TextView) itemView.findViewById(R.id.go_traffic_detail_price);
            goSeatType = (TextView) itemView.findViewById(R.id.go_traffic_detail_seat_type);

            backTrafficList = (RelativeLayout) itemView.findViewById(R.id.back_travel_detail_traffic_list);
            backTitle = (TextView) itemView.findViewById(R.id.back_travel_detail_title);
            backTitleDate = (TextView) itemView.findViewById(R.id.back_travel_detail_title_date);
            backStartTime = (TextView) itemView.findViewById(R.id.back_traffic_detail_start_time);
            backEndTime = (TextView) itemView.findViewById(R.id.back_traffic_detail_end_time);
            backStartStation = (TextView) itemView.findViewById(R.id.back_traffic_detail_start_station);
            backEndStation = (TextView) itemView.findViewById(R.id.back_traffic_detail_end_station);
            backNo = (TextView) itemView.findViewById(R.id.back_traffic_list_no);
            backPrice = (TextView) itemView.findViewById(R.id.back_traffic_detail_price);
            backSeatType = (TextView) itemView.findViewById(R.id.back_traffic_detail_seat_type);

            addSight.setOnClickListener(this);
            goTrafficList.setOnClickListener(this);
            backTrafficList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                switch (v.getId()) {
                    case R.id.travel_detail_add_sight:
                        onRecyclerViewListener.onAddClick(this.getPosition(), v);
                        break;
                    case R.id.go_travel_detail_traffic_list:
                        onRecyclerViewListener.onSelectGo(this.getPosition());
                        break;
                    case R.id.back_travel_detail_traffic_list:
                        onRecyclerViewListener.onSelectBack(this.getPosition());
                        break;
                }
            }
        }
    }

}
