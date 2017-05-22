package com.zhizulx.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhizulx.tt.DB.entity.DetailDispEntity;
import com.zhizulx.tt.R;
import com.zhizulx.tt.utils.ImageUtil;
import com.zhizulx.tt.utils.TravelUIHelper;

import java.util.List;

//import com.bumptech.glide.Glide;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/17/15.
 */
public class DetailDispAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onDayClick(View v, int position);
        void onSightClick(View v, int position);
        void onHotelClick(View v, int position);
        void onTrafficClick(View v, int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<DetailDispEntity> mList;
    private Context ctx;
    private static final int NULL = 0;
    private static final int DAY = 1;
    private static final int SIGHT = 2;
    private static final int HOTEL = 3;
    private static final int TRAFFIC = 4;
    private static final int STATUS_DISP = 0;
    private static final int STATUS_EDIT = 1;
    private static final int STATUS_CANNOT_EDIT = 2;
    private int trafficEnd = 0;

    public DetailDispAdapter(Context ctx, List<DetailDispEntity> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    private void trafficStatus() {
        int i = 0;
        for (DetailDispEntity detailDispEntity : mList) {
            if (detailDispEntity.getType() == TRAFFIC) {
               trafficEnd = i;
            }
            i ++;
        }
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
            case NULL:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp_null, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lpn = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lpn);
                holder = new NullViewHolder(view);
                break;

            case DAY:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp_day, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lpd = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lpd);
                holder = new DayViewHolder(view);
                break;

            case SIGHT:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp_sight, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lps);
                holder = new SightViewHolder(view);
                break;

            case HOTEL:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp_hotel, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lph = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lph);
                holder = new HotelViewHolder(view);
                break;

            case TRAFFIC:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_item_detail_disp_traffic, null);
                //        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
                LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lpt);
                holder = new TrafficViewHolder(view);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        DetailDispEntity detailDispEntity = mList.get(i);

        switch (getItemViewType(i)){
            case NULL:
                NullViewHolder nullViewHolder = (NullViewHolder) viewHolder;
                break;
            case DAY:
                DayViewHolder dayViewHolder = (DayViewHolder) viewHolder;
                dayViewHolder.day.setText(detailDispEntity.getTitle());
                break;
            case SIGHT:
                SightViewHolder sightViewHolder = (SightViewHolder) viewHolder;
                sightViewHolder.title.setText(detailDispEntity.getTitle());
                ImageUtil.GlideRoundRectangleAvatar(ctx, detailDispEntity.getImage(), sightViewHolder.avatar);
                //Glide.with(ctx).load(detailDispEntity.getImage()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(sightViewHolder.avatar);
                if (detailDispEntity.getEdited() == STATUS_CANNOT_EDIT) {
                    sightViewHolder.mask.setVisibility(View.VISIBLE);
                } else {
                    sightViewHolder.mask.setVisibility(View.GONE);
                }
                break;
            case HOTEL:
                HotelViewHolder hotelViewHolder = (HotelViewHolder) viewHolder;
                hotelViewHolder.title.setText(detailDispEntity.getTitle());
                Glide.with(ctx).load(detailDispEntity.getImage()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(hotelViewHolder.avatar);
                switch (detailDispEntity.getEdited()) {
                    case STATUS_DISP://可查看
                        hotelViewHolder.mask.setVisibility(View.GONE);
                        hotelViewHolder.info.setVisibility(View.VISIBLE);
                        hotelViewHolder.map.setVisibility(View.VISIBLE);
                        hotelViewHolder.selectHotel.setVisibility(View.GONE);
                        break;
                    case STATUS_EDIT://可选择
                        hotelViewHolder.mask.setVisibility(View.GONE);
                        hotelViewHolder.info.setVisibility(View.GONE);
                        hotelViewHolder.map.setVisibility(View.GONE);
                        hotelViewHolder.selectHotel.setVisibility(View.VISIBLE);
                        break;
                    case STATUS_CANNOT_EDIT://不可点击
                        hotelViewHolder.mask.setVisibility(View.VISIBLE);
                        hotelViewHolder.info.setVisibility(View.VISIBLE);
                        hotelViewHolder.map.setVisibility(View.VISIBLE);
                        hotelViewHolder.selectHotel.setVisibility(View.GONE);
                        break;
                }
                break;
            case TRAFFIC:
                trafficStatus();
                TrafficViewHolder trafficViewHolder = (TrafficViewHolder) viewHolder;
                if (detailDispEntity.getTitle().equals("飞机")) {
                    trafficViewHolder.selectResult.setBackgroundResource(R.drawable.detail_disp_traffic_plane);
                    trafficViewHolder.lytrafficCollect.setBackgroundResource(R.drawable.detail_disp_traffic_select_plane);
                    trafficViewHolder.plane.setTextColor(ctx.getResources().getColor(R.color.price));
                    trafficViewHolder.train.setTextColor(ctx.getResources().getColor(R.color.not_clicked));
                } else {
                    trafficViewHolder.selectResult.setBackgroundResource(R.drawable.detail_disp_traffic_train);
                    trafficViewHolder.lytrafficCollect.setBackgroundResource(R.drawable.detail_disp_traffic_select_train);
                    trafficViewHolder.plane.setTextColor(ctx.getResources().getColor(R.color.not_clicked));
                    trafficViewHolder.train.setTextColor(ctx.getResources().getColor(R.color.price));
                }
                trafficViewHolder.trafficTime.setText(detailDispEntity.getTime());

                switch (detailDispEntity.getEdited()) {
                    case STATUS_DISP:
                        trafficViewHolder.selectResultMask.setVisibility(View.GONE);
                        trafficViewHolder.trafficTimeMask.setVisibility(View.GONE);
                        trafficViewHolder.lytrafficCollect.setVisibility(View.GONE);
                        trafficViewHolder.flTrafficDisp.setVisibility(View.VISIBLE);
                        break;
                    case STATUS_EDIT:
                        trafficViewHolder.selectResultMask.setVisibility(View.GONE);
                        trafficViewHolder.trafficTimeMask.setVisibility(View.GONE);
                        trafficViewHolder.lytrafficCollect.setVisibility(View.VISIBLE);
                        trafficViewHolder.flTrafficDisp.setVisibility(View.GONE);
                        break;
                    case STATUS_CANNOT_EDIT:
                        trafficViewHolder.selectResultMask.setVisibility(View.VISIBLE);
                        trafficViewHolder.trafficTimeMask.setVisibility(View.VISIBLE);
                        trafficViewHolder.lytrafficCollect.setVisibility(View.GONE);
                        trafficViewHolder.flTrafficDisp.setVisibility(View.VISIBLE);
                        break;
                }

                if (i == trafficEnd) {
                    trafficViewHolder.lytrafficTime.setBackgroundResource(R.drawable.detail_disp_traffic_end);
                } else {
                    trafficViewHolder.lytrafficTime.setBackgroundResource(R.drawable.detail_disp_traffic_start);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class NullViewHolder extends RecyclerView.ViewHolder {
        public NullViewHolder(View itemView) {
            super(itemView);
        }
    }

    class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView day;

        public DayViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.detail_disp_day);
            day.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewListener.onDayClick(v, this.getPosition());
        }
    }

    class SightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public FrameLayout rlSight;
        public TextView title;
        public TextView info;
        public TextView map;
        public ImageView mask;
        public ImageView avatar;

        public SightViewHolder(View itemView) {
            super(itemView);
            rlSight = (FrameLayout) itemView.findViewById(R.id.travel_item_detail_disp_sight_bk);
            avatar = (ImageView)  itemView.findViewById(R.id.sight_avatar);
            title = (TextView) itemView.findViewById(R.id.detail_disp_sight_title);
            info = (TextView) itemView.findViewById(R.id.detail_disp_sight_info);
            map = (TextView) itemView.findViewById(R.id.detail_disp_sight_map);
            mask = (ImageView) itemView.findViewById(R.id.detail_disp_sight_unclick);
            avatar.setOnClickListener(this);
            info.setOnClickListener(this);
            map.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewListener.onSightClick(v, this.getPosition());
        }
    }

    class HotelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public FrameLayout rlHotel;
        public TextView title;
        public TextView info;
        public TextView map;
        public TextView selectHotel;
        public ImageView mask;
        public ImageView avatar;

        public HotelViewHolder(View itemView) {
            super(itemView);
            rlHotel = (FrameLayout) itemView.findViewById(R.id.travel_item_detail_disp_hotel_bk);
            avatar = (ImageView)  itemView.findViewById(R.id.hotel_avatar);
            title = (TextView) itemView.findViewById(R.id.detail_disp_hotel_title);
            info = (TextView) itemView.findViewById(R.id.detail_disp_hotel_info);
            map = (TextView) itemView.findViewById(R.id.detail_disp_hotel_map);
            selectHotel = (TextView) itemView.findViewById(R.id.detail_disp_hotel_select);
            mask = (ImageView) itemView.findViewById(R.id.detail_disp_hotel_unclick);
            avatar.setOnClickListener(this);
            selectHotel.setOnClickListener(this);
            info.setOnClickListener(this);
            map.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewListener.onHotelClick(v, this.getPosition());
        }
    }

    class TrafficViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView selectResult;
        public TextView plane;
        public TextView train;
        public ImageView selectResultMask;
        public FrameLayout flTrafficDisp;
        public LinearLayout lytrafficCollect;
        public LinearLayout lytrafficTime;
        public TextView trafficTime;
        public ImageView trafficTimeMask;

        public TrafficViewHolder(View itemView) {
            super(itemView);
            selectResult = (ImageView) itemView.findViewById(R.id.detail_disp_traffic_select_result);
            selectResultMask = (ImageView) itemView.findViewById(R.id.detail_disp_traffic_select_result_mask);
            plane = (TextView) itemView.findViewById(R.id.detail_disp_traffic_plane);
            train = (TextView) itemView.findViewById(R.id.detail_disp_traffic_train);
            flTrafficDisp = (FrameLayout) itemView.findViewById(R.id.fl_detail_disp_traffic_disp);
            lytrafficCollect = (LinearLayout) itemView.findViewById(R.id.ly_detail_disp_traffic_collect);
            lytrafficTime = (LinearLayout) itemView.findViewById(R.id.detail_disp_traffic_time);
            trafficTime = (TextView) itemView.findViewById(R.id.detail_disp_traffic_time_result);

            trafficTimeMask = (ImageView) itemView.findViewById(R.id.detail_disp_traffic_time_mask);
            selectResult.setOnClickListener(this);
            plane.setOnClickListener(this);
            train.setOnClickListener(this);
            lytrafficTime.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewListener.onTrafficClick(v, this.getPosition());
        }
    }
}
