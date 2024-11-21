package com.cse3310.myfitnesstracker.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cse3310.myfitnesstracker.FitnessDatabaseHelper;
import com.cse3310.myfitnesstracker.LoginActivity;
import com.cse3310.myfitnesstracker.R;
import com.cse3310.myfitnesstracker.RegisterActivity;
import com.cse3310.myfitnesstracker.Singleton;
import com.cse3310.myfitnesstracker.databinding.FragmentGoalsBinding;
import com.cse3310.myfitnesstracker.databinding.FragmentSettingsBinding;
import com.cse3310.myfitnesstracker.ui.goals.MyCheckBox;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private FitnessDatabaseHelper db = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button clearButton = root.findViewById(R.id.clearDataButton);

        db = Singleton.getInstance().getDb(getContext());



        clearButton.setOnClickListener(v -> {

            db.resetDatabase();
            Toast.makeText(getContext(), "Data Cleared!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}