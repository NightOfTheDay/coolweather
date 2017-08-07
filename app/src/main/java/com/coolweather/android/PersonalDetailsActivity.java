package com.coolweather.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.coolweather.android.db.User;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.width;
import static com.coolweather.android.R.attr.height;

/**
 * 个人信息
 */
public class PersonalDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView detailsCv;
    private CircleImageView circleImageView;
    private Button button;


    //列表
    private RecyclerView recyclerView;
    private String[] names = {"用户名","性别","爱好","出生日期"};
    private String[] hints = {"修改用户名","修改性别","修改爱好","修改出生日期"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        ActivityCollector.addActivity(this);

        detailsCv = (CardView) findViewById(R.id.details_cv);
        detailsCv.setOnClickListener(this);
        circleImageView = (CircleImageView)findViewById(R.id.details_civ);
        button = (Button) findViewById(R.id.back_button);

        button.setOnClickListener(this);

        //从数据库查询列表数据
        List<User> list = DataSupport.findAll(User.class);
        String name = list.get(0).getName();
        String sex = list.get(0).getSex();
        String hobby = list.get(0).getHobby();
        String birthday = list.get(0).getBirthday();

        if (name!=null && !"".equals(name)){
            hints[0] = name;
        }
        if (sex!=null && !"".equals(sex)){
            hints[1] = sex;
        }
        if (hobby!=null && !"".equals(hobby)){
            hints[2] = hobby;
        }
        if (birthday!=null && !"".equals(birthday)){
            hints[3] = birthday;
        }




        //添加数据
        List<FruitPersonalDetails> fruitList = data();
        //获取列表
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_personal_details_list);
        //水平分割线
        /*recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL_LIST));*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        FruitPersonalDetailsAdapter fruitPersonalDetailsAdapter = new FruitPersonalDetailsAdapter(fruitList);
        recyclerView.setAdapter(fruitPersonalDetailsAdapter);

    }




    /**
     * 加载个人信息列表数据
     * @return
     */
    public List<FruitPersonalDetails> data(){
        List<FruitPersonalDetails> fruits = new ArrayList<>();
        for (int i = 0;i<names.length;i++){
            FruitPersonalDetails fruitPersonalDetails = new FruitPersonalDetails();
            fruitPersonalDetails.setName(names[i]);
            fruitPersonalDetails.setHint(hints[i]);
            fruits.add(fruitPersonalDetails);
        }
        return  fruits;
    }




    @Override
    protected void onResume() {
        super.onResume();
        //从数据库查询头像信息
        List<User> user = DataSupport.findAll(User.class);
        String headPortrait = user.get(0).getHeadPortrait();
        Log.d("PersonalDetailsActivity", "headPortrait: "+headPortrait);
        if (headPortrait!=null && !"".equals(headPortrait)){
           Glide.with(this).load(headPortrait).placeholder(R.mipmap.ic_launcher).into(circleImageView);
        }
    }

    //弹框使用
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
                                    //调用授权调用
                                    authorization();
                                }

                            }
                        }).create().show();
                break;
            case R.id.back_button:
                finish();
                break;
            default:
                break;
        }
    }

    private static Uri uri;//相机
    private static String imagePath = null;//相册
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
     * 拍照后返回结果和选择完照片调用
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0://照相
                if (resultCode == RESULT_OK){
                    //显示图片
                    Glide.with(this).load(uri).into(circleImageView);
                    //存app数据库
                    User user = new User();
                    user.setHeadPortrait(uri.toString());
                    user.updateAll();
                    //存服务器
                }
                break;
            case 1://相册
                if (resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19){
                        //4.4及以上系统使用这个方法处理图片
                        handlelmageOnKitKat(data);
                        //存app数据库
                        User user = new User();
                        user.setHeadPortrait(imagePath);
                        user.updateAll();
                    } else {
                        //4.4及以下系统使用这个方法处理图片
                        handlelmageBeforeKitKat(data);
                        //存app数据库
                        User user = new User();
                        user.setHeadPortrait(imagePath);
                        user.updateAll();
                    }


                    //存服务器
                }
                break;
            default:
                break;

        }
    }


    /**
     * 相册调用时授权
     */
    public void authorization(){
        if(ContextCompat.checkSelfPermission(PersonalDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PersonalDetailsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            //授过权直接调用
            photoAlbum();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //同意授权调用
                    photoAlbum();
                }else{
                    //不同意提示
                    Toast.makeText(this, "您没有授权无法选择照片", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,1);//打开相册,1为唯一id
    }

    //4.4以上
    @TargetApi(19)
    private void handlelmageOnKitKat(Intent data){

        Uri uri = data.getData();
       if (DocumentsContract.isDocumentUri(this,uri)){
           //如果是document类型的Uri，则通过documentid处理
           String docld = DocumentsContract.getDocumentId(uri);
           if ("com.android.providers.media.documents".equals(uri.getAuthority())){
               String id = docld.split(":")[1];//解析出数字格式的 id
               String selection = MediaStore.Images.Media._ID + "=" + id;
               imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
           } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
               Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docld));
               imagePath = getImagePath(contentUri,null);
           }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
           //如果是 content 类型的Uri，则使用普通方式处理
           imagePath = getImagePath(uri,null);
       } else if ("file".equalsIgnoreCase(uri.getScheme())){
           //如果是file类型的Uri，直接获取图片路径即可
           imagePath = uri.getPath();
       }
        //显示图片
        displayImage(imagePath);
    }


    //4.4以下
    private void handlelmageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        //显示图片
        displayImage(imagePath);
    }

    /**
     * 获取图片地址
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和seletion来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor !=null ){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    /**
     * 显示图片
     * @param imagePath
     */
    private void displayImage(String imagePath){
        Log.d("PersonalDetailsActivity", "imagePath: "+imagePath);
        if (imagePath != null){
            //显示图片
            Glide.with(this).load(imagePath).into(circleImageView);
            //存app数据库
            //存服务器
        }else{
            Toast.makeText(this, "照片选择失败", Toast.LENGTH_SHORT).show();
        }
    }


}
