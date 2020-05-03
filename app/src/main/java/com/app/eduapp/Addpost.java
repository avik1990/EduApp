package com.app.eduapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.eduapp.databinding.AddpostActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.VolleySingleton;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Addpost extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    AddpostActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final String TAG = "TedPicker";
    ArrayList<Uri> image_uris = new ArrayList<>();
    Calendar myCalendar = Calendar.getInstance();
    String ClassID = "", SectionID = "", SubjectsID = "", DiaryID = "", json_image_string = "";
    ProgressDialog pDialog;
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("Loading...");
        db=new LCDatabaseHandler(mContext);
        ClassID = getIntent().getExtras().getString("ClassID");
        SectionID = getIntent().getExtras().getString("SectionID");
        SubjectsID = getIntent().getExtras().getString("SubjectsID");
        DiaryID = getIntent().getExtras().getString("DiaryID");

        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.addpost_activity_inc);
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.addpostActivity.toolbar.tvChild.setText("Student Diary");
        binding.navView.setItemIconTintList(null);
        binding.addpostActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.addpostActivity.btnAttachfile.setOnClickListener(this);
        binding.addpostActivity.btnCancel.setOnClickListener(this);
        binding.addpostActivity.btnSave.setOnClickListener(this);
        binding.addpostActivity.toolbar.cartView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.addpostActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.addpostActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    @Override
    public void onClick(View v) {
        if (v == binding.addpostActivity.btnAttachfile) {
            Config config = new Config();
            config.setCameraHeight(R.dimen.app_camera_height);
            config.setToolbarTitleRes(R.string.tedpickers);
            config.setSelectionMin(0);
            config.setSelectionLimit(5);
            config.setSelectedBottomHeight(R.dimen.bottom_height);
            config.setFlashOn(true);
            getImages(config);
        } else if (v == binding.addpostActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        }else if (v == binding.addpostActivity.toolbar.cartView) {
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


    private void getImages(Config config) {
        ImagePickerActivity.setConfig(config);
        Intent intent = new Intent(this, ImagePickerActivity.class);
        if (image_uris != null) {
            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                if (image_uris != null) {
                    showMedia();
                }
            }
        }
    }

    private void showMedia() {
        binding.addpostActivity.selectedPhotosContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            binding.addpostActivity.selectedPhotosContainer.setVisibility(View.VISIBLE);
        }
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (Uri uri : image_uris) {
            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item_upload, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            Glide.with(this)
                    .load(uri.toString())
                    .into(thumbnail);
            binding.addpostActivity.selectedPhotosContainer.addView(imageHolder);
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }
    }

    private void postTeachersDiary() {
        json_image_string = "{\"data\":[]}";
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.submitDiaryPostEMP) + "?EmployeeID=" + EdUtils.get_userID(mContext) + "&ClassID=" + ClassID + "&SectionID=" + SectionID + "&DiaryID=" + DiaryID + "&SubjectsID=" + SubjectsID;
        pDialog.show();
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
                                pDialog.dismiss();
                                EdUtils.showToastShort(mContext, msg);
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
                        Log.e("Volley", "Error");
                        pDialog.dismiss();
                    }
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("PostHeading", binding.addpostActivity.etReason.getText().toString());
                params.put("PostDescription", binding.addpostActivity.etLeavereason.getText().toString());
                params.put("AcceptAcknowledgement", "Y");
                params.put("AllowComments", "N");
                params.put("PostType", "");
                params.put("PostStudentsData", "");
                params.put("PostAttachemnetFileData", json_image_string);

                for (Object o : params.keySet()) {
                    String key = o.toString();
                    String value = params.get(key);
                    Log.d("posted_values", key + ":" + value);
                }
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

}
