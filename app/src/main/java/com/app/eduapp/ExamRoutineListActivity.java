package com.app.eduapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.adapter.ExamRoutineAdapter;
import com.app.eduapp.common.RecyclerViewMargin;
import com.app.eduapp.databinding.ActivityRoutineListIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.ExamSubjects;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExamRoutineListActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ExamRoutineAdapter adapter;
    ActivityRoutineListIncBinding binding;
    Context mContext;
    String ExamID;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    ExamSubjects examSubjects;
    String ClassID = "", SectionID="";
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_list_inc);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        db=new LCDatabaseHandler(mContext);

        ExamID = getIntent().getExtras().getString("ExamID");
        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            ClassID = getIntent().getExtras().getString("ClassID");
            SectionID = getIntent().getExtras().getString("SectionID");
        } else {
            ClassID = EdUtils.get_classID(mContext);
            SectionID = EdUtils.get_sectionID(mContext);
        }


        initView();

        if (cd.isConnected()) {
            pDialog.show();
            getExamSubjectsList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityRoutineList.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityRoutineList.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding.activityRoutineList.toolbar.tvChild.setText("Exam");
        binding.navView.setItemIconTintList(null);
        binding.activityRoutineList.stuSection.setVisibility(View.GONE);
        binding.activityRoutineList.stuName.setVisibility(View.GONE);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityRoutineList.toolbar.btnMenu.setOnClickListener(this);
        binding.activityRoutineList.toolbar.cartView.setOnClickListener(this);
    }

    private void showList() {
        binding.activityRoutineList.examType.setText(examSubjects.getExamTitle());
        binding.activityRoutineList.className.setText(examSubjects.getClassName() + " Section " + examSubjects.getSectionName());
        adapter = new ExamRoutineAdapter(this, examSubjects.getData().getExamSubjectsList());
        binding.activityRoutineList.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMargin decoration = new RecyclerViewMargin(20, examSubjects.getData().getExamSubjectsList().size());
        binding.activityRoutineList.recyclerView.addItemDecoration(decoration);
        binding.activityRoutineList.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        if (v == binding.activityRoutineList.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityRoutineList.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getExamSubjectsList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ExamSubjects> call = redditAPI.getExamSubjectsList(ExamID, ClassID, SectionID);
        call.enqueue(new Callback<ExamSubjects>() {

            @Override
            public void onResponse(Call<ExamSubjects> call, retrofit2.Response<ExamSubjects> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    examSubjects = response.body();
                    if (examSubjects.getStatus().equalsIgnoreCase("1")) {
                        if (examSubjects.getData().getExamSubjectsList().size() > 0) {
                            showList();
                        }
                    } else {
                        EdUtils.showToastShort(mContext, examSubjects.getMessage());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ExamSubjects> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
