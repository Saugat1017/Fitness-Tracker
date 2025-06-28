package com.cse3310.myfitnesstracker.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cse3310.myfitnesstracker.MainActivity;
import com.cse3310.myfitnesstracker.R;
import com.cse3310.myfitnesstracker.Singleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class WorkoutTrackingService extends Service implements SensorEventListener {
    private static final String TAG = "WorkoutTrackingService";
    private static final String CHANNEL_ID = "WorkoutTrackingChannel";
    private static final int NOTIFICATION_ID = 1;

    private final IBinder binder = new LocalBinder();
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private String currentActivity = "Unknown";
    private long startTime;
    private int stepCount = 0;
    private double totalDistance = 0.0;
    private int caloriesBurned = 0;
    private List<Location> locationHistory = new ArrayList<>();
    private boolean isTracking = false;

    // Callback interface for UI updates
    public interface WorkoutTrackingCallback {
        void onStepCountChanged(int stepCount);

        void onDistanceChanged(double distance);

        void onCaloriesChanged(int calories);

        void onDurationChanged(long duration);
    }

    private WorkoutTrackingCallback callback;

    public class LocalBinder extends Binder {
        public WorkoutTrackingService getService() {
            return WorkoutTrackingService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        initializeSensors();
        initializeLocationServices();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            currentActivity = intent.getStringExtra("activity_type");
            startWorkout();
        }

        startForeground(NOTIFICATION_ID, createNotification());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Workout Tracking",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Shows workout progress");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Workout in Progress")
                .setContentText(currentActivity + " - " + stepCount + " steps")
                .setSmallIcon(R.drawable.ic_workout)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    private void initializeSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.w(TAG, "Step sensor not available");
        }
    }

    private void initializeLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && isTracking) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        locationHistory.add(location);
                        updateDistance();
                    }
                }
            }
        };
    }

    public void startWorkout() {
        startTime = System.currentTimeMillis();
        stepCount = 0;
        totalDistance = 0.0;
        caloriesBurned = 0;
        locationHistory.clear();
        isTracking = true;

        startLocationUpdates();
        Log.d(TAG, "Workout started: " + currentActivity);
    }

    public void stopWorkout() {
        isTracking = false;
        stopLocationUpdates();

        // Save workout to database
        saveWorkout();

        stopForeground(true);
        stopSelf();
        Log.d(TAG, "Workout stopped");
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000) // 5 seconds
                .setFastestInterval(3000); // 3 seconds

        try {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission not granted", e);
        }
    }

    private void stopLocationUpdates() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void updateDistance() {
        if (locationHistory.size() >= 2) {
            Location prevLocation = locationHistory.get(locationHistory.size() - 2);
            Location currentLocation = locationHistory.get(locationHistory.size() - 1);

            double distance = prevLocation.distanceTo(currentLocation);
            totalDistance += distance;

            if (callback != null) {
                callback.onDistanceChanged(totalDistance);
            }
        }
    }

    private void calculateCalories() {
        // Simple calorie calculation based on steps and activity type
        double caloriesPerStep = 0.04; // Approximate calories per step

        switch (currentActivity.toLowerCase()) {
            case "running":
                caloriesPerStep = 0.08;
                break;
            case "cycling":
                caloriesPerStep = 0.06;
                break;
            case "walking":
            default:
                caloriesPerStep = 0.04;
                break;
        }

        caloriesBurned = (int) (stepCount * caloriesPerStep);

        if (callback != null) {
            callback.onCaloriesChanged(caloriesBurned);
        }
    }

    private void saveWorkout() {
        long duration = (System.currentTimeMillis() - startTime) / 1000; // Duration in seconds
        String date = java.time.LocalDate.now().toString();

        // Save to database
        Singleton.getInstance().getDb(this).addWorkout(
                Singleton.getInstance().getDb(this).getUserID(),
                currentActivity,
                (int) duration,
                stepCount,
                caloriesBurned,
                date,
                totalDistance,
                0, // Heart rate (would need heart rate sensor)
                "Workout completed successfully");
    }

    public void setCallback(WorkoutTrackingCallback callback) {
        this.callback = callback;
    }

    // Getter methods for current workout data
    public int getStepCount() {
        return stepCount;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public long getDuration() {
        return isTracking ? (System.currentTimeMillis() - startTime) / 1000 : 0;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public boolean isTracking() {
        return isTracking;
    }

    // SensorEventListener implementation
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && isTracking) {
            stepCount = (int) event.values[0];

            if (callback != null) {
                callback.onStepCountChanged(stepCount);
            }

            calculateCalories();
            updateNotification();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void updateNotification() {
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(NOTIFICATION_ID, createNotification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        stopLocationUpdates();
    }
}