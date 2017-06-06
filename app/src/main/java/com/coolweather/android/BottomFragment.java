package com.coolweather.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 底部菜单
 */
public class BottomFragment extends Fragment {

    private TextView weatherText;
    private TextView meText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_menu,container,false);
        weatherText = (TextView) view.findViewById(R.id.weather_text);
        meText = (TextView) view.findViewById(R.id.me_text);
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        weatherText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherText.setSelected(false);
            }
        });
        meText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meText.setSelected(true);
            }
        });




    }
}
