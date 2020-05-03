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

import com.app.eduapp.adapter.ExamNameAdapter;
import com.app.eduapp.adapter.MatrixTableAdapterStatusPending;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.databinding.ActivityRoutineIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.ClassRoutineByParent;
import com.app.eduapp.pojo.ExamListBean;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoutineActivity extends AppCompatActivity implements ClickEventLisener,
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    ExamNameAdapter adapter;
    ActivityRoutineIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    ExamListBean examNameBean;
    String ClassID = "", SectionID = "";
    ClassRoutineByParent classRoutineByParent;
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
            getRoutineDetails();
        }
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_routine_inc);
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityRoutine.toolbar.btnMenu.setOnClickListener(this);
        binding.activityRoutine.toolbar.tvChild.setText("Class Schedule");
        binding.activityRoutine.toolbar.cartView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityRoutine.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityRoutine.toolbar.tvCartcount.setText(String.valueOf(count));
    }


    @Override
    public void clickTrigger(View v, int position) {
        Intent intent = new Intent(RoutineActivity.this, ExamRoutineListActivity.class);
        intent.putExtra("ExamID", examNameBean.getData().getExamList().get(position).getExamID());
        intent.putExtra("ClassID", ClassID);
        intent.putExtra("SectionID", SectionID);
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
        if (v == binding.activityRoutine.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        }else if (v == binding.activityRoutine.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }


    private void getRoutineDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ClassRoutineByParent> call = redditAPI.getClassRoutineByParent(ClassID, SectionID);
        call.enqueue(new Callback<ClassRoutineByParent>() {

            @Override
            public void onResponse(Call<ClassRoutineByParent> call, retrofit2.Response<ClassRoutineByParent> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    classRoutineByParent = response.body();
                    if (classRoutineByParent.getStatus().equalsIgnoreCase("1")) {
                        if (classRoutineByParent.getData().getDayOfWeekList().size() > 0) {
                            prepareresources();
                        }
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ClassRoutineByParent> call, Throwable t) {
                Log.d("Error", t.getMessage());
                pDialog.dismiss();
            }
        });
    }

    private void prepareresources() {
        binding.activityRoutine.className.setText(classRoutineByParent.getClassName() + " Section " + classRoutineByParent.getSectionName());
        if (classRoutineByParent.getData().getDayOfWeekList().size() > 0) {
            binding.activityRoutine.table.setVisibility(View.VISIBLE);
            String[][] casedata = new String[classRoutineByParent.getData().getClassRoutineList().size() + 1][classRoutineByParent.getData().getDayOfWeekList().size()];
            for (int i = 0; i < classRoutineByParent.getData().getDayOfWeekList().size(); i++) {
                int j = 0;
                int k = i + j;
                casedata[j][k] = classRoutineByParent.getData().getDayOfWeekList().get(i).getDayOfWeekCode();
            }

            for (int j = 1; j < classRoutineByParent.getData().getClassRoutineList().size() + 1; j++) {
                for (int i = 0; i < classRoutineByParent.getData().getDayOfWeekList().size(); i++) {
                    if (classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getDayOfWeekCode().isEmpty()) {
                        String text = "<medium><font color=\"#000000\">" + classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getStartTime() + " - " + classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getEndsTime() + "<br>" + " </font></medium>" + " <font color=\"#c76c3f\">" + classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getSlotName() + "</font>";
                        casedata[j][i] = text;
                    } else {
                        String text = "<font color=\"#000000\">" + classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getSubjectsName() + " </font>" + "<br>" + " <font color=\"#e5004f\">" + classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getEmployeeName() + "</font>";
                        casedata[j][i] = text;
                    }
                }
            }
            MatrixTableAdapterStatusPending<String> matrixTableAdapter = new MatrixTableAdapterStatusPending<String>(this, casedata);
            binding.activityRoutine.table.setAdapter(matrixTableAdapter);
        }
    }

}
