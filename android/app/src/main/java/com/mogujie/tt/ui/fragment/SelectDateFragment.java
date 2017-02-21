package com.mogujie.tt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mogujie.tt.DB.entity.DateEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.config.DateType;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.adapter.DayAdapter;
import com.mogujie.tt.ui.base.TTBaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置页面
 */
public class SelectDateFragment extends TTBaseFragment{
	private View curView = null;
    private Intent intent;
	private RecyclerView rvCalendar1;
    private RecyclerView rvCalendar2;
    private RecyclerView rvCalendar3;
    private RecyclerView rvCalendar4;
    private RecyclerView rvCalendar5;
    private DayAdapter dayAdapter1;
    private DayAdapter dayAdapter2;
    private DayAdapter dayAdapter3;
    private DayAdapter dayAdapter4;
    private DayAdapter dayAdapter5;
    Map<Integer, List<DateEntity>> date = new HashMap<>();
    private List<DateEntity> dateEntityList1 = new ArrayList<>();
    private List<DateEntity> dateEntityList2 = new ArrayList<>();
    private List<DateEntity> dateEntityList3 = new ArrayList<>();
    private List<DateEntity> dateEntityList4 = new ArrayList<>();
    private List<DateEntity> dateEntityList5 = new ArrayList<>();
    private static int MAX_MONTH_NUM = 5;
    private TextView monthTitle1;
    private TextView monthTitle2;
    private TextView monthTitle3;
    private TextView monthTitle4;
    private TextView monthTitle5;
    private List<TextView> monthTitleList = new ArrayList<>();
    private int selectCnt = 0;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private String strStartDate = "";
    private String strEndDate = "";
    private Date dStartDate;
    private Date dEndDate;
    private Date today = new Date();

    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {

            }
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imServiceConnector.connect(this.getActivity());
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.travel_fragment_select_date, topContentView);
        intent = getActivity().getIntent();
		initRes();
        initDate();
        initDateList();
        initDayRecycleView();
		return curView;
	}

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        imServiceConnector.disconnect(getActivity());
    }

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
		setTopTitle("往返日期");
		setTopLeftButton(R.drawable.back_x);
		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
                if (strStartDate.equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.create_travel_not_select_start_date), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (strEndDate.equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.create_travel_not_select_end_date), Toast.LENGTH_SHORT).show();
                    return;
                }
				if (getFragmentManager().getBackStackEntryCount() == 0) {
                    intent.putExtra("startDate", strStartDate);
                    intent.putExtra("endDate", strEndDate);
					getActivity().setResult(102, intent);
					getActivity().finish();
					return;
				}
				getFragmentManager().popBackStack();
			}
		});
        rvCalendar1 = (RecyclerView)curView.findViewById(R.id.rv_calendar_month1);
        rvCalendar2 = (RecyclerView)curView.findViewById(R.id.rv_calendar_month2);
        rvCalendar3 = (RecyclerView)curView.findViewById(R.id.rv_calendar_month3);
        rvCalendar4 = (RecyclerView)curView.findViewById(R.id.rv_calendar_month4);
        rvCalendar5 = (RecyclerView)curView.findViewById(R.id.rv_calendar_month5);

        monthTitle1 = (TextView) curView.findViewById(R.id.calendar_month1);
        monthTitle2 = (TextView) curView.findViewById(R.id.calendar_month2);
        monthTitle3 = (TextView) curView.findViewById(R.id.calendar_month3);
        monthTitle4 = (TextView) curView.findViewById(R.id.calendar_month4);
        monthTitle5 = (TextView) curView.findViewById(R.id.calendar_month5);

        monthTitleList.add(monthTitle1);
        monthTitleList.add(monthTitle2);
        monthTitleList.add(monthTitle3);
        monthTitleList.add(monthTitle4);
        monthTitleList.add(monthTitle5);

        date.put(0, dateEntityList1);
        date.put(1, dateEntityList2);
        date.put(2, dateEntityList3);
        date.put(3, dateEntityList4);
        date.put(4, dateEntityList5);

        tvStartDate = (TextView)curView.findViewById(R.id.select_date_start);
        tvEndDate = (TextView)curView.findViewById(R.id.select_date_end);
	}

    private void initDate() {

    }

	@Override
	protected void initHandler() {
	}


    private void initDateList() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        for (int monthIndex = 0; monthIndex < MAX_MONTH_NUM; monthIndex ++) {
            List<DateEntity> dateEntities = date.get(monthIndex);
            calendar.add(Calendar.MONTH, 1);

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            setHideDate(dateEntities, calendar);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            monthTitleList.get(monthIndex).setText(sdf.format(calendar.getTime()));
            for (int dayIndex = 1; dayIndex <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); dayIndex ++) {
                calendar.set(Calendar.DAY_OF_MONTH, dayIndex);
                dateEntities.add(getDateEntityFromCalendar(calendar, DateType.normal));
            }
        }
    }

    private DateEntity getDateEntityFromCalendar(Calendar calendar, int type) {
        DateEntity dateEntity = new DateEntity();
        dateEntity.setDate(calendar.getTime());
        dateEntity.setType(type);
        dateEntity.setWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        if (dateEntity.getDate().before(today)) {
            dateEntity.setType(DateType.cannot_select);
        }

        if (dateEntity.getType() != DateType.blank && dateEqual(today, dateEntity.getDate())) {
            dateEntity.setType(DateType.today);
        }

        return dateEntity;
    }

    private void setHideDate(List<DateEntity> dateEntities, Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            return;
        } else {
            for (int weekIndex = 1; weekIndex < dayOfWeek; weekIndex ++) {
                dateEntities.add(getDateEntityFromCalendar(calendar, DateType.blank));
            }
        }
    }

    private void initDayRecycleView() {
        rvCalendar1.setHasFixedSize(true);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 7);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvCalendar1.setLayoutManager(layoutManager1);
        DayAdapter.OnRecyclerViewListener dayRVListener1 = new DayAdapter.OnRecyclerViewListener() {
            @Override
            public void onSelectClick(int position) {
                setDate(dateEntityList1.get(position));
            }
        };
        dayAdapter1 = new DayAdapter(getActivity(), dateEntityList1);
        dayAdapter1.setOnRecyclerViewListener(dayRVListener1);
        rvCalendar1.setAdapter(dayAdapter1);

        rvCalendar2.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getActivity(), 7);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rvCalendar2.setLayoutManager(layoutManager2);
        DayAdapter.OnRecyclerViewListener dayRVListener2 = new DayAdapter.OnRecyclerViewListener() {
            @Override
            public void onSelectClick(int position) {
                setDate(dateEntityList2.get(position));
            }
        };
        dayAdapter2 = new DayAdapter(getActivity(), dateEntityList2);
        dayAdapter2.setOnRecyclerViewListener(dayRVListener2);
        rvCalendar2.setAdapter(dayAdapter2);

        rvCalendar3.setHasFixedSize(true);
        GridLayoutManager layoutManager3 = new GridLayoutManager(getActivity(), 7);
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        rvCalendar3.setLayoutManager(layoutManager3);
        DayAdapter.OnRecyclerViewListener dayRVListener3 = new DayAdapter.OnRecyclerViewListener() {
            @Override
            public void onSelectClick(int position) {
                setDate(dateEntityList3.get(position));
            }
        };
        dayAdapter3 = new DayAdapter(getActivity(), dateEntityList3);
        dayAdapter3.setOnRecyclerViewListener(dayRVListener3);
        rvCalendar3.setAdapter(dayAdapter3);

        rvCalendar4.setHasFixedSize(true);
        GridLayoutManager layoutManager4 = new GridLayoutManager(getActivity(), 7);
        layoutManager4.setOrientation(LinearLayoutManager.VERTICAL);
        rvCalendar4.setLayoutManager(layoutManager4);
        DayAdapter.OnRecyclerViewListener dayRVListener4 = new DayAdapter.OnRecyclerViewListener() {
            @Override
            public void onSelectClick(int position) {
                setDate(dateEntityList4.get(position));
            }
        };
        dayAdapter4 = new DayAdapter(getActivity(), dateEntityList4);
        dayAdapter4.setOnRecyclerViewListener(dayRVListener4);
        rvCalendar4.setAdapter(dayAdapter4);

        rvCalendar5.setHasFixedSize(true);
        GridLayoutManager layoutManager5 = new GridLayoutManager(getActivity(), 7);
        layoutManager5.setOrientation(LinearLayoutManager.VERTICAL);
        rvCalendar5.setLayoutManager(layoutManager5);
        DayAdapter.OnRecyclerViewListener dayRVListener5 = new DayAdapter.OnRecyclerViewListener() {
            @Override
            public void onSelectClick(int position) {
                setDate(dateEntityList5.get(position));
            }
        };
        dayAdapter5 = new DayAdapter(getActivity(), dateEntityList5);
        dayAdapter5.setOnRecyclerViewListener(dayRVListener5);
        rvCalendar5.setAdapter(dayAdapter5);
    }

    private void setDate(DateEntity dateEntity) {
        Date date =  dateEntity.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        boolean exchange = false;

        if (selectCnt % 2 == 0) {
            dStartDate = date;
            dEndDate = null;
        } else {
            dEndDate = date;
        }

        if (dEndDate != null) {
            exchange = false;
            if (dStartDate.after(dEndDate)) {
                exchange = true;
            }
        }

        if (selectCnt % 2 == 0) {
            strStartDate = sdf.format(dStartDate);
            tvStartDate.setText(strStartDate);
            tvEndDate.setText("");
            tvEndDate.setHint(getString(R.string.end_date_hint));
            setSelectDateBk(dStartDate, dEndDate);
        } else {
            if (exchange) {
                strStartDate = sdf.format(dEndDate);
                tvStartDate.setText(strStartDate);
                strEndDate = sdf.format(dStartDate);
                setSelectDateBk(dEndDate, dStartDate);
            } else {
                strEndDate = sdf.format(dEndDate);
                setSelectDateBk(dStartDate, dEndDate);
            }
            tvEndDate.setText(strEndDate);
        }
        selectCnt ++;
        freshCalendar();
    }

    private void setSelectDateBk(Date start, Date end) {
        for (int monthIndex = 0; monthIndex < date.size(); monthIndex ++) {
            List<DateEntity> month = date.get(monthIndex);
            for (int dayIndex = 0; dayIndex < month.size(); dayIndex ++) {
                DateEntity day = month.get(dayIndex);
                if (day.getType() == DateType.cannot_select || day.getType() == DateType.blank) {
                    continue;
                }

                day.setType(DateType.normal);
                if (dateEqual(today, day.getDate())){
                    day.setType(DateType.today);
                }

                if (end == null) {
                    if (start.equals(day.getDate())) {
                        day.setType(DateType.select);
                        continue;
                    }
                    continue;
                }

                if (start.equals(end) && start.equals(day.getDate())) {
                    day.setType(DateType.select);
                    continue;
                }

                if (day.getDate().equals(start)) {
                    day.setType(DateType.start);
                }

                if (day.getDate().equals(end)) {
                    day.setType(DateType.end);
                }

                if (day.getDate().after(start) && day.getDate().before(end)) {
                    day.setType(DateType.between);
                }
            }
        }
    }

    private void freshCalendar() {
        dayAdapter1.notifyDataSetChanged();
        dayAdapter2.notifyDataSetChanged();
        dayAdapter3.notifyDataSetChanged();
        dayAdapter4.notifyDataSetChanged();
        dayAdapter5.notifyDataSetChanged();
    }

    private boolean dateEqual(Date left, Date right) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strLeft = sdf.format(left);
        String strRight = sdf.format(right);
        if (strLeft.equals(strRight)) {
            return true;
        } else {
            return false;
        }
    }

}