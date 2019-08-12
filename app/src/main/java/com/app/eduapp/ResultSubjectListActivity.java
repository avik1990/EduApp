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

import com.app.eduapp.adapter.ResultSubjectsListAdapter;
import com.app.eduapp.common.RecyclerViewMargin;
import com.app.eduapp.databinding.ActivityRoutineListIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.ResultSubjectsList;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultSubjectListActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ResultSubjectsListAdapter adapter;
    ActivityRoutineListIncBinding binding;
    Context mContext;
    String ExamID;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    ResultSubjectsList resultSubjectsList;
    String sectionid = "", classid = "", studentid = "";
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
        db = new LCDatabaseHandler(mContext);
        ExamID = getIntent().getExtras().getString("ExamID");
        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            sectionid = getIntent().getExtras().getString("SectionID");
            classid = getIntent().getExtras().getString("ClassID");
            studentid = getIntent().getExtras().getString("studentid");
        } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
            sectionid = EdUtils.get_sectionID(mContext);
            classid = EdUtils.get_classID(mContext);
            studentid = EdUtils.get_studentID(mContext);
        }
        initView();

        if (cd.isConnected()) {
            pDialog.show();
            getResultSubjectsList();
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
        binding.activityRoutineList.className.setVisibility(View.GONE);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityRoutineList.toolbar.btnMenu.setOnClickListener(this);
        binding.activityRoutineList.toolbar.cartView.setOnClickListener(this);
        binding.activityRoutineList.stuSection.setVisibility(View.VISIBLE);
        binding.activityRoutineList.stuName.setVisibility(View.VISIBLE);
    }

    private void showList() {
        binding.activityRoutineList.examType.setText(resultSubjectsList.getExamTitle());
        adapter = new ResultSubjectsListAdapter(this, resultSubjectsList.getData().getResultSubjectsList());
        binding.activityRoutineList.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMargin decoration = new RecyclerViewMargin(20, resultSubjectsList.getData().getResultSubjectsList().size());
        binding.activityRoutineList.recyclerView.addItemDecoration(decoration);
        binding.activityRoutineList.recyclerView.setAdapter(adapter);
        binding.activityRoutineList.stuName.setText(resultSubjectsList.getStudentsName());
        binding.activityRoutineList.stuSection.setText(resultSubjectsList.getClassName() + " Section " + resultSubjectsList.getSectionName());
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

    private void getResultSubjectsList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ResultSubjectsList> call = redditAPI.getResultSubjectsList(ExamID, studentid, classid, sectionid);
        call.enqueue(new Callback<ResultSubjectsList>() {

            @Override
            public void onResponse(Call<ResultSubjectsList> call, retrofit2.Response<ResultSubjectsList> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    resultSubjectsList = response.body();
                    if (resultSubjectsList.getStatus().equalsIgnoreCase("1")) {
                        if (resultSubjectsList.getData().getResultSubjectsList().size() > 0) {
                            showList();
                        }
                    } else {
                        EdUtils.showToastShort(mContext, resultSubjectsList.getMessage());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultSubjectsList> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
