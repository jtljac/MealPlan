package com.jacob.mealplan.ui.mealcomponents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jacob.mealplan.R;

public class MealComponentsFragment extends Fragment {

    private MealComponentsViewModel mealComponentsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mealComponentsViewModel =
                ViewModelProviders.of(this).get(MealComponentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_meal_components, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        mealComponentsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}