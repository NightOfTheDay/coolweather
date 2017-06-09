package com.coolweather.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息
 */
public class PersonalDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView detailsCv;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        detailsCv = (CardView) findViewById(R.id.details_cv);
        detailsCv.setOnClickListener(this);
        circleImageView = (CircleImageView)findViewById(R.id.details_civ);

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
                                    camera ();
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

    private static Uri uri;
    /**
     * 拍照调用
     */
    public void camera (){
        //获取路径下的图片信息
        File file = new File(getExternalCacheDir(),"head.jpg");
        try {
            //判断是否有图片文件
            if (file.exists()){
                file.delete();//删除图片
                file.createNewFile();//创建文件图片
            }
        }catch (IOException e){
            e.printStackTrace();
        }


        //判断是否大于Android7.0版本
        if (Build.VERSION.SDK_INT>24){
            //获取图片的真实路径
            uri = FileProvider.getUriForFile(PersonalDetailsActivity.this,"PDAhead",file);
        } else {
            //获取图片的真实路径
           uri = Uri.fromFile(file);
        }

        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,0);

    }

    /**
     * 拍照后返回结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == RESULT_OK){
                    //显示图片
                    Glide.with(this).load(uri).into(circleImageView);
                    //存app数据库
                    //存服务器
                }
                break;
            default:
                break;

        }
    }


    /**
     *  相册调用
     */
    public void photoAlbum(){
    }
}
