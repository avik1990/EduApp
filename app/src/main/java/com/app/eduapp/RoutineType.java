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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.databinding.ActivityRtypeIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.app.eduapp.teacher.TClassListActivity;

public class RoutineType extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ActivityRtypeIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    ConnectionDetector cd;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityRtypeUs.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityRtypeUs.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rtype_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        binding.activityRtypeUs.toolbar.btnMenu.setOnClickListener(this);
        binding.activityRtypeUs.toolbar.tvChild.setText("Class Schedule");
        binding.activityRtypeUs.toolbar.btnMenu.setOnClickListener(this);
        binding.activityRtypeUs.toolbar.cartView.setOnClickListener(this);
        binding.activityRtypeUs.rlMyroutine.setOnClickListener(this);
        binding.activityRtypeUs.rlSturoutine.setOnClickListener(this);
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
        if (v == binding.activityRtypeUs.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityRtypeUs.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        } else if (v == binding.activityRtypeUs.rlMyroutine) {
            Intent i = new Intent(mContext, TeaRoutineActivity.class);
            i.putExtra("from", "routine");
            startActivity(i);
        } else if (v == binding.activityRtypeUs.rlSturoutine) {
            Intent i = new Intent(mContext, TClassListActivity.class);
            i.putExtra("from", "routinetype");
            startActivity(i);
        }
    }
}
