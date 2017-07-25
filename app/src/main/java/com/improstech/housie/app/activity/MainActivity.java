package com.improstech.housie.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.improstech.housie.app.R;
import com.improstech.housie.app.app.Config;
import com.improstech.housie.app.fragment.HomeFragment;
import com.improstech.housie.app.fragment.Registration;
import com.improstech.housie.app.helper.DataHelper;
import com.improstech.housie.app.util.NotificationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = MainActivity.class.getSimpleName();
    private DataHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        dh = new DataHelper(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        String isLogin = null;
        if (extras != null) {
            isLogin = extras.getString("Key_IsLogin");
        }
        if (savedInstanceState==null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_container, Registration.newInstance(), "MainActivity")
                    //.addToBackStack(null)
                    .commit();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    if (message != null && message.equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), "Message from server = " + message, Toast.LENGTH_LONG).show();
                        // loadDropDownQuestion();
                    } else if (message != null && message.equalsIgnoreCase("2")) {
                        Toast.makeText(getApplicationContext(), "Message from server = " + message, Toast.LENGTH_LONG).show();
                        // load2ndFunction();
                    } else {
                        Toast.makeText(getApplicationContext(), "Nothing from server", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
        Config.FIREBASE_ID = regId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        if (NotificationUtils.msg != null && NotificationUtils.msg.equalsIgnoreCase("1")) {
            Toast.makeText(getApplicationContext(), "Message from server = " + NotificationUtils.msg, Toast.LENGTH_LONG).show();
            // loadDropDownQuestion();
        } else if (NotificationUtils.msg != null && NotificationUtils.msg.equalsIgnoreCase("2")) {
            Toast.makeText(getApplicationContext(), "Message from server = " + NotificationUtils.msg, Toast.LENGTH_LONG).show();
            // load2ndFunction();
        } else {
            Toast.makeText(getApplicationContext(), "Nothing from server", Toast.LENGTH_LONG).show();
        }
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exportDB();
        finish();
    }

    public void exportDB() {
        String SAMPLE_DB_NAME = dh.getDatabaseName();
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + getApplicationContext().getPackageName() + "/databases/" + SAMPLE_DB_NAME;
        String backupDBPath = SAMPLE_DB_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
           // Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
