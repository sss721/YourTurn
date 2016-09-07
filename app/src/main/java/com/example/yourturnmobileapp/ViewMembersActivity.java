package com.example.yourturnmobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import helper.CustomListAdapter;
import helper.CustomMemberListAdapter;
import helper.TaskDatabaseHelper;
import models.Task;
import models.User;

public class ViewMembersActivity extends Activity {

    private static final String TAG = "ViewMembersActivity";
    Context context;
    ListView listView;
    ArrayList<User> memberList;
    private static final String url = "jdbc:mysql://androidinstance.cgrfkz1xyvvw.us-east-1.rds.amazonaws.com:3306/androidyourturn";
    private static final String user = "awsuser";
    private static final String pass = "apoorva123";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);

        context=this;
        Connect(this);
        //CustomMemberListAdapter adapter=new CustomMemberListAdapter(this, members);
        listView=(ListView)findViewById(R.id.membersListView);
        //listView.setAdapter(adapter);


    }

    private class Connect extends AsyncTask<String, Void, ArrayList<User>> {

        private ViewMembersActivity viewMembersActivity;
        ArrayList<User> memberList = new ArrayList<>();

        public Connect(ViewMembersActivity activity) {
            this.viewMembersActivity = activity;
        }

        @Override
        protected ArrayList<User> doInBackground(String... urls) {

            Connection con = null;
            Statement stmt = null;
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database connection successful");
                stmt = con.createStatement();

                String queryStr = "SELECT * FROM PERSONS";
                ResultSet rs = stmt.executeQuery(queryStr);
                while(rs.next()){
                    User userObj = new User();
                    userObj.setUserId(rs.getLong(1));
                    userObj.setUserName(rs.getString(2));
                    userObj.setUserNumber(rs.getString(3));
                    userObj.setUserBDate(rs.getString(4));
                    userObj.setUserEmail(rs.getString(5));
                    userObj.setUserGroup(rs.getString(6));
                    memberList.add(userObj);
                }
                rs.close();
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
            return memberList;
        }

        @Override
        protected void onPostExecute(ArrayList<User> memberList) {
            CustomMemberListAdapter adapter = new CustomMemberListAdapter(this.viewMembersActivity, memberList);
            listView.setAdapter(adapter);
        }
    }

    public void Connect(ViewMembersActivity activity) {
        Connect task = new Connect(activity);
        task.execute();
    }

}
