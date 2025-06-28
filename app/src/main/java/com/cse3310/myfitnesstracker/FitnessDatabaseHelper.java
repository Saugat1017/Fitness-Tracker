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
    private static final int DATABASE_VERSION = 2; // Updated version for new tables

    // Table names
    private static final String TABLE_USER = "user_data";
    private static final String TABLE_WORKOUT = "workout_history";
    private static final String TABLE_GOAL = "goal_data";
    private static final String TABLE_ACHIEVEMENT = "achievements";
    private static final String TABLE_WORKOUT_PLAN = "workout_plans";
    private static final String TABLE_SOCIAL = "social_data";

    // User table columns
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_GOALCMPLT = "gcmplt";
    private static final String COLUMN_USER_GOALTOTAL = "gtotal";
    private static final String COLUMN_USER_IS_SUBSCRIBED = "is_subscribed";
    private static final String COLUMN_USER_WEIGHT = "weight";
    private static final String COLUMN_USER_HEIGHT = "height";
    private static final String COLUMN_USER_AGE = "age";
    private static final String COLUMN_USER_GENDER = "gender";
    private static final String COLUMN_USER_TOTAL_STEPS = "total_steps";
    private static final String COLUMN_USER_TOTAL_CALORIES = "total_calories";

    // Goal table columns
    private static final String COLUMN_GOAL_ID = "goal_id";
    private static final String COLUMN_GOAL_USERID = "user_id";
    private static final String COLUMN_GOAL_TITLE = "title";
    private static final String COLUMN_GOAL_DESCRIPTION = "description";
    private static final String COLUMN_GOAL_TARGET = "target";
    private static final String COLUMN_GOAL_CURRENT = "current";
    private static final String COLUMN_GOAL_TYPE = "type";
    private static final String COLUMN_GOAL_DEADLINE = "deadline";
    private static final String COLUMN_GOAL_COMPLETED = "completed";

    // Workout table columns
    private static final String COLUMN_WORKOUT_ID = "workout_id";
    private static final String COLUMN_WORKOUT_USERID = "user_id";
    private static final String COLUMN_WORKOUT_ACTIVITY = "activity";
    private static final String COLUMN_WORKOUT_DURATION = "duration";
    private static final String COLUMN_WORKOUT_STEPS = "steps";
    private static final String COLUMN_WORKOUT_CALORIES = "calories";
    private static final String COLUMN_WORKOUT_DATE = "date";
    private static final String COLUMN_WORKOUT_DISTANCE = "distance";
    private static final String COLUMN_WORKOUT_HEART_RATE = "heart_rate";
    private static final String COLUMN_WORKOUT_NOTES = "notes";

    // Achievement table columns
    private static final String COLUMN_ACHIEVEMENT_ID = "achievement_id";
    private static final String COLUMN_ACHIEVEMENT_USERID = "user_id";
    private static final String COLUMN_ACHIEVEMENT_TITLE = "title";
    private static final String COLUMN_ACHIEVEMENT_DESCRIPTION = "description";
    private static final String COLUMN_ACHIEVEMENT_ICON = "icon";
    private static final String COLUMN_ACHIEVEMENT_DATE_EARNED = "date_earned";
    private static final String COLUMN_ACHIEVEMENT_TYPE = "type";

    // Workout Plan table columns
    private static final String COLUMN_PLAN_ID = "plan_id";
    private static final String COLUMN_PLAN_USERID = "user_id";
    private static final String COLUMN_PLAN_NAME = "name";
    private static final String COLUMN_PLAN_DESCRIPTION = "description";
    private static final String COLUMN_PLAN_DURATION = "duration";
    private static final String COLUMN_PLAN_DIFFICULTY = "difficulty";
    private static final String COLUMN_PLAN_EXERCISES = "exercises";
    private static final String COLUMN_PLAN_ACTIVE = "active";

    // Social table columns
    private static final String COLUMN_SOCIAL_ID = "social_id";
    private static final String COLUMN_SOCIAL_USERID = "user_id";
    private static final String COLUMN_SOCIAL_FRIENDID = "friend_id";
    private static final String COLUMN_SOCIAL_STATUS = "status";
    private static final String COLUMN_SOCIAL_DATE = "date";

    private int usrId;
    private String usrEmail;
    private String usrName;
    private int usrGoalCmplt;
    private int usrGoalTotal;
    private int usrIsSubscribed;
    private double usrWeight;
    private double usrHeight;
    private int usrAge;
    private String usrGender;
    private long usrTotalSteps;
    private long usrTotalCalories;
    private boolean userInstantiated = false;

    public FitnessDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User table with enhanced columns
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_PASSWORD + " TEXT, "
                + COLUMN_USER_EMAIL + " TEXT, "
                + COLUMN_USER_GOALCMPLT + " INTEGER, "
                + COLUMN_USER_GOALTOTAL + " INTEGER, "
                + COLUMN_USER_IS_SUBSCRIBED + " INTEGER, "
                + COLUMN_USER_WEIGHT + " REAL, "
                + COLUMN_USER_HEIGHT + " REAL, "
                + COLUMN_USER_AGE + " INTEGER, "
                + COLUMN_USER_GENDER + " TEXT, "
                + COLUMN_USER_TOTAL_STEPS + " INTEGER, "
                + COLUMN_USER_TOTAL_CALORIES + " INTEGER)";

        db.execSQL(CREATE_USER_TABLE);

        // Create Workout History table with enhanced columns
        String CREATE_WORKOUT_TABLE = "CREATE TABLE " + TABLE_WORKOUT + "("
                + COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WORKOUT_USERID + " INTEGER, "
                + COLUMN_WORKOUT_ACTIVITY + " TEXT, "
                + COLUMN_WORKOUT_DURATION + " INTEGER, "
                + COLUMN_WORKOUT_STEPS + " INTEGER, "
                + COLUMN_WORKOUT_CALORIES + " INTEGER, "
                + COLUMN_WORKOUT_DATE + " TEXT, "
                + COLUMN_WORKOUT_DISTANCE + " REAL, "
                + COLUMN_WORKOUT_HEART_RATE + " INTEGER, "
                + COLUMN_WORKOUT_NOTES + " TEXT)";
        db.execSQL(CREATE_WORKOUT_TABLE);

        // Create enhanced Goal table
        String CREATE_GOAL_TABLE = "CREATE TABLE " + TABLE_GOAL + "("
                + COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GOAL_USERID + " INTEGER, "
                + COLUMN_GOAL_TITLE + " TEXT, "
                + COLUMN_GOAL_DESCRIPTION + " TEXT, "
                + COLUMN_GOAL_TARGET + " REAL, "
                + COLUMN_GOAL_CURRENT + " REAL, "
                + COLUMN_GOAL_TYPE + " TEXT, "
                + COLUMN_GOAL_DEADLINE + " TEXT, "
                + COLUMN_GOAL_COMPLETED + " INTEGER)";
        db.execSQL(CREATE_GOAL_TABLE);

        // Create Achievement table
        String CREATE_ACHIEVEMENT_TABLE = "CREATE TABLE " + TABLE_ACHIEVEMENT + "("
                + COLUMN_ACHIEVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ACHIEVEMENT_USERID + " INTEGER, "
                + COLUMN_ACHIEVEMENT_TITLE + " TEXT, "
                + COLUMN_ACHIEVEMENT_DESCRIPTION + " TEXT, "
                + COLUMN_ACHIEVEMENT_ICON + " TEXT, "
                + COLUMN_ACHIEVEMENT_DATE_EARNED + " TEXT, "
                + COLUMN_ACHIEVEMENT_TYPE + " TEXT)";
        db.execSQL(CREATE_ACHIEVEMENT_TABLE);

        // Create Workout Plan table
        String CREATE_WORKOUT_PLAN_TABLE = "CREATE TABLE " + TABLE_WORKOUT_PLAN + "("
                + COLUMN_PLAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLAN_USERID + " INTEGER, "
                + COLUMN_PLAN_NAME + " TEXT, "
                + COLUMN_PLAN_DESCRIPTION + " TEXT, "
                + COLUMN_PLAN_DURATION + " INTEGER, "
                + COLUMN_PLAN_DIFFICULTY + " TEXT, "
                + COLUMN_PLAN_EXERCISES + " TEXT, "
                + COLUMN_PLAN_ACTIVE + " INTEGER)";
        db.execSQL(CREATE_WORKOUT_PLAN_TABLE);

        // Create Social table
        String CREATE_SOCIAL_TABLE = "CREATE TABLE " + TABLE_SOCIAL + "("
                + COLUMN_SOCIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SOCIAL_USERID + " INTEGER, "
                + COLUMN_SOCIAL_FRIENDID + " INTEGER, "
                + COLUMN_SOCIAL_STATUS + " TEXT, "
                + COLUMN_SOCIAL_DATE + " TEXT)";
        db.execSQL(CREATE_SOCIAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add new columns to existing tables
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_USER_WEIGHT + " REAL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_USER_HEIGHT + " REAL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_USER_AGE + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_USER_GENDER + " TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_USER_TOTAL_STEPS + " INTEGER DEFAULT 0");
            db.execSQL(
                    "ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_USER_TOTAL_CALORIES + " INTEGER DEFAULT 0");

            db.execSQL("ALTER TABLE " + TABLE_WORKOUT + " ADD COLUMN " + COLUMN_WORKOUT_DISTANCE + " REAL DEFAULT 0");
            db.execSQL(
                    "ALTER TABLE " + TABLE_WORKOUT + " ADD COLUMN " + COLUMN_WORKOUT_HEART_RATE + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_WORKOUT + " ADD COLUMN " + COLUMN_WORKOUT_NOTES + " TEXT DEFAULT ''");

            db.execSQL("ALTER TABLE " + TABLE_GOAL + " ADD COLUMN " + COLUMN_GOAL_DESCRIPTION + " TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_GOAL + " ADD COLUMN " + COLUMN_GOAL_TARGET + " REAL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_GOAL + " ADD COLUMN " + COLUMN_GOAL_CURRENT + " REAL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_GOAL + " ADD COLUMN " + COLUMN_GOAL_TYPE + " TEXT DEFAULT 'general'");
            db.execSQL("ALTER TABLE " + TABLE_GOAL + " ADD COLUMN " + COLUMN_GOAL_DEADLINE + " TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_GOAL + " ADD COLUMN " + COLUMN_GOAL_COMPLETED + " INTEGER DEFAULT 0");
        }
    }

    public void addUser(String name, String password, String email, int gcmplt, int gtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_GOALCMPLT, gcmplt);
        values.put(COLUMN_USER_GOALTOTAL, gtotal);
        values.put(COLUMN_USER_IS_SUBSCRIBED, 0);
        values.put(COLUMN_USER_WEIGHT, 0.0);
        values.put(COLUMN_USER_HEIGHT, 0.0);
        values.put(COLUMN_USER_AGE, 0);
        values.put(COLUMN_USER_GENDER, "");
        values.put(COLUMN_USER_TOTAL_STEPS, 0);
        values.put(COLUMN_USER_TOTAL_CALORIES, 0);

        db.insert(TABLE_USER, null, values);
        db.close();

        usrGoalCmplt = gcmplt;
        usrGoalTotal = gtotal;

        checkLoginCredentials(name, password);
    }

    public void updateUser(int userID, int gcmplt, int gtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_GOALCMPLT, gcmplt);
        values.put(COLUMN_USER_GOALTOTAL, gtotal);
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[] { String.valueOf(userID) });
        db.close();
        usrGoalCmplt = gcmplt;
        usrGoalTotal = gtotal;
    }

    public void updateSubscription(int userID, int subscribed) {
        usrIsSubscribed = subscribed;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_IS_SUBSCRIBED, subscribed);
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[] { String.valueOf(userID) });
        db.close();
    }

    public void addWorkout(int userID, String activity, int duration, int steps, int calories, String date,
            double distance, int heartRate, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT_USERID, userID);
        values.put(COLUMN_WORKOUT_ACTIVITY, activity);
        values.put(COLUMN_WORKOUT_DURATION, duration);
        values.put(COLUMN_WORKOUT_STEPS, steps);
        values.put(COLUMN_WORKOUT_CALORIES, calories);
        values.put(COLUMN_WORKOUT_DATE, date);
        values.put(COLUMN_WORKOUT_DISTANCE, distance);
        values.put(COLUMN_WORKOUT_HEART_RATE, heartRate);
        values.put(COLUMN_WORKOUT_NOTES, notes);

        db.insert(TABLE_WORKOUT, null, values);
        db.close();
    }

    public void addGoal(int userID, String title, String description, double target, String type, String deadline) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_USERID, userID);
        values.put(COLUMN_GOAL_TITLE, title);
        values.put(COLUMN_GOAL_DESCRIPTION, description);
        values.put(COLUMN_GOAL_TARGET, target);
        values.put(COLUMN_GOAL_CURRENT, 0.0);
        values.put(COLUMN_GOAL_TYPE, type);
        values.put(COLUMN_GOAL_DEADLINE, deadline);
        values.put(COLUMN_GOAL_COMPLETED, 0);

        db.insert(TABLE_GOAL, null, values);
        db.close();
    }

    public void removeGoal(int userID, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GOAL, COLUMN_GOAL_USERID + " = ? AND " + COLUMN_GOAL_TITLE + " = ?",
                new String[] { String.valueOf(userID), title });
        db.close();
    }

    public int getUserID() {
        if (!userInstantiated)
            return -1;
        else
            return usrId;
    }

    public String getName() {
        if (!userInstantiated)
            return null;
        else
            return usrName;
    }

    public String getEmail() {
        if (!userInstantiated)
            return null;
        else
            return usrEmail;
    }

    public int getGoalCmplt() {
        if (!userInstantiated)
            return -1;
        else
            return usrGoalCmplt;
    }

    public int getUsrGoalTotal() {
        if (!userInstantiated)
            return -1;
        else
            return usrGoalTotal;
    }

    public int getIsSubscribed() {
        if (!userInstantiated)
            return 0;
        else
            return usrIsSubscribed;
    }

    public boolean isUserInstantiated() {
        return userInstantiated;
    }

    public List<String> getWorkoutHistory() {
        List<String> workoutList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WORKOUT + " WHERE " + COLUMN_WORKOUT_USERID + " = " + usrId
                + " ORDER BY " + COLUMN_WORKOUT_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                String workout = "Activity: " + cursor.getString(cursor.getColumnIndex(COLUMN_WORKOUT_ACTIVITY)) +
                        "\nDuration: " + cursor.getString(cursor.getColumnIndex(COLUMN_WORKOUT_DURATION)) + " minutes" +
                        "\nSteps: " + cursor.getString(cursor.getColumnIndex(COLUMN_WORKOUT_STEPS)) +
                        "\nCalories: " + cursor.getString(cursor.getColumnIndex(COLUMN_WORKOUT_CALORIES)) +
                        "\nDate: " + cursor.getString(cursor.getColumnIndex(COLUMN_WORKOUT_DATE)) +
                        "\nDistance: " + cursor.getDouble(cursor.getColumnIndex(COLUMN_WORKOUT_DISTANCE)) + " km";
                workoutList.add(workout);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return workoutList;
    }

    public HashMap<Integer, ArrayList<String>> getGoalData() {
        HashMap<Integer, ArrayList<String>> goalMap = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_GOAL + " WHERE " + COLUMN_GOAL_USERID + " = " + usrId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int goalId = cursor.getInt(cursor.getColumnIndex(COLUMN_GOAL_ID));
                ArrayList<String> goalInfo = new ArrayList<>();
                goalInfo.add(cursor.getString(cursor.getColumnIndex(COLUMN_GOAL_TITLE)));
                goalInfo.add(cursor.getString(cursor.getColumnIndex(COLUMN_GOAL_DESCRIPTION)));
                goalInfo.add(String.valueOf(cursor.getDouble(cursor.getColumnIndex(COLUMN_GOAL_TARGET))));
                goalInfo.add(String.valueOf(cursor.getDouble(cursor.getColumnIndex(COLUMN_GOAL_CURRENT))));
                goalInfo.add(cursor.getString(cursor.getColumnIndex(COLUMN_GOAL_TYPE)));
                goalInfo.add(cursor.getString(cursor.getColumnIndex(COLUMN_GOAL_DEADLINE)));
                goalInfo.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_GOAL_COMPLETED))));
                goalMap.put(goalId, goalInfo);
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
                @SuppressLint("Range")
                int userID = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
                @SuppressLint("Range")
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
                @SuppressLint("Range")
                int gcmplt = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GOALCMPLT));
                @SuppressLint("Range")
                int gtotal = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GOALTOTAL));
                @SuppressLint("Range")
                int issub = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_IS_SUBSCRIBED));

                if (usrMap.containsKey(userID)) {
                    Objects.requireNonNull(usrMap.get(userID)).add(name);
                    Objects.requireNonNull(usrMap.get(userID)).add(email);
                    Objects.requireNonNull(usrMap.get(userID)).add(String.valueOf(gcmplt));
                    Objects.requireNonNull(usrMap.get(userID)).add(String.valueOf(gtotal));
                } else {
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

    public void logout() {
        userInstantiated = false;
        usrId = -1;
        usrName = null;
        usrEmail = null;
        usrGoalCmplt = -1;
        usrGoalTotal = -1;
    }

    @SuppressLint("Range")
    public boolean checkLoginCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " =? AND "
                + COLUMN_USER_PASSWORD + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { username, password });

        boolean loginSuccess = cursor.moveToFirst();

        if (loginSuccess) {
            usrId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            usrName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            usrEmail = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
            usrGoalCmplt = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GOALCMPLT));
            usrGoalTotal = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GOALTOTAL));
            usrIsSubscribed = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_IS_SUBSCRIBED));
            usrWeight = cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_WEIGHT));
            usrHeight = cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_HEIGHT));
            usrAge = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_AGE));
            usrGender = cursor.getString(cursor.getColumnIndex(COLUMN_USER_GENDER));
            usrTotalSteps = cursor.getLong(cursor.getColumnIndex(COLUMN_USER_TOTAL_STEPS));
            usrTotalCalories = cursor.getLong(cursor.getColumnIndex(COLUMN_USER_TOTAL_CALORIES));

            userInstantiated = true;
        }

        cursor.close();
        db.close();

        return loginSuccess;
    }

    public void updateUserProfile(int userID, double weight, double height, int age, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_WEIGHT, weight);
        values.put(COLUMN_USER_HEIGHT, height);
        values.put(COLUMN_USER_AGE, age);
        values.put(COLUMN_USER_GENDER, gender);
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[] { String.valueOf(userID) });
        db.close();

        usrWeight = weight;
        usrHeight = height;
        usrAge = age;
        usrGender = gender;
    }

    public void updateUserStats(int userID, long totalSteps, long totalCalories) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_TOTAL_STEPS, totalSteps);
        values.put(COLUMN_USER_TOTAL_CALORIES, totalCalories);
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[] { String.valueOf(userID) });
        db.close();

        usrTotalSteps = totalSteps;
        usrTotalCalories = totalCalories;
    }

    public void updateGoalProgress(int goalID, double current) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_CURRENT, current);
        db.update(TABLE_GOAL, values, COLUMN_GOAL_ID + " = ?", new String[] { String.valueOf(goalID) });
        db.close();
    }

    public void addAchievement(int userID, String title, String description, String icon, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACHIEVEMENT_USERID, userID);
        values.put(COLUMN_ACHIEVEMENT_TITLE, title);
        values.put(COLUMN_ACHIEVEMENT_DESCRIPTION, description);
        values.put(COLUMN_ACHIEVEMENT_ICON, icon);
        values.put(COLUMN_ACHIEVEMENT_DATE_EARNED,
                new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        .format(new java.util.Date()));
        values.put(COLUMN_ACHIEVEMENT_TYPE, type);

        db.insert(TABLE_ACHIEVEMENT, null, values);
        db.close();
    }

    public void addWorkoutPlan(int userID, String name, String description, int duration, String difficulty,
            String exercises) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAN_USERID, userID);
        values.put(COLUMN_PLAN_NAME, name);
        values.put(COLUMN_PLAN_DESCRIPTION, description);
        values.put(COLUMN_PLAN_DURATION, duration);
        values.put(COLUMN_PLAN_DIFFICULTY, difficulty);
        values.put(COLUMN_PLAN_EXERCISES, exercises);
        values.put(COLUMN_PLAN_ACTIVE, 0);

        db.insert(TABLE_WORKOUT_PLAN, null, values);
        db.close();
    }

    public void addFriend(int userID, int friendID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOCIAL_USERID, userID);
        values.put(COLUMN_SOCIAL_FRIENDID, friendID);
        values.put(COLUMN_SOCIAL_STATUS, "pending");
        values.put(COLUMN_SOCIAL_DATE, java.time.LocalDate.now().toString());

        db.insert(TABLE_SOCIAL, null, values);
        db.close();
    }

    public double getWeight() {
        return userInstantiated ? usrWeight : 0.0;
    }

    public double getHeight() {
        return userInstantiated ? usrHeight : 0.0;
    }

    public int getAge() {
        return userInstantiated ? usrAge : 0;
    }

    public String getGender() {
        return userInstantiated ? usrGender : "";
    }

    public long getTotalSteps() {
        return userInstantiated ? usrTotalSteps : 0;
    }

    public long getTotalCalories() {
        return userInstantiated ? usrTotalCalories : 0;
    }

    public static synchronized FitnessDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FitnessDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { username });
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }
}