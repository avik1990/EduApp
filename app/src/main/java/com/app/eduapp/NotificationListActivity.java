package com.app.eduapp;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.app.eduapp.adapter.NotificationListApapter;
import com.app.eduapp.databinding.NotiIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.ModelInbox;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import java.util.List;


public class NotificationListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    NotiIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    NotificationListApapter studentListApapter;
    ProgressDialog pDialog;
    ModelInbox getStudentsList;
    LCDatabaseHandler db;
    List<ModelInbox> listnotification;

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
        setuplistview();
    }


    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.noti_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.notilistActivity.toolbar.tvChild.setText("Notifications");
        binding.notilistActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.navView.setItemIconTintList(null);
        binding.notilistActivity.rcvStudentlist.setLayoutManager(new LinearLayoutManager(this));
        binding.notilistActivity.rcvStudentlist.setItemAnimator(new DefaultItemAnimator());
        binding.notilistActivity.toolbar.cartView.setVisibility(View.INVISIBLE);
        binding.notilistActivity.toolbar.tvCartcount.setVisibility(View.INVISIBLE);
        // setuplistview();
    }

    public void setuplistview() {
        listnotification = db.getInstance(mContext).getPushlist("1");
        if (listnotification.size() > 0) {
            studentListApapter = new NotificationListApapter(mContext, R.layout.row_notification, listnotification, "MonthListActivity");
            binding.notilistActivity.rcvStudentlist.setAdapter(studentListApapter);
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
        if (v == binding.notilistActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        }
    }
}
