package com.example.yourturnmobileapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import helper.CustomListAdapter;
import helper.TaskDatabaseHelper;
import models.Task;


public class ViewTaskActivity extends Activity {

    private static final String TAG = "ViewTaskActivity";
    Task task = new Task();
    ListView listView;
    public Context context;
    private static final String url = "jdbc:mysql://androidinstance.cgrfkz1xyvvw.us-east-1.rds.amazonaws.com:3306/androidyourturn";
    private static final String user = "awsuser";
    private static final String pass = "apoorva123";
    ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        context=this;
        Connect(this);
        listView=(ListView)findViewById(R.id.listView);
    }

    public void setList(ArrayList<Task> taskArrayList) {
        this.tasks = taskArrayList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Connect extends AsyncTask<String, Void, ArrayList<Task>> {

        private ViewTaskActivity viewTaskActivity;
        ArrayList<Task> taskList = new ArrayList<>();

        public Connect(ViewTaskActivity activity) {
            this.viewTaskActivity = activity;
        }

        @Override
        protected ArrayList<Task> doInBackground(String... urls) {

            Connection con = null;
            Statement stmt = null;
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database connection successful");
                stmt = con.createStatement();

                String queryStr = "SELECT * FROM TASKS";
                ResultSet rs = stmt.executeQuery(queryStr);
                while(rs.next()){
                    Task task = new Task();
                    task.setTaskId(rs.getLong(1));
                    task.setTaskName(rs.getString(2));
                    task.setTaskDesc(rs.getString(3));
                    task.setDueDate(rs.getString(4));
                    task.setAssignee(rs.getString(5));
                    task.setAssignor(rs.getString(6));
                    task.setReminder(rs.getBoolean(7));
                    task.setStatus(rs.getBoolean(8));
                    taskList.add(task);
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
            return taskList;
        }

        @Override
        protected void onPostExecute(ArrayList<Task> taskList) {
            viewTaskActivity.setList(this.taskList);
            CustomListAdapter adapter=new CustomListAdapter(this.viewTaskActivity, taskList);
            //listView=(ListView)findViewById(R.id.listView);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    Task selectedTaskItem = tasks.get(+position);
                    //String selectedTask = tasks.get(+position).getTaskName();
                    //Toast.makeText(getApplicationContext(), selectedTask, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), ViewTaskDetailsActivity.class);
                    intent.putExtra("TaskObject", selectedTaskItem);
                    intent.putExtra("TaskName", selectedTaskItem.getTaskName());
                    startActivity(intent);
                }
            });
        }
    }

    public void Connect(ViewTaskActivity activity) {
        Connect task = new Connect(activity);
        task.execute();
    }
}
