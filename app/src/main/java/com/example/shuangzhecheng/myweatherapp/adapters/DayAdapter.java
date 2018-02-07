package com.example.shuangzhecheng.myweatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shuangzhecheng.myweatherapp.R;
import com.example.shuangzhecheng.myweatherapp.model.Day;

/**
 * Created by shuangzhecheng on 2/6/16.
 */

public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context,Day[] days){
        mContext=context;
        mDays=days;
    }
    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.daily_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.iconLabel=(ImageView)convertView.findViewById(R.id.iconImageView);
            viewHolder.dayLabel=(TextView)convertView.findViewById(R.id.nameDayLabel);
            viewHolder.temperature=(TextView)convertView.findViewById(R.id.TemperatureLabel);
            viewHolder.circleImageView = (ImageView) convertView.findViewById(R.id.circleImageView);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        Day day=mDays[position];
        viewHolder.iconLabel.setImageResource(day.getIconId());
        viewHolder.temperature.setText(day.getMaxTemperature()+"");
        viewHolder.dayLabel.setText(day.getDayOfTheWeek());
        viewHolder.circleImageView.setImageResource(R.drawable.bg_temperature);

        return convertView;
    }

    private static class ViewHolder{
        ImageView iconLabel;
        TextView temperature;
        TextView dayLabel;
        ImageView circleImageView;


    }
}
