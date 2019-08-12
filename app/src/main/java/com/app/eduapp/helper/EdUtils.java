package com.app.eduapp.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;


import com.app.eduapp.ContactUs;
import com.app.eduapp.Dashboard;
import com.app.eduapp.EventCalender;
import com.app.eduapp.ExamListActivity;
import com.app.eduapp.ExamsHeldListActivity;
import com.app.eduapp.Fees;
import com.app.eduapp.LeaveApplicationList;
import com.app.eduapp.LeaveListActivity;
import com.app.eduapp.LoginActivity;
import com.app.eduapp.MonthListActivity;
import com.app.eduapp.NoticeActivity;
import com.app.eduapp.OTPActivity;
import com.app.eduapp.Profile;
import com.app.eduapp.R;
import com.app.eduapp.RoutineActivity;
import com.app.eduapp.RoutineType;
import com.app.eduapp.SettingActivity;
import com.app.eduapp.StudentDiaryActivity;
import com.app.eduapp.TeaRoutineActivity;
import com.app.eduapp.tattendance.AClassListActivity;
import com.app.eduapp.teacher.TClassListActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EdUtils {
    final static int M_REQUEST_CODE = 100;
    public static final String SMS_ORIGIN = "VK-040003";
    public static final String SMS_ORIGIN1 = "WEBAPP";
    public static final String SMS_ORIGIN2 = "TESTSM";
    public static List<String> list_usertype = new ArrayList<>();
    public static List<String> list_usertypecode = new ArrayList<>();

    public static void shareAll(Context mContext, String heading, String links) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, heading);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heading);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, links);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    public static String getFormasssttedDate1(String normal_date) {
        String anni = normal_date;
        String formated_date = "";
        if (anni.length() > 6) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date;
            try {
                date = originalFormat.parse(anni);
                formated_date = targetFormat.format(date);  // 20120821
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            formated_date = anni;
        }
        return formated_date;
    }



    public static String getFormasssttedDate31(String normal_date) {
        String anni = normal_date;
        String formated_date = "";
        if (anni.length() > 6) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = originalFormat.parse(anni);
                formated_date = targetFormat.format(date);  // 20120821
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            formated_date = anni;
        }
        return formated_date;
    }




    public static String getFormasssttedDate12(String normal_date) {
        String anni = normal_date;
        String formated_date = "";
        if (anni.length() > 6) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date;
            try {
                date = originalFormat.parse(anni);
                formated_date = targetFormat.format(date);  // 20120821
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            formated_date = anni;
        }
        return formated_date;
    }






    public static String getDayfromdate(String date) {
        String finalDay = "";
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            Date dt1 = format1.parse(date);
            DateFormat format2 = new SimpleDateFormat("EEEE");
            finalDay = format2.format(dt1);
        } catch (Exception e) {
            finalDay = "";
        }
        return finalDay;
    }


    public static void phoneCall(String number, Context mContext) {
        TelephonyManager telMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {
            case 0:
            case 1:
                showToastShort(mContext, "Call cannot be placed");
                break;

            case TelephonyManager.SIM_STATE_READY:
                try {

                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public static String ReturnDay(Context mContext, int no) {
        String day = "";
        switch (no) {
            case 7:
                day = "SU";
                break;
            case 1:
                day = "M";
                break;
            case 2:
                day = "TU";
                break;
            case 3:
                day = "W";
                break;
            case 4:
                day = "TH";
                break;
            case 5:
                day = "F";
                break;
            case 6:
                day = "SA";
                break;
        }
        return day;
    }


    public static void shareEvent(Context mContext, String heading, String links) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, heading);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heading);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, links);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    public static String getFormattedDate0(String normal_date) {
        String anni = normal_date;
        String formated_date = "";
        if (anni.length() > 6) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM");
            Date date;
            try {
                date = originalFormat.parse(anni);
                formated_date = targetFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            formated_date = anni;
        }
        return formated_date;
    }

    public static String getCurrentDate() {
        SimpleDateFormat parserSDF = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = new Date();
        String formated_date = parserSDF.format(date);
        return formated_date;
    }

    public static String getCurrentDate1() {
        SimpleDateFormat parserSDF = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = new Date();
        Date dateBefore = new Date(date.getTime() - 7 * 24 * 3600 * 1000l);

        String formated_date = parserSDF.format(dateBefore);
        return formated_date;
    }


    public static String getCurrentDateMinus() {
        SimpleDateFormat parserSDF = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        Date date = new Date();
        String formated_date = parserSDF.format(date);
        return formated_date;
    }

    //----------------------------------------------


    //---------------------------------------


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "[A-Za-z0-9._%+-]+@[A-Za-z0-9._]+\\.[A-Za-z]{2,4}";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    //---------------------------------------------------------------------
    public static boolean isStringNullOrEmpty(String str) {
        boolean result = false;
        if (str == null || str.equals("")) {

            result = true;
        }

        return result;

    }


    public static void shareAllEvents(Context mContext, String heading, String sub) {
        //String title = heading;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heading);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sub);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }


    public static void shareAll(Context mContext, String heading, String sub, String links) {
        //String title = heading;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE, heading);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, links);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heading);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    public static void setActivity_flag(Context mContext, String cat) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("acvivity_flag", cat);
        editor.commit();
    }

    public static String getActivity_flag(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String name = preferences.getString("acvivity_flag", "");
        return name;
    }

    public static void sendMail(Context mContext, String mailto) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + mailto));
        try {
            mContext.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public static void sendMail(Context mContext, String mailto,
                                String message, int M_REQUEST_CODE) {

        String body = "\n\nMessage: " + message.trim();

		/*Intent mailer = new Intent(Intent.ACTION_SEND);
        mailer.setType("message/rfc822");
		mailer.putExtra(Intent.EXTRA_EMAIL, new String[] { mailto });
		mailer.putExtra(Intent.EXTRA_SUBJECT, "Enquiry From Lions Clubs 322B1 App");
		mailer.putExtra(Intent.EXTRA_TEXT, body);
		mContext.startActivity(Intent.createChooser(mailer, "Send email..."));


		String body = "\n\nName: " + name + "\nEmail Id: "
				+ email + "\nMobile: " + mobile + "\nAddress: " + address
				+ "\nProfession: " + profession+
				"\n\nMessage: " + message.trim();
*/
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + mailto));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry From Lions Clubs 322B1 App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        //emailIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        try {
            //mContext.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            ((Activity) mContext).startActivityForResult(Intent.createChooser(emailIntent, "Send email using..."), M_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    //------------------------------------------------------------------------------------------

    /**
     * @param mContext
     * @param emailTo
     * @param subject
     * @param emailText
     * @param filePathsMap
     * @param M_REQUEST_CODE
     */
    @SuppressLint("NewApi")
    public static void sendMail(Context mContext, String emailTo,
                                String subject, String emailText, LinkedHashMap filePathsMap, int M_REQUEST_CODE) {
        //need to "send multiple" to get more than one attachment
        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
        //has to be an ArrayList
        ArrayList<Uri> uris = new ArrayList<Uri>();
        //convert from paths to Android friendly Parcelable Uri's
            /*for (String file : filePaths){
                File fileIn = new File(file);
		        Uri u = Uri.fromFile(fileIn);
		        uris.add(u);
		    }*/
        Iterator entries = filePathsMap.entrySet().iterator();
        while (entries.hasNext()) {
            Entry thisEntry = (Entry) entries.next();
            //Object key   = thisEntry.getKey();
            Object value = thisEntry.getValue();
            File fileIn = new File(value.toString());
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        //mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        ///////////////////////////////////////////////////////////////////////////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            emailIntent.setType(null); // If we're using a selector, then clear the type to null. I don't know why this is needed, but it doesn't work without it.
            final Intent restrictIntent = new Intent(Intent.ACTION_SENDTO);
            Uri data = Uri.parse("mailto:?to=" + emailTo);
            restrictIntent.setData(data);
            emailIntent.setSelector(restrictIntent);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////
        try {
            ((Activity) mContext).startActivityForResult(Intent.createChooser(emailIntent, "Send email using..."), M_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    //------------------------------------------------------------------------------------------

    public static void openURL(Context mContext, String URL) {

        String inputUrl = URL;
        /*if (!inputUrl.contains("http://"))
            inputUrl = "http://" + inputUrl;*/

        URL url = null;
        try {
            url = new URL(inputUrl);
        } catch (MalformedURLException e) {
            Log.v("myApp", "bad url entered");
        }
        if (url == null) {

        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(inputUrl));
            mContext.startActivity(i);
        }

    }


    public static void openURL1(Context mContext, String URL) {
        String inputUrl = URL;
        if (!inputUrl.contains("http://"))
            inputUrl = "http://" + inputUrl;

        URL url = null;
        try {
            url = new URL(inputUrl);
        } catch (MalformedURLException e) {
            Log.v("myApp", "bad url entered");
        }
        if (url == null) {

        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(inputUrl));
            mContext.startActivity(i);
        }

    }

    public static void setOTP_Text(Context mContext, String cat) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("otp_text", cat);
        editor.commit();
    }

    public static String getOTP_Text(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String name = preferences.getString("otp_text", "");
        return name;
    }

    //---------------------------------------------------------------------------------------
    public static void sendMail(Context mContext, String mailto,
                                String name, String email, String mobile,
                                String message, int M_REQUEST_CODE) {
        // String body1 = msg.trim() + "\n\nPhone Number: " + ph +
        // "\nEmail Id: " + mail;
        String body = "Name: " + name + "\nEmail: "
                + email + "\nMobile: " + mobile +
                "\n\nMessage:\n " + message.trim();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + mailto));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry From Lions Clubs 322B1 App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        //emailIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        try {
            //mContext.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            ((Activity) mContext).startActivityForResult(Intent.createChooser(emailIntent, "Send email using..."), M_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendMail(Context mContext, String mailto, int M_REQUEST_CODE) {
        // String body1 = msg.trim() + "\n\nPhone Number: " + ph +
        // "\nEmail Id: " + mail;
        /*String body = "Name: " + name + "\nEmail: "
                + email + "\nMobile: " + mobile +
				"\n\nMessage:\n " + message.trim();*/

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + mailto));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry From Lions Clubs 322B1 App");
        //emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        //emailIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        try {
            //mContext.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            ((Activity) mContext).startActivityForResult(Intent.createChooser(emailIntent, "Send email using..."), M_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    //---------------------------------------------------------------------------------------

    /**
     * @param normal_date
     * @return dd MMMM
     */

    public static String getFormattedDate_event(String normal_date) {
        String anni = normal_date;
        String formated_date = "";
        if (anni.length() > 6) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = originalFormat.parse(anni);
                formated_date = targetFormat.format(date);
            } catch (ParseException e) {
                //When the Exception is happening trying to parse for another different date format
                if (anni.length() > 6) {
                    SimpleDateFormat originalFormat1 = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                    SimpleDateFormat targetFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1;
                    try {
                        date1 = originalFormat1.parse(anni);
                        formated_date = targetFormat1.format(date1);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    formated_date = anni;
                }
            }
        } else {
            formated_date = anni;
        }
        return formated_date;
    }

    public static String getFormattedDate_event_1(String normal_date) {
        String anni = normal_date;
        String formated_date = "";
        if (anni.length() > 6) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            //SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = originalFormat.parse(anni);
                formated_date = targetFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            formated_date = anni;
        }
        return formated_date;
    }


    public static String formatDateToString(Date date, String format) {
        String timeZone = "";
        // null check
        if (date == null) return null;
        // create SimpleDateFormat object with input format
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // default system timezone if passed null or empty
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        // set timezone to SimpleDateFormat
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        // return Date in required format with timezone as String
        return sdf.format(date);
    }


    //---------------------------------------------------------------------------------------------------------------
    public static boolean getGCMPreferences(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        boolean flag = loginPreferences.getBoolean("GCM_REGISTRATION_STATUS", false);
        Log.i("GCM_REGISTRATION: ", "" + flag);
        return flag;
    }

    public static boolean getisVerified(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        boolean flag = loginPreferences.getBoolean("d_ride_later", false);
        return flag;
    }

    public static void setisVerified(Context mContext, boolean isVerified) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("d_ride_later", isVerified);
        editor.apply();
    }

    /**
     * @param mContext
     * @param NOTIFY_VAL
     * @param NOTIFY_NOTI
     */
    public static void setNotificationMessage(Context mContext, String NOTIFY_VAL, String NOTIFY_NOTI) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NOTIFY_VAL", NOTIFY_VAL);
        editor.putString("NOTIFY_NOTI", NOTIFY_NOTI);
        editor.commit();
    }

    public static String getNotificationMessage(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String notification_msg = loginPreferences.getString("NOTIFY_VAL", "");
        Log.i("NOTIFY_VAL : ", "" + notification_msg);
        return notification_msg;
    }

    public static String get_userPhone(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_phone", "0");
        return a_key;
    }

    public static void set_userPhone(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_phone", a_key);
        editor.commit();
    }


    public static void set_userType(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usertype", a_key);
        editor.commit();
    }


    public static String get_userType(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("usertype", "0");
        return a_key;
    }


    public static void set_userID(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userID", a_key);
        editor.commit();
    }


    public static String get_userID(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("userID", "0");
        return a_key;
    }

    /////////////////////////////////

    public static void set_RadioChecked(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("RadioChecked", a_key);
        editor.commit();
    }


    public static String get_RadioChecked(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("RadioChecked", "0");
        return a_key;
    }


    public static void set_studentID(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("studentID", a_key);
        editor.commit();
    }


    public static String get_studentID(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("studentID", "0");
        return a_key;
    }

    public static void set_classID(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("classID", a_key);
        editor.commit();
    }


    public static String get_classID(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("classID", "0");
        return a_key;
    }

    public static void set_sectionID(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sectionID", a_key);
        editor.commit();
    }

    public static String get_sectionID(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("sectionID", "0");
        return a_key;
    }
/////////////////////////////////////////////






















/* public static String get_ParentsID(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("ParentsID", "0");
        return a_key;
    }
    public static void set_ParentsID(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ParentsID", a_key);
        editor.commit();
    }


    public static String get_EmployeeID(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("EmployeeID", "0");
        return a_key;
    }

    public static void set_EmployeeID(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EmployeeID", a_key);
        editor.commit();
    }*/

    public static String getCurrentFormattedDate() {
        SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        String formated_date = parserSDF.format(date);
        return formated_date;
    }

    public static String get_ParentsName(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("ParentsName", "0");
        return a_key;
    }

    public static void set_ParentsName(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ParentsName", a_key);
        editor.commit();
    }

    public static String get_ProfilePicture(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("ProfilePicture", "0");
        return a_key;
    }

    public static void set_ProfilePicture(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ProfilePicture", a_key);
        editor.commit();
    }


    public static String get_kidid(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("kid_id", "0");
        return a_key;
    }

    public static void set_kidid(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("kid_id", a_key);
        editor.commit();
    }

    public static String getMobilePreferences(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("mobile", "");
        return a_key;
    }

    public static void setMobilePreferences(Context mContext, String mobile) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public static void setMemberIdPreferences(Context mContext, String m_id) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("m_id", m_id);
        editor.commit();
    }

    public static String getMemberIdPreferences(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String m_id = loginPreferences.getString("m_id", "");
        return m_id;
    }

    public static void setMemberEditPreferences(Context mContext, String m_id) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("edit_url", m_id);
        editor.commit();
    }

    public static String getMemberEditPreferences(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String m_id = loginPreferences.getString("edit_url", "");
        return m_id;
    }


    public static String getMemberNamePreferences(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String MemDirLastUpdate = loginPreferences.getString("member_name", "");
        return MemDirLastUpdate;
    }

    public static void setMemberNamePreferences(Context mContext, String MemDirLastUpdate) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("member_name", MemDirLastUpdate);
        editor.commit();
    }

    public static void setRouteDetailsDom(Context mContext, String user_name) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("route_dom", user_name);
        editor.apply();
    }

    public static String getRouteDetailsDom(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        return preferences.getString("route_dom", "");
    }


    public static void setOldRoutepref(Context mContext, String mobile) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("route_dom_old", mobile);
        editor.apply();
    }

    public static String getOldRoutePref(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("route_dom_old", "");
        return a_key;
    }


    public static String getgcmtoken(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref123", 0); // 0 - for private mode
        return preferences.getString("gcm_token", "");
    }

    public static void setgcmtokenPreferences(Context mContext, String user_address) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref123", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gcm_token", user_address);
        editor.apply();
    }

    public static void cleardata(Context mContext) {
        SharedPreferences settings = mContext.getSharedPreferences("Kppref", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    public static void showToastShort(Context mContext, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastLong(Context mContext, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showLogoutAlert(final Context context, String msg, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent profileintent = new Intent(context, LoginActivity.class);
                context.startActivity(profileintent);
                ((Activity) context).overridePendingTransition(R.anim.anim_in_reverse, R.anim.anim_out_reverse);
                ((Activity) context).finishAffinity();
                cleardata(context);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(context.getResources().getColor(R.color.black));
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(context.getResources().getColor(R.color.black));
    }

    public static String getdeviceid(Context mContext) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (!telephonyManager.getDeviceId().equals(null)) {
            }
            return telephonyManager.getDeviceId();
        } catch (Exception e) // else
        {
            return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }


    public static void openNavDrawer(int id, final Context mContext) {
        if (id == R.id.nav_profile) {
            if (!(mContext instanceof Profile)) {
                Intent i = new Intent(mContext, Profile.class);
                mContext.startActivity(i);
            }
        }
        if (id == R.id.nav_dash) {
            if (!(mContext instanceof Dashboard)) {
                Intent i = new Intent(mContext, Dashboard.class);
                mContext.startActivity(i);
            }
        }
        if (id == R.id.nav_attendance) {

            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                if (!(mContext instanceof AClassListActivity)) {
                    Intent i = new Intent(mContext, AClassListActivity.class);
                    mContext.startActivity(i);
                }
            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                if (!(mContext instanceof MonthListActivity)) {
                    Intent i = new Intent(mContext, MonthListActivity.class);
                    mContext.startActivity(i);
                }
            }
        }
        if (id == R.id.nav_schoolnotice) {
            if (!(mContext instanceof NoticeActivity)) {
                Intent i = new Intent(mContext, NoticeActivity.class);
                mContext.startActivity(i);
            }
        }
        if (id == R.id.nav_eventcalender) {
            if (!(mContext instanceof EventCalender)) {
                Intent i = new Intent(mContext, EventCalender.class);
                mContext.startActivity(i);
            }
        }
        if (id == R.id.nav_studentdiary) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                if (!(mContext instanceof TClassListActivity)) {
                    Intent i = new Intent(mContext, TClassListActivity.class);
                    i.putExtra("from", "diary");
                    mContext.startActivity(i);
                }
            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                if (!(mContext instanceof StudentDiaryActivity)) {
                    Intent i = new Intent(mContext, StudentDiaryActivity.class);
                    mContext.startActivity(i);
                }
            }
        }
        if (id == R.id.nav_exam) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                if (!(mContext instanceof TClassListActivity)) {
                    Intent i = new Intent(mContext, TClassListActivity.class);
                    i.putExtra("from", "exam");
                    mContext.startActivity(i);
                }

            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                if (!(mContext instanceof ExamListActivity)) {
                    Intent i = new Intent(mContext, ExamListActivity.class);
                    mContext.startActivity(i);
                }
            }
        }
        if (id == R.id.nav_fees) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                EdUtils.showToastShort(mContext, "Not accessible for teachers");
            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                if (!(mContext instanceof Fees)) {
                    Intent i = new Intent(mContext, Fees.class);
                    mContext.startActivity(i);
                }
            }
        }
        if (id == R.id.nav_settings) {
            if (!(mContext instanceof SettingActivity)) {
                Intent i = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(i);
            }
        }
        if (id == R.id.nav_result) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                if (!(mContext instanceof TClassListActivity)) {
                    Intent i = new Intent(mContext, TClassListActivity.class);
                    i.putExtra("from", "result");
                    mContext.startActivity(i);
                }

            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                if (!(mContext instanceof ExamsHeldListActivity)) {
                    Intent i = new Intent(mContext, ExamsHeldListActivity.class);
                    mContext.startActivity(i);
                }
            }
        }

        if (id == R.id.nav_routine) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                if (!(mContext instanceof RoutineType)) {
                    Intent i = new Intent(mContext, RoutineType.class);
                    i.putExtra("from", "routine");
                    mContext.startActivity(i);
                }
            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                if (!(mContext instanceof RoutineActivity)) {
                    Intent i = new Intent(mContext, RoutineActivity.class);
                    mContext.startActivity(i);
                }
            }

            /*if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                Intent i = new Intent(mContext, RoutineType.class);
                i.putExtra("from", "routine");
                startActivity(i);
            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                Intent i = new Intent(mContext, RoutineActivity.class);
                startActivity(i);
            }*/
        }

        if (id == R.id.nav_update) {
            EdUtils.openURL(mContext, "https://play.google.com/store/apps/details?id=com.app.eduapp");
        }

        if (id == R.id.nav_signout) {
            showLogoutAlert(mContext, "Are you sure?", "Log Out");
        }
        /*if (id == R.id.nav_contactus) {
            if (!(mContext instanceof ContactUs)) {
                Intent i = new Intent(mContext, ContactUs.class);
                mContext.startActivity(i);;;;
            }
        }*/

        if (id == R.id.nav_leaveapplication) {
            if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
                if (!(mContext instanceof LeaveApplicationList)) {
                    Intent i = new Intent(mContext, LeaveApplicationList.class);
                    mContext.startActivity(i);
                }

            } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
                if (!(mContext instanceof LeaveListActivity)) {
                    Intent i = new Intent(mContext, LeaveListActivity.class);
                    mContext.startActivity(i);
                }
            }
        }
    }

    /* public static File createTemporaryFile(String part, String ext, String IMAGE_DIRECTORY_NAME) throws Exception {
         File mediaStorageDir = new File(
                 Environment
                         .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
         if (!mediaStorageDir.exists()) {
             if (!mediaStorageDir.mkdirs()) {
                 //Log.wtf("TAG", "Oops! Failed create "
                 //        + AppConfig.IMAGE_DIRECTORY_NAME + " directory");
                 return null;
             }
         }
         String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                 Locale.getDefault()).format(new Date());
         File mediaFile;
         mediaFile = new File(mediaStorageDir.getPath() + File.separator
                 + "IMG_" + timeStamp + ".jpg");

         return mediaFile;
     }*/
    public static File createTemporaryFile(String part, String ext, String IMAGE_DIRECTORY_NAME) throws Exception {
        File mediaStorageDir = null;
        if (new CheckForSDCard().isSDCardPresent()) {
            mediaStorageDir = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + IMAGE_DIRECTORY_NAME);
        } else {
        }
       /* File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //Log.wtf("TAG", "Oops! Failed create "
                //        + AppConfig.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }


    public static void set_isPrinciple(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("IsPrinciple", a_key);
        editor.commit();
    }


    public static String get_isPrinciple(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("IsPrinciple", "0");
        return a_key;
    }

}
