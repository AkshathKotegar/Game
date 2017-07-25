package com.improstech.housie.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.improstech.housie.app.R;
import com.improstech.housie.app.helper.SessionManager;

/**
 * Created by User2 on 19-07-2017.
 */

public class SplashScreen extends AppCompatActivity {

    private static int TIME = 1000;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);

        session = new SessionManager(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!session.isLoggedIn()) {
                    logoutUser();
                } else {
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    //intent.putExtra("Key_IsLogin", "true");
                    startActivity(intent);
                }
                finish();
            }
        }, TIME);
    }

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        //intent.putExtra("Key_IsLogin", "false");
        startActivity(intent);
        finish();
    }
}
