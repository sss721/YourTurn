package com.example.yourturnmobileapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import helper.AlarmReceiver;
import helper.SessionHelper;
import helper.TaskDatabaseHelper;
import models.Task;
import models.User;

/**
 * Created by Apoorva on 10/31/2015.
 */
public class AddTaskActivity extends Activity {

    private static final String TAG = "AddTaskActivity";
    private static final String url = "jdbc:mysql://androidinstance.cgrfkz1xyvvw.us-east-1.rds.amazonaws.com:3306/androidyourturn";
    private static final String user = "awsuser";
    private static final String pass = "apoorva123";
    EditText dueDate;
    Calendar tripCalendarDate;
    public Spinner assignee;
    EditText taskName;
    EditText taskDesc;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private EditText alarmTimePicker;
    TimePicker myTimePicker;
    Button setReminder;
    TimePickerDialog timePickerDialog;
    final static int RQS_1 = 1;
    private SessionHelper session;
    Context cntx;
    Task task;
    ArrayList<String> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskName = (EditText) findViewById(R.id.taskName);
        taskDesc = (EditText) findViewById(R.id.taskDesc);
        alarmTimePicker = (EditText) findViewById(R.id.alarmTimePicker);
        setReminder = (Button) findViewById(R.id.setReminder);
        assignee = (Spinner) findViewById(R.id.assignee);

        ConnectToLoad(this);



        final ImageButton createTask1 = (ImageButton) findViewById(R.id.createTask1);
        createTask1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = createTask();
                Connect();
            }
        });

        final ImageButton canTask = (ImageButton) findViewById(R.id.canTask);
        canTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTask();
            }
        });

        dueDate = (EditText) findViewById(R.id.dueDate);
        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    Calendar currentDate = Calendar.getInstance();
                    int currentYear = currentDate.get(Calendar.YEAR);
                    int currentMonth = currentDate.get(Calendar.MONTH);
                    int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog mDatePicker = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            tripCalendarDate = Calendar.getInstance();
                            tripCalendarDate.set(selectedyear, selectedmonth, selectedday);
                            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                            dueDate.setText(format1.format(tripCalendarDate.getTime()));
                            dueDate.clearFocus();
                        }
                    }, currentYear, currentMonth, currentDay);

                    mDatePicker.setTitle("Select Due Date");
                    mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    mDatePicker.show();
                }
            }
        });

        setReminder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setReminder.setVisibility(View.INVISIBLE);
                alarmTimePicker.setVisibility(View.VISIBLE);
                openTimePickerDialog(false);

            }});






        //ArrayList<String> assignees = new ArrayList<String>();
        //TaskDatabaseHelper registerDatabaseHelper = new TaskDatabaseHelper(getBaseContext());
        //SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
        //String groupID = sp.getString("groupID", null);
        //assignees = registerDatabaseHelper.fetchGroupMembers(groupID);
        //assignees.add(0, "--Select--");
        //assignees.add("Apoorva");



    }

    public void setList(ArrayList<String> memList) {
        this.memberList = memList;
    }

    private void openTimePickerDialog(boolean is24r){
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(
                AddTaskActivity.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if(calSet.compareTo(calNow) <= 0){
                //Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal){
        alarmTimePicker.setText(targetCal.getTime().getHours() + ":" + targetCal.getTime().getMinutes());
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }

    public Task createTask() {
        Task task = new Task();
        task.setTaskName(taskName.getText().toString());
        task.setTaskDesc(taskDesc.getText().toString());
        task.setAssignee(String.valueOf(assignee.getSelectedItem()));
        try {
            SimpleDateFormat from = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");
            Date date = from.parse(dueDate.getText().toString());       // 01/02/2014
            String mysqlString = to.format(date);     // 2014-02-01
            task.setDueDate(mysqlString);

        } catch(ParseException exception) {
            exception.printStackTrace();
        }
        task.setAssignor("Keval");
        final ParseObject parsedb = new ParseObject("TestObject");
        parsedb.put("TaskName", taskName.getText().toString());
        parsedb.put("TaskDesc",taskDesc.getText().toString() );
        parsedb.put("Assignee", String.valueOf(assignee.getSelectedItem()));
        parsedb.put("Date",dueDate.getText().toString());
        parsedb.put("Assignor","Keval" ) ;
        parsedb.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if(e==null){
                    String objectId = parsedb.getObjectId();


                }
            }
        });
        return task;
    }

    public void cancelTask() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
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

                String queryStr = "INSERT INTO TASKS (tname, tdesc, tdate, tassignee, tassignor, treminder, tstatus) VALUES ('" +
                        task.getTaskName()+"','"+task.getTaskDesc()+
                        "','"+task.getDueDate()+ "','" + task.getAssignee() + "','" + task.getAssignor()+"',0,0)";
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
            Toast.makeText(getBaseContext(), "Task added successfully", Toast.LENGTH_LONG).show();
            Intent viewTaskIntent = new Intent(getBaseContext(), ViewTaskActivity.class);
            startActivity(viewTaskIntent);
        }
    }

    public void Connect() {
        Connect task = new Connect();
        task.execute();

    }

    private class ConnectToLoad extends AsyncTask<String, Void, ArrayList<String>> {

        private AddTaskActivity addTaskActivity;
        private ArrayList<String> memberList = new ArrayList<>();

        public ConnectToLoad(AddTaskActivity activity) {
            this.addTaskActivity = activity;
        }


        @Override
        protected ArrayList<String> doInBackground(String... urls) {

            Connection con = null;
            Statement stmt = null;
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database connection successful");
                stmt = con.createStatement();

               String queryString = "SELECT * FROM PERSONS";
               ResultSet rs = stmt.executeQuery(queryString);
                String uName;
                while(rs.next()){
                    uName = rs.getString(2);
                    memberList.add(uName);
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
            }
            return memberList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> memberList) {
            //memberList = this.addTaskActivity.memberList;
            addTaskActivity.setList(this.memberList);
            ArrayAdapter dataAdapter = new ArrayAdapter(this.addTaskActivity, android.R.layout.simple_spinner_item,this.memberList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            assignee.setAdapter(dataAdapter);
        }
    }

    public void ConnectToLoad(AddTaskActivity activity) {
        ConnectToLoad task = new ConnectToLoad(activity);
        task.execute();

    }

}

