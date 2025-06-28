package com.cse3310.myfitnesstracker.ui.progress;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cse3310.myfitnesstracker.R;
import com.cse3310.myfitnesstracker.Singleton;
import com.cse3310.myfitnesstracker.FitnessDatabaseHelper;
import com.cse3310.myfitnesstracker.databinding.FragmentProgressBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ProgressFragment extends Fragment {

    private FragmentProgressBinding binding;
    private FitnessDatabaseHelper db;
    private TextView tvTotalWorkouts, tvTotalSteps, tvTotalCalories, tvAvgDuration;
    private BarChart chartWeeklyProgress;
    private PieChart chartActivityDistribution;
    private Spinner spinnerTimeRange;
    private LinearLayout llStatsContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        ProgressViewModel progressViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        binding = FragmentProgressBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = Singleton.getInstance().getDb(getContext());
        initializeViews(root);
        setupProgressCharts();
        loadProgressData();
        setupTimeRangeSpinner();

        // Show subscription ad if not subscribed
        if (db.getIsSubscribed() == 0) {
            showSubscriptionAd();
        }

        return root;
    }

    private void initializeViews(View root) {
        tvTotalWorkouts = root.findViewById(R.id.tv_total_workouts);
        tvTotalSteps = root.findViewById(R.id.tv_total_steps);
        tvTotalCalories = root.findViewById(R.id.tv_total_calories);
        tvAvgDuration = root.findViewById(R.id.tv_avg_duration);
        chartWeeklyProgress = root.findViewById(R.id.chart_weekly_progress);
        chartActivityDistribution = root.findViewById(R.id.chart_activity_distribution);
        spinnerTimeRange = root.findViewById(R.id.spinner_time_range);
        llStatsContainer = root.findViewById(R.id.ll_stats_container);
    }

    private void setupProgressCharts() {
        setupWeeklyProgressChart();
        setupActivityDistributionChart();
    }

    private void setupWeeklyProgressChart() {
        chartWeeklyProgress.getDescription().setEnabled(false);
        chartWeeklyProgress.setTouchEnabled(true);
        chartWeeklyProgress.setDragEnabled(true);
        chartWeeklyProgress.setScaleEnabled(true);
        chartWeeklyProgress.setPinchZoom(true);
        chartWeeklyProgress.setBackgroundColor(getResources().getColor(R.color.white));
        chartWeeklyProgress.getXAxis().setDrawGridLines(false);
        chartWeeklyProgress.getAxisLeft().setDrawGridLines(true);
        chartWeeklyProgress.getAxisRight().setEnabled(false);
        chartWeeklyProgress.getLegend().setEnabled(true);

        List<BarEntry> entries = getWeeklyProgressData();
        BarDataSet dataSet = new BarDataSet(entries, "Weekly Steps");
        dataSet.setColor(getResources().getColor(R.color.purple_500));
        dataSet.setValueTextColor(getResources().getColor(R.color.text_primary));

        BarData barData = new BarData(dataSet);
        chartWeeklyProgress.setData(barData);
        chartWeeklyProgress.invalidate();
    }

    private void setupActivityDistributionChart() {
        chartActivityDistribution.getDescription().setEnabled(false);
        chartActivityDistribution.setUsePercentValues(true);
        chartActivityDistribution.getLegend().setEnabled(true);
        chartActivityDistribution.setEntryLabelTextSize(12f);
        chartActivityDistribution.setEntryLabelColor(getResources().getColor(R.color.text_primary));

        List<PieEntry> entries = getActivityDistributionData();
        PieDataSet dataSet = new PieDataSet(entries, "Activity Types");
        dataSet.setColors(getActivityColors());
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(getResources().getColor(R.color.white));

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(chartActivityDistribution));
        chartActivityDistribution.setData(pieData);
        chartActivityDistribution.invalidate();
    }

    private List<BarEntry> getWeeklyProgressData() {
        List<BarEntry> entries = new ArrayList<>();
        // Sample data for the last 7 days
        String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, (float) (Math.random() * 10000)));
        }
        return entries;
    }

    private List<PieEntry> getActivityDistributionData() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "Walking"));
        entries.add(new PieEntry(30f, "Running"));
        entries.add(new PieEntry(20f, "Cycling"));
        entries.add(new PieEntry(10f, "Other"));
        return entries;
    }

    private List<Integer> getActivityColors() {
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.green_500));
        colors.add(getResources().getColor(R.color.blue_500));
        colors.add(getResources().getColor(R.color.orange_500));
        colors.add(getResources().getColor(R.color.purple_500));
        return colors;
    }

    private void loadProgressData() {
        if (db.isUserInstantiated()) {
            // Load workout history
            List<String> workouts = db.getWorkoutHistory();
            int totalWorkouts = workouts.size();

            // Calculate totals
            long totalSteps = db.getTotalSteps();
            long totalCalories = db.getTotalCalories();
            int avgDuration = totalWorkouts > 0 ? 45 : 0; // Sample average duration

            // Update UI
            tvTotalWorkouts.setText(String.valueOf(totalWorkouts));
            tvTotalSteps.setText(String.valueOf(totalSteps));
            tvTotalCalories.setText(String.valueOf(totalCalories));
            tvAvgDuration.setText(avgDuration + " min");

            // Add achievement badges
            addAchievementBadges(totalWorkouts, totalSteps, totalCalories);
        }
    }

    private void addAchievementBadges(int totalWorkouts, long totalSteps, long totalCalories) {
        llStatsContainer.removeAllViews();

        // Workout count achievements
        if (totalWorkouts >= 10) {
            addBadge("10 Workouts", "Completed 10 workouts", R.color.green_500);
        }
        if (totalWorkouts >= 50) {
            addBadge("50 Workouts", "Completed 50 workouts", R.color.blue_500);
        }
        if (totalWorkouts >= 100) {
            addBadge("100 Workouts", "Completed 100 workouts", R.color.purple_500);
        }

        // Step count achievements
        if (totalSteps >= 100000) {
            addBadge("100K Steps", "Walked 100,000 steps", R.color.orange_500);
        }
        if (totalSteps >= 500000) {
            addBadge("500K Steps", "Walked 500,000 steps", R.color.red_500);
        }

        // Calorie achievements
        if (totalCalories >= 10000) {
            addBadge("10K Calories", "Burned 10,000 calories", R.color.green_500);
        }
    }

    private void addBadge(String title, String description, int color) {
        View badgeView = LayoutInflater.from(getContext()).inflate(R.layout.item_achievement_badge, null);

        TextView tvBadgeTitle = badgeView.findViewById(R.id.tv_badge_title);
        TextView tvBadgeDescription = badgeView.findViewById(R.id.tv_badge_description);
        View badgeBackground = badgeView.findViewById(R.id.badge_background);

        tvBadgeTitle.setText(title);
        tvBadgeDescription.setText(description);
        badgeBackground.setBackgroundColor(color);

        llStatsContainer.addView(badgeView);
    }

    private void setupTimeRangeSpinner() {
        String[] timeRanges = { "Last 7 Days", "Last 30 Days", "Last 3 Months", "All Time" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, timeRanges);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeRange.setAdapter(adapter);

        spinnerTimeRange.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateChartData(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void updateChartData(int timeRangeIndex) {
        // Update chart data based on selected time range
        List<BarEntry> entries = new ArrayList<>();

        switch (timeRangeIndex) {
            case 0: // Last 7 days
                for (int i = 0; i < 7; i++) {
                    entries.add(new BarEntry(i, (float) (Math.random() * 10000)));
                }
                break;
            case 1: // Last 30 days
                for (int i = 0; i < 30; i++) {
                    entries.add(new BarEntry(i, (float) (Math.random() * 10000)));
                }
                break;
            case 2: // Last 3 months
                for (int i = 0; i < 12; i++) {
                    entries.add(new BarEntry(i, (float) (Math.random() * 30000)));
                }
                break;
            case 3: // All time
                for (int i = 0; i < 6; i++) {
                    entries.add(new BarEntry(i, (float) (Math.random() * 50000)));
                }
                break;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColor(getResources().getColor(R.color.purple_500));

        BarData barData = new BarData(dataSet);
        chartWeeklyProgress.setData(barData);
        chartWeeklyProgress.invalidate();
    }

    private void showSubscriptionAd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_ad, null);
        builder.setView(dialogView);

        ImageView btn = dialogView.findViewById(R.id.btn_close);

        AlertDialog adDialog = builder.create();
        adDialog.show();

        btn.setOnClickListener(v -> {
            adDialog.hide();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}