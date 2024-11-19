package com.cse3310.myfitnesstracker.ui.exercise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse3310.myfitnesstracker.R;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private final List<Exercise> exerciseList;

    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.exerciseImage.setImageResource(exercise.getImageResId());
        holder.exerciseName.setText(exercise.getName());
        holder.exerciseDescription.setText(exercise.getDescription());

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Exercise: " + exercise.getName(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        ImageView exerciseImage;
        TextView exerciseName, exerciseDescription;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseImage = itemView.findViewById(R.id.exercise_image);
            exerciseName = itemView.findViewById(R.id.exercise_name);
            exerciseDescription = itemView.findViewById(R.id.exercise_description);
        }
    }
}
