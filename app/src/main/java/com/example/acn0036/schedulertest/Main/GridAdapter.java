package com.example.acn0036.schedulertest.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.acn0036.schedulertest.Data.DatabaseHelper;
import com.example.acn0036.schedulertest.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ACN0036 on 2017-09-22.
 */

public class GridAdapter extends BaseAdapter{

    private final List<String> list;
    private final LayoutInflater inflater;

    private Calendar mCal;

    public GridAdapter(Context context, List<String> list) {
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_calender_gridview, parent, false);
            holder = new ViewHolder();
            holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
            holder.tvScheduleGridView = (TextView) convertView.findViewById(R.id.tv_schedule_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvItemGridView.setText("" + getItem(position));

        mCal = Calendar.getInstance();

        //today bold
        Integer today = mCal.get(Calendar.DAY_OF_MONTH);
        String sToday = String.valueOf(today);
        if (sToday.equals(getItem(position))) {
            holder.tvItemGridView.setTypeface(null, Typeface.BOLD);
        }


//                holder.tvScheduleGridView.setVisibility(View.VISIBLE);


        return convertView;
    }

    private class ViewHolder {
        TextView tvItemGridView;
        TextView tvScheduleGridView;
    }
}
