package com.coolweather.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.db.User;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 登录
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editAccount;

    private EditText editPass;

    private EditText editPhone;

    private Button buttonAffirm;

    private Button buttonReturn;

    private Button backButton;

    private TextView titleText;

    private ImageView imagePhone;

    private ImageView imageCheck;

    private ImageView imagePass;


    // 有效验证
    private Boolean b = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //初始化
        editAccount = (EditText) findViewById(R.id.log_edit_account);
        editPass = (EditText) findViewById(R.id.log_edit_pass);
        buttonAffirm = (Button) findViewById(R.id.log_button_affirm);
        buttonReturn = (Button) findViewById(R.id.log_button_return);
        backButton = (Button) findViewById(R.id.back_button);
        titleText = (TextView) findViewById(R.id.title_text);
        //点击事件
        buttonAffirm.setOnClickListener(this);
        buttonReturn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        titleText.setText("登录");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //点击事件触发
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_button_affirm:
                finish();
                //注册
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.log_button_return:
                //注册
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.back_button:
                finish();//销毁活动
                break;
            default:
                break;
        }
    }


    //弹框
    public void toast(String text) {
        Toast.makeText(LogInActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LogIn Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
