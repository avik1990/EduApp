package com.app.eduapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.eduapp.common.ImagePickerActivity;
import com.app.eduapp.databinding.StudentprofileActivityIncBinding;
import com.app.eduapp.helper.CheckForSDCard;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.MapUtils;
import com.app.eduapp.helper.RetroClient;
import com.app.eduapp.pojo.GetParentsProfile;
import com.app.eduapp.pojo.ProfileImageUploadResponse;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class StudentProfile extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    StudentprofileActivityIncBinding binding;
    Context mContext;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    GetParentsProfile getParentsProfile;
    LCDatabaseHandler db;
    String position = "0";
    public static final int REQUEST_IMAGE = 100;
    List<MultipartBody.Part> parts = new ArrayList<>();
    ProfileImageUploadResponse responseApiModel;
    String mCurrentPhotoPath;

    public final String downloadDirectory = "LMETEduCare";
    File apkStorage = null;
    File outputFile = null;
    SimpleDateFormat dateFormat;
    Calendar cal;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait..");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        db = new LCDatabaseHandler(mContext);
        position = getIntent().getExtras().getString("position");

        dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
        cal = Calendar.getInstance();

        initView();
        setUpviewData();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.studentprofile_activity_inc);
        binding.navView.setItemIconTintList(null);
        binding.studentprofileActivity.toolbar.tvChild.setText("Student Profile");
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.studentprofileActivity.toolbar.btnMenu.setOnClickListener(this);
        binding.studentprofileActivity.toolbar.cartView.setOnClickListener(this);
        binding.studentprofileActivity.imgPlus.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.studentprofileActivity.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.studentprofileActivity.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    @Override
    public void onClick(View v) {
        if (v == binding.studentprofileActivity.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.studentprofileActivity.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        } else if (v == binding.studentprofileActivity.imgPlus) {
            //showImagePickerOptions();

            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(StudentProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(StudentProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfile.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                StudentProfile.this.openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri photoUri = data.getParcelableExtra("path");
                String name = dateFormat.format(cal.getTime());
                try {
                    // You can update this bitmap to your server
                    try {
                        if (new CheckForSDCard().isSDCardPresent()) {
                            apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + downloadDirectory);
                        } else {
                            Toast.makeText(mContext, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
                            // pDialog.dismiss();
                        }

                        if (!apkStorage.exists()) {
                            apkStorage.mkdir();
                        }
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

                        outputFile = new File(apkStorage, name + ".jpg");//Create Output file in Main File

                        //Create New File if not present
                        if (!outputFile.exists()) {
                            outputFile.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(outputFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        binding.studentprofileActivity.ivProfile.setImageBitmap(bitmap);
                        binding.studentprofileActivity.ivProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
                        fos.close();

                        /*final Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            public void run() {
                                pDialog.dismiss();
                                try {
                                    openFile(mContext, outputFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                            }
                        }, 6000);*/

                        //Log.d("image_uris", "" + photoUri);
                        //String paths=data.getParcelableExtra("path").toString().replace("file:///data/user","/storage/emulated");
                        //Uri.fromFile(new File(paths));
                        //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        String outputFile1 = outputFile.toString();
                        Log.e("outputFile1", outputFile1);
                        parts.add(preparefilepart(new File(outputFile.toString())));
                       uploadfile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // loading profile image from local cache
                    // loadProfile(photoUri.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private MultipartBody.Part preparefilepart(File file) {
       // File bin = new File(uri.toString());
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), file);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("ProfileImage", file.getName(), reqBody);
        return partImage;
    }


    private void uploadfile() {
        pDialog.show();
        ApiServices api = RetroClient.getApiServices();
        Call<ProfileImageUploadResponse> upload = api.uploadProfileImage(parts, EdUtils.get_studentID(mContext), EdUtils.get_classID(mContext));

        upload.enqueue(new Callback<ProfileImageUploadResponse>() {

            @Override
            public void onResponse(Call<ProfileImageUploadResponse> call, retrofit2.Response<ProfileImageUploadResponse> response) {
                pDialog.dismiss();
                responseApiModel = response.body();
                Log.d("RETRO", "ON RESPONSE  : " + response.body().toString());
                if (response.body().getStatus().equals("1")) {
                    EdUtils.showToastShort(mContext, "Success");
                    /*if (response.body().getData().getUploadedFileName().size() > 0) {
                        // json_image_string = getJsonString(response.body().getData());
                        // postTeachersDiary();
                    }*/
                } else {
                    EdUtils.showToastShort(mContext, "Failure");
                }
            }

            @Override
            public void onFailure(Call<ProfileImageUploadResponse> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                pDialog.dismiss();
            }
        });

    }


    private void loadProfile(String url) {
      /*  Glide.with(this).load(url)
                .into(binding.studentprofileActivity.ivProfile);
        binding.studentprofileActivity.ivProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
*/
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setUpviewData() {
        if (MapUtils.list_student.size() > 0) {
            binding.studentprofileActivity.etName.setText(MapUtils.list_student.get(Integer.parseInt(position)).getFullName());
            binding.studentprofileActivity.etPhno.setText(MapUtils.list_student.get(Integer.parseInt(position)).getPrimaryMobileNumber());
            binding.studentprofileActivity.etAddress.setText(MapUtils.list_student.get(Integer.parseInt(position)).getAddress());

            try {
                binding.studentprofileActivity.etClass.setText(MapUtils.list_student.get(Integer.parseInt(position)).getClassName() + " " + MapUtils.list_student.get(Integer.parseInt(position)).getSectionName());
            } catch (Exception e) {

            }
            try {
                binding.studentprofileActivity.etBloodgroup.setText(MapUtils.list_student.get(Integer.parseInt(position)).getBloodGroupName());
            } catch (Exception e) {

            }
            try {
                Picasso.get()
                        .load(MapUtils.list_student.get(Integer.parseInt(position)).getProfileImage())
                        //.resize(50, 50)
                        //.centerCrop()
                        .into(binding.studentprofileActivity.ivProfile);
            } catch (Exception e) {

            }

        }
    }


}
