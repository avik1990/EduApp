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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.adapter.MonthListApapter;
import com.app.eduapp.databinding.MonthlistActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.Months;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonthListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    MonthlistActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    MonthListApapter monthListApapter;
    ProgressDialog pDialog;
    Months getMonthsList;
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog=new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        db=new LCDatabaseHandler(mContext);
        initView();

        if(cd.isConnected()){
            pDialog.show();
            getMonthList();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.monthlistActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.monthlistActivity.toolbar.tvCartcount.setText(String.valueOf(count));
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
                    if(getMonthsList.getData().getMonthsList().size()>0) {
                        setuplistview();
                    }
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Months> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.monthlist_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.monthlistActivity.toolbar.tvChild.setText("View Attendance");
        binding.monthlistActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.navView.setItemIconTintList(null);
        binding.monthlistActivity.rcvMonthlist.setLayoutManager(new LinearLayoutManager(this));
        binding.monthlistActivity.rcvMonthlist.setItemAnimator(new DefaultItemAnimator());
        binding.monthlistActivity.toolbar.cartView.setOnClickListener(this);
    }

    private void setuplistview() {
        if (getMonthsList.getData().getMonthsList().size() > 0) {
            monthListApapter = new MonthListApapter(mContext, R.layout.row_monthlist, getMonthsList.getData().getMonthsList(), "MonthListActivity");
            binding.monthlistActivity.rcvMonthlist.setAdapter(monthListApapter);
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
        if (v == binding.monthlistActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        }else if (v == binding.monthlistActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }


}
