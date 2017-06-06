package com.coolweather.android;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    static String location = null;
    static ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定信息
        mLocationClient = new LocationClient(getApplicationContext());
        //注册定位监听器
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);

        //提取缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //判断跳那个活动
        if (prefs.getString("weather",null) != null){
            Intent intent = new Intent(MainActivity.this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }


        //权限list
        List<String> permissionList = new ArrayList<String>();
        //授权权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{
            //启动定位
            requestLocation();
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String [permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }

        pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("提示");
        pd.setMessage("定位中......");
        pd.setCancelable(false);
        pd.show();



    }





    //权限返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for(int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意才可以使用权限",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    //启动定位
                    requestLocation();
                }else{
                    Toast.makeText(this,"发生未知错误" ,Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
    //启动定位
    private void requestLocation(){
        LocationClientOption option = new LocationClientOption();
        //option.setScanSpan(2000);//间隔时间
        option.setIsNeedAddress(true);//获取详细地址
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }



    //获取定位信息
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            location = bdLocation.getCity();
            Log.d("MainActivity", "onReceiveLocation: "+location);
            if (location!=null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        ChooseAreaFragment cf = (ChooseAreaFragment)getSupportFragmentManager().findFragmentById(R.id.choose_area_fragment);
                        cf.setLocation(location);

                    }
                });
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        ChooseAreaFragment cf = (ChooseAreaFragment)getSupportFragmentManager().findFragmentById(R.id.choose_area_fragment);
                        cf.setLocation("定位失败");

                    }
                });
            }


        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    //活动销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭定位
        mLocationClient.stop();
    }




}
