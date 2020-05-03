package com.app.eduapp.teacher;

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

import com.app.eduapp.NotificationListActivity;
import com.app.eduapp.R;
import com.app.eduapp.databinding.TmyprofileActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.EmployeeProfile;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMyProfile extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    TmyprofileActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    EmployeeProfile getParentsProfile;
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        db=new LCDatabaseHandler(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait..");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        if (cd.isConnected()) {
            getMyProfile();
        }

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.tmyprofileActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.tmyprofileActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.tmyprofile_activity_inc);
        binding.navView.setItemIconTintList(null);
        binding.tmyprofileActivity.toolbar.tvChild.setText("Teacher Profile");
        binding.navView.setNavigationItemSelectedListener(this);
        binding.tmyprofileActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.tmyprofileActivity.toolbar.cartView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.tmyprofileActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.tmyprofileActivity.toolbar.cartView) {
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


    private void getMyProfile() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<EmployeeProfile> call = redditAPI.getEmployeeProfile(EdUtils.get_userID(mContext), EdUtils.getdeviceid(mContext), "abcdef");
        call.enqueue(new Callback<EmployeeProfile>() {

            @Override
            public void onResponse(Call<EmployeeProfile> call, retrofit2.Response<EmployeeProfile> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getParentsProfile = response.body();
                    setUpviewData();
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EmployeeProfile> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setUpviewData() {
        binding.tmyprofileActivity.etName.setText(getParentsProfile.getData().getParentsProfile().getEmployeeName());
        binding.tmyprofileActivity.etPhno.setText(getParentsProfile.getData().getParentsProfile().getPrimaryMobileNumber());
        binding.tmyprofileActivity.etAddress.setText(getParentsProfile.getData().getParentsProfile().getEmployeeAddress());
        binding.tmyprofileActivity.etEmail.setText(getParentsProfile.getData().getParentsProfile().getEmailAddress());
        binding.tmyprofileActivity.tvDesignation.setText(getParentsProfile.getData().getParentsProfile().getDesignationName());

        try {
            Picasso.get()
                    .load(getParentsProfile.getData().getParentsProfile().getProfilePicture())
                    .resize(50, 50)
                    .centerCrop()
                    .into(binding.tmyprofileActivity.ivImage);
        } catch (Exception e) {
        }
    }
}
