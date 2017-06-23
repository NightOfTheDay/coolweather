package com.coolweather.android;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolweather.android.db.User;

import java.util.List;

/**
 * 个人中心适配器
 * Created by Administrator on 2017/6/8.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder>  implements View.OnClickListener{

    private List<Fruit> fruitList;

    @Override
    public void onClick(View v) {
        switch ((int)v.getTag()){
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:
                new AlertDialog.Builder(v.getContext())
                        .setTitle("提示")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage("您确定要退出程序吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //存数据库
                                User u = new User();
                                u.setQuit("0");
                                u.updateAll();
                                ActivityCollector.finishAll();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
            default:
                break;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.personage_icon);
            textView  = (TextView) view.findViewById(R.id.personage_text);
        }
    }


    public FruitAdapter(List<Fruit> fruitList) {
        this.fruitList = fruitList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personage_menu,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(FruitAdapter.ViewHolder holder, int position) {
        Fruit fruit = fruitList.get(position);
        holder.imageView.setImageResource(fruit.getImageId());
        holder.textView.setText(fruit.getName());
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }
}
