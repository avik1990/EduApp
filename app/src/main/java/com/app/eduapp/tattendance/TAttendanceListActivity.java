package com.app.eduapp.tattendance;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.app.eduapp.R;
import com.app.eduapp.adapter.AttendanceListApapter;
import com.app.eduapp.databinding.AttendanceActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.GetMonthlyAttendance;
import com.app.eduapp.pojo.Months;
import com.app.eduapp.retrofit.api.ApiServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TAttendanceListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    AttendanceActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    List<Months.MonthsList> list_month = new ArrayList<>();
    List<GetMonthlyAttendance.MonthlyAttendanceList> list_attend = new ArrayList<>();
    AttendanceListApapter monthListApapter;
    String position = "0";
    int increment = 0;
    ProgressDialog pDialog;
    Months getMonthsList;
    GetMonthlyAttendance getMonthlyAttendance;
    String YearMonth = "", studentid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        position = getIntent().getStringExtra("position");
        studentid = getIntent().getStringExtra("studentid");

        increment = Integer.parseInt(position);

        initView();

        if (cd.isConnected()) {
            pDialog.show();
            getMonthList();
        }
    }


    private void getMonthList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Months> call = redditAPI.getMonthLists();
        call.enqueue(new Callback<Months>() {

            @Override
            public void onResponse(Call<Months> call, retrofit2.Response<Months> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getMonthsList = response.body();
                    if (getMonthsList.getData().getMonthsList().size() > 0) {
                        list_month.clear();
                        list_month = getMonthsList.getData().getMonthsList();
                        setmonthtext(Integer.parseInt(position));
                    }
                }
            }

            @Override
            public void onFailure(Call<Months> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void getMonthlyAttendance(String YearMonth) {
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetMonthlyAttendance> call = redditAPI.getMonthlyAttendance(studentid, YearMonth);
        call.enqueue(new Callback<GetMonthlyAttendance>() {

            @Override
            public void onResponse(Call<GetMonthlyAttendance> call, retrofit2.Response<GetMonthlyAttendance> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getMonthlyAttendance = response.body();
                    if (getMonthlyAttendance.getData().getMonthlyAttendanceList().size() > 0) {
                        list_attend.clear();
                        list_attend = getMonthlyAttendance.getData().getMonthlyAttendanceList();
                        setuplistview();
                    }
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetMonthlyAttendance> call, Throwable t) {
                pDialog.dismiss();
            }
        });

    }

    private void setmonthtext(int position) {
        binding.attendanceActivity.tvMonth.setText(list_month.get(position).getYearMonthFullName());
        YearMonth = list_month.get(position).getYearMonth();
        getMonthlyAttendance(YearMonth);
    }


    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.attendance_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.attendanceActivity.toolbar.tvChild.setText("Attendance");
        binding.attendanceActivity.rcvMonthlist.setLayoutManager(new LinearLayoutManager(this));
        binding.attendanceActivity.rcvMonthlist.setItemAnimator(new DefaultItemAnimator());
        binding.navView.setItemIconTintList(null);
        binding.attendanceActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.attendanceActivity.ivNext.setOnClickListener(this);
        binding.attendanceActivity.ivPrevious.setOnClickListener(this);
    }


    private void setuplistview() {
        if (list_attend.size() > 0) {
            monthListApapter = new AttendanceListApapter(mContext, R.layout.row_attendlist, list_attend, "AttendanceListActivity");
            binding.attendanceActivity.rcvMonthlist.setAdapter(monthListApapter);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == binding.attendanceActivity.ivNext) {
            if (cd.isConnected()) {
                if (increment < (list_month.size() - 1)) {
                    increment++;
                    pDialog.show();
                    setmonthtext(increment);
                }
            }
        } else if (v == binding.attendanceActivity.ivPrevious) {
            if (cd.isConnected()) {
                if (increment > 0) {
                    increment--;
                    pDialog.show();
                    setmonthtext(increment);
                }
            }

        }
        if (v == binding.attendanceActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
