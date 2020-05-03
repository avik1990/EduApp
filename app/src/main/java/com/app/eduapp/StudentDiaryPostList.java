package com.app.eduapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.app.eduapp.adapter.studentdiarypostlistadapter.StuPostListExpAdapter;
import com.app.eduapp.databinding.ActivityStudiarypostIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.StudentPostList;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.app.eduapp.teacher.TAddpost;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentDiaryPostList extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    ActivityStudiarypostIncBinding binding;
    Context mContext;
    StuPostListExpAdapter adapterExp;
    Gson gson;
    ProgressDialog pDialog;
    StudentPostList notice;
    ConnectionDetector cd;
    String ClassID = "", SectionID = "", SubjectsID = "", DiaryID = "", classname = "", subjectname = "", sectionname = "";
    Calendar myCalendar = Calendar.getInstance();
    String st_todate = "", st_fromdate = "";
    LCDatabaseHandler db;


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

        classname = getIntent().getExtras().getString("classname");
        subjectname = getIntent().getExtras().getString("subjectname");
        sectionname = getIntent().getExtras().getString("sectionname");

        initView();

        st_fromdate = EdUtils.getCurrentDate1();
        st_todate = EdUtils.getCurrentDate();
        binding.activityStudiarypost.tvFromDate.setText("From : " + st_fromdate);
        binding.activityStudiarypost.tvToDate.setText("To : " + st_todate);
        binding.activityStudiarypost.tvFromDay.setText(EdUtils.getDayfromdate(st_fromdate));
        binding.activityStudiarypost.tvToDay.setText(EdUtils.getDayfromdate(st_todate));
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
        binding.activityStudiarypost.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityStudiarypost.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_studiarypost_inc);
        binding.activityStudiarypost.setHandlers(this);
        binding.navView.setItemIconTintList(null);
        binding.activityStudiarypost.btnPost.setOnClickListener(this);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityStudiarypost.toolbar.tvChild.setText("Student Diary");
        binding.activityStudiarypost.tvLabel.setText(classname + " Section " + sectionname + " " + subjectname);

        binding.activityStudiarypost.toolbar.btnMenu.setOnClickListener(this);
        binding.activityStudiarypost.vFromdate.setOnClickListener(this);
        binding.activityStudiarypost.vTodate.setOnClickListener(this);
        binding.activityStudiarypost.toolbar.cartView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == binding.activityStudiarypost.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityStudiarypost.btnPost) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                Intent i = new Intent(mContext, TAddpost.class);
                i.putExtra("ClassID", ClassID);
                i.putExtra("SectionID", SectionID);
                i.putExtra("studentwise", "0");
                i.putExtra("SubjectsID", SubjectsID);
                i.putExtra("DiaryID", DiaryID);
                i.putExtra("ClassName", classname);
                i.putExtra("SectionName", sectionname);
                i.putExtra("SubjectsName", subjectname);
                i.putExtra("from", "normal");
                startActivity(i);
            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                Intent i = new Intent(mContext, ParentDiaryPost.class);
                i.putExtra("ClassID", ClassID);
                i.putExtra("SectionID", SectionID);
                i.putExtra("SubjectsID", SubjectsID);
                i.putExtra("DiaryID", DiaryID);
                i.putExtra("ClassName", classname);
                i.putExtra("SectionName", sectionname);
                i.putExtra("SubjectsName", subjectname);
                startActivity(i);
            }
        } else if (v == binding.activityStudiarypost.vFromdate) {
            DatePickerDialog fromdatedialog = new DatePickerDialog(StudentDiaryPostList.this, fromdate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            try {
                String st_fromdate = binding.activityStudiarypost.tvToDate.getText().toString().replace("To : ", "").trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(st_fromdate);
                    long millis = date.getTime();
                    fromdatedialog.getDatePicker().setMaxDate(millis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            fromdatedialog.show();
        } else if (v == binding.activityStudiarypost.vTodate) {
            DatePickerDialog todateDialog = new DatePickerDialog(StudentDiaryPostList.this, todate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            try {
                String st_fromdate = binding.activityStudiarypost.tvFromDate.getText().toString().replace("From : ", "").trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(st_fromdate);
                    long millis = date.getTime();
                    todateDialog.getDatePicker().setMinDate(millis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            todateDialog.show();
        } else if (v == binding.activityStudiarypost.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
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
            binding.activityStudiarypost.tvFromDate.setText("From : " + strDateTime);
            binding.activityStudiarypost.tvFromDay.setText(EdUtils.getDayfromdate(strDateTime));
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
            binding.activityStudiarypost.tvToDate.setText("To : " + strDateTime);
            binding.activityStudiarypost.tvToDay.setText(EdUtils.getDayfromdate(strDateTime));
            if (cd.isConnected()) {
                getNoticeList();
            }
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void inflateadapter() {
        if (notice.getData().getDiaryData().size() > 0) {
            adapterExp = new StuPostListExpAdapter(mContext, notice.getData().getDiaryData());
            binding.activityStudiarypost.rvRecycle.setLayoutManager(new LinearLayoutManager(this));
            binding.activityStudiarypost.rvRecycle.setAdapter(adapterExp);
        } else {
            EdUtils.showToastShort(mContext, "No Data Found");
        }
    }

    private void getNoticeList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<StudentPostList> call = redditAPI.getStudentPostList(EdUtils.get_studentID(mContext), ClassID, SectionID, SubjectsID, st_fromdate.replaceAll("/", "-"), st_todate.replaceAll("/", "-"));
        call.enqueue(new Callback<StudentPostList>() {

            @Override
            public void onResponse(Call<StudentPostList> call, retrofit2.Response<StudentPostList> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    notice = response.body();
                    if (notice.getStatus().equalsIgnoreCase("1")) {
                        inflateadapter();
                    } else {
                        EdUtils.showToastShort(mContext, notice.getMessage());
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
