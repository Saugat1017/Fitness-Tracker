package com.cse3310.myfitnesstracker.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cse3310.myfitnesstracker.FitnessDatabaseHelper;
import com.cse3310.myfitnesstracker.LoginActivity;
import com.cse3310.myfitnesstracker.Singleton;
import com.cse3310.myfitnesstracker.databinding.FragmentLogoutBinding;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FitnessDatabaseHelper db = Singleton.getInstance().getDb(getContext());
        db.logout();

        Toast.makeText(getContext(), "Logged Out!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), LoginActivity.class));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

}
