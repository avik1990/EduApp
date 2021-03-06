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

import com.app.eduapp.adapter.StudentListApapter;
import com.app.eduapp.databinding.StudentlistActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.MapUtils;
import com.app.eduapp.pojo.GetStudentsList;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    StudentlistActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    StudentListApapter studentListApapter;
    ProgressDialog pDialog;
    GetStudentsList getStudentsList;
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
        db=new LCDatabaseHandler(mContext);

        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
        if (cd.isConnected()) {
            pDialog.show();
            getMonthList();
        }
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.studentlistActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.studentlistActivity.toolbar.tvCartcount.setText(String.valueOf(count));
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
        Call<GetStudentsList> call = redditAPI.getStudentsList(EdUtils.get_userID(mContext));
        call.enqueue(new Callback<GetStudentsList>() {

            @Override
            public void onResponse(Call<GetStudentsList> call, retrofit2.Response<GetStudentsList> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getStudentsList = response.body();
                    if (getStudentsList.getData().getStudentsList().size() > 0) {
                        setuplistview();
                    }
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetStudentsList> call, Throwable t) {
                Log.d("Error", t.getMessage());
                pDialog.dismiss();
            }
        });
    }


    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.studentlist_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.studentlistActivity.toolbar.tvChild.setText("Student Profile");
        binding.studentlistActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.navView.setItemIconTintList(null);
        binding.studentlistActivity.rcvStudentlist.setLayoutManager(new LinearLayoutManager(this));
        binding.studentlistActivity.rcvStudentlist.setItemAnimator(new DefaultItemAnimator());
        binding.studentlistActivity.toolbar.cartView.setOnClickListener(this);
    }

    private void setuplistview() {
        MapUtils.list_student.clear();
        if (getStudentsList.getData().getStudentsList().size() > 0) {
            MapUtils.list_student = getStudentsList.getData().getStudentsList();
            studentListApapter = new StudentListApapter(mContext, R.layout.row_studentlist, getStudentsList.getData().getStudentsList(), "MonthListActivity");
            binding.studentlistActivity.rcvStudentlist.setAdapter(studentListApapter);
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
        if (v == binding.studentlistActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        }else if (v == binding.studentlistActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }
}
