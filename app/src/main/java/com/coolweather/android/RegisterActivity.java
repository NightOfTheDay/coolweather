package com.coolweather.android;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.appindexing.Thing;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化
        editAccount = (EditText)findViewById(R.id.edit_account);
        editPass = (EditText)findViewById(R.id.edit_pass);
        editPhone = (EditText)findViewById(R.id.edit_phone);
        buttonAffirm = (Button)findViewById(R.id.button_affirm);
        buttonReturn = (Button)findViewById(R.id.button_return);
        backButton = (Button)findViewById(R.id.back_button);
        titleText = (TextView)findViewById(R.id.title_text);
        imageCheck = (ImageView)findViewById(R.id.image_check);
        imagePhone = (ImageView)findViewById(R.id.image_phone);
        imagePass = (ImageView)findViewById(R.id.image_pass);
        //点击事件
        buttonAffirm.setOnClickListener(this);
        buttonReturn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        titleText.setText("注册");
        //验证
        validVerification();
    }

    //点击事件触发
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_affirm:
                //点击按钮让EditText失去焦点
                buttonAffirm.setFocusable(true);
                buttonAffirm.setFocusableInTouchMode(true);
                buttonAffirm.requestFocus();

                if (imageCheck.getDrawable()!=null&&
                        imagePhone.getDrawable()!=null&&
                        imagePass.getDrawable()!=null){
                    //保存数据库信息
                    User u = new User();
                    u.setAccount(editAccount.getText().toString());
                    u.setPass(editPass.getText().toString());
                    u.setPhone(editPhone.getText().toString());
                    u.setRemember("0");
                    u.setQuit("0");
                    u.save();
                    //提示
                    AlertDialog.Builder aelrt = new AlertDialog.Builder(RegisterActivity.this);
                    aelrt.setTitle("提示");
                    aelrt.setMessage("注册成功");
                    aelrt.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           finish();//销毁活动
                        }
                    });
                    aelrt.show();
                }else {
                    toast("请填写正确信息");
                }

                break;
            case R.id.button_return:
                finish();//销毁活动
                break;
            case R.id.back_button:
                finish();//销毁活动
                break;
            default:
                break;
        }
    }



    //验证
    public void validVerification(){
        //验证账号合法性
        editAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    imageCheck .setImageResource(0);//清除图片
                    String editAccountV = editAccount.getText().toString();
                    //验证空值
                    if (TextUtils.isEmpty(editAccountV)){
                        toast("账号不能为空");
                    }else{
                        //查询数据库对比信息
                        List<User> u = DataSupport.where("account=?",editAccountV).find(User.class);
                        if(u.size() !=0){
                            toast("账号存在");
                        }else{
                            //无账号
                            Glide.with(RegisterActivity.this).load(R.drawable.check).into(imageCheck);//显示图片

                        }
                    }

                }
            }
        });

        //验证密码
        editPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String editPassV = editPass.getText().toString();
                    if (TextUtils.isEmpty(editPassV)){
                        imagePass .setImageResource(0);//清除图片
                        toast("密码不能为空");
                    }else{
                        Glide.with(RegisterActivity.this).load(R.drawable.check).into(imagePass);//显示图片
                    }

                }
            }
        });

        //验证手机合法性
        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String editPhoneV = editPhone.getText().toString();
                    if (editPhoneV.length() != 11){
                        imagePhone .setImageResource(0);//清除图片
                        toast("手机号码有误");
                    }else{
                        //确认按钮释放
                        buttonAffirm.setFocusableInTouchMode(false);
                        Glide.with(RegisterActivity.this).load(R.drawable.check).into(imagePhone);//显示图片
                    }
                }
            }
        });
    }



    /**
     * 键盘收起
     * @param event
     * @return
     */
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

    /**
     * 键盘收起
     * @return
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


    //弹框
    public void toast(String text){
        Toast.makeText(RegisterActivity.this,text,Toast.LENGTH_SHORT).show();
    }
}
