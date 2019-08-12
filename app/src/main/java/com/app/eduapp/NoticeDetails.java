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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.eduapp.adapter.DiaryAcknowledApapter;
import com.app.eduapp.adapter.NoticeAcknowledApapter;
import com.app.eduapp.adapter.ViewPagerAdapter;
import com.app.eduapp.databinding.NoticedetailsActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.PhotoLoader;
import com.app.eduapp.helper.VolleySingleton;
import com.app.eduapp.pojo.GetOrPostDiaryAcknowledgement;
import com.app.eduapp.pojo.GetOrPostNoticeAcknowledgement;
import com.app.eduapp.pojo.NoticeDetailsP;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticeDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    NoticedetailsActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    String notice_id;
    ProgressDialog pDialog;
    NoticeDetailsP noticeDetailsP;
    LCDatabaseHandler db;
    Dialog myDialog;
    SimpleDateFormat dateFormat;
    Calendar cal;
    int position = 0;
    List<String> imagelist = new ArrayList<>();
    GetOrPostNoticeAcknowledgement getOrPostNoticeAcknowledgement;
    NoticeAcknowledApapter noticeAcknowledApapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        myDialog = new Dialog(this);
        dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
        cal = Calendar.getInstance();
        notice_id = getIntent().getExtras().getString("notice_id");
        db = new LCDatabaseHandler(mContext);
        initView();

        if (cd.isConnected()) {
            getNoticeDetails();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.noticedetailsActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.noticedetailsActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.noticedetails_activity_inc);
        binding.noticedetailsActivity.toolbar.tvChild.setText("Notice");
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        binding.noticedetailsActivity.tvAddcomment.setOnClickListener(this);
        binding.noticedetailsActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.noticedetailsActivity.toolbar.cartView.setOnClickListener(this);
        binding.noticedetailsActivity.rlvScknowledge.setOnClickListener(this);

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            binding.noticedetailsActivity.tvMonth.setText("Acknowledged By");
            binding.noticedetailsActivity.rlvScknowledge.setVisibility(View.VISIBLE);
        } else {
            binding.noticedetailsActivity.rlvScknowledge.setVisibility(View.VISIBLE);
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
        if (v == binding.noticedetailsActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.noticedetailsActivity.tvAddcomment) {
            Intent i = new Intent(mContext, PostComments.class);
            i.putExtra("ID", notice_id);
            i.putExtra("from", "NoticeDetails");
            startActivity(i);
        } else if (v == binding.noticedetailsActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        } else if (v == binding.noticedetailsActivity.rlvScknowledge) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                if (cd.isConnected()) {
                    GetOrPostDiaryAcknowledgement();
                }
            } else {
                if (cd.isConnected()) {
                    pDialog.show();
                    PostAcknowledge();
                }
            }
        }
    }


    private void getNoticeDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // notice_id="7";
        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<NoticeDetailsP> call = redditAPI.getNoticeDetails(notice_id);
        call.enqueue(new Callback<NoticeDetailsP>() {

            @Override
            public void onResponse(Call<NoticeDetailsP> call, retrofit2.Response<NoticeDetailsP> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    noticeDetailsP = response.body();
                    //if(getParentsProfile.getData().getParentsProfile()) {
                    setupView();
                    //}
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<NoticeDetailsP> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setupView() {
        binding.noticedetailsActivity.tvTitle.setText(noticeDetailsP.getData().getNoticeData().getNoticeTitle());
        binding.noticedetailsActivity.tvDate.setText(noticeDetailsP.getData().getNoticeData().getNoticeDate());
        binding.noticedetailsActivity.tvNoticedata.setText(noticeDetailsP.getData().getNoticeData().getNoticeDescription());
        showphotolist();
    }


    private void showphotolist() {
        imagelist.clear();
        binding.noticedetailsActivity.selectedPhotosContainer.removeAllViews();
        if (noticeDetailsP.getData().getNoticeData().getAttachemnetFiles().size() >= 1) {
            binding.noticedetailsActivity.selectedPhotosContainer.setVisibility(View.VISIBLE);
        }
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        for (int i = 0; i < noticeDetailsP.getData().getNoticeData().getAttachemnetFiles().size(); i++) {
            imagelist.add(noticeDetailsP.getData().getNoticeData().getAttachemnetFiles().get(i).getOriginalAttachemnetFile());
            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item_upload, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);
            Picasso.get()
                    .load(noticeDetailsP.getData().getNoticeData().getAttachemnetFiles().get(i).getAttachemnetFile())
                    .into(thumbnail);
            binding.noticedetailsActivity.selectedPhotosContainer.addView(imageHolder);
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

    private void PostAcknowledge() {
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.GetOrPostNoticeAcknowledgement) + "?NoticeID=" + notice_id;

        Log.d("url_login", url_login);
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
                params.put("IsAcknowledgementSubmit", "Y");
                params.put("ParentsID", EdUtils.get_userID(mContext));
                params.put("DeviceToken", EdUtils.getgcmtoken(mContext));
                params.put("StudentsID", EdUtils.get_studentID(mContext));

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


    private void GetOrPostDiaryAcknowledgement() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetOrPostNoticeAcknowledgement> call = redditAPI.GetOrPostNoticeAcknowledgement(notice_id, EdUtils.get_studentID(mContext));

        call.enqueue(new Callback<GetOrPostNoticeAcknowledgement>() {

            @Override
            public void onResponse(Call<GetOrPostNoticeAcknowledgement> call, retrofit2.Response<GetOrPostNoticeAcknowledgement> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getOrPostNoticeAcknowledgement = response.body();
                    if (getOrPostNoticeAcknowledgement.getStatus().equalsIgnoreCase("1")) {
                        setupListView();
                    } else {
                        EdUtils.showToastShort(mContext, getOrPostNoticeAcknowledgement.getMessage());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetOrPostNoticeAcknowledgement> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setupListView() {
        ImageView iv_close;
        RecyclerView rv_acknowledge;
        myDialog.setContentView(R.layout.list_popup);
        iv_close = myDialog.findViewById(R.id.iv_close);
        rv_acknowledge = myDialog.findViewById(R.id.rv_acknowledge);
        rv_acknowledge.setLayoutManager(new LinearLayoutManager(this));
        rv_acknowledge.setItemAnimator(new DefaultItemAnimator());
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        if (getOrPostNoticeAcknowledgement.getData().getNoticeAcknowledgementList().size() > 0) {
            noticeAcknowledApapter = new NoticeAcknowledApapter(mContext, R.layout.row_studentlist, getOrPostNoticeAcknowledgement.getData().getNoticeAcknowledgementList(), "AClassListActivity");
            rv_acknowledge.setAdapter(noticeAcknowledApapter);
        }
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
