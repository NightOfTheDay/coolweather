package com.coolweather.android;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 个人信息
 */
public class PersonalDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView detailsCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        detailsCv = (CardView) findViewById(R.id.details_cv);
        detailsCv.setOnClickListener(this);

    }

    private String[] items={"拍照", "从相机选择"};
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.details_cv:
                new AlertDialog.Builder(this)
                        .setTitle("修改头像")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (which==0){
                                    //调用相机

                                } else if (which==1){
                                    //调用相册
                                }



                            }
                        }).create().show();
                break;
            default:
                break;
        }
    }
}
