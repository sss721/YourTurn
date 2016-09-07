package com.example.yourturnmobileapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.yourturnmobileapp.MainActivity;


public class SplashScreenActivity extends Activity {

    static ConnectivityManager cm;
    AlertDialog di;
    AlertDialog.Builder diBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        final ImageView slr = (ImageView) findViewById(R.id.imageView);
        final Animation rt = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
       // final Animation fo = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);

        slr.startAnimation(rt);
        rt.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        diBuild = new AlertDialog.Builder(SplashScreenActivity.this);

        if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()||cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting())
        {
            Intent e = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(e);
        }
        else
        {
            diBuild.setMessage("This application requires Internet connection. Would you like to connect to the Internet?");
            diBuild.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            diBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            di=diBuild.create();
            di.show();
        }
    }
}
