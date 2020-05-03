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
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.eduapp.databinding.TaddpostIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.FileUtils;
import com.app.eduapp.helper.RetroClient;
import com.app.eduapp.helper.VolleySingleton;
import com.app.eduapp.pojo.FileUploadResponse;
import com.app.eduapp.pojo.GetEmployeeListBySubjectsClass;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParentDiaryPost extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Context mContext;
    TaddpostIncBinding binding;
    ConnectionDetector cd;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ArrayList<Uri> image_uris = new ArrayList<>();
    String st_ack = "Y", st_allowc = "Y";
    ProgressDialog pDialog;
    String ClassID = "", SectionID = "", SubjectsID = "", DiaryID = "", studentwise = "";
    // StudentsListByClassSectionEMP studentsListByClassSectionEMP;
    List<Integer> list_stu_ids = new ArrayList<>();
    View view;
    CheckBox checkbox;
    String post_type, json_student_string = "", json_image_string = "";
    boolean student_selected = false;
    private String finalfileUploadPath = "";
    List<MultipartBody.Part> parts = new ArrayList<>();
    FileUploadResponse responseApiModel;
    String ClassName = "", SectionName = "", SubjectsName = "";
    LCDatabaseHandler db;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String[] EmployeeID_arr, EmployeeName_arr;
    String EmployeeID, EmployeeName;
    GetEmployeeListBySubjectsClass getEmployeeListBySubjectsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        db = new LCDatabaseHandler(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("Loading...");
        ClassID = getIntent().getExtras().getString("ClassID");
        SectionID = getIntent().getExtras().getString("SectionID");
        SubjectsID = getIntent().getExtras().getString("SubjectsID");
        DiaryID = getIntent().getExtras().getString("DiaryID");
        //  studentwise = getIntent().getExtras().getString("studentwise");
        ClassName = getIntent().getExtras().getString("ClassName");
        SectionName = getIntent().getExtras().getString("SectionName");
        SubjectsName = getIntent().getExtras().getString("SubjectsName");
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        post_type = "STU";
        list_stu_ids.clear();
        list_stu_ids.add(Integer.parseInt(EdUtils.get_studentID(mContext)));

        initView();
        if (cd.isConnected()) {
            binding.taddpostActivity.vSp1.setVisibility(View.VISIBLE);
            getEmployeeList();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.taddpostActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.taddpostActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.taddpost__inc);
        binding.navView.setItemIconTintList(null);
        binding.taddpostActivity.toolbar.tvChild.setText("Student Diary");
        binding.navView.setNavigationItemSelectedListener(this);
        binding.taddpostActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.taddpostActivity.btnAttachfile.setOnClickListener(this);
        binding.taddpostActivity.tvClass.setText(ClassName + " Section " + SectionName + " " + SubjectsName);
        binding.taddpostActivity.btnCancel.setOnClickListener(this);
        binding.taddpostActivity.btnSave.setOnClickListener(this);
        binding.taddpostActivity.toolbar.cartView.setOnClickListener(this);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.chk_acknow:
                if (checked) {
                    st_ack = "Y";
                } else
                    st_ack = "N";
                break;
            case R.id.chk_allowcomments:
                if (checked) {
                    st_allowc = "Y";
                } else
                    st_allowc = "N";
                break;
        }
    }


    @Override
    public void onClick(View v) {
        if (v == binding.taddpostActivity.btnAttachfile) {
            if (checkpermission()) {
                opencamera();
            }
        } else if (v == binding.taddpostActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.taddpostActivity.btnSave) {
            // EdUtils.showToastShort(mContext, "" + image_uris.size());
            // uploadFileTosServer(image_uris);
            if (cd.isConnected()) {
                if (!EmployeeID.isEmpty()) {
                    if (!binding.taddpostActivity.etReason.getText().toString().trim().isEmpty()) {
                        if (!binding.taddpostActivity.etLeavereason.getText().toString().trim().isEmpty()) {
                            if (post_type.equalsIgnoreCase("STU")) {
                                for (int i = 0; i < list_stu_ids.size(); i++) {
                                    if (!list_stu_ids.get(i).toString().equalsIgnoreCase("0")) {
                                        createJson(list_stu_ids.get(i).toString());
                                    }
                                }
                                if (!json_student_string.isEmpty()) {
                                    if (image_uris.size() > 0) {
                                        for (int i = 0; i < image_uris.size(); i++) {
                                            parts.add(preparefilepart(image_uris.get(i)));
                                        }
                                        uploadfile();
                                    } else {
                                        json_image_string = "{\"data\":[]}";
                                        postParentDiary();
                                    }
                                } else {
                                    EdUtils.showToastShort(mContext, "Select Students");
                                }
                            } else {
                                json_student_string = "";
                                if (image_uris.size() > 0) {
                                    for (int i = 0; i < image_uris.size(); i++) {
                                        parts.add(preparefilepart(image_uris.get(i)));
                                    }
                                    uploadfile();
                                } else {
                                    json_image_string = "{\"data\":[]}";
                                    postParentDiary();
                                }
                            }
                        } else {
                            EdUtils.showToastShort(mContext, "Enter Description");
                        }
                    } else {
                        EdUtils.showToastShort(mContext, "Enter Reason");
                    }
                } else {
                    EdUtils.showToastShort(mContext, "Select Teacher");
                }


            } else {
                EdUtils.showToastShort(mContext, "No Internet Connection");
            }
        } else if (v == binding.taddpostActivity.btnCancel) {
            finish();
        } else if (v == binding.taddpostActivity.toolbar.cartView) {
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

    private MultipartBody.Part preparefilepart(Uri uri) {
        File bin = new File(uri.toString());
        /*FileBody bin1 = new FileBody(bin);

        part_image = getRealPathFromURI(uri);
        File imagefile = new File(part_image);*/
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), bin);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("PostFileName[]", bin.getName(), reqBody);

        return partImage;
    }


    private String createJson(String ids) {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject item = new JSONObject();
        try {
            item.put("StudentsID", ids);
            array.put(item);
            json.put("data", array);
            Log.d("Json", json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        json_student_string = json.toString();
        student_selected = true;
        return json.toString();
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
        } else if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(mContext, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                //opencamera();
            }
        }
    }

    private void showMedia() {
        binding.taddpostActivity.selectedPhotosContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            binding.taddpostActivity.selectedPhotosContainer.setVisibility(View.VISIBLE);
        }
        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (Uri uri : image_uris) {
            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item_upload, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            Glide.with(this)
                    .load(uri.toString())
                    .into(thumbnail);
            binding.taddpostActivity.selectedPhotosContainer.addView(imageHolder);
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }
    }

    private void postParentDiary() {
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.submitDiaryPostParent) + "?StudentsID=" + EdUtils.get_studentID(mContext) + "&ClassID=" + ClassID + "&SectionID=" + SectionID + "&DiaryID=" + DiaryID + "&SubjectsID=" + SubjectsID;
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
                params.put("PostHeading", binding.taddpostActivity.etReason.getText().toString());
                params.put("PostDescription", binding.taddpostActivity.etLeavereason.getText().toString());
                params.put("AcceptAcknowledgement", st_ack);
                params.put("AllowComments", st_allowc);
                params.put("PostType", post_type);
                params.put("PostStudentsData", json_student_string);
                params.put("EmployeeID", EmployeeID);
                params.put("ParentsID", EdUtils.get_userID(mContext));
                params.put("PostAttachemnetFileData", json_image_string.replace("UploadedFileName", "data"));

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
                    //EdUtils.showToastShort(mContext, response.body().getMessage());
                    if (response.body().getData().getUploadedFileName().size() > 0) {

                        json_image_string = getJsonString(response.body().getData());
                        postParentDiary();
                        /*json_image_string=
                        postTeachersDiary();*/
                    }
                } else {
                    EdUtils.showToastShort(mContext, "Failure");
                    /*Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();*/
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
        // Before converting to GSON check value of id
        Gson gson = null;
        //  if (student.i == 0) {
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        /*} else {
            gson = new Gson();
        }*/
        //Log.d("JsonValue", gson.toJson(student).replace("UploadedFileName", "data"));
        return gson.toJson(student);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        //https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        //File file = new File(FileUtils.getPath(this, fileUri));
        finalfileUploadPath = FileUtils.getPath(mContext, fileUri);
        //File file = new File(FileUtils.getPath(mContext, fileUri));
        File file = new File(finalfileUploadPath);

        // create RequestBody instance from file
        RequestBody requestFile = null;
        try {
            requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MultipartBody.Part.createFormData(partName, "test", requestFile);
    }


    private boolean checkpermission() {
        boolean flag = false;
        if (ActivityCompat.checkSelfPermission(mContext, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ParentDiaryPost.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ParentDiaryPost.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ParentDiaryPost.this, permissionsRequired[2])
                    ) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ParentDiaryPost.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
                ActivityCompat.requestPermissions(ParentDiaryPost.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(ParentDiaryPost.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ParentDiaryPost.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ParentDiaryPost.this, permissionsRequired[2])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentDiaryPost.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ParentDiaryPost.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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


    private void getEmployeeList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<GetEmployeeListBySubjectsClass> call = redditAPI.GetEmployeeListBySubjectsClass(EdUtils.get_classID(mContext), EdUtils.get_sectionID(mContext));
        call.enqueue(new Callback<GetEmployeeListBySubjectsClass>() {

            @Override
            public void onResponse(Call<GetEmployeeListBySubjectsClass> call, retrofit2.Response<GetEmployeeListBySubjectsClass> response) {
                Log.d("Response", response.toString());
                if (response.isSuccessful()) {
                    getEmployeeListBySubjectsClass = response.body();
                    if (getEmployeeListBySubjectsClass.getStatus().equalsIgnoreCase("1")) {
                        if (getEmployeeListBySubjectsClass.getData().getEmployeeList().size() > 0) {
                            inflateSpinner();
                        }
                    } else {
                        EdUtils.showToastShort(mContext, getEmployeeListBySubjectsClass.getMessage());
                    }

                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetEmployeeListBySubjectsClass> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void inflateSpinner() {
        if (getEmployeeListBySubjectsClass.getData().getEmployeeList().size() > 0) {
            EmployeeName_arr = new String[getEmployeeListBySubjectsClass.getData().getEmployeeList().size() + 1];
            EmployeeName_arr[0] = "Select Teacher";

            for (int i = 0; i < getEmployeeListBySubjectsClass.getData().getEmployeeList().size(); i++) {
                EmployeeName_arr[i + 1] = getEmployeeListBySubjectsClass.getData().getEmployeeList().get(i).getEmployeeName();
            }
            inflatespinner();
        }
    }

    private void inflatespinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_textview, EmployeeName_arr);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_custom_textview);
        binding.taddpostActivity.spinnerCustom.setAdapter(spinnerArrayAdapter);

        binding.taddpostActivity.spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    EmployeeID = getEmployeeListBySubjectsClass.getData().getEmployeeList().get(i - 1).getEmployeeID();
                    //EdUtils.showToastShort(mContext, st_user_type);
                } else {
                    EmployeeID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
