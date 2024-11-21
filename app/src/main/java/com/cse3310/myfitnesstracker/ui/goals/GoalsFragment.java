package com.cse3310.myfitnesstracker.ui.goals;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cse3310.myfitnesstracker.FitnessDatabaseHelper;
import com.cse3310.myfitnesstracker.R;
import com.cse3310.myfitnesstracker.Singleton;
import com.cse3310.myfitnesstracker.databinding.FragmentGoalsBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GoalsFragment extends Fragment {

    public static final int MAX_GOALS = 15; // Goal limit
    private int numGoals = 0;
    private int goalsCompleted = 0;
    private int totalGoals = 0;
    private TextView progressPct;
    private ProgressBar pBar;
    private FragmentGoalsBinding binding;
    private FitnessDatabaseHelper db = null;

    private HashMap<Integer, ArrayList<String>> goalMap = null;
    private ArrayList<String> myGoals = null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button addButton = root.findViewById(R.id.addGoalButton);
        LinearLayout goalsContainer = root.findViewById(R.id.goalsLayout); // Layout for goals
        progressPct = root.findViewById(R.id.progressPct);
        pBar = root.findViewById(R.id.progressBar);

        db = Singleton.getInstance().getDb(getContext());

        if(db.isUserInstantiated()) {
            goalsCompleted = db.getGoalCmplt();
            totalGoals = db.getUsrGoalTotal();

            goalMap = db.getGoalData();

            myGoals = goalMap.get(db.getUserID());

            if(myGoals != null)
            {
                numGoals = myGoals.size();

                for (String goal : myGoals) {
                    MyCheckBox checkBox = new MyCheckBox(getContext(),this, goalsContainer, db);
                    checkBox.setText(goal);
                    goalsContainer.addView(checkBox);
                }
            }
            if(goalsCompleted != 0 && totalGoals != 0)
            {
                progressPct.setText(new StringBuilder().append(((int) ((float)goalsCompleted / totalGoals * 100))).append("%").toString());
                pBar.setProgress((int) ((float)goalsCompleted / totalGoals * 100));
            }
        }
        if(db.getIsSubscribed() == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = inflater.inflate(R.layout.popup_ad, null);
            builder.setView(dialogView);

            ImageView btn = dialogView.findViewById(R.id.btn_close);

            AlertDialog adDialog = builder.create();

            adDialog.show();

            btn.setOnClickListener(v -> {
                adDialog.hide();
            });
        }



        addButton.setOnClickListener(v -> {
            AlertDialog.Builder inputBuilder = new AlertDialog.Builder(getContext());
            inputBuilder.setTitle("Add Goal");

            EditText input = new EditText(getContext());
            inputBuilder.setView(input);

            inputBuilder.setPositiveButton("Add", (dialog, which) -> {
                String userInput = input.getText().toString().trim();
                if (userInput.isEmpty()) {
                    Toast.makeText(getContext(), "Input cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (numGoals >= MAX_GOALS) {
                    Toast.makeText(getContext(), "Too many goals!", Toast.LENGTH_SHORT).show();
                } else {
                    // Add goal as a CheckBox
                    MyCheckBox checkBox = new MyCheckBox(getContext(),this, goalsContainer, db);
                    checkBox.setText(userInput);

                    goalsContainer.addView(checkBox);

                    db.addGoal(db.getUserID(), userInput);

                    totalGoals++;
                    numGoals++;

                    progressPct.setText(new StringBuilder().append(((int) ((float)goalsCompleted / totalGoals * 100))).append("%").toString());
                    pBar.setProgress((int) ((float)goalsCompleted / totalGoals * 100));

                    db.updateUser(db.getUserID(), goalsCompleted, totalGoals);

                    Toast.makeText(getContext(), "Goal Added...", Toast.LENGTH_SHORT).show();
                }
            });

            inputBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            inputBuilder.show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        db.updateUser(db.getUserID(), goalsCompleted, totalGoals);
        super.onDestroyView();
        binding = null;
    }

    public void finishGoals()
    {
        numGoals--;
        goalsCompleted++;
        pBar.setProgress((int) ((float)goalsCompleted / totalGoals * 100));
        progressPct.setText(new StringBuilder().append(((int) ((float)goalsCompleted / totalGoals * 100))).append("%").toString());
        db.updateUser(db.getUserID(), goalsCompleted, totalGoals);
    }
}
