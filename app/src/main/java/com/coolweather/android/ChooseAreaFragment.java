package com.coolweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 城市省份碎片类
 * Created by Administrator on 2017/5/31.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;//提示框
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<String>();

    /**
     *省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<County> countyList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;

    /**
     * 加载碎片初始化
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    /**
     * UI加载完触发-设置点击事件
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();//加载城市数据
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();//加载县数据
                } else if (currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        //设置backButton点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY){
                    queryCities();//加载城市数据
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();//加载省份数据
                }
            }
        });
        queryProvinces();//加载省份数据
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);//按钮显示隐藏
        provinceList = DataSupport.findAll(Province.class);//从数据库查询数据
        if (provinceList.size()>0){
            dataList.clear();//清空
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();//适配器更新数据
            listView.setSelection(0);//移动指针
            currentLevel = LEVEL_PROVINCE;
        }else{
            //从服务器上查询数据
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);//显示
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);//从数据库查询数据
        if (cityList.size()>0){
            dataList.clear();//清空
            for (City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();//适配器更新数据
            listView.setSelection(0);//移动指针
            currentLevel = LEVEL_CITY;
        }else {
            //从服务器上查询数据
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据查询，如果没有查询到再去服务器查询
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);//从数据库查询数据
        if (countyList.size()>0){
            dataList.clear();//清空
            for (County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();//适配器更新数据
            listView.setSelection(0);//移动指针
            currentLevel = LEVEL_COUNTY;
        }else {
            //从服务器上查询数据
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     * @param address
     * @param type
     */
    private void queryFromServer(String address,final String type){
        showProgressDialog();//显示提示框
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            //请求响应数据回调到触发
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);//解析和处理服务器返回的省级数据
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());//解析和处理服务器返回的市级数据
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());//解析和处理服务器返回的县级数据
                }

                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();//关闭进度对话框
                            if ("province".equals(type)){
                                queryProvinces();//查询选中市内所有的省
                            } else if ("city".equals(type)){
                                queryCities();//查询选中市内所有的市
                            } else if ("county".equals(type)){
                                queryCounties();//查询选中市内所有的县
                            }
                        }
                    });
                }
            }

            //查询服务器无响应时触发
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });

    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载.....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (progressDialog !=null){
            progressDialog.dismiss();
        }
    }


}
