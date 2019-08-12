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

import com.app.eduapp.adapter.ResultExamAdapter;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.common.RecyclerViewMargin;
import com.app.eduapp.databinding.ActivityExamHeldListIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.ExamResultListBean;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ExamsHeldListActivity extends AppCompatActivity implements ClickEventLisener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ResultExamAdapter adapter;
    ActivityExamHeldListIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    ExamResultListBean resultSubjectsList;
    String sectionid = "", classid = "", studentid = "";
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            sectionid = getIntent().getExtras().getString("sectionid");
            classid = getIntent().getExtras().getString("classid");
            studentid = getIntent().getExtras().getString("studentid");
        } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
            sectionid = EdUtils.get_sectionID(mContext);
            classid = EdUtils.get_classID(mContext);
            studentid = EdUtils.get_studentID(mContext);
        }
        db = new LCDatabaseHandler(mContext);
        initView();
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

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
        binding.activityExamsHeldList.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityExamsHeldList.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_held_list_inc);
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityExamsHeldList.toolbar.btnMenu.setOnClickListener(this);
        binding.activityExamsHeldList.toolbar.tvChild.setText("Result");
        binding.activityExamsHeldList.toolbar.cartView.setOnClickListener(this);
    }

    @Override
    public void clickTrigger(View v, int position) {
        Intent intent = new Intent(mContext, ResultSubjectListActivity.class);
        intent.putExtra("ExamID", resultSubjectsList.getData().getExamResultList().get(position).getExamID());
        intent.putExtra("ClassID", classid);
        intent.putExtra("SectionID", sectionid);
        intent.putExtra("studentid", studentid);
        startActivity(intent);
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
        if (v == binding.activityExamsHeldList.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityExamsHeldList.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
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
        Call<ExamResultListBean> call = redditAPI.getExamResultList(studentid, classid, sectionid);
        call.enqueue(new Callback<ExamResultListBean>() {

            @Override
            public void onResponse(Call<ExamResultListBean> call, retrofit2.Response<ExamResultListBean> response) {
                Log.d("response", "" + response);
                if (response.isSuccessful()) {
                    resultSubjectsList = response.body();
                    if (resultSubjectsList.getData().getExamResultList().size() > 0) {
                        showList();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ExamResultListBean> call, Throwable t) {
                pDialog.dismiss();
            }
        });

    }

    private void showList() {
        adapter = new ResultExamAdapter(this, resultSubjectsList.getData().getExamResultList(), this);
        binding.activityExamsHeldList.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMargin decoration = new RecyclerViewMargin(15, resultSubjectsList.getData().getExamResultList().size());
        binding.activityExamsHeldList.recyclerView.addItemDecoration(decoration);
        binding.activityExamsHeldList.recyclerView.setAdapter(adapter);
    }


}
