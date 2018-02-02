package com.example.acn0036.schedulertest.AddSchedule;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.acn0036.schedulertest.Data.DatabaseHelper;
import com.example.acn0036.schedulertest.Main.MainActivity;
import com.example.acn0036.schedulertest.Data.ScheduleListAdapter;
import com.example.acn0036.schedulertest.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddScheduleActivity extends Activity {

    private Button mAddLocationButton;
    private TextView mDateView, mLocationEditText, mBackTextView;
    private EditText mMemoEidtText, mTitleEditText;
    private Button mSaveButton, mCancelButton;
    private ListView mDataListView;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private CheckBox mSetAlarm;

    private DatabaseHelper mDataBaseHelper;

    private ScheduleListAdapter mScheduleListAdapter;

    private ArrayList scheduleDataArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        mAddLocationButton = (Button) findViewById(R.id.btn_addlocation);
        mLocationEditText = (TextView) findViewById(R.id.edit_location);
        mMemoEidtText = (EditText) findViewById(R.id.edit_memo);
        mSaveButton = (Button) findViewById(R.id.btn_save);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mDateView = (TextView) findViewById(R.id.dateView);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mTitleEditText = (EditText) findViewById(R.id.edit_title);

        mSetAlarm = (CheckBox)findViewById(R.id.setAlarm);

        mDataListView = (ListView) findViewById(R.id.databaseschedule);
        mBackTextView = (TextView) findViewById(R.id.tv_back);

        mTimePicker.setIs24HourView(true); //to set 24 hours mode

        Intent intentDB = getIntent();
        String[] arr = intentDB.getStringArrayExtra("arr");

        if (arr == null) {// insert : main에서 받아온 값이 없을 경우
            Log.e("insert", "new data");

        } else {// update : main에서 schedule을 받아왔을 경우

            Log.e("update", "db id = " + arr[0]);

            String[] date = (arr[2].split("/"));
            String[] time = arr[3].split(":");
            for(int i = 0; i<3; i++) {
                Log.e("split date", date[i]);
            }
            mTitleEditText.setText(arr[1]);
            mDatePicker.updateDate(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));
//            mTimePicker.setHour(Integer.parseInt(time[0]));
//            mTimePicker.setMinute(Integer.parseInt(time[1]));
            mLocationEditText.setText(arr[4]);
            mMemoEidtText.setText(arr[5]);

        }

        mAddLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapintent = new Intent();
                mapintent.setClass(AddScheduleActivity.this, MapsActivity.class);
                startActivityForResult(mapintent, 2);
            }
        });


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String[] arr = intent.getStringArrayExtra("arr");

                int day = mDatePicker.getDayOfMonth();
                int month = mDatePicker.getMonth() + 1;
                int year = mDatePicker.getYear();

                int hour = mTimePicker.getCurrentHour();
                int min = mTimePicker.getCurrentMinute();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String formatedDate = sdf.format(new Date(year, month, day, hour, min));
                Date dateAll = sdf.parse(formatedDate, new ParsePosition(0));

                String Title = mTitleEditText.getText().toString();
                String Date = year + "/" + String.format("%02d", month) + "/" + String.format("%02d", day);
                String Time = hour + ":" + min;
                String Location = mLocationEditText.getText().toString();
                String Memo = mMemoEidtText.getText().toString();

                Log.e("insert data", Title + "/" + Location + "/" + Memo);

                Log.e("insert date time", Date + "   " + hour + ":" + min);

                mDataBaseHelper = new DatabaseHelper(getApplicationContext());

                if (arr == null)
                    mDataBaseHelper.insertData(Title, Date, Time, Location, Memo);
                else
                    mDataBaseHelper.updateData(arr[0], Title, Date, Time, Location, Memo);

                //알람
                //todo
                if(mSetAlarm.isChecked()) {
                    Log.e("set alarm", "when " + dateAll);
                    setAlarm(getApplicationContext(), dateAll);
                }
                else releaseAlarm(getApplicationContext());

                Intent intentDate = new Intent(AddScheduleActivity.this, MainActivity.class);
                intentDate.putExtra("date", Date);
                startActivity(intentDate);

                init();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }

    private void init() {
        mTitleEditText.setText("");
        mLocationEditText.setText("");
        mMemoEidtText.setText("");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            String location = data.getStringExtra("location");
            mLocationEditText.setText(location);
        }
    }

    //알람 등록
    private void setAlarm(Context context, Date date){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonth());
        calendar.set(Calendar.DATE, date.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, date.getHours());
        calendar.set(Calendar.MINUTE, date.getMinutes());
        calendar.set(Calendar.SECOND, 0);

        Intent Intent = new Intent(AddScheduleActivity.this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        Log.e("알람 울림", "errrrrrrrrrrrrr");
    }

    // 알람 해제
    private void releaseAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent(AddScheduleActivity.this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);
        alarmManager.cancel(pIntent);
    }

}