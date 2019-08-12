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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.adapter.AllStuLeaveListApapter;
import com.app.eduapp.adapter.EmpLeaveListApapter;
import com.app.eduapp.databinding.LeaveActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.AllLeaveApplicationListEMP;
import com.app.eduapp.pojo.EmpLeaveApplicationList;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeLeaveListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    LeaveActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    EmpLeaveListApapter monthListApapter;
    ProgressDialog pDialog;
    EmpLeaveApplicationList leave;
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
        db = new LCDatabaseHandler(mContext);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
        if (cd.isConnected()) {
            getLeaavetList();
        }
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.leaveActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.leaveActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.leave_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        binding.leaveActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.leaveActivity.toolbar.tvChild.setText("Leave Application");
        binding.leaveActivity.tvLabel.setText("Employee Application");
        binding.leaveActivity.btnLeave.setVisibility(View.GONE);
        binding.leaveActivity.rcvLeavelist.setLayoutManager(new LinearLayoutManager(this));
        binding.leaveActivity.rcvLeavelist.setItemAnimator(new DefaultItemAnimator());
        binding.leaveActivity.btnLeave.setOnClickListener(this);
        binding.leaveActivity.toolbar.cartView.setOnClickListener(this);

    }

    private void setuplistview() {
        if (leave.getData().getStudentLeaveApplicationList().size() > 0) {
            monthListApapter = new EmpLeaveListApapter(mContext, R.layout.row_leave, leave.getData().getStudentLeaveApplicationList(), "MyLeaveListActivity");
            binding.leaveActivity.rcvLeavelist.setAdapter(monthListApapter);
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
        if (v == binding.leaveActivity.btnLeave) {
            Intent i = new Intent(mContext, ApplyLeave.class);
            startActivity(i);
        } else if (v == binding.leaveActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.leaveActivity.toolbar.cartView) {
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
        Call<EmpLeaveApplicationList> call = redditAPI.getEmployeeLeaveApplicationListEMP(EdUtils.get_userID(mContext));
        call.enqueue(new Callback<EmpLeaveApplicationList>() {

            @Override
            public void onResponse(Call<EmpLeaveApplicationList> call, retrofit2.Response<EmpLeaveApplicationList> response) {
                Log.d("Response", response.toString());
                if (response.isSuccessful()) {
                    leave = response.body();
                    if (leave.getStatus().equalsIgnoreCase("1")) {
                        if (leave.getData().getStudentLeaveApplicationList().size() > 0) {
                            setuplistview();
                        }
                    } else {
                        EdUtils.showToastShort(mContext, leave.getMessage());
                    }

                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EmpLeaveApplicationList> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


}
