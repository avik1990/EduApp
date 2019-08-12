package com.app.eduapp.teacher;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;

import com.app.eduapp.Addpost;
import com.app.eduapp.NotificationListActivity;
import com.app.eduapp.R;
import com.app.eduapp.adapter.studentdiarypostlistadapter.StuPostListExpAdapter;
import com.app.eduapp.databinding.ActivityTpostIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.StudentPostList;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TDiaryPostList extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    ActivityTpostIncBinding binding;
    Context mContext;
    StuPostListExpAdapter adapterExp;
    Gson gson;
    ProgressDialog pDialog;
    StudentPostList notice;
    ConnectionDetector cd;
    String ClassID = "", SectionID = "", SubjectsID = "", DiaryID = "";
    Calendar myCalendar = Calendar.getInstance();
    String st_todate = "", st_fromdate = "";
    LCDatabaseHandler db;
    String ClassName = "", SectionName = "", SubjectsName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        db = new LCDatabaseHandler(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        gson = new Gson();

        ClassID = getIntent().getExtras().getString("ClassID");
        SectionID = getIntent().getExtras().getString("SectionID");
        SubjectsID = getIntent().getExtras().getString("SubjectsID");
        DiaryID = getIntent().getExtras().getString("DiaryID");

        ClassName = getIntent().getExtras().getString("ClassName");
        SectionName = getIntent().getExtras().getString("SectionName");
        SubjectsName = getIntent().getExtras().getString("SubjectsName");


        initView();

        st_fromdate = EdUtils.getCurrentDate1();
        st_todate = EdUtils.getCurrentDate();
        binding.activityTpost.tvFromDate.setText("From : " + st_fromdate);
        binding.activityTpost.tvToDate.setText("To : " + st_todate);
        binding.activityTpost.tvFromDay.setText(EdUtils.getDayfromdate(st_fromdate));
        binding.activityTpost.tvToDay.setText(EdUtils.getDayfromdate(st_todate));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
        if (cd.isConnected()) {
            getNoticeList();
        }
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityTpost.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityTpost.toolbar.tvCartcount.setText(String.valueOf(count));
    }

   /* ClassName = getIntent().getExtras().getString("ClassName");
    SectionName = getIntent().getExtras().getString("SectionName");
    SubjectsName*/

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tpost_inc);
        binding.activityTpost.setHandlers(this);
        binding.navView.setItemIconTintList(null);
        binding.activityTpost.btnPost.setOnClickListener(this);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityTpost.toolbar.tvChild.setText("Teacher Diary");
        binding.activityTpost.tvLabel.setText(ClassName + " Section " + SectionName + " " + SubjectsName);
        binding.activityTpost.toolbar.btnMenu.setOnClickListener(this);
        binding.activityTpost.btnPostStudentwise.setOnClickListener(this);
        binding.activityTpost.vFromdate.setOnClickListener(this);
        binding.activityTpost.vTodate.setOnClickListener(this);
        binding.activityTpost.toolbar.cartView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == binding.activityTpost.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityTpost.btnPost) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                Intent i = new Intent(mContext, TAddpost.class);
                i.putExtra("ClassID", ClassID);
                i.putExtra("SectionID", SectionID);
                i.putExtra("studentwise", "0");
                i.putExtra("SubjectsID", SubjectsID);
                i.putExtra("DiaryID", DiaryID);

                i.putExtra("ClassName", ClassName);
                i.putExtra("SectionName", SectionName);
                i.putExtra("SubjectsName", SubjectsName);
                startActivity(i);
            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                Intent i = new Intent(mContext, Addpost.class);
                i.putExtra("ClassID", ClassID);
                i.putExtra("SectionID", SectionID);
                i.putExtra("SubjectsID", SubjectsID);
                i.putExtra("DiaryID", DiaryID);
                startActivity(i);
            }
        } else if (v == binding.activityTpost.btnPostStudentwise) {
            Intent i = new Intent(mContext, TAddpost.class);
            i.putExtra("ClassID", ClassID);
            i.putExtra("SectionID", SectionID);
            i.putExtra("SubjectsID", SubjectsID);
            i.putExtra("studentwise", "1");
            i.putExtra("DiaryID", DiaryID);
            i.putExtra("ClassName", ClassName);
            i.putExtra("SectionName", SectionName);
            i.putExtra("SubjectsName", SubjectsName);
            i.putExtra("from", "normal");
            startActivity(i);

        } else if (v == binding.activityTpost.vFromdate) {
            DatePickerDialog dpd = new DatePickerDialog(TDiaryPostList.this, fromdate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            try {
                String st_fromdate = binding.activityTpost.tvToDate.getText().toString().replace("To : ", "").trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(st_fromdate);
                    long millis = date.getTime();
                    dpd.getDatePicker().setMaxDate(millis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dpd.show();
        } else if (v == binding.activityTpost.vTodate) {
            DatePickerDialog dpd = new DatePickerDialog(TDiaryPostList.this, todate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            try {
                String st_fromdate = binding.activityTpost.tvFromDate.getText().toString().replace("From : ", "").trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(st_fromdate);
                    long millis = date.getTime();
                    dpd.getDatePicker().setMinDate(millis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dpd.show();
        } else if (v == binding.activityTpost.toolbar.cartView) {
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

    private void inflateadapter() {
        binding.activityTpost.tvLabel.setText(notice.getClassName() + " Section " + notice.getSectionName() + " " + notice.getSubjectsName());
        if (notice.getData().getDiaryData().size() > 0) {
            binding.activityTpost.rvRecycle.setVisibility(View.VISIBLE);
            adapterExp = new StuPostListExpAdapter(mContext, notice.getData().getDiaryData());
            binding.activityTpost.rvRecycle.setLayoutManager(new LinearLayoutManager(this));
            binding.activityTpost.rvRecycle.setAdapter(adapterExp);
        } else {
            binding.activityTpost.rvRecycle.setVisibility(View.GONE);
            EdUtils.showToastShort(mContext, "No Notice Found");
        }
    }

    DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String years;
            String month1 = "";
            years = "" + year;
            int month = monthOfYear;
            month = month + 1;
            if (month < 10) {
                month1 = "0" + month;
            } else {
                month1 = String.valueOf(month);
            }
            String strDateTime = dayOfMonth + "/" + month1 + "/" + years;
            st_fromdate = strDateTime;
            binding.activityTpost.tvFromDate.setText("From : " + strDateTime);
            binding.activityTpost.tvFromDay.setText(EdUtils.getDayfromdate(strDateTime));
            if (cd.isConnected()) {
                getNoticeList();
            }
        }
    };
    DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String years;
            String month1 = "";
            years = "" + year;
            int month = monthOfYear;
            month = month + 1;
            if (month < 10) {
                month1 = "0" + month;
            } else {
                month1 = String.valueOf(month);
            }
            String strDateTime = dayOfMonth + "/" + month1 + "/" + years;
            st_todate = strDateTime;
            binding.activityTpost.tvToDate.setText("To : " + strDateTime);
            binding.activityTpost.tvToDay.setText(EdUtils.getDayfromdate(strDateTime));
            if (cd.isConnected()) {
                getNoticeList();
            }
        }
    };

    private void getNoticeList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<StudentPostList> call = redditAPI.getDiaryListBySubjectsEMP(EdUtils.get_userID(mContext), ClassID, SectionID, SubjectsID, st_fromdate.replaceAll("/", "-"), st_todate.replaceAll("/", "-"));
        call.enqueue(new Callback<StudentPostList>() {

            @Override
            public void onResponse(Call<StudentPostList> call, retrofit2.Response<StudentPostList> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    notice = response.body();
                    if (notice.getStatus().equalsIgnoreCase("1")) {
                        binding.activityTpost.rvRecycle.setVisibility(View.VISIBLE);
                        inflateadapter();
                    } else {
                        EdUtils.showToastShort(mContext, notice.getMessage());
                        binding.activityTpost.rvRecycle.setVisibility(View.GONE);
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StudentPostList> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
