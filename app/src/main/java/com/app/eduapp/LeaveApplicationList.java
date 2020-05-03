package com.app.eduapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.adapter.LeaveApplicationApapter;
import com.app.eduapp.databinding.LeaveApplistIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.LeaveApplicationListEMP;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaveApplicationList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    LeaveApplistIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    LeaveApplicationApapter monthListApapter;
    ProgressDialog pDialog;
    LeaveApplicationListEMP leaveApplicationListEMP;
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
        binding.leaveAppactivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.leaveAppactivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }


    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.leave_applist_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        binding.leaveAppactivity.toolbar.btnMenu.setOnClickListener(this);
        binding.leaveAppactivity.toolbar.tvChild.setText("Leave Application");
        binding.leaveAppactivity.toolbar.cartView.setOnClickListener(this);
    }

    private void setuplistview() {
        binding.leaveAppactivity.tvCount0.setSolidColor("#ffffff");
        binding.leaveAppactivity.tvCount1.setSolidColor("#ffffff");
        binding.leaveAppactivity.tvCount3.setSolidColor("#ffffff");
        binding.leaveAppactivity.tvCount0.setTextColor(Color.parseColor("#000000"));
        binding.leaveAppactivity.tvCount1.setTextColor(Color.parseColor("#000000"));
        binding.leaveAppactivity.tvCount3.setTextColor(Color.parseColor("#000000"));
        binding.leaveAppactivity.tvCount0.setText(leaveApplicationListEMP.getData().getLeaveApplicationList().getMyLeaveApplicationCount());
        binding.leaveAppactivity.tvCount1.setText(leaveApplicationListEMP.getData().getLeaveApplicationList().getStudentLeaveApplicationCount());

        binding.leaveAppactivity.rlvLeave.setOnClickListener(this);
        binding.leaveAppactivity.rlvLeave1.setOnClickListener(this);
        binding.leaveAppactivity.rlvLeave11.setOnClickListener(this);
        if (EdUtils.get_isPrinciple(mContext).equalsIgnoreCase("Y")) {
            binding.leaveAppactivity.rlvLeave11.setVisibility(View.VISIBLE);
            binding.leaveAppactivity.tvCount3.setText(leaveApplicationListEMP.getData().getLeaveApplicationList().getEmployeeLeaveApplicationCount());
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
        if (v == binding.leaveAppactivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.leaveAppactivity.rlvLeave) {
            Intent i = new Intent(mContext, MyLeaveListActivity.class);
            startActivity(i);
        } else if (v == binding.leaveAppactivity.rlvLeave1) {
            Intent i = new Intent(mContext, AllLeaveListActivity.class);
            startActivity(i);
        } else if (v == binding.leaveAppactivity.rlvLeave11) {
            Intent i = new Intent(mContext, EmployeeLeaveListActivity.class);
            startActivity(i);
        } else if (v == binding.leaveAppactivity.toolbar.cartView) {
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
        Call<LeaveApplicationListEMP> call = redditAPI.getLeaveApplicationListEMP(EdUtils.get_userID(mContext));
        call.enqueue(new Callback<LeaveApplicationListEMP>() {

            @Override
            public void onResponse(Call<LeaveApplicationListEMP> call, retrofit2.Response<LeaveApplicationListEMP> response) {
                Log.d("Response", response.toString());
                if (response.isSuccessful()) {
                    leaveApplicationListEMP = response.body();
                    if (leaveApplicationListEMP.getStatus().equalsIgnoreCase("1")) {
                        setuplistview();
                    } else {
                        EdUtils.showToastShort(mContext, leaveApplicationListEMP.getMessage());
                    }

                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LeaveApplicationListEMP> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


}
