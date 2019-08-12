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

import com.app.eduapp.databinding.MyprofileActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.GetParentsProfile;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyProfile extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    MyprofileActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    GetParentsProfile getParentsProfile;
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait..");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        db=new LCDatabaseHandler(mContext);
        
        if (cd.isConnected()) {
            getMyProfile();
        }

        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.myprofile_activity_inc);
        binding.myprofileActivity.toolbar.tvChild.setText("Guardian Profile");
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.myprofileActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.myprofileActivity.toolbar.cartView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.myprofileActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        }else if (v == binding.myprofileActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.myprofileActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.myprofileActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getMyProfile() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url) + "" + getResources().getString(R.string.getParentsProfile);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetParentsProfile> call = redditAPI.getProfile(EdUtils.get_userID(mContext), EdUtils.getdeviceid(mContext), "abcdef");
        call.enqueue(new Callback<GetParentsProfile>() {

            @Override
            public void onResponse(Call<GetParentsProfile> call, retrofit2.Response<GetParentsProfile> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getParentsProfile = response.body();
                    //   if(getParentsProfile.getData().getParentsProfile()) {
                    setUpviewData();
                    //   }
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetParentsProfile> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setUpviewData() {
        binding.myprofileActivity.etName.setText(getParentsProfile.getData().getParentsProfile().getParentsName());
        binding.myprofileActivity.etPhno.setText(getParentsProfile.getData().getParentsProfile().getPrimaryMobileNumber());
        binding.myprofileActivity.etAddress.setText(getParentsProfile.getData().getParentsProfile().getParentsAddress());
    }


}
