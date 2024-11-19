package com.cse3310.myfitnesstracker.ui.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse3310.myfitnesstracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exerciseList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Populate data
        exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise("Push-Ups", "A basic upper body exercise.", R.drawable.pushup));
        exerciseList.add(new Exercise("Squats", "Strengthens legs and glutes.", R.drawable.squat));
        exerciseList.add(new Exercise("Plank", "Core strengthening exercise.", R.drawable.plank));

        // Set Adapter
        exerciseAdapter = new ExerciseAdapter(exerciseList);
        recyclerView.setAdapter(exerciseAdapter);

        // Floating Action Button for adding new exercises (optional)
        FloatingActionButton fab = view.findViewById(R.id.fab_add_exercise);
        fab.setOnClickListener(v -> Toast.makeText(getContext(), "Add New Exercise (Feature Pending)", Toast.LENGTH_SHORT).show());

        return view;
    }
}
