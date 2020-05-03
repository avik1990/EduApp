package com.app.eduapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.eduapp.databinding.LoginActivityBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.helper.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LoginActivityBinding binding;
    Context mContext;
    ConnectionDetector cd;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        initView();
    }

    private void initView() {

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        cd = new ConnectionDetector(mContext);
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSubmit) {
            if (!binding.etPhoneno.getText().toString().isEmpty() && binding.etPhoneno.getText().toString().length() == 10) {
                if (cd.isConnected()) {
                    pDialog.show();
                    makelogin();
                }
                //Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();
                /*Intent i = new Intent(mContext, OTPActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
            } else {
                Toast.makeText(mContext, "Enter Phone Number Properly", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void makelogin() {
        EdUtils.list_usertype.clear();
        EdUtils.list_usertypecode.clear();
        String url_login = mContext.getResources().getString(R.string.base_url)
                + getResources().getString(R.string.checkParentsLogin);

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
                                EdUtils.set_userType(mContext, jObj.getJSONObject("data").getString("UserType"));
                                if (jObj.getJSONObject("data").getString("UserType").equalsIgnoreCase("BOTH")) {
                                    JSONArray a = jObj.getJSONObject("data").getJSONArray("UserTypeArr");
                                    for (int i = 0; i < a.length(); i++) {
                                        EdUtils.list_usertype.add(a.getJSONObject(i).getString("OriginalName"));
                                        EdUtils.list_usertypecode.add(a.getJSONObject(i).getString("ShortCode"));
                                    }

                                }

                                EdUtils.set_userPhone(mContext, binding.etPhoneno.getText().toString());
                               /* if (jObj.getJSONObject("data").getString("UserType").equalsIgnoreCase("PRN")) {
                                    EdUtils.set_userID(mContext, jObj.getJSONObject("data").getString("ParentsID"));
                                } else if (jObj.getJSONObject("data").getString("UserType").equalsIgnoreCase("TCH")) {
                                    EdUtils.set_userID(mContext, jObj.getJSONObject("data").getString("EmployeeID"));
                                } else if (jObj.getJSONObject("data").getString("UserType").equalsIgnoreCase("TCH")) {
                                    EdUtils.set_userID(mContext, jObj.getJSONObject("data").getString("EmployeeID"));
                                }
                                EdUtils.set_userPhone(mContext, binding.etPhoneno.getText().toString());
                                EdUtils.set_ParentsName(mContext, jObj.getJSONObject("data").getString("ParentsName"));
                                EdUtils.set_ProfilePicture(mContext, jObj.getJSONObject("data").getString("ProfilePicture"));*/
                                pDialog.dismiss();
                                goToOtpActivity();
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
                params.put("PrimaryMobileNumber", binding.etPhoneno.getText().toString());
                params.put("DeviceToken", EdUtils.getgcmtoken(mContext));
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

    private void goToOtpActivity() {
        Intent i = new Intent(mContext, OTPActivity.class);
        startActivity(i);
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


}
