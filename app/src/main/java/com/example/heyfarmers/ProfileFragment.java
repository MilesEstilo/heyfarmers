package com.example.heyfarmers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private ActivityResultLauncher<String> pickImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView imageView = view.findViewById(R.id.imageView4);
        EditText editTextTextPersonName = view.findViewById(R.id.editTextTextPersonName);

        // Load saved name
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String savedName = sharedPreferences.getString("user_name", "");
        editTextTextPersonName.setText(savedName);

        // Initialize pickImage launcher
        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageView.setImageURI(uri);
                    }
                });

        Button changeProfilePictureButton = view.findViewById(R.id.button2);
        changeProfilePictureButton.setOnClickListener(v -> pickImage.launch("image/*"));

        editTextTextPersonName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newName = editTextTextPersonName.getText().toString();
                saveUserName(newName);
            }
        });

        Button logoutButton = view.findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            // Clear user data from SharedPreferences
            clearUserData();

            // Redirect to MainActivity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void saveUserName(String newName) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", newName);
        editor.apply();
    }

    private void clearUserData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data
        editor.apply();
    }
}
