package com.example.acn0036.schedulertest.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ACN0036 on 2017-09-18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String NAME_DATABASE = "database.db"; //데이터베이스 이름
    private final static int VERSION_DATABASE = 1;
    private final static String NAME_TABLE = "SCHEDULE_TABLE";
    private final static String NAME_TABLE_MEMBER = "MEMBER_TABLE";

    private final static String COLUMN_ID = "id";
    private final static String COLUMN_TITLE = "title";
    private final static String COLUMN_DATE = "date";
    private final static String COLUMN_TIME = "time";
    private final static String COLUMN_LOCATION = "location";
    private final static String COLUMN_MEMO = "memo";

    private final static String COLUMN_MEMBER_NUM = "number";
    private final static String COLUMN_MEMBER_ID = "id";
    private final static String COLUMN_MEMBER_PW = "password";


    private final static String QUERY_CREATE =
            "CREATE TABLE " + NAME_TABLE + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_TITLE + " TEXT, "
                    + COLUMN_DATE + " TEXT, "
                    + COLUMN_TIME + " TEXT, "
                    + COLUMN_LOCATION + " TEXT, "
                    + COLUMN_MEMO + " TEXT)";

    private final static String QUERY_CREATE_MEMBER =
            "CREATE TABLE " + NAME_TABLE_MEMBER + "("
                    + COLUMN_MEMBER_NUM + " integer primary key autoincrement, "
                    + COLUMN_MEMBER_ID + " TEXT, "
                    + COLUMN_MEMBER_PW + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, NAME_DATABASE, null, VERSION_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE);
        db.execSQL(QUERY_CREATE_MEMBER);
        Log.e("테이블 생성", NAME_DATABASE);

        String sql = "INSERT INTO " + NAME_TABLE_MEMBER + " (" + COLUMN_MEMBER_ID + ", " + COLUMN_MEMBER_PW + ")"
                + " VALUES ('" + "asdf" + "', '" + "1234" + "')";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + NAME_TABLE);
    }

    public void insertData(String title, String date, String time, String location, String memo) {
        String sql = "INSERT INTO " + NAME_TABLE + " (" + COLUMN_TITLE + ", " + COLUMN_DATE + ", " + COLUMN_TIME + ", " + COLUMN_LOCATION + ", " + COLUMN_MEMO + ")"
                + " VALUES ('" + title + "', '" + date + "', '" + time + "', '" + location + "', '" + memo + "')";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void updateData(String id, String title, String date, String time, String location, String memo) {
        String sql = "UPDATE " + NAME_TABLE
                + " SET " + COLUMN_TITLE + "='" + title + "', "
                + COLUMN_DATE + "='" + date + "', "
                + COLUMN_TIME + "='" + time + "', "
                + COLUMN_LOCATION + "='" + location + "', "
                + COLUMN_MEMO + "='" + memo
                + "' WHERE " + COLUMN_ID + "='" + id + "'";
        Log.e("Update sql", sql);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void deleteData(int id) {
        String sql = "DELETE FROM " + NAME_TABLE
                + " WHERE " + COLUMN_ID + "='" + id + "'";
        SQLiteDatabase db = getWritableDatabase();
        Log.e("Delete SQL", sql);
        db.execSQL(sql);
        db.close();
    }


    public ArrayList<ScheduleData> selectDay(String date) {
        ArrayList<ScheduleData> valueArrayList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + NAME_TABLE + " WHERE " + COLUMN_DATE + "='" + date + "'";
        Log.e("sql", "sql :: " + sql);

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                Log.e("cursor", "cursor while");
                ScheduleData ScheduleData = new ScheduleData();
                ScheduleData.setID(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                ScheduleData.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                ScheduleData.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                ScheduleData.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                ScheduleData.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
                ScheduleData.setMemo(cursor.getString(cursor.getColumnIndex(COLUMN_MEMO)));
                valueArrayList.add(ScheduleData);
            } while (cursor.moveToNext());

            Log.e("valueArrayList size", "valueArrayList size :: " + valueArrayList.size());
        }
        return valueArrayList;
    }

    public ArrayList<ScheduleData> selectAll() {
        ArrayList<ScheduleData> valueArrayList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + NAME_TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        do {
            ScheduleData ScheduleData = new ScheduleData();
            ScheduleData.setID(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            ScheduleData.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            ScheduleData.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            ScheduleData.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
            ScheduleData.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
            ScheduleData.setMemo(cursor.getString(cursor.getColumnIndex(COLUMN_MEMO)));
            valueArrayList.add(ScheduleData);
        } while (cursor.moveToNext());
        return valueArrayList;
    }

    public ArrayList<MemberData> findMember(String id) {
        ArrayList<MemberData> valueArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + NAME_TABLE_MEMBER + " WHERE " + COLUMN_MEMBER_ID + "='" + id + "'";

        Log.e("sql", "sql :: " + sql);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        do {
            MemberData ScheduleData = new MemberData();
            ScheduleData.setID(cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)));
            ScheduleData.setPassWord(cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_PW)));
            valueArrayList.add(ScheduleData);
        } while (cursor.moveToNext());
        return valueArrayList;
    }

}

