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
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.eduapp.adapter.NoticecommentsAdapter;
import com.app.eduapp.adapter.StupostcommentsAdapter;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.common.RecyclerViewMargin;
import com.app.eduapp.databinding.ActivityStucommentsIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.VolleySingleton;
import com.app.eduapp.pojo.NCommentBean;
import com.app.eduapp.pojo.SCommentBean;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostComments extends AppCompatActivity implements ClickEventLisener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    StupostcommentsAdapter adapter;
    NoticecommentsAdapter nadapter;
    ActivityStucommentsIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    SCommentBean scommentBean;
    NCommentBean ncommentBean;
    ConnectionDetector cd;
    String ID;
    String user_type = "", from = "";
    String ids = "";
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stucomments_inc);
        mContext = this;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        cd = new ConnectionDetector(mContext);
        db = new LCDatabaseHandler(mContext);

        from = getIntent().getExtras().getString("from");
        if (from.equalsIgnoreCase("DiaryPostDetails")) {
            ID = getIntent().getExtras().getString("ID");
        } else if (from.equalsIgnoreCase("NoticeDetails")) {
            ID = getIntent().getExtras().getString("ID");
        }

        initView();

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
            user_type = "P";
            ids = "ParentsID";
        } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            user_type = "E";
            ids = "EmployeeID";
        }

        if (cd.isConnected()) {
            if (from.equalsIgnoreCase("DiaryPostDetails")) {
                getDiaryCommentList();
            } else if (from.equalsIgnoreCase("NoticeDetails")) {
                getNoticeCommentList();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityStupostcomment.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityStupostcomment.toolbar.tvCartcount.setText(String.valueOf(count));
    }


    private void initView() {
        binding.activityStupostcomment.toolbar.btnMenu.setOnClickListener(this);
        binding.activityStupostcomment.toolbar.tvChild.setText("Comments");
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityStupostcomment.imgSearch.setOnClickListener(this);
        binding.activityStupostcomment.toolbar.cartView.setOnClickListener(this);
    }

    private void showDiaryCommentList() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(false);
        adapter = new StupostcommentsAdapter(this, scommentBean.getData().getDiaryCommnetsList(), this);
        binding.activityStupostcomment.recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewMargin decoration = new RecyclerViewMargin(15, scommentBean.getData().getDiaryCommnetsList().size());
        binding.activityStupostcomment.recyclerView.addItemDecoration(decoration);
        binding.activityStupostcomment.recyclerView.setAdapter(adapter);
    }

    private void showNoticeCommentList() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(false);
        nadapter = new NoticecommentsAdapter(this, ncommentBean.getData().getNoticeCommnetsList(), this);
        binding.activityStupostcomment.recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewMargin decoration = new RecyclerViewMargin(15, ncommentBean.getData().getNoticeCommnetsList().size());
        binding.activityStupostcomment.recyclerView.addItemDecoration(decoration);
        binding.activityStupostcomment.recyclerView.setAdapter(nadapter);
    }


    @Override
    public void clickTrigger(View v, int position) {
       /* Intent intent = new Intent(mContext, ExamRoutineListActivity.class);
        startActivity(intent);*/
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
        if (v == binding.activityStupostcomment.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityStupostcomment.imgSearch) {
            if (!binding.activityStupostcomment.etComment.getText().toString().trim().isEmpty()) {
                if (from.equalsIgnoreCase("DiaryPostDetails")) {
                    postDiarycomment();
                } else if (from.equalsIgnoreCase("NoticeDetails")) {
                    postNoticecomment();
                }
            }
        } else if (v == binding.activityStupostcomment.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    private void getDiaryCommentList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<SCommentBean> call = redditAPI.GetOrPostDiaryCommnets(ID);
        call.enqueue(new Callback<SCommentBean>() {

            @Override
            public void onResponse(Call<SCommentBean> call, retrofit2.Response<SCommentBean> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    scommentBean = response.body();
                    if (!scommentBean.getStatus().equalsIgnoreCase("0")) {
                        if (scommentBean.getData().getDiaryCommnetsList().size() > 0) {
                            showDiaryCommentList();
                        }
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SCommentBean> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void getNoticeCommentList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<NCommentBean> call = redditAPI.GetOrPostNoticeCommnets(ID);
        call.enqueue(new Callback<NCommentBean>() {

            @Override
            public void onResponse(Call<NCommentBean> call, retrofit2.Response<NCommentBean> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    ncommentBean = response.body();
                    if (!ncommentBean.getStatus().equalsIgnoreCase("0")) {
                        if (ncommentBean.getData().getNoticeCommnetsList().size() > 0) {
                            showNoticeCommentList();
                        }
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<NCommentBean> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void postNoticecomment() {
        pDialog.show();
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.GetOrPostNoticeCommnets) + "?NoticeID=" + ID;
        Log.d("url_login", url_login);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            String msg = jObj.getString("Message");
                            if (status.equals("1")) {
                                pDialog.dismiss();
                                binding.activityStupostcomment.etComment.setText("");
                                getNoticeCommentList();
                            } else {
                                pDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                    }
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("IsCommentsSubmit", "Y");
                params.put("CommentsBy", user_type);
                params.put(ids, EdUtils.get_userID(mContext));
                params.put("CommentsBody", binding.activityStupostcomment.etComment.getText().toString().trim());

                for (Object o : params.keySet()) {
                    String key = o.toString();
                    String value = params.get(key);
                    Log.d("posted_values1", key + ":" + value);
                }
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }


    private void postDiarycomment() {
        pDialog.show();
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.GetOrPostDiaryCommnets) + "?DiaryPostID=" + ID;
        Log.d("url_login", url_login);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            String msg = jObj.getString("Message");
                            if (status.equals("1")) {
                                pDialog.dismiss();
                                binding.activityStupostcomment.etComment.setText("");
                                getDiaryCommentList();
                            } else {
                                pDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                    }
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("IsCommentsSubmit", "Y");
                params.put("CommentsBy", user_type);
                params.put(ids, EdUtils.get_userID(mContext));
                params.put("CommentsBody", binding.activityStupostcomment.etComment.getText().toString().trim());

                for (Object o : params.keySet()) {
                    String key = o.toString();
                    String value = params.get(key);
                    Log.d("posted_values2", key + ":" + value);
                }
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }


}
