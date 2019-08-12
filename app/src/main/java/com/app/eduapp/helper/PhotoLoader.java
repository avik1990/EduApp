package com.app.eduapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PhotoLoader implements Target {
    private final String name;
    public final String downloadDirectory = "LMETEduCare";
    File apkStorage = null;
    private static final String TAG = "Download Task";
    File outputFile = null;
    Context mContext;
    ProgressDialog pDialog;

    public PhotoLoader(Context mContext, String name) {
        this.name = name;
        this.mContext = mContext;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Downloading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onPrepareLoad(Drawable arg0) {
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
        try {
            if (new CheckForSDCard().isSDCardPresent()) {
                apkStorage = new File(
                        Environment.getExternalStorageDirectory() + "/"
                                + downloadDirectory);
            } else {
                Toast.makeText(mContext, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
            //If File is not present create directory
            if (!apkStorage.exists()) {
                apkStorage.mkdir();
                Log.e(TAG, "Directory Created.");
            }

            outputFile = new File(apkStorage, name + ".jpg");//Create Output file in Main File

            //Create New File if not present
            if (!outputFile.exists()) {
                outputFile.createNewFile();
                Log.e(TAG, "File Created");
            }
            FileOutputStream fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
            fos.close();

            final Timer t = new Timer();
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
            }, 6000);


        } catch (Exception e) {
            pDialog.dismiss();
            e.printStackTrace();
        }
    }


    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        pDialog.dismiss();
    }

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;
        //   Uri uri = Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
