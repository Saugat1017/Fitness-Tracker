package com.cse3310.myfitnesstracker.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse3310.myfitnesstracker.FitnessDatabaseHelper;
import com.cse3310.myfitnesstracker.R;
import com.cse3310.myfitnesstracker.Singleton;
import com.cse3310.myfitnesstracker.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FitnessDatabaseHelper db;
    private TextView tvWelcome, tvTodaySteps, tvTodayCalories, tvGoalProgress;
    private ProgressBar pbDailyGoal;
    private CardView cvQuickWorkout, cvAddGoal;
    private LineChart chartWeeklyActivity;
    private RecyclerView rvRecentWorkouts;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = Singleton.getInstance().getDb(getContext());
        initializeViews(root);
        setupDashboard();
        setupQuickActions();
        setupWeeklyChart();
        setupRecentWorkouts();

        return root;
    }

    private void initializeViews(View root) {
        tvWelcome = root.findViewById(R.id.tv_welcome);
        tvTodaySteps = root.findViewById(R.id.tv_today_steps);
        tvTodayCalories = root.findViewById(R.id.tv_today_calories);
        tvGoalProgress = root.findViewById(R.id.tv_goal_progress);
        pbDailyGoal = root.findViewById(R.id.pb_daily_goal);
        cvQuickWorkout = root.findViewById(R.id.cv_quick_workout);
        cvAddGoal = root.findViewById(R.id.cv_add_goal);
        chartWeeklyActivity = root.findViewById(R.id.chart_weekly_activity);
        rvRecentWorkouts = root.findViewById(R.id.rv_recent_workouts);
    }

    private void setupDashboard() {
        if (db.isUserInstantiated()) {
            String userName = db.getName();
            tvWelcome.setText("Welcome back, " + userName + "!");

            // Get today's stats
            long todaySteps = getTodaySteps();
            long todayCalories = getTodayCalories();

            tvTodaySteps.setText(String.valueOf(todaySteps));
            tvTodayCalories.setText(String.valueOf(todayCalories));

            // Calculate goal progress
            int goalProgress = calculateGoalProgress();
            tvGoalProgress.setText(goalProgress + "%");
            pbDailyGoal.setProgress(goalProgress);
        }
    }

    private void setupQuickActions() {
        cvQuickWorkout.setOnClickListener(v -> {
            showQuickWorkoutDialog();
        });

        cvAddGoal.setOnClickListener(v -> {
            showAddGoalDialog();
        });
    }

    private void setupWeeklyChart() {
        chartWeeklyActivity.getDescription().setEnabled(false);
        chartWeeklyActivity.setTouchEnabled(true);
        chartWeeklyActivity.setDragEnabled(true);
        chartWeeklyActivity.setScaleEnabled(true);
        chartWeeklyActivity.setPinchZoom(true);
        chartWeeklyActivity.setBackgroundColor(getResources().getColor(R.color.white));

        List<Entry> entries = getWeeklyActivityData();
        LineDataSet dataSet = new LineDataSet(entries, "Weekly Activity");
        dataSet.setColor(getResources().getColor(R.color.purple_500));
        dataSet.setCircleColor(getResources().getColor(R.color.purple_500));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        chartWeeklyActivity.setData(lineData);
        chartWeeklyActivity.invalidate();
    }

    private void setupRecentWorkouts() {
        rvRecentWorkouts.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> recentWorkouts = db.getWorkoutHistory();
        if (recentWorkouts.size() > 5) {
            recentWorkouts = recentWorkouts.subList(0, 5);
        }
        RecentWorkoutAdapter adapter = new RecentWorkoutAdapter(recentWorkouts);
        rvRecentWorkouts.setAdapter(adapter);
    }

    private long getTodaySteps() {
        // This would integrate with step counter sensor
        // For now, return a sample value
        return db.getTotalSteps() + (long) (Math.random() * 1000);
    }

    private long getTodayCalories() {
        // This would calculate based on activity and BMR
        // For now, return a sample value
        return db.getTotalCalories() + (long) (Math.random() * 100);
    }

    private int calculateGoalProgress() {
        // Calculate progress towards daily goal
        long todaySteps = getTodaySteps();
        int dailyGoal = 10000; // Default 10k steps
        return Math.min((int) ((todaySteps * 100) / dailyGoal), 100);
    }

    private List<Entry> getWeeklyActivityData() {
        List<Entry> entries = new ArrayList<>();
        // Sample data for the last 7 days
        for (int i = 0; i < 7; i++) {
            entries.add(new Entry(i, (float) (Math.random() * 10000)));
        }
        return entries;
    }

    private void showQuickWorkoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_quick_workout, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Setup quick workout options
        Button btnWalking = dialogView.findViewById(R.id.btn_walking);
        Button btnRunning = dialogView.findViewById(R.id.btn_running);
        Button btnCycling = dialogView.findViewById(R.id.btn_cycling);

        btnWalking.setOnClickListener(v -> {
            startQuickWorkout("Walking");
            dialog.dismiss();
        });

        btnRunning.setOnClickListener(v -> {
            startQuickWorkout("Running");
            dialog.dismiss();
        });

        btnCycling.setOnClickListener(v -> {
            startQuickWorkout("Cycling");
            dialog.dismiss();
        });
    }

    private void showAddGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Goal");
        builder.setMessage("Goal functionality will be implemented in the Goals section.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Toast.makeText(getContext(), "Please use the Goals section to add goals", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void startQuickWorkout(String activity) {
        // Start workout tracking
        Toast.makeText(getContext(), "Starting " + activity + " workout...", Toast.LENGTH_SHORT).show();
        // Here you would start the workout tracking service
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Adapter for recent workouts
    private static class RecentWorkoutAdapter extends RecyclerView.Adapter<RecentWorkoutAdapter.ViewHolder> {
        private List<String> workouts;

        public RecentWorkoutAdapter(List<String> workouts) {
            this.workouts = workouts;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_workout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String workout = workouts.get(position);
            holder.tvWorkoutInfo.setText(workout);
        }

        @Override
        public int getItemCount() {
            return workouts.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvWorkoutInfo;

            ViewHolder(View itemView) {
                super(itemView);
                tvWorkoutInfo = itemView.findViewById(R.id.tv_workout_info);
            }
        }
    }
}