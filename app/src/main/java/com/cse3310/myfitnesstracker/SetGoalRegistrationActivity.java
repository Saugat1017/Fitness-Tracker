package com.cse3310.myfitnesstracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Spinner;

import com.cse3310.myfitnesstracker.ui.goals.MyCheckBox;
import com.google.android.material.navigation.NavigationView;


public class SetGoalRegistrationActivity extends AppCompatActivity{

    Spinner goalSelectorDropDown;
    Button addGoalButton;
    EditText goalNumber;
    private int goalNum;
    private String goal;
    private FitnessDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_add_goal);

        addGoalButton = findViewById(R.id.addGoalButton);
        goalNumber = findViewById(R.id.goalNumber);

        // Set up drop down menu for selecting goal
        goalSelectorDropDown = findViewById(R.id.goalSelectorDropDown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.goalDropDownSelections, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSelectorDropDown.setAdapter(adapter);

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Convert the text to an integer
                    goalNum = Integer.parseInt(String.valueOf(goalNumber.getText()));
                    Toast.makeText(SetGoalRegistrationActivity.this, "The number is: " + goalNum, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid number
                    Toast.makeText(SetGoalRegistrationActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
                goal = goalSelectorDropDown.getSelectedItem().toString();
                Toast.makeText(SetGoalRegistrationActivity.this, "Goal: " + goal, Toast.LENGTH_SHORT).show();

                // Add to database

            }

        });

        // Implement back button
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

}
