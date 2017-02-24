package com.mogujie.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.SightEntity;
import com.mogujie.tt.R;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class DetailDispSightAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private List<SightEntity> list;


    public DetailDispSightAdapter(Context ctx, List<SightEntity> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_sight, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new SightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        SightViewHolder holder = (SightViewHolder) viewHolder;
        holder.title.setText(list.get(i).getName());
        holder.bk.setBackground(ctx.getResources().getDrawable(R.drawable.iv_detail_sight));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SightViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView bk;

        public SightViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_detail_sight);
            bk = (ImageView) itemView.findViewById(R.id.iv_detail_sight);
        }
    }

}
