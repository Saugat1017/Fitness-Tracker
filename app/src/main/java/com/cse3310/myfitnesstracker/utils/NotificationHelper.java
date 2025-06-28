package com.cse3310.myfitnesstracker.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.cse3310.myfitnesstracker.MainActivity;
import com.cse3310.myfitnesstracker.R;

public class NotificationHelper {
    private static final String CHANNEL_ID_WORKOUT = "workout_channel";
    private static final String CHANNEL_ID_GOALS = "goals_channel";
    private static final String CHANNEL_ID_REMINDERS = "reminders_channel";

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            // Workout channel
            NotificationChannel workoutChannel = new NotificationChannel(
                    CHANNEL_ID_WORKOUT,
                    "Workout Notifications",
                    NotificationManager.IMPORTANCE_HIGH);
            workoutChannel.setDescription("Notifications for workout tracking and progress");

            // Goals channel
            NotificationChannel goalsChannel = new NotificationChannel(
                    CHANNEL_ID_GOALS,
                    "Goal Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            goalsChannel.setDescription("Notifications for goal achievements and progress");

            // Reminders channel
            NotificationChannel remindersChannel = new NotificationChannel(
                    CHANNEL_ID_REMINDERS,
                    "Reminder Notifications",
                    NotificationManager.IMPORTANCE_LOW);
            remindersChannel.setDescription("Daily reminders and motivational messages");

            notificationManager.createNotificationChannel(workoutChannel);
            notificationManager.createNotificationChannel(goalsChannel);
            notificationManager.createNotificationChannel(remindersChannel);
        }
    }

    public static void showWorkoutReminder(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
                .setSmallIcon(R.drawable.ic_workout)
                .setContentTitle("Time for a Workout!")
                .setContentText("Don't forget to stay active today. Your body will thank you!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(1001, builder.build());
    }

    public static void showGoalAchievement(Context context, String goalTitle) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_GOALS)
                .setSmallIcon(R.drawable.ic_goal)
                .setContentTitle("Goal Achieved! 🎉")
                .setContentText("Congratulations! You've completed: " + goalTitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(1002, builder.build());
    }

    public static void showAchievementUnlocked(Context context, String achievementTitle) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_GOALS)
                .setSmallIcon(R.drawable.ic_trophy)
                .setContentTitle("Achievement Unlocked! 🏆")
                .setContentText("You've earned: " + achievementTitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(1003, builder.build());
    }

    public static void showMotivationalMessage(Context context, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
                .setSmallIcon(R.drawable.ic_workout)
                .setContentTitle("Motivation 💪")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(1004, builder.build());
    }
}