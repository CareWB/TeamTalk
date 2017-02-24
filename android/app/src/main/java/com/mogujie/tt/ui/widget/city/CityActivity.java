package com.mogujie.tt.ui.widget.city;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.github.promeg.pinyinhelper.Pinyin;
import com.mogujie.tt.DB.sp.SystemConfigSp;
import com.mogujie.tt.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CityActivity extends Activity implements MySlideView.onTouchListener, MyCityAdapter.onItemClickListener {
    public  static List<City> cityList = new ArrayList<>();
    private Set<String> firstPinYin = new LinkedHashSet<>();
    public static List<String> pinyinList = new ArrayList<>();
    private PinyinComparator pinyinComparator;

    private MySlideView mySlideView;
    private CircleTextView circleTxt;

    private ImageView back;

    private RecyclerView recyclerView;
    private MyCityAdapter adapter;
    private LinearLayoutManager layoutManager;

    private String locationCity = "";
    private String city = "";
    private Intent intent;
    private TextView selectCityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_city);
        intent = getIntent();
        initView();
        //initLocation();
    }

    private void initView() {
        back = (ImageView)findViewById(R.id.select_city_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBcak();
            }
        });
        selectCityResult = (TextView)findViewById(R.id.select_city_result);
        locationCity = SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOCAL_CITY);
        if (locationCity != null) {
            selectCityResult.setText(getString(R.string.local_city_hint) + locationCity);
        }
        selectCityResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCity(locationCity);
                goBcak();
            }
        });

        cityList.clear();
        firstPinYin.clear();
        pinyinList.clear();

        mySlideView = (MySlideView) findViewById(R.id.my_slide_view);
        circleTxt = (CircleTextView) findViewById(R.id.my_circle_view);
        pinyinComparator = new PinyinComparator();
        for (int i = 0; i < City.stringCitys.length; i++) {
            City city = new City();
            city.setCityName(City.stringCitys[i]);
            city.setCityPinyin(transformPinYin(City.stringCitys[i]));
            cityList.add(city);
        }
        Collections.sort(cityList, pinyinComparator);
        for (City city : cityList) {
            firstPinYin.add(city.getCityPinyin().substring(0, 1));

        }
        for (String string : firstPinYin) {
            pinyinList.add(string);
        }
        mySlideView.setListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_sticky_example);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyCityAdapter(getApplicationContext(), cityList);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new StickyDecoration(getApplicationContext()));
    }

    @Override
    public void itemClick(int position) {
        //setCity(cityList.get(position).getCityName());
        //Toast.makeText(getApplicationContext(), "你选择了:" + cityList.get(position).getCityName(), Toast.LENGTH_SHORT).show();
        setCity(cityList.get(position).getCityName());
        goBcak();
    }

    public String transformPinYin(String character) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < character.length(); i++) {
            buffer.append(Pinyin.toPinyin(character.charAt(i)));
        }
        return buffer.toString();
    }

    @Override
    public void showTextView(String textView, boolean dismiss) {

        if (dismiss) {
            circleTxt.setVisibility(View.GONE);
        } else {
            circleTxt.setVisibility(View.VISIBLE);
            circleTxt.setText(textView);
        }

        int selectPosition = 0;
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getFirstPinYin().equals(textView)) {
                selectPosition = i;
                break;
            }
        }
        scrollPosition(selectPosition);
    }


    public class PinyinComparator implements Comparator<City> {
        @Override
        public int compare(City cityFirst, City citySecond) {
            return cityFirst.getCityPinyin().compareTo(citySecond.getCityPinyin());
        }
    }

    private void scrollPosition(int index) {
        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        int lastPosition = layoutManager.findLastVisibleItemPosition();
        if (index <= firstPosition) {
            recyclerView.scrollToPosition(index);
        } else if (index <= lastPosition) {
            int top = recyclerView.getChildAt(index - firstPosition).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            recyclerView.scrollToPosition(index);
        }
    }

    private void setCity(String city) {
        this.city = city;
    }

    private void goBcak() {
        intent.putExtra("city", city);
        setResult(100, intent);
        finish();
    }
}
