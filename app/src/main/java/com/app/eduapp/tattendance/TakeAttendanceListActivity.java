package com.app.eduapp.tattendance;

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
import android.widget.CompoundButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.eduapp.NotificationListActivity;
import com.app.eduapp.R;
import com.app.eduapp.adapter.TakeAttendancetListApapter;
import com.app.eduapp.databinding.TakeattendActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.VolleySingleton;
import com.app.eduapp.pojo.StudentsListByClassSectionEMP;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TakeAttendanceListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    TakeattendActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    TakeAttendancetListApapter studentListApapter;
    ProgressDialog pDialog;
    StudentsListByClassSectionEMP studentsListByClassSectionEMP;
    String ClassID, SectionID;
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
        ClassID = getIntent().getExtras().getString("ClassID");
        SectionID = getIntent().getExtras().getString("SectionID");

        initView();

        if (cd.isConnected()) {
            pDialog.show();
            getStudentList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.takeattendanceActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.takeattendanceActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.takeattend_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.takeattendanceActivity.toolbar.tvChild.setText("Take Attendance");
        binding.takeattendanceActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.navView.setItemIconTintList(null);
        binding.takeattendanceActivity.rcvStudentlist.setLayoutManager(new LinearLayoutManager(this));
        binding.takeattendanceActivity.rcvStudentlist.setItemAnimator(new DefaultItemAnimator());
        binding.takeattendanceActivity.btnCancel.setOnClickListener(this);
        binding.takeattendanceActivity.btnSave.setOnClickListener(this);
        binding.takeattendanceActivity.toolbar.cartView.setOnClickListener(this);
        binding.takeattendanceActivity.btnCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    for (int i = 0; i < studentsListByClassSectionEMP.getData().getStudentsList().size(); i++) {
                        studentsListByClassSectionEMP.getData().getStudentsList().get(i).setCheck_selected("P");
                    }
                    studentListApapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < studentsListByClassSectionEMP.getData().getStudentsList().size(); i++) {
                        studentsListByClassSectionEMP.getData().getStudentsList().get(i).setCheck_selected("A");
                    }
                    studentListApapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void getStudentList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<StudentsListByClassSectionEMP> call = redditAPI.getStudentsListByClassSectionEMP(ClassID, SectionID);
        call.enqueue(new Callback<StudentsListByClassSectionEMP>() {

            @Override
            public void onResponse(Call<StudentsListByClassSectionEMP> call, retrofit2.Response<StudentsListByClassSectionEMP> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    studentsListByClassSectionEMP = response.body();
                    if (!studentsListByClassSectionEMP.getStatus().equalsIgnoreCase("0")) {
                        binding.takeattendanceActivity.tvLabel.setText(studentsListByClassSectionEMP.getClassName() + " Section " + studentsListByClassSectionEMP.getSectionName());
                        inflateStudentView();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StudentsListByClassSectionEMP> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void inflateStudentView() {
        if (studentsListByClassSectionEMP.getData().getStudentsList().size() > 0) {
            studentListApapter = new TakeAttendancetListApapter(mContext, R.layout.row_studentlist_attend, studentsListByClassSectionEMP.getData().getStudentsList(), "MonthListActivity", studentsListByClassSectionEMP.getClassName() + " Section " + studentsListByClassSectionEMP.getSectionName());
            binding.takeattendanceActivity.rcvStudentlist.setAdapter(studentListApapter);
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
        if (v == binding.takeattendanceActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.takeattendanceActivity.btnSave) {
            if (cd.isConnected()) {
                if (!getJsonString().equalsIgnoreCase("{}")) {
                    pDialog.show();
                    postAttendanceData();
                } else {
                    EdUtils.showToastShort(mContext, "No student selected for attendance");
                }

                /* */
            }
        } else if (v == binding.takeattendanceActivity.btnCancel) {
            finish();
        } else if (v == binding.takeattendanceActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    private String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < studentsListByClassSectionEMP.getData().getStudentsList().size(); i++) {
                if (studentsListByClassSectionEMP.getData().getStudentsList().get(i).getCheck_selected().equalsIgnoreCase("P")) {
                    String a = studentsListByClassSectionEMP.getData().getStudentsList().get(i).getStudentsID();
                    JSONObject productObject = new JSONObject();
                    productObject.accumulate("StudentsID", a);
                    //productObject.accumulate("AttendStatus", studentsListByClassSectionEMP.getData().getStudentsList().get(i).getCheck_selected());
                    jsonArray.put(productObject);
                    jsonObject.put("data", jsonArray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("JsonString", jsonObject.toString());
        return jsonObject.toString();
    }

    private void postAttendanceData() {
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.submitStudentsAttendanceEMP) + "?EmployeeID=" + EdUtils.get_userID(mContext) + "&ClassID=" + ClassID + "&SectionID=" + SectionID;

        Log.d("url_attend", url_login);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {
                    Boolean flag = false;

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            String msg = jObj.getString("Message");

                            if (status.equals("1")) {
                                EdUtils.showToastShort(mContext, msg);
                                finish();
                            } else {
                                EdUtils.showToastShort(mContext, msg);
                            }
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        pDialog.dismiss();
                    }
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("StudentsListArr", getJsonString());

                for (Object o : params.keySet()) {
                    String key = o.toString();
                    String value = params.get(key);
                    Log.d("posted_values", key + "--" + value);
                }
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }
}
