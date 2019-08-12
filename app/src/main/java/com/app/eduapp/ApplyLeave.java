package com.app.eduapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.eduapp.databinding.LeaveapplyActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.RetroClient;
import com.app.eduapp.helper.VolleySingleton;
import com.app.eduapp.pojo.FileUploadResponse;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ApplyLeave extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    LeaveapplyActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ArrayList<Uri> image_uris = new ArrayList<>();
    Calendar myCalendar = Calendar.getInstance();
    List<MultipartBody.Part> parts = new ArrayList<>();
    FileUploadResponse responseApiModel;
    ProgressDialog pDialog;
    String json_image_string = "";
    String startDate = "", endDate = "";
    String url_login = "", user_type = "";
    LCDatabaseHandler db;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("Loading...");
        db = new LCDatabaseHandler(mContext);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        // EdUtils.showToastShort(mContext, user_type);
        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            user_type = "TCH";
        } else {
            user_type = "STU";
        }

        initView();

        startDate = EdUtils.getCurrentDate();
        endDate = EdUtils.getCurrentDate();
        binding.leaveapplyActivity.tvFromdate.setText("From : " + startDate);
        binding.leaveapplyActivity.tvTodate.setText("To : " + endDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.leaveapplyActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.leaveapplyActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.leaveapply_activity_inc);
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.leaveapplyActivity.toolbar.tvChild.setText("Leave Application");
        binding.leaveapplyActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.leaveapplyActivity.btnAttachfile.setOnClickListener(this);
        binding.leaveapplyActivity.btnCancel.setOnClickListener(this);
        binding.leaveapplyActivity.btnSave.setOnClickListener(this);
        binding.leaveapplyActivity.tvFromdate.setOnClickListener(this);
        binding.leaveapplyActivity.tvTodate.setOnClickListener(this);
        binding.leaveapplyActivity.toolbar.cartView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == binding.leaveapplyActivity.btnAttachfile) {
            if (checkpermission()) {
                opencamera();
            }
        } else if (v == binding.leaveapplyActivity.tvFromdate) {
            DatePickerDialog dpd = new DatePickerDialog(ApplyLeave.this, fromdate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dpd.getDatePicker().setMinDate(System.currentTimeMillis());

           /* try {
                String st_fromdate = binding.activityStudiarypost.tvToDate.getText().toString().replace("To : ", "").trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(st_fromdate);
                    long millis = date.getTime();
                    fromdatedialog.getDatePicker().setMaxDate(millis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/


            dpd.show();

        } else if (v == binding.leaveapplyActivity.tvTodate) {
            DatePickerDialog todateDialog = new DatePickerDialog(ApplyLeave.this, todate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            try {
                String st_fromdate = binding.leaveapplyActivity.tvFromdate.getText().toString().replace("From : ", "").trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(st_fromdate);
                    long millis = date.getTime();
                    todateDialog.getDatePicker().setMinDate(millis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            todateDialog.show();
        } else if (v == binding.leaveapplyActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.leaveapplyActivity.btnCancel) {
            finish();
        } else if (v == binding.leaveapplyActivity.btnSave) {
            if (!binding.leaveapplyActivity.etReason.getText().toString().trim().isEmpty()) {
                if (!binding.leaveapplyActivity.etLeavereason.getText().toString().trim().isEmpty()) {
                    if (image_uris.size() > 0) {
                        for (int i = 0; i < image_uris.size(); i++) {
                            parts.add(preparefilepart(image_uris.get(i)));
                        }
                        uploadfile();
                    } else {
                        json_image_string = "{\"data\":[]}";
                        postTeachersDiary();
                    }
                } else {
                    EdUtils.showToastShort(mContext, "Enter Leave Details");
                }
            } else {
                EdUtils.showToastShort(mContext, "Enter Leave Reason");
            }
        } else if (v == binding.leaveapplyActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    private void opencamera() {
        Config config = new Config();
        config.setCameraHeight(R.dimen.app_camera_height);
        config.setToolbarTitleRes(R.string.tedpickers);
        config.setSelectionMin(0);
        config.setSelectionLimit(3);
        config.setSelectedBottomHeight(R.dimen.bottom_height);
        config.setFlashOn(true);
        getImages(config);
    }

    private boolean checkpermission() {
        boolean flag = false;
        if (ActivityCompat.checkSelfPermission(mContext, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ApplyLeave.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ApplyLeave.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ApplyLeave.this, permissionsRequired[2])
            ) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ApplyLeave.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        //  Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(ApplyLeave.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            flag = true;
        }

        return flag;

    }

    private MultipartBody.Part preparefilepart(Uri uri) {
        File bin = new File(uri.toString());
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), bin);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("PostFileName[]", bin.getName(), reqBody);
        return partImage;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String years;
            String month1 = "";
            years = "" + year;
            int month = monthOfYear;
            month = month + 1;
            if (month < 10) {
                month1 = "0" + month;
            } else {
                month1 = String.valueOf(month);
            }
            startDate = dayOfMonth + "/" + month1 + "/" + years;
            binding.leaveapplyActivity.tvFromdate.setText("From : " + startDate);
        }
    };

    DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String years;
            String month1 = "";
            years = "" + year;
            int month = monthOfYear;
            month = month + 1;
            if (month < 10) {
                month1 = "0" + month;
            } else {
                month1 = String.valueOf(month);
            }
            endDate = dayOfMonth + "/" + month1 + "/" + years;
            binding.leaveapplyActivity.tvTodate.setText("To : " + endDate);
        }
    };

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
                Log.d("image_uris", "" + image_uris);
                if (image_uris != null) {
                    showMedia();
                }
            }
        } else if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(mContext, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                //opencamera();
            }
        }
    }

    private void showMedia() {
        binding.leaveapplyActivity.selectedPhotosContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            binding.leaveapplyActivity.selectedPhotosContainer.setVisibility(View.VISIBLE);
        }
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (Uri uri : image_uris) {
            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            Glide.with(this)
                    .load(uri.toString())
                    .into(thumbnail);
            binding.leaveapplyActivity.selectedPhotosContainer.addView(imageHolder);
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }
    }


    private void uploadfile() {
        pDialog.show();
        ApiServices api = RetroClient.getApiServices();
        Call<FileUploadResponse> upload = api.uploadImage(parts);

        upload.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(Call<FileUploadResponse> call, retrofit2.Response<FileUploadResponse> response) {
                pDialog.dismiss();
                responseApiModel = response.body();
                Log.d("RETRO", "ON RESPONSE  : " + response.body().toString());
                if (response.body().getStatus().equals("1")) {
                    if (response.body().getData().getUploadedFileName().size() > 0) {
                        json_image_string = getJsonString(response.body().getData());
                        postTeachersDiary();
                    }
                } else {
                    EdUtils.showToastShort(mContext, "Failure");
                }
            }

            @Override
            public void onFailure(Call<FileUploadResponse> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                pDialog.dismiss();
            }
        });

    }


    private String getJsonString(FileUploadResponse.Dataum student) {
        Gson gson = null;
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(student);
    }


    private void postTeachersDiary() {
        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            url_login = mContext.getResources().getString(R.string.base_url)
                    + getResources().getString(R.string.postLeaveApplication) + "?EmployeeID=" + EdUtils.get_userID(mContext);
        } else {
            url_login = mContext.getResources().getString(R.string.base_url)
                    + getResources().getString(R.string.postLeaveApplication) + "?StudentsID=" + EdUtils.get_studentID(mContext);
        }

        pDialog.show();
        Log.d("url_login", url_login);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {
                    Boolean flag = false;

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            String msg = jObj.getString("Message");
                            if (status.equals("1")) {
                                pDialog.dismiss();
                                EdUtils.showToastShort(mContext, msg);
                                finish();
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
                params.put("LeaveTitle", binding.leaveapplyActivity.etReason.getText().toString());
                params.put("LeaveDescription", binding.leaveapplyActivity.etLeavereason.getText().toString());
                params.put("LeaveStartDate", startDate.replaceAll("/", "-"));
                params.put("LeaveEndDate", endDate.replaceAll("/", "-"));
                params.put("LeaveApplicationBy", user_type);
                params.put("PostAttachemnetFileData", json_image_string.replace("UploadedFileName", "data"));
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(mContext, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                // opencamera();
            }
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                opencamera();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(ApplyLeave.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ApplyLeave.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ApplyLeave.this, permissionsRequired[2])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApplyLeave.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ApplyLeave.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
