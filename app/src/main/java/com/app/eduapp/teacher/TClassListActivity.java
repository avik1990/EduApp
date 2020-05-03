package com.app.eduapp.teacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.ApplyLeave;
import com.app.eduapp.NotificationListActivity;
import com.app.eduapp.R;
import com.app.eduapp.adapter.TClassListApapter;
import com.app.eduapp.databinding.StudentdiaryActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.TDiaryClass;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TClassListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    StudentdiaryActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    TClassListApapter tDiaryListApapter;
    ProgressDialog pDialog;
    TDiaryClass studentDiary;
    String from = "";
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        db=new LCDatabaseHandler(mContext);
        from = getIntent().getExtras().getString("from");

        initView();

        if (cd.isConnected()) {
            getLeaavetList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.studentdiaryActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.studentdiaryActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.studentdiary_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        binding.studentdiaryActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.studentdiaryActivity.rcvLeavelist.setLayoutManager(new LinearLayoutManager(this));
        binding.studentdiaryActivity.rcvLeavelist.setItemAnimator(new DefaultItemAnimator());
        binding.studentdiaryActivity.btnLeave.setOnClickListener(this);
        binding.studentdiaryActivity.toolbar.cartView.setOnClickListener(this);

        binding.navView.setItemIconTintList(null);
        if (from.equalsIgnoreCase("exam")) {
            binding.studentdiaryActivity.tvLabel.setText("Exam");
            binding.studentdiaryActivity.toolbar.tvChild.setText("Exam");
        } else if (from.equalsIgnoreCase("result")) {
            binding.studentdiaryActivity.tvLabel.setText("Student Result");
            binding.studentdiaryActivity.toolbar.tvChild.setText("Class Result");
        } else if (from.equalsIgnoreCase("diary")) {
            binding.studentdiaryActivity.tvLabel.setText("Diary");
            binding.studentdiaryActivity.toolbar.tvChild.setText("Diary");
        } else if (from.equalsIgnoreCase("routine")) {
            binding.studentdiaryActivity.tvLabel.setText("Teacher Schedule");
            binding.studentdiaryActivity.toolbar.tvChild.setText("Class Schedule");
        }else if (from.equalsIgnoreCase("routinetype")) {
            binding.studentdiaryActivity.tvLabel.setText("Student Schedule");
            binding.studentdiaryActivity.toolbar.tvChild.setText("Class Schedule");
        }



    }

    private void setuplistview() {
        if (studentDiary.getData().getDiaryListArr().size() > 0) {
            tDiaryListApapter = new TClassListApapter(mContext, R.layout.row_studentdiary, studentDiary.getData().getDiaryListArr(), from);
            binding.studentdiaryActivity.rcvLeavelist.setAdapter(tDiaryListApapter);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.studentdiaryActivity.btnLeave) {
            Intent i = new Intent(mContext, ApplyLeave.class);
            startActivity(i);
        } else if (v == binding.studentdiaryActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.studentdiaryActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    private void getLeaavetList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<TDiaryClass> call = redditAPI.getClassSectionListEMP(EdUtils.get_userID(mContext));
        call.enqueue(new Callback<TDiaryClass>() {

            @Override
            public void onResponse(Call<TDiaryClass> call, retrofit2.Response<TDiaryClass> response) {
                if (response.isSuccessful()) {
                    Log.d("String", "" + response);
                    studentDiary = response.body();
                    if (studentDiary.getData().getDiaryListArr().size() > 0) {
                        setuplistview();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TDiaryClass> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

}
