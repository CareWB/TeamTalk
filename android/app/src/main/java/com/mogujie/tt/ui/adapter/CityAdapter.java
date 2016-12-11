package com.mogujie.tt.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogujie.tt.R;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class CityAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
        void onItemBtnClick(View view, int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<String> list;

    public CityAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_city, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new SelectCityResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        SelectCityResultViewHolder holder = (SelectCityResultViewHolder) viewHolder;
        holder.name.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SelectCityResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image;
        public TextView name;
        public ImageView opt;
        private LinearLayout lyCity;

        public SelectCityResultViewHolder(View itemView) {
            super(itemView);
            lyCity = (LinearLayout)  itemView.findViewById(R.id.ly_city);
            image = (ImageView) itemView.findViewById(R.id.city_image);
            name = (TextView) itemView.findViewById(R.id.city_name);
            opt = (ImageView) itemView.findViewById(R.id.city_select);
            lyCity.setOnClickListener(this);
            opt.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                if (v.getId() == R.id.city_select) {
                    onRecyclerViewListener.onItemBtnClick(v, this.getPosition());
                } else {
                    onRecyclerViewListener.onItemClick(this.getPosition());
                }
            }
        }
    }

}
