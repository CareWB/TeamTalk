package com.mogujie.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private List<DetailDispEntity> mList;
    private Context ctx;

    public DetailDispAdapter(Context ctx, List<DetailDispEntity> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        DetailDispEntity detailDispEntity = mList.get(i);
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        if (detailDispEntity.getType() % 2 == 0) {
            holder.right.setVisibility(View.GONE);
            holder.leftPic.setBackground(ctx.getResources().getDrawable(R.drawable.gulangyu));
            holder.leftTitle.setText("鼓浪屿");
        } else {
            holder.left.setVisibility(View.GONE);
            holder.rightPic.setBackground(ctx.getResources().getDrawable(R.drawable.xiamendaxue));
            holder.rightTitle.setText("厦门大学");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        public CardView left;
        public CardView right;
        public TextView leftTitle;
        public TextView rightTitle;
        public ImageView leftPic;
        public ImageView rightPic;


        public DetailViewHolder(View itemView) {
            super(itemView);
            left = (CardView) itemView.findViewById(R.id.detail_disp_left);
            right = (CardView) itemView.findViewById(R.id.detail_disp_right);
            leftTitle = (TextView) itemView.findViewById(R.id.detail_disp_left_title);
            rightTitle = (TextView) itemView.findViewById(R.id.detail_disp_right_title);
            leftPic = (ImageView) itemView.findViewById(R.id.detail_disp_left_pic);
            rightPic = (ImageView) itemView.findViewById(R.id.detail_disp_right_pic);
        }

    }
}
