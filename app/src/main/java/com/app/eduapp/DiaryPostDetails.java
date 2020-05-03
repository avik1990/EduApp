package com.app.eduapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.app.eduapp.adapter.DiaryStudentsListApapter;
import com.app.eduapp.adapter.ViewPagerAdapter;
import com.app.eduapp.databinding.StupostdetailsIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.PhotoLoader;
import com.app.eduapp.helper.VolleySingleton;
import com.app.eduapp.pojo.ApproveDisapprove;
import com.app.eduapp.pojo.DiaryDetails;
import com.app.eduapp.pojo.GetOrPostDiaryAcknowledgement;
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

public class DiaryPostDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    List<String> imagelist = new ArrayList<>();
    StupostdetailsIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    DiaryDetails diaryDetails;
    GetOrPostDiaryAcknowledgement getOrPostDiaryAcknowledgement;
    String DiaryPostID = "", SectionID = "", SubjectsID = "", ClassID = "";
    LCDatabaseHandler db;
    Dialog myDialog;
    SimpleDateFormat dateFormat;
    Calendar cal;
    int position = 0;
    DiaryAcknowledApapter diaryAcknowledApapter;
    DiaryStudentsListApapter diaryStudentsListApapter;

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
        myDialog = new Dialog(this);
        dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
        cal = Calendar.getInstance();

        DiaryPostID = getIntent().getExtras().getString("DiaryPostID");
        SectionID = getIntent().getExtras().getString("SectionID");
        SubjectsID = getIntent().getExtras().getString("SubjectsID");
        ClassID = getIntent().getExtras().getString("ClassID");

        // EdUtils.showToastShort(mContext,"hello");
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
        binding.activityStudiarypostdetails.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityStudiarypostdetails.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.stupostdetails_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityStudiarypostdetails.toolbar.tvChild.setText("Student Diary");
        binding.navView.setItemIconTintList(null);
        binding.activityStudiarypostdetails.toolbar.btnMenu.setOnClickListener(this);
        binding.activityStudiarypostdetails.tvAddcomment.setOnClickListener(this);
        binding.activityStudiarypostdetails.vDelete.setOnClickListener(this);
        binding.activityStudiarypostdetails.icEdit.setOnClickListener(this);
        binding.activityStudiarypostdetails.toolbar.cartView.setOnClickListener(this);
        binding.activityStudiarypostdetails.rlvScknowledge.setOnClickListener(this);
        binding.activityStudiarypostdetails.rlvAssigned.setOnClickListener(this);

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            // binding.activityStudiarypostdetails.vDelete.setVisibility(View.VISIBLE);
            binding.activityStudiarypostdetails.icEdit.setVisibility(View.GONE);
            // binding.activityStudiarypostdetails.rlvScknowledge.setVisibility(View.VISIBLE);
            binding.activityStudiarypostdetails.rlvAssigned.setVisibility(View.VISIBLE);
            // binding.activityStudiarypostdetails.tv.setVisibility(View.VISIBLE);

        } else {
            // binding.activityStudiarypostdetails.tvtype.
            binding.activityStudiarypostdetails.rlvAssigned.setVisibility(View.GONE);
            //  binding.activityStudiarypostdetails.rlvScknowledge.setVisibility(View.VISIBLE);
            //binding.activityStudiarypostdetails.vDelete.setVisibility(View.GONE);
            binding.activityStudiarypostdetails.icEdit.setVisibility(View.GONE);
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
        if (v == binding.activityStudiarypostdetails.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityStudiarypostdetails.tvAddcomment) {
            Intent i = new Intent(mContext, PostComments.class);
            i.putExtra("ID", DiaryPostID);
            i.putExtra("from", "DiaryPostDetails");
            startActivity(i);
        } else if (v == binding.activityStudiarypostdetails.vDelete) {
            deletepost();
        } else if (v == binding.activityStudiarypostdetails.icEdit) {
            EdUtils.showToastShort(mContext, "Edit Clicked");
        } else if (v == binding.activityStudiarypostdetails.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        } else if (v == binding.activityStudiarypostdetails.rlvAssigned) {
            setupStudentListView();
        } else if (v == binding.activityStudiarypostdetails.rlvScknowledge) {

            if (diaryDetails.getData().getDiaryDetails().getDiaryApplicationBy().equalsIgnoreCase("PRN")) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    //  binding.activityStudiarypostdetails.tvMonth.setText("Acknowledge");
                    if (cd.isConnected()) {
                        pDialog.show();
                        PostAcknowledge();
                    }
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    // binding.activityStudiarypostdetails.tvMonth.setText("Acknowledged By");
                    if (cd.isConnected()) {
                        GetOrPostDiaryAcknowledgement();
                    }
                }
            } else if (diaryDetails.getData().getDiaryDetails().getDiaryApplicationBy().equalsIgnoreCase("TCH")) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    //binding.activityStudiarypostdetails.tvMonth.setText("Acknowledge By");
                    if (cd.isConnected()) {
                        GetOrPostDiaryAcknowledgement();
                    }
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    // binding.activityStudiarypostdetails.tvMonth.setText("Acknowledge");
                    if (cd.isConnected()) {
                        pDialog.show();
                        PostAcknowledge();
                    }
                }
            }
            /*if (diaryDetails.getData().getDiaryDetails().getDiaryApplicationBy().equalsIgnoreCase("PRN")) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    if (cd.isConnected()) {
                        pDialog.show();
                        PostAcknowledge();
                    }
                } else {
                    if (cd.isConnected()) {
                        GetOrPostDiaryAcknowledgement();
                    }
                }
            } else {
                if (cd.isConnected()) {
                    GetOrPostDiaryAcknowledgement();
                }
            }*/
           /* if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                if (cd.isConnected()) {
                    GetOrPostDiaryAcknowledgement();
                }
            } else {
                if (cd.isConnected()) {
                    pDialog.show();
                    PostAcknowledge();
                }
            }*/
        }
    }

    private void getNoticeDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<DiaryDetails> call;

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            call = redditAPI.getDiaryDetailsEMP(EdUtils.get_userID(mContext), ClassID, SectionID, SubjectsID, DiaryPostID);
        } else {
            call = redditAPI.getDiaryDetails(EdUtils.get_studentID(mContext), ClassID, SectionID, SubjectsID, DiaryPostID);
        }

        call.enqueue(new Callback<DiaryDetails>() {

            @Override
            public void onResponse(Call<DiaryDetails> call, retrofit2.Response<DiaryDetails> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    diaryDetails = response.body();
                    if (diaryDetails.getStatus().equalsIgnoreCase("1")) {
                        // EdUtils.showToastShort(mContext,""+diaryDetails.getData().getDiaryDetails().getDiaryStudentsList().size());
                        setupView();
                    }
                    //}adadasda
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DiaryDetails> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setupView() {
        binding.activityStudiarypostdetails.tvDate.setText(diaryDetails.getData().getDiaryDetails().getDiaryPostDate());
        binding.activityStudiarypostdetails.tvNoticedata.setText(diaryDetails.getData().getDiaryDetails().getPostDescription());
        binding.activityStudiarypostdetails.tvDesignation.setText(diaryDetails.getData().getDiaryDetails().getEmployeeDesignation());
        binding.activityStudiarypostdetails.tvHeading.setText(diaryDetails.getData().getDiaryDetails().getPostHeading());
        binding.activityStudiarypostdetails.tvEmpname.setText(diaryDetails.getData().getDiaryDetails().getEmployeeName());
        binding.activityStudiarypostdetails.tvMonthV.setText(diaryDetails.getData().getDiaryDetails().getClassName() + " Section " + diaryDetails.getData().getDiaryDetails().getSectionName() + " " + diaryDetails.getData().getDiaryDetails().getSubjectsName());

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            if (diaryDetails.getData().getDiaryDetails().PostType.equalsIgnoreCase("STU")) {
                binding.activityStudiarypostdetails.rlvAssigned.setVisibility(View.VISIBLE);
            } else {
                binding.activityStudiarypostdetails.rlvAssigned.setVisibility(View.GONE);
            }
        } else {
            binding.activityStudiarypostdetails.rlvAssigned.setVisibility(View.GONE);
        }

        if (diaryDetails.getData().getDiaryDetails().getDiaryApplicationBy().equalsIgnoreCase("PRN")) {
            binding.activityStudiarypostdetails.tvAssigned.setText("View Student");
            binding.activityStudiarypostdetails.tvType.setText("Posted By Parent: " + diaryDetails.getData().getDiaryDetails().getParentsName() + "\nStudent Name: " + diaryDetails.getData().getDiaryDetails().getDiaryStudentsList().get(0).getFullName());
        } else {
            binding.activityStudiarypostdetails.tvAssigned.setText("Assigned Students");
        }
        /*if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
            if (EdUtils.get_userID(mContext).equalsIgnoreCase(diaryDetails.getData().getDiaryDetails().getPostByUserID())) {
                binding.activityStudiarypostdetails.rlvScknowledge.setVisibility(View.GONE);
            }
        }*/

        if (diaryDetails.getData().getDiaryDetails().getAcceptAcknowledgement().equals("Y")) {
            binding.activityStudiarypostdetails.rlvScknowledge.setVisibility(View.VISIBLE);
            if (diaryDetails.getData().getDiaryDetails().getDiaryApplicationBy().equalsIgnoreCase("PRN")) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    binding.activityStudiarypostdetails.tvMonth.setText("Acknowledge");
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    binding.activityStudiarypostdetails.tvMonth.setText("Acknowledged By");
                }
            } else if (diaryDetails.getData().getDiaryDetails().getDiaryApplicationBy().equalsIgnoreCase("TCH")) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    binding.activityStudiarypostdetails.tvMonth.setText("Acknowledge By");
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    binding.activityStudiarypostdetails.tvMonth.setText("Acknowledge");
                }
            }
        } else {
            binding.activityStudiarypostdetails.rlvScknowledge.setVisibility(View.GONE);
        }


        if (diaryDetails.getData().getDiaryDetails().getAttachemnetFiles().size() > 0) {
            showphotolist();
        }

        binding.activityStudiarypostdetails.vDelete.setVisibility(View.GONE);

       /* if (EdUtils.get_userType(mContext).equals(diaryDetails.getData().getDiaryDetails().getDiaryApplicationBy())) {
            if (diaryDetails.getData().getDiaryDetails().getPostByUserID().equals(EdUtils.get_userID(mContext))) {
                binding.activityStudiarypostdetails.vDelete.setVisibility(View.VISIBLE);
            } else {
                binding.activityStudiarypostdetails.vDelete.setVisibility(View.GONE);
            }
        }*/


    }

    private void showphotolist() {
        imagelist.clear();
        binding.activityStudiarypostdetails.selectedPhotosContainer.removeAllViews();
        if (diaryDetails.getData().getDiaryDetails().getAttachemnetFiles().size() >= 1) {
            binding.activityStudiarypostdetails.selectedPhotosContainer.setVisibility(View.VISIBLE);
        }

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        for (int i = 0; i < diaryDetails.getData().getDiaryDetails().getAttachemnetFiles().size(); i++) {
            imagelist.add(diaryDetails.getData().getDiaryDetails().getAttachemnetFiles().get(i).getOriginalAttachemnetFile());
            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item_upload, null);
            ImageView thumbnail = imageHolder.findViewById(R.id.media_image);

            Picasso.get()
                    .load(diaryDetails.getData().getDiaryDetails().getAttachemnetFiles().get(i).getAttachemnetFile())
                    .into(thumbnail);

            binding.activityStudiarypostdetails.selectedPhotosContainer.addView(imageHolder);
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

    private void deletepost() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ApproveDisapprove> call = redditAPI.deleteDiaryPost(EdUtils.get_userID(mContext), ClassID, SectionID, SubjectsID, DiaryPostID);
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


    private void PostAcknowledge() {
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.GetOrPostDiaryAcknowledgement) + "?DiaryPostID=" + DiaryPostID;

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
                if (diaryDetails.getData().getDiaryDetails().getDiaryApplicationBy().equalsIgnoreCase("PRN")) {
                    params.put("EmployeeID", EdUtils.get_userID(mContext));
                } else {
                    params.put("ParentsID", EdUtils.get_userID(mContext));
                }
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


    private void GetOrPostDiaryAcknowledgement() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetOrPostDiaryAcknowledgement> call = null;

        // if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
        call = redditAPI.GetOrPostDiaryAcknowledgement(DiaryPostID);
        //  }

        call.enqueue(new Callback<GetOrPostDiaryAcknowledgement>() {

            @Override
            public void onResponse(Call<GetOrPostDiaryAcknowledgement> call, retrofit2.Response<GetOrPostDiaryAcknowledgement> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getOrPostDiaryAcknowledgement = response.body();
                    if (getOrPostDiaryAcknowledgement.getStatus().equalsIgnoreCase("1")) {
                        setupListView();
                    } else {
                        EdUtils.showToastShort(mContext, getOrPostDiaryAcknowledgement.getMessage());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetOrPostDiaryAcknowledgement> call, Throwable t) {
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
        if (getOrPostDiaryAcknowledgement.getData().getDiaryAcknowledgementList().size() > 0) {
            diaryAcknowledApapter = new DiaryAcknowledApapter(mContext, R.layout.row_studentlist, getOrPostDiaryAcknowledgement.getData().getDiaryAcknowledgementList(), "AClassListActivity");
            rv_acknowledge.setAdapter(diaryAcknowledApapter);
        }
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    private void setupStudentListView() {
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
        if (diaryDetails.getData().getDiaryDetails().getDiaryStudentsList().size() > 0) {
            diaryStudentsListApapter = new DiaryStudentsListApapter(mContext, R.layout.row_studentlist, diaryDetails.getData().getDiaryDetails().getDiaryStudentsList(), "AClassListActivity");
            rv_acknowledge.setAdapter(diaryStudentsListApapter);
        }
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
