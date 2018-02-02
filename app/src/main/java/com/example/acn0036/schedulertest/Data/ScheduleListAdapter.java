package com.example.acn0036.schedulertest.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.acn0036.schedulertest.Data.ScheduleData;
import com.example.acn0036.schedulertest.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ACN0036 on 2017-09-18.
 */

public class ScheduleListAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<ScheduleData> mScheduleList = new ArrayList<>();

    public ScheduleListAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setScheduleArrayList(ArrayList<ScheduleData> ScheduleArrayList) {
        this.mScheduleList = ScheduleArrayList;
        notifyDataSetChanged();
    }

    public void getSchedule(ArrayList<ScheduleData> ScheduleArrayList) {
        this.mScheduleList = ScheduleArrayList;
    }

    @Override
    public int getCount() {
        return mScheduleList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_schedule_listview, null);
        String arr[] = new String[6];

        ScheduleData ScheduleData = mScheduleList.get(position);
        String id = ScheduleData.getID();
        String title = ScheduleData.getTitle();
        String date = ScheduleData.getDate();
        String time = ScheduleData.getTime();
        String location = ScheduleData.getLocation();
        String memo = ScheduleData.getMemo();

        arr[0] = id;
        arr[1] = title;
        arr[2] = date;
        arr[3] = time;
        arr[4] = location;
        arr[5] = memo;

        ((TextView) view.findViewById(R.id.list_title)).setText(arr[1]);
        ((TextView) view.findViewById(R.id.list_time)).setText(arr[3]);
        ((TextView) view.findViewById(R.id.list_location)).setText(arr[4]);
        ((TextView) view.findViewById(R.id.list_memo)).setText(arr[5]);

        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String[] getItem(int position) {
        String arr[] = new String[6];

        // list data->db data

        ScheduleData ScheduleData = mScheduleList.get(position);

        String id = ScheduleData.getID();
        String title = ScheduleData.getTitle();
        String date = ScheduleData.getDate();
        String time = ScheduleData.getTime();
        String location = ScheduleData.getLocation();
        String memo = ScheduleData.getMemo();

        arr[0] = id;
        arr[1] = title;
        arr[2] = date;
        arr[3] = time;
        arr[4] = location;
        arr[5] = memo;

        return arr;
    }

}
