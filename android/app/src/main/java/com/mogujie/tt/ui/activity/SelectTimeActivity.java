package com.mogujie.tt.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.joybar.librarycalendar.data.CalendarDate;
import com.joybar.librarycalendar.fragment.CalendarViewFragment;
import com.joybar.librarycalendar.fragment.CalendarViewPagerFragment;
import com.mogujie.tt.R;
import com.mogujie.tt.ui.base.TTBaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectTimeActivity extends TTBaseFragmentActivity implements
        CalendarViewPagerFragment.OnPageChangeListener,
        CalendarViewFragment.OnDateClickListener,
        CalendarViewFragment.OnDateCancelListener {

    private boolean isChoiceModelSingle = true;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvMonth;
    private CalendarDate start;
    private CalendarDate end;

    static final int START = 1;
    static final int END = 2;
    private int clickFlag = START;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_fragment_activity_select_time);
        initRes();
        initFragment();
    }

    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        Fragment fragment = CalendarViewPagerFragment.newInstance(isChoiceModelSingle);
        tx.replace(R.id.fl_select_time, fragment);
        tx.commit();
    }

    @Override
    public void onDateClick(CalendarDate calendarDate) {
        if (clickFlag == START) {
            start = calendarDate;
        } else {
            end = calendarDate;
        }
        dateDisp();
    }

    @Override
    public void onDateCancel(CalendarDate calendarDate) {}

    @Override
    public void onPageChange(int year, int month) {
        tvMonth.setText(year + "年" + month + "月");
    }

    private void initRes() {
        tvStart = (TextView)findViewById(R.id.select_time_start);
        tvEnd = (TextView)findViewById(R.id.select_time_end);
        tvMonth = (TextView)findViewById(R.id.select_time_month);

        View.OnClickListener dateClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.select_time_start:
                        clickFlag = START;
                        break;
                    case R.id.select_time_end:
                        clickFlag = END;
                        break;
                }
            }
        };
        tvStart.setOnClickListener(dateClickListener);
        tvEnd.setOnClickListener(dateClickListener);
    }

    private static String dateToString(CalendarDate calendarDate) {
        StringBuffer stringBuffer = new StringBuffer();
        if (calendarDate != null) {
            stringBuffer.append(calendarDate.getSolar().solarYear + "." +
                    calendarDate.getSolar().solarMonth + "." +
                    calendarDate.getSolar().solarDay).append(" ");
            return stringBuffer.toString();
        } else {
            return "";
        }
    }

    private void dateDisp() {
        tvStart.setText(dateToString(start));
        tvEnd.setText(dateToString(end));
    }
}
