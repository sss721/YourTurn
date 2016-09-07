package com.example.yourturnmobileapp;

import  android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;


public class LoginActivity extends Activity implements View.OnClickListener {
    ImageButton imageButton;
    EditText editText,editText2;
    Button Reghere;
    LoginButton fblogin;
    private CallbackManager callbackManager;
    ProfilePictureView profilePictureView;
    private TextView greeting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager= CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        Reghere = (Button) findViewById(R.id.Reghere);
        imageButton.setOnClickListener(this);
        Reghere.setOnClickListener(this);
        fblogin = (LoginButton) findViewById(R.id.fblogin_button);
        greeting = (TextView) findViewById(R.id.greeting);
        editText = (EditText) findViewById(R.id.username);
        editText2 = (EditText) findViewById(R.id.password);

        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
                greeting.setText("Welcome" + parameters);
                Intent mainLobby = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainLobby);
            }

            @Override
            public void onCancel() {


            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI() {

        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            profilePictureView.setProfileId(profile.getId());
            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
        } else {
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageButton:
                startActivity((new Intent(this, MainActivity.class)));

                break;
            case R.id.Reghere:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}
