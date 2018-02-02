package com.example.acn0036.schedulertest.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.acn0036.schedulertest.Data.DatabaseHelper;
import com.example.acn0036.schedulertest.Data.MemberData;
import com.example.acn0036.schedulertest.Main.MainActivity;
import com.example.acn0036.schedulertest.R;
import com.example.acn0036.schedulertest.common.Common;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends Activity {

    private EditText mEditTextID, mEditTextPW;
    private Button mButtonLogin;
    private CheckBox mCheckBoxKeep;

    private ArrayList<MemberData> memberDataArrayList;
    private boolean mFinishFlag = false;

    private DatabaseHelper mDatabaseHelper;

    private final String mID = "asdf";
    private final String mPASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initDatabase();

        loginCheck();
    }

    private void loginCheck() {

        mEditTextID = (EditText) findViewById(R.id.editTextID);
        mEditTextPW = (EditText) findViewById(R.id.editTextPW);
        mCheckBoxKeep = (CheckBox) findViewById(R.id.checkboxKeep);
        mButtonLogin = (Button) findViewById(R.id.btn_Login);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mEditTextID.getText().toString();
                String password = mEditTextPW.getText().toString();

                Log.e("id ", "id: "+id+", password: "+password);

                memberDataArrayList = new ArrayList<>();
                memberDataArrayList = mDatabaseHelper.findMember(id);

//                if (id != null && password != null && memberDataArrayList != null && memberDataArrayList.contains(password)) {
              if (id != null && password != null && id.equals(mID) && mPASSWORD.equals(password)) {
                    if (mCheckBoxKeep.isChecked()) {
                        insertData(id, password, true);
                    } else {
                        insertData("","",false);
                    }

                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    initform();
                    finish();
                } else {
                    Common.showAlertDilog(LoginActivity.this, "로그인 실패", "ID와 PW를 확인하세요");
                    initform();
                    return;
                }

            }
        });
        mEditTextID.setText(getID());
        mEditTextPW.setText(getPW());
        mCheckBoxKeep.setChecked(getCheckBox());
    }

    private void insertData(String id, String password, Boolean checkbox) {
        SharedPreferences sharedPref = getSharedPreferences(
                "SHAREDPREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("id", id);
        editor.putString("password", password);
        editor.putBoolean("checkbox", checkbox);
        editor.commit();
    }

    private String getID() {
        SharedPreferences sharedPref = getSharedPreferences(
                "SHAREDPREFERENCES", MODE_PRIVATE);
        return sharedPref.getString("id", "");
    }

    private String getPW() {
        SharedPreferences sharedPref = getSharedPreferences(
                "SHAREDPREFERENCES", MODE_PRIVATE);
        return sharedPref.getString("password", "");
    }

    private Boolean getCheckBox() {
        SharedPreferences sharedPref = getSharedPreferences(
                "SHAREDPREFERENCES", MODE_PRIVATE);
        return sharedPref.getBoolean("checkbox", true);
    }

    private void initform() {
        mEditTextPW.setText("");
        mEditTextID.setText("");
    }

    private void initDatabase() {
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        if (mFinishFlag) {
            super.onBackPressed();
        } else {
            mFinishFlag = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mFinishFlag = false;
                    cancel();
                }
            }, 2000);
            Toast.makeText(getApplicationContext(), "백버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_LONG).show();
        }
    }
}
