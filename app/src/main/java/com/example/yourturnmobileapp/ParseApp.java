package com.example.yourturnmobileapp;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class ParseApp extends Application{
    public void onCreate()
    {
        super.onCreate();
        Parse.initialize(this, "YnerTeSjrbq1HslTjHd5EkTE8h23lIFlT4ZSfxYw", "JhwpvGyywd9GmnIINbAg5r5KipGXbfcWj29vfYI0");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
