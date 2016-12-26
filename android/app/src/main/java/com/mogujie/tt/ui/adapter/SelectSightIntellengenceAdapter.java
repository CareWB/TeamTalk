package com.mogujie.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mogujie.tt.DB.entity.SightEntity;
import com.mogujie.tt.R;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class SelectSightIntellengenceAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<SightEntity> list;
    private Context ctx;

    public SelectSightIntellengenceAdapter(Context ctx, List<SightEntity> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_select_sight_intellengence, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new IntellengenceSightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        IntellengenceSightViewHolder holder = (IntellengenceSightViewHolder) viewHolder;
        holder.position = i;
        SightEntity sightEntity = list.get(i);
        if (sightEntity.getSelect() == 1) {
            holder.select.setVisibility(View.VISIBLE);
        } else {
            holder.select.setVisibility(View.GONE);
        }

        holder.name.setText(sightEntity.getName());
        Glide.with(ctx).load(sightEntity.getPic()).into(holder.sight);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class IntellengenceSightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView select;
        public ImageView sight;
        public TextView name;
        public int position;

        public IntellengenceSightViewHolder(View itemView) {
            super(itemView);
            select = (ImageView) itemView.findViewById(R.id.iv_item_intellengence_selected);
            sight = (ImageView) itemView.findViewById(R.id.iv_item_select_sight_intellengence);
            name = (TextView) itemView.findViewById(R.id.iv_item_intellengence_name);
            sight.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
        }
    }
}
