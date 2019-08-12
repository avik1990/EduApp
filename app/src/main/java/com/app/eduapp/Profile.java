package com.app.eduapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.databinding.ProfileActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.app.eduapp.teacher.TMyProfile;

public class Profile extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ProfileActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        db=new LCDatabaseHandler(mContext);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.profileActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.profileActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.profile_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.profileActivity.rlMyprofile.setOnClickListener(this);
        binding.profileActivity.rlStudentprofile.setOnClickListener(this);
        binding.profileActivity.toolbar.tvChild.setText("Profile");
        binding.navView.setItemIconTintList(null);
        binding.profileActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.profileActivity.toolbar.cartView.setOnClickListener(this);
        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            binding.profileActivity.rlStudentprofile.setVisibility(View.GONE);
        } else {
            binding.profileActivity.rlStudentprofile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.profileActivity.rlMyprofile) {
            if (cd.isConnected()) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    Intent i = new Intent(mContext, TMyProfile.class);
                    startActivity(i);
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    Intent i = new Intent(mContext, MyProfile.class);
                    startActivity(i);
                }

            } else {
                EdUtils.showToastShort(mContext, "Internet Connection Required");
            }
        } else if (v == binding.profileActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.profileActivity.rlStudentprofile) {
            if (cd.isConnected()) {
                Intent i = new Intent(mContext, StudentListActivity.class);
                startActivity(i);
            } else {
                EdUtils.showToastShort(mContext, "Internet Connection Required");
            }
        } else if (v == binding.profileActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
