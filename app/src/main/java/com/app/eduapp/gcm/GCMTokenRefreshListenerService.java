package com.app.eduapp.gcm;

import android.util.Log;

import com.app.eduapp.helper.EdUtils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class GCMTokenRefreshListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseApp.initializeApp(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        EdUtils.setgcmtokenPreferences(getBaseContext(), refreshedToken);
        Log.d("TOKEN==", refreshedToken);
    }
}