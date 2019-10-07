package com.jacob.mealplan.ui.mealcomponents;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jacob.mealplan.R;

import java.io.File;

public class MealComponentsFragment extends Fragment {

    private MealComponentsViewModel mealComponentsViewModel;
    private File[] componentFiles;

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

        FloatingActionButton fab = root.findViewById(R.id.fabAddComponent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment mealMaker = new MakeMealComponentDialogFragment();
                mealMaker.show(getFragmentManager(), "TAG");
            }
        });

        componentFiles = root.getContext().getDir("MealComponents", Context.MODE_PRIVATE).listFiles();
        Log.i("MEALS", String.valueOf(componentFiles.length));

        if (componentFiles.length < 1){
            Toast.makeText(getContext(), R.string.noMeals, Toast.LENGTH_LONG)
                    .show();
        }

        return root;
    }
}