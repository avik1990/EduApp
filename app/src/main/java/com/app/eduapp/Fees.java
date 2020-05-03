package com.app.eduapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.databinding.ActivityFeesIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.FeesReports;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fees extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ActivityFeesIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    FeesReports feesReports;
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
        initview();

        if (cd.isConnected()) {
            pDialog.show();
            getFeesReportsByStudents();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityFees.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityFees.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fees_inc);
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityFees.toolbar.btnMenu.setOnClickListener(this);
        binding.activityFees.toolbar.tvChild.setText("Fees Status");
        binding.activityFees.toolbar.btnMenu.setOnClickListener(this);
        binding.activityFees.toolbar.cartView.setOnClickListener(this);
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
        if (v == binding.activityFees.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityFees.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    private void getFeesReportsByStudents() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<FeesReports> call = redditAPI.getFeesReportsByStudents(EdUtils.get_studentID(mContext));
        call.enqueue(new Callback<FeesReports>() {

            @Override
            public void onResponse(Call<FeesReports> call, retrofit2.Response<FeesReports> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    feesReports = response.body();
                    if (feesReports.getStatus().equalsIgnoreCase("1")) {
                        showdata();
                    }
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<FeesReports> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void showdata() {
        binding.activityFees.tvPaidtill.setText("Fees Paid Till :" + feesReports.getData().getFeesReportsDetails().getFeesPaidTillMonthDate());
        binding.activityFees.tvMsg.setText("Fees Paid :" + feesReports.getData().getFeesReportsDetails().getIsAnnualFeespaid());

    }

}
