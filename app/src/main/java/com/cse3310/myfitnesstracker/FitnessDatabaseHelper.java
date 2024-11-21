package com.cse3310.myfitnesstracker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class FitnessDatabaseHelper extends SQLiteOpenHelper {
    private static FitnessDatabaseHelper instance;
    private static final String DATABASE_NAME = "FitnessTracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USER = "user_data";
    private static final String TABLE_WORKOUT = "workout_history";
    private static final String TABLE_GOAL = "goal_data";

    // User table columns
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_GOALCMPLT = "gcmplt";
    private static final String COLUMN_USER_GOALTOTAL = "gtotal";
    private static final String COLUMN_USER_IS_SUBSCRIBED = "is_subscribed";


    // Goal table columns
    private static final String COLUMN_GOAL_ID = "goal_id";
    private static final String COLUMN_GOAL_USERID = "user_id";
    private static final String COLUMN_GOAL_TITLE = "title";

    // Workout table columns
    private static final String COLUMN_WORKOUT_ID = "workout_id";
    private static final String COLUMN_WORKOUT_USERID = "user_id";
    private static final String COLUMN_WORKOUT_ACTIVITY = "activity";
    private static final String COLUMN_WORKOUT_DURATION = "duration";
    private static final String COLUMN_WORKOUT_STEPS = "steps";
    private static final String COLUMN_WORKOUT_CALORIES = "calories";
    private static final String COLUMN_WORKOUT_DATE = "date";


    private int usrId;
    private String usrEmail;
    private String usrName;
    private int usrGoalCmplt;
    private int usrGoalTotal;
    private int usrIsSubscribed; // 1 is subscribed, 0 is not subscribed
    private boolean userInstantiated = false;

    public FitnessDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_PASSWORD + " TEXT, "
                + COLUMN_USER_EMAIL + " TEXT, "
                + COLUMN_USER_GOALCMPLT + " INTEGER, "
                + COLUMN_USER_GOALTOTAL + " INTEGER, "
                + COLUMN_USER_IS_SUBSCRIBED + " INTEGER)";

        db.execSQL(CREATE_USER_TABLE);

        // Create Workout History table
        String CREATE_WORKOUT_TABLE = "CREATE TABLE " + TABLE_WORKOUT + "("
                + COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WORKOUT_USERID + " INTEGER, "
                + COLUMN_WORKOUT_ACTIVITY + " TEXT, "
                + COLUMN_WORKOUT_DURATION + " INTEGER, "
                + COLUMN_WORKOUT_STEPS + " INTEGER, "
                + COLUMN_WORKOUT_CALORIES + " INTEGER, "
                + COLUMN_WORKOUT_DATE + " TEXT)";
        db.execSQL(CREATE_WORKOUT_TABLE);

        String CREATE_GOAL_TABLE = "CREATE TABLE " + TABLE_GOAL + "("
                + COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GOAL_USERID + " INTEGER, "
                + COLUMN_GOAL_TITLE + " TEXT)";
        db.execSQL(CREATE_GOAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOAL);
        onCreate(db);
    }

    public void addUser(String name, String password, String email, int gcmplt, int gtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_GOALCMPLT, gcmplt);
        values.put(COLUMN_USER_GOALTOTAL, gtotal);
        values.put(COLUMN_USER_IS_SUBSCRIBED, 0); //always starts as 0

        db.insert(TABLE_USER, null, values);
        db.close();

        usrGoalCmplt = gcmplt;
        usrGoalTotal = gtotal;
    }

    public void updateUser(int userID, int gcmplt, int gtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_GOALCMPLT, gcmplt);
        values.put(COLUMN_USER_GOALTOTAL, gtotal);
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userID)});
        db.close();
        usrGoalCmplt = gcmplt;
        usrGoalTotal = gtotal;
    }

    public void updateSubscription(int userID, int subscribed) {
        usrIsSubscribed = subscribed;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_IS_SUBSCRIBED, subscribed);
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userID)});
        db.close();
    }

    public void addWorkout(int userID, String activity, int duration, int steps, int calories, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT_USERID, userID);
        values.put(COLUMN_WORKOUT_ACTIVITY, activity);
        values.put(COLUMN_WORKOUT_DURATION, duration);
        values.put(COLUMN_WORKOUT_STEPS, steps);
        values.put(COLUMN_WORKOUT_CALORIES, calories);
        values.put(COLUMN_WORKOUT_DATE, date);

        db.insert(TABLE_WORKOUT, null, values);
        db.close();
    }

    public void addGoal(int userID, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_USERID, userID);
        values.put(COLUMN_GOAL_TITLE, title);

        db.insert(TABLE_GOAL, null, values);
        db.close();
    }

    public void removeGoal(int userID, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GOAL, COLUMN_GOAL_USERID + " = ? AND " + COLUMN_GOAL_TITLE + " = ?", new String[]{String.valueOf(userID), title});
        db.close();
    }

    public int getUserID()
    {
        if(!userInstantiated)
            return -1;
        else
            return usrId;
    }

    public String getName()
    {
        if(!userInstantiated)
            return null;
        else
            return usrName;
    }

    public String getEmail()
    {
        if(!userInstantiated)
            return null;
        else
            return usrEmail;
    }

    public int getGoalCmplt()
    {
        if(!userInstantiated)
            return -1;
        else
            return usrGoalCmplt;
    }

    public int getUsrGoalTotal()
    {
        if(!userInstantiated)
            return -1;
        else
            return usrGoalTotal;
    }

    public int getIsSubscribed()
    {
        if(!userInstantiated)
            return 0;
        else
            return usrIsSubscribed;
    }

    public boolean isUserInstantiated()
    {
        return userInstantiated;
    }

    // Get list of Workout History
    public List<String> getWorkoutHistory() {
        List<String> workoutList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_WORKOUT;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int userID = cursor.getInt(cursor.getColumnIndex(COLUMN_WORKOUT_USERID));
                @SuppressLint("Range") String activity = cursor.getString(cursor.getColumnIndex(COLUMN_WORKOUT_ACTIVITY));
                @SuppressLint("Range") int duration = cursor.getInt(cursor.getColumnIndex(COLUMN_WORKOUT_DURATION));
                @SuppressLint("Range") int steps = cursor.getInt(cursor.getColumnIndex(COLUMN_WORKOUT_STEPS));
                @SuppressLint("Range") int calories = cursor.getInt(cursor.getColumnIndex(COLUMN_WORKOUT_CALORIES));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_WORKOUT_DATE));
                workoutList.add(activity + " - " + duration + " mins on " + date);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return workoutList;
    }

    public HashMap<Integer, ArrayList<String>> getGoalData() {
        HashMap<Integer, ArrayList<String>> goalMap = new HashMap<Integer, ArrayList<String>>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_GOAL;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int userID = cursor.getInt(cursor.getColumnIndex(COLUMN_GOAL_USERID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_GOAL_TITLE));
                if(goalMap.containsKey(userID))
                {
                    Objects.requireNonNull(goalMap.get(userID)).add(title);
                }
                else
                {
                    ArrayList<String> goalList = new ArrayList<String>();
                    goalList.add(title);
                    goalMap.put(userID, goalList);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return goalMap;
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOAL);
        onCreate(db); // Recreate the tables
        db.close();
    }

    public HashMap<Integer, ArrayList<String>> getUsers() {
        HashMap<Integer, ArrayList<String>> usrMap = new HashMap<Integer, ArrayList<String>>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int userID = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
                @SuppressLint("Range") int gcmplt = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GOALCMPLT));
                @SuppressLint("Range") int gtotal = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GOALTOTAL));
                @SuppressLint("Range") int issub = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_IS_SUBSCRIBED));

                if(usrMap.containsKey(userID))
                {
                    Objects.requireNonNull(usrMap.get(userID)).add(name);
                    Objects.requireNonNull(usrMap.get(userID)).add(email);
                    Objects.requireNonNull(usrMap.get(userID)).add(String.valueOf(gcmplt));
                    Objects.requireNonNull(usrMap.get(userID)).add(String.valueOf(gtotal));
                }
                else
                {
                    ArrayList<String> usrList = new ArrayList<String>();
                    usrList.add(name);
                    usrList.add(email);
                    usrList.add(String.valueOf(gcmplt));
                    usrList.add(String.valueOf(gtotal));
                    usrList.add(String.valueOf(issub));
                    usrMap.put(userID, usrList);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return usrMap;
    }

    public void logout()
    {
        userInstantiated = false;
        usrId = -1;
        usrName = null;
        usrEmail = null;
        usrGoalCmplt = -1;
        usrGoalTotal = -1;
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username});

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    @SuppressLint("Range")
    public boolean checkLoginCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " =? AND " + COLUMN_USER_PASSWORD + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username, password});

        boolean loginSuccess = cursor.moveToFirst();

        if (loginSuccess)
        {
            usrId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            usrName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            usrEmail = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
            usrGoalCmplt = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GOALCMPLT));
            usrGoalTotal = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GOALTOTAL));
            usrIsSubscribed = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_IS_SUBSCRIBED));

            userInstantiated = true;
        }

        cursor.close();
        db.close();

        return loginSuccess;
    }

    public static synchronized FitnessDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FitnessDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
}