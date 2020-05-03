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
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.adapter.ExamNameAdapter;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.common.RecyclerViewMargin;
import com.app.eduapp.databinding.ActivityExamIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.ExamListBean;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExamListActivity extends AppCompatActivity implements ClickEventLisener,
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    ExamNameAdapter adapter;
    ActivityExamIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    ExamListBean examNameBean;
    String ClassID = "", SectionID = "";
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

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            ClassID = getIntent().getExtras().getString("ClassID");
            SectionID = getIntent().getExtras().getString("SectionID");
        } else {
            ClassID = EdUtils.get_classID(mContext);
            SectionID = EdUtils.get_sectionID(mContext);
        }

        initView();

        if (cd.isConnected()) {
            pDialog.show();
            getExamList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityExamList.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityExamList.toolbar.tvCartcount.setText(String.valueOf(count));

    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_inc);
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityExamList.toolbar.btnMenu.setOnClickListener(this);
        binding.activityExamList.toolbar.tvChild.setText("Exam");
        binding.activityExamList.toolbar.cartView.setOnClickListener(this);
    }

    private void showList() {
        binding.activityExamList.className.setText(examNameBean.getClassName() + " Section " + examNameBean.getSectionName());
        adapter = new ExamNameAdapter(this, examNameBean.getData().getExamList(), this);
        binding.activityExamList.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMargin decoration = new RecyclerViewMargin(15, examNameBean.getData().getExamList().size());
        binding.activityExamList.recyclerView.addItemDecoration(decoration);
        binding.activityExamList.recyclerView.setAdapter(adapter);
    }


    @Override
    public void clickTrigger(View v, int position) {
        //Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
        //if (position==2){
        Intent intent = new Intent(ExamListActivity.this, ExamRoutineListActivity.class);
        intent.putExtra("ExamID", examNameBean.getData().getExamList().get(position).getExamID());
        intent.putExtra("ClassID", ClassID);
        intent.putExtra("SectionID", SectionID);
        startActivity(intent);
        // }
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
        if (v == binding.activityExamList.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityExamList.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }


    private void getExamList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ExamListBean> call = redditAPI.getExamList(ClassID, SectionID);
        call.enqueue(new Callback<ExamListBean>() {

            @Override
            public void onResponse(Call<ExamListBean> call, retrofit2.Response<ExamListBean> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    examNameBean = response.body();
                    if (examNameBean.getData().getExamList().size() > 0) {
                        showList();
                    }
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ExamListBean> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


}
