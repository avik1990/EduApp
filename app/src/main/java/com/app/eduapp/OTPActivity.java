package com.app.eduapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.eduapp.databinding.OtpActivityBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    OtpActivityBinding binding;
    Context mContext;
    Handler handler;
    int i = 60;
    int count = 0;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String[] user_type;
    String st_user_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);

        initView();

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("Both")) {
            binding.vSp1.setVisibility(View.VISIBLE);
            inflateSpinner();
        } else {
            st_user_type = EdUtils.get_userType(mContext);
            binding.vSp1.setVisibility(View.GONE);
        }
        // showTimer();
    }

    ///
    private void inflateSpinner() {
        if (EdUtils.list_usertype.size() > 0) {
            user_type = new String[EdUtils.list_usertype.size() + 1];
            user_type[0] = "Select User Type";

            for (int i = 0; i < EdUtils.list_usertype.size(); i++) {
                user_type[i + 1] = EdUtils.list_usertype.get(i);
            }
            inflatespinner();
        }
    }

    private void inflatespinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_text_bold, user_type);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_custom_text_bold);
        binding.spinnerCustom.setAdapter(spinnerArrayAdapter);

        binding.spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    st_user_type = EdUtils.list_usertypecode.get(i - 1);
                    //EdUtils.showToastShort(mContext, st_user_type);
                } else {
                    st_user_type = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showTimer() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.tvTime.setText(Html.fromHtml("<medium><font color=\"#000000\">" + "Waiting to automatically detect an OTP sent to " + "</font></medium>" + "<medium><font color=\"#000000\">" + EdUtils.getMobilePreferences(mContext) + " </font></medium>" + " <font color=\"#FF0000\">" + i + " secs" + "</font>"));
                i--;
                if (i > count) {
                    handler.postDelayed(this, 1000);
                }
                if (i == count) {
                    binding.tvResend.setVisibility(View.VISIBLE);
                    binding.tvTime.setText("");
                }
            }
        }, 200);
    }

    private void initView() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        binding = DataBindingUtil.setContentView(this, R.layout.otp_activity);
        binding.btnSubmit.setOnClickListener(this);
        binding.tvResend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSubmit) {
            if (!binding.etPhoneno.getText().toString().isEmpty()) {
                if (cd.isConnected()) {
                    if (!EdUtils.get_userType(mContext).equalsIgnoreCase("Both")) {
                        pDialog.show();
                        makelogin();
                    } else {
                        if (!st_user_type.isEmpty()) {
                            pDialog.show();
                            makelogin();
                        } else {
                            EdUtils.showToastShort(mContext, "Please Select User Type");
                        }
                    }
                }
                /*//Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), Dashboard.class);
                startActivity(i);
                EdUtils.setisVerified(mContext,true);
                finishAffinity();*/
            } else {
                EdUtils.showToastShort(mContext, "Enter Password");
            }
        } else if (v == binding.tvResend) {
            if (cd.isConnected()) {
                EdUtils.showToastShort(mContext, "OTP will be sent shortly");
                binding.tvResend.setVisibility(View.GONE);
                i = 60;
                // showTimer();
                //resendOtp();
            } else {
                EdUtils.showToastShort(mContext, "Internet Connection Required");
            }
        }
    }

    private void makelogin() {
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.parentsOTPVerification);

        Log.d("url_login", url_login);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {
                    Boolean flag = false;

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("OTPResponse", response);
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            String msg = jObj.getString("Message");
                            if (status.equals("1")) {
                                EdUtils.set_userType(mContext, jObj.getJSONObject("data").getString("UserType"));
                                if (jObj.getJSONObject("data").getString("UserType").equalsIgnoreCase("PRN")) {
                                    EdUtils.set_userID(mContext, jObj.getJSONObject("data").getString("ParentsID"));
                                } else if (jObj.getJSONObject("data").getString("UserType").equalsIgnoreCase("TCH")) {
                                    EdUtils.set_userID(mContext, jObj.getJSONObject("data").getString("EmployeeID"));
                                } /*else if (jObj.getJSONObject("data").getString("UserType").equalsIgnoreCase("TCH")) {
                                    EdUtils.set_userID(mContext, jObj.getJSONObject("data").getString("EmployeeID"));
                                }*/


                                EdUtils.set_isPrinciple(mContext, jObj.getJSONObject("data").getString("IsPrinciple"));
                                EdUtils.set_userPhone(mContext, binding.etPhoneno.getText().toString());
                                EdUtils.set_ParentsName(mContext, jObj.getJSONObject("data").getString("ParentsName"));
                                EdUtils.set_ProfilePicture(mContext, jObj.getJSONObject("data").getString("ProfilePicture"));
                                pDialog.dismiss();
                                EdUtils.setisVerified(mContext, true);
                                goToDashBoardActivity();
                            } else {
                                pDialog.dismiss();
                                showSuccessDialogFalse(msg);
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
                params.put("PrimaryMobileNumber", EdUtils.get_userPhone(mContext));
                params.put("OTPNumber", binding.etPhoneno.getText().toString());
                params.put("DeviceToken", EdUtils.getgcmtoken(mContext));
                params.put("UserType", st_user_type);

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

    private void showSuccessDialogFalse(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        //builder.setTitle("Message");
        //builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialog.dismiss();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive.setTextColor(Color.BLACK);
    }

    private void goToDashBoardActivity() {
        Intent i = new Intent(mContext, Dashboard.class);
        startActivity(i);
        finishAffinity();
    }
}
