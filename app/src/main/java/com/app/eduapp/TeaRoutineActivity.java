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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.eduapp.adapter.ExamNameAdapter;
import com.app.eduapp.adapter.MatrixTableAdapterStatusPending;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.databinding.ActivityRoutineIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.TeacherRoutine;
import com.app.eduapp.pojo.ExamListBean;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeaRoutineActivity extends AppCompatActivity implements ClickEventLisener,
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    ExamNameAdapter adapter;
    ActivityRoutineIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    ExamListBean examNameBean;
    //String ClassID = "", SectionID = "";
    TeacherRoutine classRoutineByParent;
    LCDatabaseHandler db;
    String[] ClassScheduleName;
    String class_id = "";
    List<String> list_class_ids = new ArrayList<>();
    boolean datafillid=false;

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
        binding.activityRoutine.toolbar.tvChild.setText("Teacher Schedule");
        binding.activityRoutine.toolbar.cartView.setOnClickListener(this);

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            binding.activityRoutine.vSp1.setVisibility(View.VISIBLE);
        } else {
            binding.activityRoutine.vSp1.setVisibility(View.GONE);
        }
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
        /*Intent intent = new Intent(TeaRoutineActivity.this, ExamRoutineListActivity.class);
        intent.putExtra("ExamID", examNameBean.getData().getExamList().get(position).getExamID());
        intent.putExtra("ClassID", ClassID);
        intent.putExtra("SectionID", SectionID);
        startActivity(intent);*/
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
        } else if (v == binding.activityRoutine.toolbar.cartView) {
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
        Call<TeacherRoutine> call = redditAPI.getClassRoutineByTeacherEMP(EdUtils.get_userID(mContext), class_id);
        call.enqueue(new Callback<TeacherRoutine>() {

            @Override
            public void onResponse(Call<TeacherRoutine> call, retrofit2.Response<TeacherRoutine> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    classRoutineByParent = response.body();
                    if (classRoutineByParent.getStatus().equalsIgnoreCase("1")) {
                        if (classRoutineByParent.getData().getClassScheduleList().size() > 0) {
                            if(!datafillid)
                            inflateSpinner();
                        }
                        if (classRoutineByParent.getData().getDayOfWeekList().size() > 0) {
                            prepareresources();
                        }
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TeacherRoutine> call, Throwable t) {
                Log.d("Error", t.getMessage());
                pDialog.dismiss();
            }
        });
    }

    private void prepareresources() {
        binding.activityRoutine.className.setVisibility(View.GONE);
        binding.activityRoutine.vTeacher.setVisibility(View.VISIBLE);
        binding.activityRoutine.tvTeachername.setText(classRoutineByParent.getEmployeeName());
        binding.activityRoutine.tvDesignation.setText(classRoutineByParent.getDesignationName());
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
                        String text = "";
                        if (classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getSectionName().isEmpty()) {
                            text = "<font color=\"#000000\">" + classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getClassName();
                        } else {
                            text = "<font color=\"#000000\">" + classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getClassName() + " </font>" + "<br>" + " <font color=\"#e5004f\">" + "Sec- " + classRoutineByParent.getData().getClassRoutineList().get(j - 1).get(i).getSectionName() + "</font>";
                        }
                        casedata[j][i] = text;
                    }
                }
            }
            MatrixTableAdapterStatusPending<String> matrixTableAdapter = new MatrixTableAdapterStatusPending<String>(this, casedata);
            binding.activityRoutine.table.setAdapter(matrixTableAdapter);
        }
    }

    private void inflateSpinner() {
        list_class_ids.clear();
        if (classRoutineByParent.getData().getClassScheduleList().size() > 0) {
            ClassScheduleName = new String[classRoutineByParent.getData().getClassScheduleList().size() + 1];
            ClassScheduleName[0] = "Select Class Name";
            for (int i = 0; i < classRoutineByParent.getData().getClassScheduleList().size(); i++) {
                ClassScheduleName[i + 1] = classRoutineByParent.getData().getClassScheduleList().get(i).getClassScheduleName();
                list_class_ids.add(classRoutineByParent.getData().getClassScheduleList().get(i).getClassScheduleID());
            }
            inflatespinner();
        }
    }

    private void inflatespinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_textview, ClassScheduleName);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.activityRoutine.spinnerCustom.setAdapter(spinnerArrayAdapter);

        binding.activityRoutine.spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    class_id = classRoutineByParent.getData().getClassScheduleList().get(i - 1).getClassScheduleID();
                    pDialog.show();
                    getRoutineDetails();
                } else {
                    class_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        datafillid=true;
    }

}
