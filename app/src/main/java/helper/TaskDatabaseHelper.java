package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import models.Task;
import models.User;

/**
 * Created by Apoorva on 11/30/2015.
 */
public class TaskDatabaseHelper extends SQLiteOpenHelper{


    //Common to all tables
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "YOURTURN_APP_DB";

    //Task activity
    private static final String TASK_DATABASE_NAME = "TASKS";
    public static final String COLUMN_TASK_ID = "_id";
    public static final String COLUMN_TASK_NAME = "tname";
    public static final String COLUMN_TASK_DESC = "tdesc";
    public static final String COLUMN_TASK_DATE = "tdate";
    public static final String COLUMN_TASK_ASSIGNEE = "tassignee";
    public static final String COLUMN_TASK_ASSIGNOR = "tassignor";
    public static final String COLUMN_TASK_REMINDER = "treminder";
    public static final String COLUMN_TASK_STATUS = "tstatus";
    private static final String TASK_DATABASE_CREATE =
            "CREATE TABLE " + TASK_DATABASE_NAME + " ( " +
                    COLUMN_TASK_ID + " integer primary key autoincrement, " +
                    COLUMN_TASK_NAME + " varchar(25), " +
                    COLUMN_TASK_DESC + " text, " +
                    COLUMN_TASK_DATE + " date, " +
                    COLUMN_TASK_ASSIGNOR + " varchar(25), " +
                    COLUMN_TASK_ASSIGNEE + " varchar(25), " +
                    COLUMN_TASK_REMINDER + " boolean, " +
                    COLUMN_TASK_STATUS + " boolean" + ")";

    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS ";

    private static final String FETCH_TASKS_QUERY = "SELECT * FROM " + TASK_DATABASE_NAME;


    //Register activity
    private static final String USER_DATABASE_NAME = "PERSONS";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USER_NAME = "uname";
    public static final String COLUMN_USER_PHNO = "uphno";
    public static final String COLUMN_USER_BDATE = "ubdate";
    public static final String COLUMN_USER_EMAIL = "uemail";
    public static final String COLUMN_USER_PASS = "upass";
    public static final String COLUMN_USER_GROUP = "uGroup";
    private static final String USER_DATABASE_CREATE =
            "CREATE TABLE " + USER_DATABASE_NAME + " ( " +
                    COLUMN_USER_ID + " integer primary key autoincrement, " +
                    COLUMN_USER_NAME + " varchar(25), " +
                    COLUMN_USER_PHNO + " text, " +
                    COLUMN_USER_BDATE + " date, " +
                    COLUMN_USER_EMAIL + " varchar(25), " +
                    COLUMN_USER_PASS + " varchar(25), " +
                    COLUMN_USER_GROUP + " varchar(25) " +" )";

    private static final String FETCH_USERS_QUERY_WITH_ID = "SELECT * FROM " + USER_DATABASE_NAME + " WHERE " + COLUMN_USER_GROUP + " = ";

    private static final String FETCH_USERS_QUERY = "SELECT * FROM " + USER_DATABASE_NAME;


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TASK_DATABASE_CREATE);
        sqLiteDatabase.execSQL(USER_DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY+TASK_DATABASE_NAME);
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY+USER_DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long insertTask(Task task) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_TASK_NAME, task.getTaskName());
        Log.d("Task Name: ", task.getTaskName());
        values.put(COLUMN_TASK_DESC, task.getTaskDesc());
        Log.d("Task desc: ", task.getTaskDesc());
        values.put(COLUMN_TASK_DATE, task.getDueDate());
        values.put(COLUMN_TASK_ASSIGNOR, task.getAssignor());
        values.put(COLUMN_TASK_ASSIGNEE, task.getAssignee());
        values.put(COLUMN_TASK_REMINDER, false);
        values.put(COLUMN_TASK_STATUS, false);

        return getWritableDatabase().insert(TASK_DATABASE_NAME, null, values);
    }

    public ArrayList<Task> fetchTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        Cursor cursor = getReadableDatabase().rawQuery(FETCH_TASKS_QUERY, null);
        if(cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTaskId(Long.parseLong(cursor.getString(0)));
                task.setTaskName(cursor.getString(1));
                task.setTaskDesc(cursor.getString(2));
                task.setDueDate(cursor.getString(3));
                task.setAssignor(cursor.getString(4));
                task.setAssignee(cursor.getString(5));
                task.setReminder(Boolean.getBoolean(cursor.getString(6)));
                task.setStatus(Boolean.getBoolean(cursor.getString(7)));
                tasks.add(task);
            } while(cursor.moveToNext());
        }
        return tasks;
    }

    public long insertUser(User user) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_NAME, user.getUserName());
        Log.d("User Name: ", user.getUserName());
        values.put(COLUMN_USER_PHNO, user.getUserNumber());
        Log.d("User Number: ", user.getUserNumber());
        values.put(COLUMN_USER_BDATE, user.getUserBDate());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_USER_PASS,user.getUserPass());
        values.put(COLUMN_USER_GROUP, user.getUserGroup());

        return getWritableDatabase().insert(USER_DATABASE_NAME, null, values);
    }

    public ArrayList<String> fetchGroupMembers(String groupID) {
        ArrayList<String> users = new ArrayList<String>();
        String userName = "";
        Cursor cursor = getReadableDatabase().rawQuery(FETCH_USERS_QUERY_WITH_ID+ "'" + groupID + "'", null);
        if(cursor.moveToFirst()) {
            do {
                userName = cursor.getString(1);
                users.add(userName);
            } while(cursor.moveToNext());
        }
        return users;
    }

    public ArrayList<User> fetchMembers() {
        ArrayList<User> users = new ArrayList<User>();
        Cursor cursor = getReadableDatabase().rawQuery(FETCH_USERS_QUERY, null);
        if(cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserName(cursor.getString(1));
                user.setUserGroup(cursor.getString(5));
                users.add(user);
            } while(cursor.moveToNext());
        }
        return users;
    }

}
