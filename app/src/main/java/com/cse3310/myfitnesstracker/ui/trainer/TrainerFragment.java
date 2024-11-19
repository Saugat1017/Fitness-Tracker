package com.cse3310.myfitnesstracker.ui.trainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.cse3310.myfitnesstracker.databinding.FragmentTrainerBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse3310.myfitnesstracker.R;
import com.cse3310.myfitnesstracker.ui.trainer.TrainerViewModel;

import java.util.ArrayList;
import java.util.List;

public class TrainerFragment extends Fragment {

    private RecyclerView trainerRecyclerView;
    private List<Trainer> trainerList;
    private FragmentTrainerBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrainerViewModel subscriptionViewModel =
                new ViewModelProvider(this).get(TrainerViewModel.class);

        binding = FragmentTrainerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        trainerRecyclerView = root.findViewById(R.id.trainer_recycler_view);

        // Prepare sample trainer data
        trainerList = new ArrayList<>();
        trainerList.add(new Trainer("Roman Doe", 30, R.drawable.trainer1, 4.5f));
        trainerList.add(new Trainer("Jake Smith", 28, R.drawable.trainer2, 4.8f));
        trainerList.add(new Trainer("Mike Brown", 58, R.drawable.trainer3, 4.2f));

        // Set up the RecyclerView with GridLayout
        TrainerAdapter adapter = new TrainerAdapter(trainerList);
        trainerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        trainerRecyclerView.setAdapter(adapter);
        return root;
    }

    // Trainer class to hold data
    public static class Trainer {
        String name;
        int age;
        int photo;
        float rating;

        public Trainer(String name, int age, int photo, float rating) {
            this.name = name;
            this.age = age;
            this.photo = photo;
            this.rating = rating;
        }
    }

    // Adapter for RecyclerView
    public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder> {

        private final List<Trainer> trainers;

        public TrainerAdapter(List<Trainer> trainers) {
            this.trainers = trainers;
        }

        @Override
        public TrainerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainer, parent, false);
            return new TrainerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TrainerViewHolder holder, int position) {
            Trainer trainer = trainers.get(position);
            holder.nameTextView.setText(trainer.name);
            holder.ageTextView.setText("Age: " + trainer.age);
            holder.ratingBar.setRating(trainer.rating);
            holder.photoImageView.setImageResource(trainer.photo);
        }

        @Override
        public int getItemCount() {
            return trainers.size();
        }

        public class TrainerViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView, ageTextView;
            ImageView photoImageView;
            RatingBar ratingBar;

            public TrainerViewHolder(View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.trainer_name);
                ageTextView = itemView.findViewById(R.id.trainer_age);
                photoImageView = itemView.findViewById(R.id.trainer_photo);
                ratingBar = itemView.findViewById(R.id.trainer_rating);
            }
        }
    }
}
