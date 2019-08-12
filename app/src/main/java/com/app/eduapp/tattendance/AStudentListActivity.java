package com.app.eduapp.tattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.NotificationListActivity;
import com.app.eduapp.R;
import com.app.eduapp.adapter.AStudentListApapter;
import com.app.eduapp.databinding.StudentlistActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.StudentsListByClassSectionEMP;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AStudentListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    StudentlistActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    AStudentListApapter studentListApapter;
    ProgressDialog pDialog;
    StudentsListByClassSectionEMP studentsListByClassSectionEMP;
    String ClassID, SectionID;
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        db = new LCDatabaseHandler(mContext);

        ClassID=getIntent().getExtras().getString("ClassID");
        SectionID=getIntent().getExtras().getString("SectionID");
        

        initView();

        if (cd.isConnected()) {
            pDialog.show();
            getStudentList();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.studentlistActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.studentlistActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void getStudentList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<StudentsListByClassSectionEMP> call = redditAPI.getStudentsListByClassSectionEMP(ClassID, SectionID);
        call.enqueue(new Callback<StudentsListByClassSectionEMP>() {

            @Override
            public void onResponse(Call<StudentsListByClassSectionEMP> call, retrofit2.Response<StudentsListByClassSectionEMP> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    studentsListByClassSectionEMP = response.body();
                    if (!studentsListByClassSectionEMP.getStatus().equalsIgnoreCase("0")) {
                        inflateStudentView();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StudentsListByClassSectionEMP> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void inflateStudentView() {
        if (studentsListByClassSectionEMP.getData().getStudentsList().size() > 0) {
            studentListApapter = new AStudentListApapter(mContext, R.layout.row_studentlist, studentsListByClassSectionEMP.getData().getStudentsList(), "MonthListActivity",studentsListByClassSectionEMP.getClassName()+" Section "+studentsListByClassSectionEMP.getSectionName());
            binding.studentlistActivity.rcvStudentlist.setAdapter(studentListApapter);
        }
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.studentlist_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.studentlistActivity.toolbar.tvChild.setText("View Attendance");
        binding.studentlistActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.navView.setItemIconTintList(null);
        binding.studentlistActivity.rcvStudentlist.setLayoutManager(new LinearLayoutManager(this));
        binding.studentlistActivity.rcvStudentlist.setItemAnimator(new DefaultItemAnimator());
        binding.studentlistActivity.toolbar.cartView.setOnClickListener(this);
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
        if (v == binding.studentlistActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        }else if (v == binding.studentlistActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }


}
