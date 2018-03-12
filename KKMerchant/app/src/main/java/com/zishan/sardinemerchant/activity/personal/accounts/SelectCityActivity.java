package com.zishan.sardinemerchant.activity.personal.accounts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.wislie.rxjava.model.personal.CityEntity;
import com.example.wislie.rxjava.model.personal.ProvinceEntity;
import com.example.wislie.rxjava.model.personal.SortModel;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.ftas.map.amap.AmapHelper;
import com.hg.ftas.util.AssetsUtil;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.SortAdapter;
import com.zishan.sardinemerchant.utils.CharacterParser;
import com.zishan.sardinemerchant.view.CleanableEditView;
import com.zishan.sardinemerchant.view.PinyinComparator;
import com.zishan.sardinemerchant.view.SideBar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.runtimepermission.MPermissionUtils;
import top.ftas.ftasbase.log.MobileLog;
import z.sye.space.pinyin.PinyinUtils;

/**
 * Created by yang on 2017/11/3.
 * <p>
 * 选择城市
 */

public class SelectCityActivity extends BActivity {

    @BindView(R.id.cityInputText)
    CleanableEditView mCityTextSearch;
    @BindView(R.id.currentCityTag)
    TextView mCurrentCityTag;
    @BindView(R.id.currentCity)
    TextView mCurrentCity;
    @BindView(R.id.localCityTag)
    TextView mLocalCityTag;
    @BindView(R.id.localCity)
    TextView mLocalCity;
    @BindView(R.id.country_lvcountry)
    ListView sortListView;
    @BindView(R.id.top_char)
    TextView xuanfaText;
    @BindView(R.id.top_layout)
    LinearLayout xuanfuLayout;
    @BindView(R.id.dialog)
    TextView mDialog;
    @BindView(R.id.sidrbar)
    SideBar mSidrbar;
    @BindView(R.id.location_name)
    TextView mLocationNameText;

    private int lastFirstVisibleItem = -1;
    public SortAdapter adapter;
    private String provinceId;
    private String fullname;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> sourceDateList;//填充每个Item的对象，包含市名字和名字的首字母
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private CityEntity cityEntity = new CityEntity();

    private List<CityEntity> allCityList = new ArrayList<>();//放所有市级城市的一个大集合
    //高德定位
    private AmapHelper amapHelper = null;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }


    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.select_city));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_city_list_select;
    }

    @Override
    protected void initContentView() {
        initList();//初始化列表
        initData();
        setCityData(allCityList);
        initScroll();
        requestLocationPermission();
    }

    private void initScroll() {

        /**
         * 设置滚动监听， 实时跟新悬浮的字母的值
         */
        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int section = adapter.getSectionForPosition(firstVisibleItem);
                int nextSecPosition = adapter
                        .getPositionForSection(section + 1);
                if (firstVisibleItem != lastFirstVisibleItem) {
                    MarginLayoutParams params = (MarginLayoutParams) xuanfuLayout
                            .getLayoutParams();
                    params.topMargin = 0;
                    xuanfuLayout.setLayoutParams(params);
                    xuanfaText.setText(String.valueOf((char) section));
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = xuanfuLayout.getHeight();
                        int bottom = childView.getBottom();
                        MarginLayoutParams params = (MarginLayoutParams) xuanfuLayout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            xuanfuLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                xuanfuLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    private void initData() {
        Gson gson = new Gson();

        //选择省市区数据

        String fileContent = AssetsUtil.getFromAssets(SelectCityActivity.this.getApplicationContext(), "district");

        ArrayList<ProvinceEntity> listProvince = gson.fromJson(fileContent,//所有省份的集合
                new TypeToken<List<ProvinceEntity>>() {
                }.getType());


        for (int i = 0; i < listProvince.size(); i++) {

            ProvinceEntity provinceEntity = listProvince.get(i);

            //省的代码id
            provinceId = provinceEntity.getAdcode();
            fullname = provinceEntity.getFullname();

            List<CityEntity> cityList = provinceEntity.getDistricts();//取出每个省份的市级城市

            for (int j = 0; j < cityList.size(); j++) {
                CityEntity cityEntity = cityList.get(j);
                cityEntity.setProvinceId(provinceId);
                cityEntity.setProvinceName(fullname);
                allCityList.add(cityEntity);//所有的市放到市的一个大集合中
            }
        }

    }

    private void setCityData(List<CityEntity> cityList) {
        allCityList = cityList;
        int count = cityList.size();
        String[] list = new String[count];
        for (int i = 0; i < count; i++) {
            CityEntity cityEntity = cityList.get(i);
            String fullname = cityEntity.getFullname();//拿到它的一个全称

            //如果当前对象中fullname为空时，就去它的简称name
            if (TextUtils.isEmpty(fullname)) {
                String name = cityEntity.getName();
                list[i] = name;
            } else {
                String name = cityEntity.getFullname();
                list[i] = name;//解析所有的市城市，放在一个市的数组里边
            }

        }

        List<SortModel> sortModels = filledData(list);
        sourceDateList.addAll(sortModels);//这个已经是拿到Item 一个集合了
        int size = sourceDateList.size();//所有获得的名字变成字母的一个集合
        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList, pinyinComparator);//a-z顺序排好，刷新适配器
        adapter.notifyDataSetChanged();
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {

        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {

            int length = date.length;
            SortModel sortModel = new SortModel();
            String name = date[i];
            sortModel.setName(name);//1.数组里边市的名字赋值到Item的对象中
            //汉字转换成拼音
            if (!TextUtils.isEmpty(name)) {
                String pinyin = PinyinUtils.hanziToPinyin(name);
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    String size = sortString.toUpperCase();
                    sortModel.setSortLetters(size);//2.设置每个市级城市的名字首字母
                    mSortList.add(sortModel);
                } else {
                    sortModel.setSortLetters("#");
                    mSortList.add(sortModel);
                }
            }
        }
        return mSortList;
    }

    private void initList() {
        sourceDateList = new ArrayList<SortModel>();//创建了一个空的Item集合
        adapter = new SortAdapter(SelectCityActivity.this, sourceDateList);
        sortListView.setAdapter(adapter);

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSidrbar.setTextView(mDialog);
        //设置右侧触摸监听
        mSidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private String cityName;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityName = ((SortModel) adapter.getItem(position)).getName();
                returnData(cityName);
            }
        });

        //根据输入框输入值的改变来过滤搜索
        mCityTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : sourceDateList) {
                String name = sortModel.getName();
                if (name.contains(filterStr) || characterParser.getSelling(name).startsWith(filterStr)) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @OnClick({R.id.get_location, R.id.location_name})
    public void onClick(View view) {
        switch (view.getId()) {
            //获取位置
            case R.id.get_location:
                mLocationNameText.setText("");
                requestLocationPermission();
                break;

            case R.id.location_name:
                String cityName = mLocationNameText.getText().toString();
                if (!TextUtils.isEmpty(cityName)) {
                    returnData(cityName);
                }
                break;
        }
    }

    private void returnData(String cityName) {

        cityEntity = CityEntity.findCity(allCityList, cityName);
        if (cityEntity == null) return;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        String cityNames = cityEntity.getFullname();//市名
        bundle.putString("fullname", cityNames);
        String adcode = cityEntity.getAdcode();
        String provinceId = cityEntity.getProvinceId();
        String provinceName = cityEntity.getProvinceName();
        bundle.putString("provinceName", provinceName);
        bundle.putString("adcode", adcode);
        bundle.putString("provinceId", provinceId);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void gpsLocation() {

        amapHelper = new AmapHelper(SelectCityActivity.this);
        amapHelper.setListener(new AMapLocationListener() {
            private boolean hasLocation = false;
            private int count = 0;

            @Override
            public void onLocationChanged(AMapLocation location) {
                if (location != null && !hasLocation) {
                    try {
                        mLocationNameText.setText(location.getCity());
                        hasLocation = true;
                        amapHelper.stopLocation();
                    } catch (Exception e) {
//                        error("定位错误", CallbackErrorCode.GET_LOCATION_ERROR, "无法获取到位置信息");
                        e.printStackTrace();
                    }
                } else if (count++ > 3) {
                    amapHelper.stopLocation();
                    if (!hasLocation && count > 5) {
//                        error("定位错误", CallbackErrorCode.GET_LOCATION_ERROR, "无法获取到位置信息");
                        MobileLog.e("tes", "定位超时，次数大于5");
                    }
                }
            }
        });
        amapHelper.startLocation();

    }

    //定位权限
    private void requestLocationPermission() {

        MPermissionUtils.requestPermissionsResult(this, Constant.PERMISSION_GAODE_LOCATION, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION}
                , new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        gpsLocation();
                    }

                    @Override
                    public void onPermissionDenied() {
                    }
                });
    }

    public static String sHA1(Context context) {

        try {

            PackageInfo info = context.getPackageManager().getPackageInfo(

                    context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA1");

            byte[] publicKey = md.digest(cert);

            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < publicKey.length; i++) {

                String appendString = Integer.toHexString(0xFF & publicKey[i])

                        .toUpperCase(Locale.US);

                if (appendString.length() == 1)

                    hexString.append("0");

                hexString.append(appendString);

            }

            return hexString.toString();

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


}
