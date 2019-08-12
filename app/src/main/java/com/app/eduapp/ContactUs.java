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

import com.app.eduapp.databinding.ActivityContactusIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.SchoolDetails;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ContactUs extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ActivityContactusIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    SchoolDetails getSchoolDetails;
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        cd = new ConnectionDetector(mContext);
        db = new LCDatabaseHandler(mContext);
        initview();

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
        binding.activityContactUs.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityContactUs.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contactus_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        binding.activityContactUs.toolbar.btnMenu.setOnClickListener(this);
        binding.activityContactUs.toolbar.tvChild.setText("Contact Us");
        binding.activityContactUs.toolbar.btnMenu.setOnClickListener(this);
        binding.activityContactUs.toolbar.cartView.setOnClickListener(this);
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
        if (v == binding.activityContactUs.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityContactUs.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
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
        binding.activityContactUs.schoolTxt.setText(getSchoolDetails.getData().getSchoolDetails().getSchoolName());
        binding.activityContactUs.address.setText(getSchoolDetails.getData().getSchoolDetails().getSchoolAddress());
        binding.activityContactUs.numberOne.setText(getSchoolDetails.getData().getSchoolDetails().getSchoolPrimaryPhone());
        binding.activityContactUs.numberTwo.setText(getSchoolDetails.getData().getSchoolDetails().getSchoolSecondryPhone());
        try {
            Picasso.get()
                    .load(getSchoolDetails.getData().getSchoolDetails().getSchoolLogo())
                    .into(binding.activityContactUs.topIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
