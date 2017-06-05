package com.coolweather.android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.coolweather.android.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;

import static android.provider.Contacts.PresenceColumns.OFFLINE;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        //启动线程
        new MyAsyncTask().execute();
    }


    /**
     * 启动界面线程
     */
    class MyAsyncTask extends AsyncTask<Void, Void, Integer> {
        private static final int FAILURE = 0; // 失败
        private static final int SUCCESS = 1; // 成功
        private  boolean DATA;//
        private static final int SHOW_TIME_MIN = 2000;

        @Override
        protected Integer doInBackground(Void... arg0) {
            //计算延长时间
            int result;
            result = loadingCache();
            long startTime = System.currentTimeMillis();
            long loadingTime = System.currentTimeMillis() - startTime;
            if (loadingTime < SHOW_TIME_MIN) {
                try {
                    Thread.sleep(SHOW_TIME_MIN - loadingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //耗时逻辑
            //判断是否记住密码了
            List<User> list = DataSupport.where("quit=?","1").find(User.class);
            // 查询信息
            Cursor cursor = DataSupport.findBySQL("select * from User where quit=?  and remember=?","1","1");
            //有记住账号和没退出的账号
            DATA = cursor.moveToFirst();
            return result;
        }

        /**
         * 返回值运行
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            //跳转Activity
            if(DATA){
                finish();
                //跳转主页
                Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(i);
            }else{
                finish();
                //跳转登录
                Intent i = new Intent(WelcomeActivity.this, LogInActivity.class);
                startActivity(i);
            }
            //显示消失效果
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }


        //网络数据加载时间
        public int loadingCache() {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null) {
                return FAILURE;
            }
            return SUCCESS;
        }
    }


}
