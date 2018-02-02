package com.example.acn0036.schedulertest.Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.acn0036.schedulertest.AddSchedule.AddScheduleActivity;
import com.example.acn0036.schedulertest.Data.DatabaseHelper;
import com.example.acn0036.schedulertest.Data.ScheduleData;
import com.example.acn0036.schedulertest.Data.ScheduleListAdapter;
import com.example.acn0036.schedulertest.R;

public class MainActivity extends Activity {

    private TextView mDateTextView, mBackTextView, mScheduleTextView;
    private GridView mCalenderGridView;
    private ListView mDataListView;
    private Button mAddScheduleButton, mNextMonthButton, mPreMonthButton;

    private GridAdapter gridAdapter;
    private ScheduleListAdapter mScheduleListAdapter;

    private ArrayList<String> dayList;
    private ArrayList<ScheduleData> scheduleDataArrayList;
    //    private ScheduleListAdapter scheduleDataArrayList;
    private Calendar mCal;

    private DatabaseHelper mDatabaseHelper;

    private int mNullCal;


    final Date curdate = new Date(System.currentTimeMillis());
    final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
    final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

    int year = curdate.getYear();
    int month = curdate.getMonth() + 1;
    int day = curdate.getDay();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDateTextView = (TextView) findViewById(R.id.tv_date);
        mCalenderGridView = (GridView) findViewById(R.id.gridview);
        mDataListView = (ListView) findViewById(R.id.databaseschedule);
        mAddScheduleButton = (Button) findViewById(R.id.btn_addSchedule);
        mBackTextView = (TextView) findViewById(R.id.tv_back);
        mNextMonthButton = (Button) findViewById(R.id.btn_nextMonth);
        mPreMonthButton = (Button) findViewById(R.id.btn_preMonth);

        mScheduleListAdapter = new ScheduleListAdapter(getApplicationContext());
        mDataListView.setAdapter(mScheduleListAdapter);

        initDatabase();
        initLayout();

        Log.e("year", "" + year + " / " + month + " / " + day);

        //이전 달
        mPreMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month -= 1;
                if (month == 0) {
                    month = 12;
                    year -= 1;
                }
                mDateTextView.setText(year+1900 + "/" + String.format("%02d", month));
                Date date = new Date(year, month-1, day);
                Log.e("curdate" , date+"");
                makeCalendar(date);
            }
        });

        //다음 달
        mNextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month += 1;
                if (month > 12) {
                    month = 1;
                    year += 1;
                }
                mDateTextView.setText(year+1900 + "/" +String.format("%02d", month));
                Date date = new Date(year, month-1, day);
                Log.e("curdate" , date+"");
                makeCalendar(date);
            }
        });

        //현재 날짜
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = curdate.getYear();
                month = curdate.getMonth() + 1;
                mDateTextView.setText(year+1900 + "/" + String.format("%02d", month));
                makeCalendar(curdate);
            }
        });

        //일정 추가
        mAddScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddScheduleActivity.class);
                startActivity(intent);
            }
        });

        //일정 수정
        mDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position clicked item", "" + position);
                String[] arr = mScheduleListAdapter.getItem(position);
                for (int i = 0; i < 6; i++) {
                    Log.e("db", arr[i]);
                }

                Intent intentDB = new Intent();
                intentDB.putExtra("arr", arr);
                intentDB.setClass(MainActivity.this, AddScheduleActivity.class);
                startActivity(intentDB);
            }
        });


        mDataListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                mDatabaseHelper = new DatabaseHelper(getApplicationContext());

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("일정 삭제").setMessage("일정을 삭제하시겠습니까?");
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.e("position", "" + position);

                        String[] arr = mScheduleListAdapter.getItem(position);

                        for (int i = 0; i < 6; i++) {
                            Log.e("db", arr[i]);
                        }

                        mDatabaseHelper.deleteData(Integer.parseInt(arr[0]));
                        viewUpdateList();
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        viewUpdateList();
        super.onResume();
    }

    private void initDatabase() {
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
    }

    private void initLayout() {
        mDateTextView.setText(curYearFormat.format(curdate) + "/" + curMonthFormat.format(curdate));

        makeCalendar(curdate);

        final int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        Log.e("daynum", Integer.toString(dayNum));

        //calender 날짜 선택시 ListView에 당일 일정 DB 출력
        mCalenderGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int select = position - (mNullCal + dayNum)-5; //선택 일
                String date = curYearFormat.format(curdate)
                        + "/" + String.format("%02d", month)
                        + "/" + String.format("%02d", select); //선택 날짜

                if (select < 1) { //요일 표시 부분 선택시 return
                    mCalenderGridView.setFocusable(false);
                    return;
                }

                Log.e("date", date);

                updateList(date);

            }
        });

    }

    //스케줄이 있는 날짜 표시 //todo
    private void setSchedule() {
        for(int i = 7; i<38; i++) {
            try {
                String date = curYearFormat.format(curdate)
                        + "/" + String.format("%02d", month)
                        + "/" + String.format("%02d", Integer.parseInt(gridAdapter.getItem(i)));
                ArrayList<ScheduleData> scheduleDataArrayList = mDatabaseHelper.selectDay(date);
                if (scheduleDataArrayList != null && scheduleDataArrayList.size() > 0) {
                    Log.e("=============", "요기에는 데이터가 있당~~");
//                    mScheduleTextView.setVisibility(View.VISIBLE);
                }
            }catch (NumberFormatException e) {}
        }
    }

    private void viewUpdateList() {
        Intent intentDate = getIntent();
        String date = intentDate.getStringExtra("date");
        if (date != null) {
            Log.e("resume date", date);
            updateList(date);
        }
    }

    //달력 생성
    private void makeCalendar(Date date) {
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        mNullCal = 0;

        //시작 요일
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        final int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
            mNullCal++;
        }

        //일 수
        setCalendarDate(mCal.get(Calendar.MONTH)+1);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        mCalenderGridView.setAdapter(gridAdapter);

        setSchedule();
    }

    //1일 이전 공백 추가
    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }
    }

    //일정 ListView Update
    private void updateList(final String date) {
        mBackTextView.post(new Runnable() {
            @Override
            public void run() {
                scheduleDataArrayList = mDatabaseHelper.selectDay(date);
                if (scheduleDataArrayList != null && scheduleDataArrayList.size() > 0) {
                    mDataListView.setVisibility(View.VISIBLE);
                    mBackTextView.setVisibility(View.GONE);
                    mScheduleListAdapter.setScheduleArrayList(scheduleDataArrayList);

                } else { //db에 일정이 없는 경우
                    mDataListView.setVisibility(View.GONE);
                    mBackTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
