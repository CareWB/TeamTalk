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

        if (go.getType() == 1) {
            holder.goLogo.setBackgroundResource(R.drawable.plane_black);
            holder.goTitle.setText(travelEntity.getStartDate()+"起飞");
        } else {
            holder.goLogo.setBackgroundResource(R.drawable.train_black);
            holder.goTitle.setText(travelEntity.getStartDate()+"开车");
        }

        holder.goStart.setText(go.getStartStation()+"\n"+go.getStartTime());
        holder.goEnd.setText(go.getEndStation()+"\n"+go.getEndTime());
        holder.goNo.setText(go.getNo());
        holder.goPrice.setText("￥"+go.getPrice()+go.getSeatClass());

        if (back.getType() == 1) {
            holder.backLogo.setBackgroundResource(R.drawable.plane_black);
            holder.backTitle.setText(travelEntity.getStartDate()+"起飞");
        } else {
            holder.backLogo.setBackgroundResource(R.drawable.train_black);
            holder.backTitle.setText(travelEntity.getStartDate()+"开车");
        }

        holder.backStart.setText(back.getStartStation()+"\n"+back.getStartTime());
        holder.backEnd.setText(back.getEndStation()+"\n"+back.getEndTime());
        holder.backNo.setText(back.getNo());
        holder.backPrice.setText("￥"+back.getPrice()+back.getSeatClass());

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
        public TextView goTitle;
        public ImageView goLogo;
        public TextView goStart;
        public TextView goEnd;
        public TextView goNo;
        public TextView goPrice;
        public TextView tvAddSight;
        public TextView backTitle;
        public ImageView backLogo;
        public TextView backStart;
        public TextView backEnd;
        public TextView backNo;
        public TextView backPrice;

        public TrafficViewHolder(View itemView) {
            super(itemView);
            addSight = (LinearLayout) itemView.findViewById(R.id.travel_detail_add_sight);
            goTrafficList = (RelativeLayout) itemView.findViewById(R.id.go_travel_detail_traffic_list);
            goTitle = (TextView) itemView.findViewById(R.id.go_travel_detail_title);
            goLogo = (ImageView) itemView.findViewById(R.id.go_travel_detail_traffic_logo);
            goStart = (TextView) itemView.findViewById(R.id.go_travel_detail_start);
            goEnd = (TextView) itemView.findViewById(R.id.go_travel_detail_end);
            goNo = (TextView) itemView.findViewById(R.id.go_travel_detail_no);
            goPrice = (TextView) itemView.findViewById(R.id.go_travel_detail_price);
            tvAddSight = (TextView) itemView.findViewById(R.id.tv_add_sight);

            backTrafficList = (RelativeLayout) itemView.findViewById(R.id.back_travel_detail_traffic_list);
            backTitle = (TextView) itemView.findViewById(R.id.back_travel_detail_title);
            backLogo = (ImageView) itemView.findViewById(R.id.back_travel_detail_traffic_logo);
            backStart = (TextView) itemView.findViewById(R.id.back_travel_detail_start);
            backEnd = (TextView) itemView.findViewById(R.id.back_travel_detail_end);
            backNo = (TextView) itemView.findViewById(R.id.back_travel_detail_no);
            backPrice = (TextView) itemView.findViewById(R.id.back_travel_detail_price);

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
