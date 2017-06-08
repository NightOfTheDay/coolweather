package com.coolweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心碎片
 */
public class PersonalCenterFragment extends Fragment {

    private View view;//缓存Fragment view
    //头像
    private CardView personalCardView;
    //列表
    private RecyclerView recyclerView;
    private String[] names = {"修改密码","设置","版本","关于","退出账户"};
    private int[] imageIds = {R.mipmap.password,R.mipmap.set,R.mipmap.versions,R.mipmap.about,R.mipmap.esc,};
    private List<Fruit> fruitList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //取消状态栏透明度
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //FragmentTabHost切换Fragment时避免重复加载UI
        if(view==null){
            view = inflater.inflate(R.layout.personal_center,null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        //头像块
        personalCardView = (CardView) view.findViewById(R.id.personal_card_view);

        //获取列表数据
        fruitList = data();
        recyclerView = (RecyclerView) view.findViewById(R.id.personal_rv);
        //水平分割线
        /*recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL_LIST));*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        FruitAdapter fruitAdapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(fruitAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //头像块点击事件
        personalCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PersonalDetailsActivity.class);
                getActivity().startActivity(intent);
            }
        });


    }


    /**
     * 加载列表数据
     * @return
     */
    public List<Fruit> data(){
        List<Fruit> fruits = new ArrayList<>();
        for (int i = 0;i<names.length;i++){
            Fruit fruit = new Fruit();
            fruit.setImageId(imageIds[i]);
            fruit.setName(names[i]);
            fruits.add(fruit);
        }
        return  fruits;
    }
}
