package com.cse3310.myfitnesstracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.ArrayList;


public class FitnessDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FitnessTracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USER = "user_data";
    private static final String TABLE_WORKOUT = "workout_history";

    // User table columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_EMAIL = "email";

    // Workout table columns
    private static final String COLUMN_WORKOUT_ID = "id";
    private static final String COLUMN_WORKOUT_USERID = "user_id";
    private static final String COLUMN_WORKOUT_ACTIVITY = "activity";
    private static final String COLUMN_WORKOUT_DURATION = "duration";
    private static final String COLUMN_WORKOUT_STEPS = "steps";
    private static final String COLUMN_WORKOUT_CALORIES = "calories";
    private static final String COLUMN_WORKOUT_DATE = "date";



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
                + COLUMN_USER_EMAIL + " TEXT)";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
        onCreate(db);
    }

    public void addUser(String name, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_EMAIL, email);

        db.insert(TABLE_USER, null, values);
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
}