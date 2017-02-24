package com.mogujie.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.DetailDispEntity;
import com.mogujie.tt.DB.entity.TrafficEntity;
import com.mogujie.tt.R;

import java.util.List;

//import com.bumptech.glide.Glide;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class DetailDispAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onTrafficClick(int position);
        void onSightClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<DetailDispEntity> mList;
    private Context ctx;
    private static final int DAY = 1;
    private static final int TRAFFIC = 2;
    private static final int SIGHT = 3;

    public DetailDispAdapter(Context ctx, List<DetailDispEntity> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (type){
            case DAY:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp_day, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lpd = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lpd);
                holder = new DayViewHolder(view);
                break;
            case TRAFFIC:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp_traffic, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lpt);
                holder = new TrafficViewHolder(view);
                break;
            case SIGHT:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp_sight, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lps);
                holder = new SightViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        DetailDispEntity detailDispEntity = mList.get(i);

        switch (getItemViewType(i)){
            case DAY:
                DayViewHolder dayViewHolder = (DayViewHolder) viewHolder;
                dayViewHolder.day.setText(detailDispEntity.getTime());
                break;
            case TRAFFIC:
                TrafficViewHolder trafficViewHolder = (TrafficViewHolder) viewHolder;
                switch (detailDispEntity.getTrafficType()) {
                    case 1:
                        trafficViewHolder.icon.setBackground(ctx.getResources().getDrawable(R.drawable.bus_blue));
                        trafficViewHolder.time.setText(detailDispEntity.getTime());
                        break;
                }
                trafficViewHolder.title.setText(detailDispEntity.getTitle());
                trafficViewHolder.content.setText(detailDispEntity.getContent());
                break;
            case SIGHT:
                SightViewHolder sightViewHolder = (SightViewHolder) viewHolder;
                sightViewHolder.title.setText(detailDispEntity.getTitle());
                sightViewHolder.content.setText(detailDispEntity.getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        public TextView day;

        public DayViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.detail_disp_day);
        }
    }

    class TrafficViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout rlTraffic;
        public ImageView icon;
        public TextView title;
        public TextView time;
        public TextView content;

        public TrafficViewHolder(View itemView) {
            super(itemView);
            rlTraffic = (RelativeLayout) itemView.findViewById(R.id.rl_detail_disp_traffic);
            icon = (ImageView) itemView.findViewById(R.id.detail_disp_traffic_icon);
            title = (TextView) itemView.findViewById(R.id.detail_disp_traffic_title);
            time = (TextView) itemView.findViewById(R.id.detail_disp_traffic_time);
            content = (TextView) itemView.findViewById(R.id.detail_disp_traffic_content);
            rlTraffic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewListener.onTrafficClick(this.getPosition());
        }
    }

    class SightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout rlSight;
        public TextView title;
        public TextView content;

        public SightViewHolder(View itemView) {
            super(itemView);
            rlSight = (RelativeLayout) itemView.findViewById(R.id.rl_detail_disp_sight);
            title = (TextView) itemView.findViewById(R.id.detail_disp_sight_title);
            content = (TextView) itemView.findViewById(R.id.detail_disp_sight_content);
            rlSight.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewListener.onSightClick(this.getPosition());
        }
    }
}
