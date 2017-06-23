package com.coolweather.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 个人信息详情适配器
 * Created by Administrator on 2017/6/8.
 */

public class FruitPersonalDetailsAdapter extends RecyclerView.Adapter<FruitPersonalDetailsAdapter.ViewHolder> {

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
        return holder;
    }

    @Override
    public void onBindViewHolder(FruitPersonalDetailsAdapter.ViewHolder holder, int position) {
        FruitPersonalDetails fruitPersonalDetails = fruitList.get(position);
        holder.textTradeName.setText(fruitPersonalDetails.getName());
        holder.textContent.setHint(fruitPersonalDetails.getHint());
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }
}
