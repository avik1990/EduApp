package com.app.eduapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.databinding.ActivitySettingsIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.GetStudentsList;
import com.app.eduapp.pojo.SchoolDetails;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingActivity extends AppCompatActivity implements ClickEventLisener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ActivitySettingsIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    GetStudentsList getStudentsList;

    SchoolDetails getSchoolDetails;
    ConnectionDetector cd;
    LinearLayout ll_container;
    RadioGroup radiogroup;
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_inc);
        binding.activitySetting.setHandlers(this);
        mContext = this;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        cd = new ConnectionDetector(mContext);
        db = new LCDatabaseHandler(mContext);

        initView();

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
            if (cd.isConnected()) {
                if (!EdUtils.get_studentID(mContext).isEmpty()) {
                    pDialog.show();
                    getStudentList();
                }
            }
        } else {
            binding.activitySetting.vMain.setVisibility(View.GONE);
        }

        if (cd.isConnected()) {
            getSchoolDetails();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activitySetting.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activitySetting.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activitySetting.toolbar.tvChild.setText("Settings");
        binding.activitySetting.toolbar.btnMenu.setOnClickListener(this);
        binding.activitySetting.toolbar.cartvie.setOnClickListener(this);
        binding.activitySetting.tvSupport.setText(Html.fromHtml("<medium><b><font color=\"#000000\">" + "Support: " + "</font></b></medium>" + "<medium><u><font color=\"#000000\">" + "www.croyezdem.com"));
        binding.activitySetting.tvCall.setText(Html.fromHtml("<medium><b><font color=\"#000000\">" + "Call: " + "</font></b></medium>" + "<medium><font color=\"#000000\">" + "+91-9614195569"));

        ll_container = findViewById(R.id.ll_container);
        binding.activitySetting.toolbar.cartView.setOnClickListener(this);
        radiogroup = findViewById(R.id.radiogroup);
        radiogroup.setOrientation(LinearLayout.VERTICAL);
    }


    @Override
    public void clickTrigger(View v, int position) {
        /*Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        if (position == 2) {
            Intent intent = new Intent(SettingActivity.this, ExamRoutineListActivity.class);
            startActivity(intent);
        }*/
    }


    @SuppressLint("NewApi")
    public void ClickEvent(View view) {
        if (view == binding.activitySetting.noIv) {
            binding.activitySetting.noIv.setImageResource(R.drawable.no_active);
            binding.activitySetting.yesIv.setImageResource(R.drawable.yes_deactive);
            //save value in sharedpreference
        } else if (view == binding.activitySetting.yesIv) {
            //save value in sharedpreference

            binding.activitySetting.yesIv.setImageResource(R.drawable.yes_active);
            binding.activitySetting.noIv.setImageResource(R.drawable.no_deactive);
        } else if (view == binding.activitySetting.dailyIv) {
            //save value in sharedpreference
            binding.activitySetting.dailyIv.setBackground(getResources().getDrawable(R.drawable.button_style_blue));
            binding.activitySetting.weeklyIv.setBackground(getResources().getDrawable(R.drawable.button_style_white));

        } else if (view == binding.activitySetting.weeklyIv) {
            //save value in sharedpreference
            binding.activitySetting.dailyIv.setBackground(getResources().getDrawable(R.drawable.button_style_white));
            binding.activitySetting.weeklyIv.setBackground(getResources().getDrawable(R.drawable.button_style_blue));
        } else if (view == binding.activitySetting.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.activitySetting.toolbar.btnMenu) {
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

    private void getStudentList() {
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
                    if (getStudentsList.getStatus().equalsIgnoreCase("1")) {
                        if (getStudentsList.getData().getStudentsList().size() > 0) {
                            for (int i = 0; i < getStudentsList.getData().getStudentsList().size(); i++) {
                                RadioButton rbn = new RadioButton(mContext);
                                rbn.setId(i);
                                rbn.setText(getStudentsList.getData().getStudentsList().get(i).getFullName());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                                rbn.setLayoutParams(params);
                                radiogroup.addView(rbn);
                            }

                            radiogroup.check(Integer.parseInt(EdUtils.get_RadioChecked(mContext)));

                            radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    EdUtils.set_RadioChecked(mContext, String.valueOf(checkedId));
                                    EdUtils.set_studentID(mContext, getStudentsList.getData().getStudentsList().get(checkedId).getStudentsID());
                                    EdUtils.set_sectionID(mContext, getStudentsList.getData().getStudentsList().get(checkedId).getSectionID());
                                    EdUtils.set_classID(mContext, getStudentsList.getData().getStudentsList().get(checkedId).getClassID());
                                }
                            });
                        }
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


    private void getSchoolDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<SchoolDetails> call = redditAPI.getSchoolDetails("1");
        call.enqueue(new Callback<SchoolDetails>() {

            @Override
            public void onResponse(Call<SchoolDetails> call, retrofit2.Response<SchoolDetails> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getSchoolDetails = response.body();
                    if (getSchoolDetails.getStatus().equalsIgnoreCase("1")) {
                        setupview();
                    }

                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SchoolDetails> call, Throwable t) {
                Log.d("Error", t.getMessage());
                pDialog.dismiss();
            }
        });
    }

    private void setupview() {
        binding.activitySetting.schoolName.setText(getSchoolDetails.getData().getSchoolDetails().getSchoolName());
        binding.activitySetting.address.setText(getSchoolDetails.getData().getSchoolDetails().getSchoolAddress());
        binding.activitySetting.numberOne.setText(getSchoolDetails.getData().getSchoolDetails().getSchoolPrimaryPhone());
        binding.activitySetting.numberTwo.setText(getSchoolDetails.getData().getSchoolDetails().getSchoolSecondryPhone());

       /* try {
            Picasso.get()
                    .load(getSchoolDetails.getData().getSchoolDetails().getSchoolLogo())
                    .into(binding.activitySetting.topIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


}
