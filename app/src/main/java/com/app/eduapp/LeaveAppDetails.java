package com.app.eduapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.eduapp.adapter.ViewPagerAdapter;
import com.app.eduapp.databinding.LeavedetailosActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.PhotoLoader;
import com.app.eduapp.pojo.ApproveDisapprove;
import com.app.eduapp.pojo.LeaveDetails;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaveAppDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    List<String> imagelist = new ArrayList<>();
    LeavedetailosActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    String leave_id;
    ProgressDialog pDialog;
    LeaveDetails leaveDetails;
    String IsApproved = "";
    LCDatabaseHandler db;
    Dialog myDialog;
    SimpleDateFormat dateFormat;
    Calendar cal;
    int position = 0;

    ImageView iv_appimgview, iv_appimgview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        db = new LCDatabaseHandler(mContext);
        myDialog = new Dialog(this);
        dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
        cal = Calendar.getInstance();
        leave_id = getIntent().getExtras().getString("leave_id");
        initView();

        if (cd.isConnected()) {
            getLeaavetDetails();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.leavedetailsActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.leavedetailsActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.leavedetailos_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.leavedetailsActivity.toolbar.tvChild.setText("Leave Application");
        binding.navView.setItemIconTintList(null);
        binding.leavedetailsActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.leavedetailsActivity.toolbar.cartView.setOnClickListener(this);
        binding.leavedetailsActivity.btnCancel.setOnClickListener(this);
        binding.leavedetailsActivity.btnSave.setOnClickListener(this);
        binding.leavedetailsActivity.btnDelete.setOnClickListener(this);

        iv_appimgview = findViewById(R.id.iv_appimgview);
        iv_appimgview1 = findViewById(R.id.iv_appimgview1);

        iv_appimgview.setOnClickListener(this);
        iv_appimgview1.setOnClickListener(this);
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
        if (v == binding.leavedetailsActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.leavedetailsActivity.btnCancel) {
            IsApproved = "D";
            setApprovalStatus();
        } else if (v == binding.leavedetailsActivity.btnSave) {
            IsApproved = "A";
            setApprovalStatus();
        } else if (v == binding.leavedetailsActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        } else if (v == binding.leavedetailsActivity.btnDelete) {
            deletepost();
        }


    }


    private void deletepost() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ApproveDisapprove> call = redditAPI.deleteLeaveApplication(leave_id);
        call.enqueue(new Callback<ApproveDisapprove>() {

            @Override
            public void onResponse(Call<ApproveDisapprove> call, retrofit2.Response<ApproveDisapprove> response) {
                if (response.isSuccessful()) {
                    Log.d("Response", response.toString());
                    ApproveDisapprove approveDisapprove = response.body();
                    EdUtils.showToastShort(mContext, approveDisapprove.getMessage());
                    finish();
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApproveDisapprove> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void getLeaavetDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<LeaveDetails> call = null;
        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            call = redditAPI.getLeaveDetails(leave_id);
        } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
            call = redditAPI.getLeaveDetails(leave_id, EdUtils.get_userID(mContext));
        }

        call.enqueue(new Callback<LeaveDetails>() {

            @Override
            public void onResponse(Call<LeaveDetails> call, retrofit2.Response<LeaveDetails> response) {
                if (response.isSuccessful()) {
                    Log.d("Response", response.toString());
                    leaveDetails = response.body();
                    if (leaveDetails.getStatus().equalsIgnoreCase("1")) {
                        if (leaveDetails.getData().getLeaveDetails() != null) {
                            setupView();
                        }
                    } else {
                        EdUtils.showToastShort(mContext, leaveDetails.getMessage());
                    }

                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LeaveDetails> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setupView() {
        try {
            if (leaveDetails.getData().getLeaveDetails().getApprovedByByDetails().getEmployeeName().isEmpty()) {
                binding.leavedetailsActivity.vApproved.setVisibility(View.GONE);
            } else {
                binding.leavedetailsActivity.vApproved.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            binding.leavedetailsActivity.vApproved.setVisibility(View.GONE);
        }

        if (leaveDetails.getData().getLeaveDetails().getAttachemnetFiles().size() > 0) {
            showphotolist();
        }

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            if (leaveDetails.getData().getLeaveDetails().getEmployeeID().equalsIgnoreCase(EdUtils.get_userID(mContext))) {
                binding.leavedetailsActivity.vBtn.setVisibility(View.GONE);
            } else {
                binding.leavedetailsActivity.vBtn.setVisibility(View.VISIBLE);
            }
            /*if (leaveDetails.getData().getLeaveDetails().getIsApproved().equalsIgnoreCase("A")) {
                binding.leavedetailsActivity.vBtn.setVisibility(View.GONE);
            } else if (leaveDetails.getData().getLeaveDetails().getIsApproved().equalsIgnoreCase("D")) {
                binding.leavedetailsActivity.vBtn.setVisibility(View.GONE);
            } else {
                try {
                  *//*  if (leaveDetails.getData().getLeaveDetails().getEmployeeID().equalsIgnoreCase(EdUtils.get_userID(mContext))) {
                        binding.leavedetailsActivity.vBtn.setVisibility(View.GONE);
                    } else {
                        binding.leavedetailsActivity.vBtn.setVisibility(View.VISIBLE);
                    }*//*
                } catch (Exception e) {
                    binding.leavedetailsActivity.vBtn.setVisibility(View.VISIBLE);
                }
            }*/
        } else {
            binding.leavedetailsActivity.vBtn.setVisibility(View.GONE);
        }


        try {
            Picasso.get()
                    .load(leaveDetails.getData().getLeaveDetails().getApprovedByByDetails().getProfileImage())
                    .into(iv_appimgview);
        } catch (Exception e) {

        }

        try {
            Picasso.get()
                    .load(leaveDetails.getData().getLeaveDetails().getProfileImage())
                    .into(iv_appimgview1);
        } catch (Exception e) {

        }
        //Log.d("TAGSSSS",leaveDetails.getData().getLeaveDetails().getParentsName());

        binding.leavedetailsActivity.tvLeaveheader.setText(leaveDetails.getData().getLeaveDetails().getLeaveTitle());
        binding.leavedetailsActivity.tvApprovedby.setText(leaveDetails.getData().getLeaveDetails().getApprovedByByDetails().getEmployeeName());
        binding.leavedetailsActivity.tvChildname.setText(leaveDetails.getData().getLeaveDetails().getFullName());
        binding.leavedetailsActivity.tvClassname.setText(leaveDetails.getData().getLeaveDetails().getClassName() + " " + leaveDetails.getData().getLeaveDetails().getSectionName());
        binding.leavedetailsActivity.tvParentname.setText(leaveDetails.getData().getLeaveDetails().getParentsName());
        binding.leavedetailsActivity.tvDate.setText("From : " + leaveDetails.getData().getLeaveDetails().getLeaveStartDate() + " To : " + leaveDetails.getData().getLeaveDetails().getLeaveEndDate());
        binding.leavedetailsActivity.tvDesc.setText(leaveDetails.getData().getLeaveDetails().getLeaveDescription());

        if (leaveDetails.getData().getLeaveDetails().getIsApproved().equalsIgnoreCase("A")) {
            binding.leavedetailsActivity.tvStatus.setText("Approved");
        } else if (leaveDetails.getData().getLeaveDetails().getIsApproved().equalsIgnoreCase("N")) {
            binding.leavedetailsActivity.tvStatus.setText("New");
        } else if (leaveDetails.getData().getLeaveDetails().getIsApproved().equalsIgnoreCase("D")) {
            binding.leavedetailsActivity.tvStatus.setText("Disapproved");
        }
        binding.leavedetailsActivity.btnDelete.setVisibility(View.GONE);

       /* if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            if (!leaveDetails.getData().getLeaveDetails().getEmployeeID().isEmpty()) {
                if (leaveDetails.getData().getLeaveDetails().getEmployeeID().equals(EdUtils.get_userID(mContext))) {
                    ///Button Visible
                    binding.leavedetailsActivity.btnDelete.setVisibility(View.VISIBLE);
                }
            } else {
                ///Button Visible
                binding.leavedetailsActivity.btnDelete.setVisibility(View.VISIBLE);
            }
        } else {
            if (leaveDetails.getData().getLeaveDetails().getStudentsID().equals(EdUtils.get_userID(mContext))) {
                ///Button Visible
                binding.leavedetailsActivity.btnDelete.setVisibility(View.VISIBLE);
            }
        }*/


    }


    private void setApprovalStatus() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ApproveDisapprove> call = redditAPI.setApproveDisapprove(leave_id, EdUtils.get_userID(mContext), IsApproved);
        call.enqueue(new Callback<ApproveDisapprove>() {

            @Override
            public void onResponse(Call<ApproveDisapprove> call, retrofit2.Response<ApproveDisapprove> response) {
                if (response.isSuccessful()) {
                    Log.d("Response", response.toString());
                    ApproveDisapprove approveDisapprove = response.body();
                    EdUtils.showToastShort(mContext, approveDisapprove.getMessage());
                    finish();
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApproveDisapprove> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void showphotolist() {
        imagelist.clear();
        binding.leavedetailsActivity.selectedPhotosContainer.removeAllViews();
        if (leaveDetails.getData().getLeaveDetails().getAttachemnetFiles().size() >= 1) {
            binding.leavedetailsActivity.selectedPhotosContainer.setVisibility(View.VISIBLE);
        }
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        for (int i = 0; i < leaveDetails.getData().getLeaveDetails().getAttachemnetFiles().size(); i++) {
            imagelist.add(leaveDetails.getData().getLeaveDetails().getAttachemnetFiles().get(i).getOriginalAttachemnetFile());
            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item_upload, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);
            Picasso.get()
                    .load(leaveDetails.getData().getLeaveDetails().getAttachemnetFiles().get(i).getAttachemnetFile())
                    .into(thumbnail);
            binding.leavedetailsActivity.selectedPhotosContainer.addView(imageHolder);
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(htpx, wdpx));
            thumbnail.setTag(i);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    position = pos;
                    //EdUtils.showToastShort(mContext,""+pos);
                    ShowPopup();
                }
            });
        }
    }


    public void ShowPopup() {
        ImageView iv_close, iv_download;
        final ViewPager vpager;
        myDialog.setContentView(R.layout.custom_popup);
        iv_close = myDialog.findViewById(R.id.iv_close);
        iv_download = myDialog.findViewById(R.id.iv_download);
        vpager = myDialog.findViewById(R.id.vpager);
        vpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        iv_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                Picasso.get()
                        .load(imagelist.get(position))
                        .into(new PhotoLoader(mContext, dateFormat.format(cal.getTime())));
            }
        });

        if (imagelist.size() > 0) {
            ViewPagerAdapter vadapter = new ViewPagerAdapter(mContext, imagelist, position);
            vpager.setAdapter(vadapter);
        }

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


}
