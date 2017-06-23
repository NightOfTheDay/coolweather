package com.coolweather.android;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 个人信息详情适配器
 * Created by Administrator on 2017/6/8.
 */

public class FruitPersonalDetailsAdapter extends RecyclerView.Adapter<FruitPersonalDetailsAdapter.ViewHolder> implements View.OnClickListener{




    private List<FruitPersonalDetails> fruitList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTradeName;
        TextView textContent;

        public ViewHolder(View view){
            super(view);
            textTradeName = (TextView) view.findViewById(R.id.text_trade_name);
            textContent  = (TextView) view.findViewById(R.id.text_content);
        }
    }


    public FruitPersonalDetailsAdapter(List<FruitPersonalDetails> fruitList) {
        this.fruitList = fruitList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_details_list,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(FruitPersonalDetailsAdapter.ViewHolder holder, int position) {
        FruitPersonalDetails fruitPersonalDetails = fruitList.get(position);
        holder.textTradeName.setText(fruitPersonalDetails.getName());
        holder.textContent.setHint(fruitPersonalDetails.getHint());
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }
    /**
     * 列表点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        final View vv = v;
        //从数据库查询列表数据
        final List<User> list = DataSupport.findAll(User.class);
        final EditText editText = new EditText(v.getContext());
        switch ((int)v.getTag()){
            case 0 ://修改名称
                new AlertDialog.Builder(v.getContext())
                        .setTitle("请输入名称")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //存数据库
                                User u = new User();
                                u.setName(editText.getText().toString());
                                u.updateAll();

                                //修改列表值
                                ViewHolder holder = new ViewHolder(vv);
                                holder.textContent.setText(editText.getText().toString());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
            case 1 ://修改性别
                String sex = list.get(0).getSex();
                int index = 0;
                if (sex !=null && "女".equals(sex)  ){
                    index = 1;
                }
                new AlertDialog.Builder(v.getContext())
                        .setTitle("请选择性别")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[] {"男","女"}, index,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //修改列表值
                                        ViewHolder holder = new ViewHolder(vv);
                                        //存数据库
                                        User u = new User();
                                        if (which == 0){
                                            u.setSex("男");
                                            holder.textContent.setText("男");
                                        }else{
                                            u.setSex("女");
                                            holder.textContent.setText("女");
                                        }
                                        u.updateAll();


                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();

                break;
            case 2 ://修改爱好
                final String[] hobbys = {"看书","游戏","写作","运动"};
                final boolean[] hobbyBoolean = {false,false,false,false};
                String hobby = list.get(0).getHobby();
                if (!TextUtils.isEmpty(hobby)){
                    String[] hs = hobby.split(",");
                    for (int i=0; hobbys.length>i; i++){
                        for (int j=0; hs.length>j; j++){
                            if (hobbys[i].equals(hs[j])){
                                hobbyBoolean[i] = true;
                            }
                        }
                    }
                }


                new AlertDialog.Builder(v.getContext())
                	.setTitle("请选择爱好")
                	.setMultiChoiceItems(hobbys, hobbyBoolean,  new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        //which 为用户点击的下标
                        //isChecked用户是否被勾选中
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            //    将客户是否被勾选的记录保存到集合中
                            hobbyBoolean[which] = isChecked;  //保存客户选择的属性是否被勾选
                        }
                    })
                	.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0;hobbyBoolean.length>i;i++){
                                if (hobbyBoolean[i]==true){
                                    sb.append(hobbys[i].toString());
                                    sb.append(",");
                                }
                            }
                            sb.deleteCharAt(sb.length()-1);

                            //存数据库
                            User u = new User();
                            u.setHobby(sb.toString());
                            u.updateAll();

                            //修改列表值
                            ViewHolder holder = new ViewHolder(vv);
                            holder.textContent.setText(sb.toString());

                        }
                    })
                	.setNegativeButton("取消", null)
                	.show();
                break;
            case 3 ://修改生日

                //从数据库获取值

                User user = list.get(0);
                Log.d("FruitPersonalDe", "onDateSet: "+user.getBirthday());
                String b = user.getBirthday();
                if (b!=null && !"".equals(b)){
                    String[] bs = b.split("-");
                    Log.d("FruitPersonalDetails", "onClick: "+bs[0]+"-"+bs[1]+"-"+bs[2]);
                    DatePickerDialog datePicker=new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String birthday = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                            //存数据库
                            User u = new User();
                            u.setBirthday(birthday);
                            u.updateAll();
                            //修改列表值
                            ViewHolder holder = new ViewHolder(vv);
                            holder.textContent.setText(birthday);
                        }

                    }, Integer.valueOf(bs[0]), Integer.valueOf(bs[1])-1, Integer.valueOf(bs[2]));
                    datePicker.show();
                }else{
                    DatePickerDialog datePicker=new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub

                            String birthday = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                            //存数据库
                            User u = new User();
                            u.setBirthday(birthday);
                            u.updateAll();
                            //修改列表值
                            ViewHolder holder = new ViewHolder(vv);
                            holder.textContent.setText(birthday);
                        }

                    }, 1990, 0, 1);
                    datePicker.show();
                }



                break;
            default:
                break;


        }

    }
}
