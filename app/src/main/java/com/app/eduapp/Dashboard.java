package com.app.eduapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.eduapp.databinding.DashActivityIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.GetStudentsList;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.app.eduapp.tattendance.AClassListActivity;
import com.app.eduapp.teacher.TClassListActivity;
import com.gun0912.tedpicker.ImagePickerActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    DashActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    GetStudentsList getStudentsList;
    boolean doubleBackToExitPressedOnce = false;
    LCDatabaseHandler db;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.READ_PHONE_STATE,
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
        db = new LCDatabaseHandler(mContext);
        permissionStatus = getSharedPreferences("permissionStatus1", MODE_PRIVATE);
        Log.d("DeviceToken", EdUtils.getgcmtoken(mContext));
        initView();
        Log.d("DeviceToekn", EdUtils.getgcmtoken(mContext));
        if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
            if (cd.isConnected()) {
                if (!EdUtils.get_studentID(mContext).isEmpty()) {
                    pDialog.show();
                    getStudentList();
                }
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
        binding.dashActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.dashActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        binding = DataBindingUtil.setContentView(this, R.layout.dash_activity_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.dashActivity.vProfile.setOnClickListener(this);
        binding.dashActivity.vAttendance.setOnClickListener(this);
        binding.dashActivity.vLeaveapp.setOnClickListener(this);
        binding.dashActivity.vSettings.setOnClickListener(this);
        binding.dashActivity.vExam.setOnClickListener(this);
        binding.dashActivity.vResult.setOnClickListener(this);
        binding.dashActivity.vSignout.setOnClickListener(this);
        binding.dashActivity.vNote.setOnClickListener(this);
        binding.dashActivity.vFees.setOnClickListener(this);
        binding.dashActivity.vEventCalender.setOnClickListener(this);
        binding.dashActivity.vStudentdiary.setOnClickListener(this);
        binding.dashActivity.vRoutine.setOnClickListener(this);
        binding.dashActivity.toolbar.cartView.setOnClickListener(this);
        binding.dashActivity.toolbar.cartvie.setOnClickListener(this);
        binding.navView.setItemIconTintList(null);
        binding.dashActivity.toolbar.tvChild.setText("Dashboard");
        binding.dashActivity.toolbar.btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.dashActivity.vProfile) {
            if (cd.isConnected()) {
                if (checkpermission()) {
                    gotoprofile();
                }
            }
        } else if (v == binding.dashActivity.vAttendance) {
            if (cd.isConnected()) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    Intent i = new Intent(mContext, AClassListActivity.class);
                    startActivity(i);
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    Intent i = new Intent(mContext, MonthListActivity.class);
                    startActivity(i);
                }
            }
        } else if (v == binding.dashActivity.vLeaveapp) {
            if (checkpermission()) {
                if (cd.isConnected()) {
                    if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                        Intent i = new Intent(mContext, LeaveApplicationList.class);
                        startActivity(i);
                    } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                        Intent i = new Intent(mContext, LeaveListActivity.class);
                        startActivity(i);
                    }
                    //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        } else if (v == binding.dashActivity.vSettings) {
            if (cd.isConnected()) {
                Intent i = new Intent(mContext, SettingActivity.class);
                startActivity(i);
            }
        } else if (v == binding.dashActivity.vExam) {
            if (cd.isConnected()) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    Intent i = new Intent(mContext, TClassListActivity.class);
                    i.putExtra("from", "exam");
                    startActivity(i);
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    Intent i = new Intent(mContext, ExamListActivity.class);
                    startActivity(i);
                }
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        } else if (v == binding.dashActivity.vResult) {
            if (cd.isConnected()) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    Intent i = new Intent(mContext, TClassListActivity.class);
                    i.putExtra("from", "result");
                    startActivity(i);
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    Intent i = new Intent(mContext, ExamsHeldListActivity.class);
                    startActivity(i);
                }
            }
        } else if (v == binding.dashActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.dashActivity.vSignout) {
            EdUtils.showLogoutAlert(mContext, "Are you sure?", "Log Out");
        } else if (v == binding.dashActivity.vNote) {
            if (checkpermission()) {
                Intent i = new Intent(mContext, NoticeActivity.class);
                startActivity(i);
            }
        } else if (v == binding.dashActivity.vFees) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                EdUtils.showToastShort(mContext, "Not accessible for teachers");
            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                Intent i = new Intent(mContext, Fees.class);
                startActivity(i);
            }
        } else if (v == binding.dashActivity.vStudentdiary) {
            if (checkpermission()) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    Intent i = new Intent(mContext, TClassListActivity.class);
                    i.putExtra("from", "diary");
                    startActivity(i);
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    Intent i = new Intent(mContext, StudentDiaryActivity.class);
                    startActivity(i);
                }
            }
        } else if (v == binding.dashActivity.vEventCalender) {
            Intent i = new Intent(mContext, EventCalender.class);
            startActivity(i);
        } else if (v == binding.dashActivity.vRoutine) {
            if (cd.isConnected()) {
                if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                    Intent i = new Intent(mContext, RoutineType.class);
                    i.putExtra("from", "routine");
                    startActivity(i);
                } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                    Intent i = new Intent(mContext, RoutineActivity.class);
                    startActivity(i);
                }
            }
        } else if (v == binding.dashActivity.toolbar.cartView) {
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

    private void getStudentList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url) + "" + getResources().getString(R.string.getStudentsList);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetStudentsList> call = redditAPI.getStudentsList(EdUtils.get_userID(mContext));
        call.enqueue(new Callback<GetStudentsList>() {

            @Override
            public void onResponse(Call<GetStudentsList> call, retrofit2.Response<GetStudentsList> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getStudentsList = response.body();
                    if (getStudentsList.getData().getStudentsList().size() > 0) {
                        EdUtils.set_RadioChecked(mContext, "0");
                        EdUtils.set_studentID(mContext, getStudentsList.getData().getStudentsList().get(0).getStudentsID());
                        EdUtils.set_sectionID(mContext, getStudentsList.getData().getStudentsList().get(0).getSectionID());
                        EdUtils.set_classID(mContext, getStudentsList.getData().getStudentsList().get(0).getClassID());
                    }
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetStudentsList> call, Throwable t) {
                Log.d("Error", t.getMessage());
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        EdUtils.showToastShort(mContext, "Press again to exit");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private boolean checkpermission() {
        boolean flag = false;
        if (ActivityCompat.checkSelfPermission(mContext, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[2])
                    || ActivityCompat.checkSelfPermission(mContext, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Need Permission");
                builder.setMessage("This app needs multiple Permission.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Dashboard.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
                builder.setTitle("Need Permission");
                builder.setMessage("This app needs multiple Permission.");
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
                ActivityCompat.requestPermissions(Dashboard.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(mContext, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                //opencamera();
            }
        }
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
                // gotoprofile();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[3])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Multiple permissions.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Dashboard.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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

    private void gotoprofile() {

        Intent i = new Intent(mContext, Profile.class);
        startActivity(i);
    }


}
