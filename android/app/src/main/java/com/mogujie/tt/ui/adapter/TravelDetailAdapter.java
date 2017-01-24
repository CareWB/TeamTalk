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
        void onSelectClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<TrafficEntity> mList;
    private Context ctx;
    private TravelEntity travelEntity;

    public TravelDetailAdapter(Context ctx, TravelEntity travelEntity, List<TrafficEntity> mList) {
        this.ctx = ctx;
        this.travelEntity = travelEntity;
        this.mList = mList;
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
        TrafficEntity trafficEntity = mList.get(i);
        TrafficViewHolder holder = (TrafficViewHolder) viewHolder;

        if (trafficEntity.getType() == 1) {
            holder.logo.setBackgroundResource(R.drawable.plane_black);
            holder.title.setText(travelEntity.getStartDate()+"起飞");
        } else {
            holder.logo.setBackgroundResource(R.drawable.train_black);
            holder.title.setText(travelEntity.getStartDate()+"开车");
        }

        holder.start.setText(trafficEntity.getStartStation()+"\n"+trafficEntity.getStartTime());
        holder.end.setText(trafficEntity.getEndStation()+"\n"+trafficEntity.getEndTime());
        holder.no.setText(trafficEntity.getNo());
        holder.price.setText("￥"+trafficEntity.getPrice()+trafficEntity.getSeatClass());

/*        if (travelEntity.get() == 0) {

        } else {
            holder.addSight.setBackground(ctx.getResources().getDrawable(R.drawable.pic4));
        }*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TrafficViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout addSight;
        public RelativeLayout trafficList;
        public TextView title;
        public ImageView logo;
        public TextView start;
        public TextView end;
        public TextView no;
        public TextView price;
        public TextView addDisp;

        public TrafficViewHolder(View itemView) {
            super(itemView);
            addSight = (LinearLayout) itemView.findViewById(R.id.travel_detail_add_sight);
            trafficList = (RelativeLayout) itemView.findViewById(R.id.travel_detail_traffic_list);
            title = (TextView) itemView.findViewById(R.id.travel_detail_title);
            logo = (ImageView) itemView.findViewById(R.id.travel_detail_traffic_logo);
            start = (TextView) itemView.findViewById(R.id.travel_detail_start);
            end = (TextView) itemView.findViewById(R.id.travel_detail_end);
            no = (TextView) itemView.findViewById(R.id.travel_detail_no);
            price = (TextView) itemView.findViewById(R.id.travel_detail_price);
            addDisp = (TextView) itemView.findViewById(R.id.tv_add_sight);

            addSight.setOnClickListener(this);
            trafficList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                if (v.getId() == R.id.travel_detail_add_sight) {
                    onRecyclerViewListener.onAddClick(this.getPosition(), v);
                } else {
                    onRecyclerViewListener.onSelectClick(this.getPosition());
                }
            }
        }
    }

}
