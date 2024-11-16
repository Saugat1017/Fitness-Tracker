package com.cse3310.myfitnesstracker;

import android.content.Context;

public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    private FitnessDatabaseHelper db = null;

    public static Singleton getInstance() {
        if(instance == null) {
            instance = new Singleton();
        }

        return instance;
    }

    public FitnessDatabaseHelper getDb(Context context)
    {
        if(db==null)
        {
            db = new FitnessDatabaseHelper(context);
        }

        return db;
    }
}
