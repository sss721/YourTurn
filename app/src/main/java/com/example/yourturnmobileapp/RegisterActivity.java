package com.example.yourturnmobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import helper.SessionHelper;
import helper.TaskDatabaseHelper;
import models.User;

/**
 * Created by Keval on 02-11-2015.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    ImageButton imageButton4;
    EditText editText3,editText4,editText5,editText6,editText7,editText8,editGroup;

    private static final String TAG = "RegisterActivity";
    private static final String url = "jdbc:mysql://androidinstance.cgrfkz1xyvvw.us-east-1.rds.amazonaws.com:3306/androidyourturn";
    private static final String user = "awsuser";
    private static final String pass = "apoorva123";
    private SessionHelper session;
    Context cntx;
    User userObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText3 = (EditText)findViewById(R.id.username);
        editText4 = (EditText)findViewById(R.id.useremail) ;
        editText5 = (EditText)findViewById(R.id.userphno);
        editText6 = (EditText)findViewById(R.id.editText6);
        editText7 = (EditText)findViewById(R.id.editText7);
        editText8 = (EditText)findViewById(R.id.userbdate);
        editGroup = (EditText) findViewById(R.id.groupname);
        imageButton4 = (ImageButton)findViewById(R.id.imageButton4);


        imageButton4.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.imageButton4:

                userObj = registerUser();
                //saveUser(user);
                Connect();
                break;
        }
    }

    public User registerUser() {
        User user = new User();

        EditText username = (EditText) findViewById(R.id.username);
        String uName = username.getText().toString();
        user.setUserName(uName);

        EditText userphno = (EditText) findViewById(R.id.userphno);
        String uPhNo = userphno.getText().toString();
        user.setUserNumber(uPhNo);

        EditText uemail = (EditText) findViewById(R.id.useremail);
        String uEmail = uemail.getText().toString();
        user.setUserEmail(uEmail);

        EditText userBDate = (EditText) findViewById(R.id.userbdate);
        String uBDate = userBDate.getText().toString();
        user.setUserBDate(uBDate);

        EditText userGroup = (EditText) findViewById(R.id.groupname);
        String roomieGroup = userGroup.getText().toString();
        user.setUserGroup(roomieGroup);

        SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.putString("groupID", roomieGroup);
        ed.commit();
        return user;
    }

    public void saveUser(User user) {
        //code to persist the data into database
        /*Connection con = null;
        Statement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connection successful");
            stmt = con.createStatement();
            int results = stmt.executeUpdate("INSERT INTO tasks VALUES ("+task.getTaskName()+","+task.getTaskDesc()+
                    ","+task.getDueDate()+",null,"+task.getAssignee()+",0,0)");
            if (results == 1) {
                Toast.makeText(getBaseContext(), "Task added successfully", Toast.LENGTH_LONG).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try{
                stmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


        Log.d("Saving Trip", user.toString());
        Intent intent = new Intent();
        intent.putExtra("TaskData", user);
        setResult(RESULT_OK, intent);
        TaskDatabaseHelper databaseHelper = new TaskDatabaseHelper(getBaseContext());
        long taskID = databaseHelper.insertUser(user);
        Toast.makeText(getBaseContext(), "User Registered successfully.", Toast.LENGTH_LONG).show();
        Log.d("USer created with id: ", Long.toString(taskID));
        //finish();
        Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent1);

    }

    public void cancelUser() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }

    private class Connect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            Connection con = null;
            Statement stmt = null;
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database connection successful");
                stmt = con.createStatement();

                String queryStr = "INSERT INTO PERSONS (uname,uphno,ubdate,umail,uGroup) VALUES ('" +
                        userObj.getUserName() + "','" + userObj.getUserNumber() + "','" + userObj.getUserBDate()
                        + "','" + userObj.getUserEmail() + "','" + userObj.getUserGroup() +"')";
                int results = stmt.executeUpdate(queryStr);
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try{
                    stmt.close();
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "User added successfully", Toast.LENGTH_LONG).show();
            Intent viewTaskIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(viewTaskIntent);
        }
    }

    public void Connect() {
        Connect task = new Connect();
        task.execute();

    }


}
