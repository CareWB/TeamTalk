package com.mogujie.tt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mogujie.tt.DB.entity.TravelEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.protobuf.IMTravel;
import com.mogujie.tt.utils.GlideRoundTransform;
import com.mogujie.tt.utils.TravelUIHelper;
import com.view.jameson.library.CardAdapterHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jameson on 8/30/16.
 */
public class TravelTipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context ctx;
    private List<TravelEntity> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private Map<Integer, String> travelTypeStringMap = new HashMap();

    public TravelTipAdapter(Context ctx, List<TravelEntity> mList) {
        this.ctx = ctx;
        this.mList = mList;
        initTravelTypeMap();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
            mCardAdapterHelper.onCreateViewHolder(parent, itemView);
            return new TravelHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_card_item, parent, false);
            mCardAdapterHelper.onCreateViewHolder(parent, itemView);
            return new NewTravelHolder(itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        if (holder instanceof TravelHolder) {
            final TravelHolder travelHolder = (TravelHolder)holder;
            TravelEntity travelEntity = mList.get(position);
            travelHolder.travelBk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (travelHolder.lyTravelMenu.getVisibility() == View.VISIBLE) {
                        travelHolder.lyTravelMenu.setVisibility(View.GONE);
                        travelHolder.travelMenu.setClickable(true);
                    }
                }
            });

            travelHolder.travelMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (travelHolder.lyTravelMenu.getVisibility() == View.GONE) {
                        travelHolder.lyTravelMenu.setVisibility(View.VISIBLE);
                        travelHolder.travelMenu.setClickable(false);
                    }
                }
            });

            travelHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TravelUIHelper.showAlertDialog(ctx, ctx.getString(R.string.travel_share), new TravelUIHelper.dialogCallback() {
                        @Override
                        public void callback() {
                            Toast.makeText(ctx,"share ok",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            travelHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TravelUIHelper.showAlertDialog(ctx, ctx.getString(R.string.travel_delete), new TravelUIHelper.dialogCallback() {
                        @Override
                        public void callback() {
                            mList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(ctx,"delete ok",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Glide.with(ctx).load(travelEntity.getDestinationBK())
                    .transform(new GlideRoundTransform(ctx, 10))
                    .into(travelHolder.travelBk);
            travelHolder.travelDays.setText(travelEntity.getDuration()+ctx.getString(R.string.day));
            travelHolder.travelDuration.setText(travelEntity.getStartDate()+"-"+travelEntity.getEndDate());
            travelHolder.travelPoints.setText(travelEntity.getThroughPoint());
            travelHolder.personNum.setText(travelEntity.getUserCnt()+ctx.getString(R.string.person));
            travelHolder.payNum.setText(travelEntity.getCost()+ctx.getString(R.string.monetary_unit));
            travelHolder.travelType.setText(travelTypeStringMap.get(travelEntity.getType()));
        } else if (holder instanceof NewTravelHolder){
            NewTravelHolder newTravelHolder = (NewTravelHolder)holder;
            newTravelHolder.newBk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TravelUIHelper.openCreateTravelActivity(ctx);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TravelHolder extends RecyclerView.ViewHolder {
        public final ImageView travelBk;
        public final ImageView travelMenu;
        public final TextView travelDays;
        public final TextView travelDuration;
        public final TextView travelPoints;
        public final TextView personNum;
        public final TextView payNum;
        public final TextView travelType;
        public final LinearLayout lyTravelMenu;
        public final TextView share;
        public final TextView delete;

        public TravelHolder(final View itemView) {
            super(itemView);
            travelBk = (ImageView)itemView.findViewById(R.id.iv_travel_destination_bk);
            travelMenu = (ImageView)itemView.findViewById(R.id.iv_travel_destination_menu);
            travelDays = (TextView)itemView.findViewById(R.id.travel_days);
            travelDuration = (TextView)itemView.findViewById(R.id.travel_duration);
            travelPoints = (TextView)itemView.findViewById(R.id.travel_points);
            personNum = (TextView)itemView.findViewById(R.id.t_person_num);
            payNum = (TextView)itemView.findViewById(R.id.t_pay_num);
            travelType = (TextView)itemView.findViewById(R.id.t_travel_type);
            lyTravelMenu = (LinearLayout)itemView.findViewById(R.id.ly_travel_menu);
            share = (TextView)itemView.findViewById(R.id.travel_share);
            delete = (TextView)itemView.findViewById(R.id.travel_delete);
        }
    }

    public class NewTravelHolder extends RecyclerView.ViewHolder {
        public final ImageView newBk;

        public NewTravelHolder(final View itemView) {
            super(itemView);
            newBk = (ImageView)itemView.findViewById(R.id.iv_new_travel_destination_bk);
        }
    }

    private void initTravelTypeMap() {
        travelTypeStringMap.put(IMTravel.TravelType.TRAVEL_TYPE_ECONOMICAL_EFFICIENCY_VALUE, ctx.getString(R.string.economical_efficiency));
        travelTypeStringMap.put(IMTravel.TravelType.TRAVEL_TYPE_ECONOMICAL_COMFORT_VALUE, ctx.getString(R.string.economical_comfort));
        travelTypeStringMap.put(IMTravel.TravelType.TRAVEL_TYPE_LUXURY_QUALITY_VALUE, ctx.getString(R.string.luxury_quality));
    }

}