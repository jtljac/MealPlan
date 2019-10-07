package com.jacob.mealplan.ui.meals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jacob.mealplan.R;

import java.io.File;

public class MealsFragment extends Fragment {

    private MealsViewModel mealsViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private File[] mealFiles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mealsViewModel =
                ViewModelProviders.of(this).get(MealsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_meals, container, false);
        mealsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        FloatingActionButton fab = root.findViewById(R.id.fabAddMeal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment mealMaker = new MakeMealComponentDialogFragment();
                mealMaker.show(getFragmentManager(), "TAG");
            }
        });

        mealFiles = root.getContext().getDir("Meals", Context.MODE_PRIVATE).listFiles();

        if (mealFiles.length < 1){
            Toast.makeText(getContext(), R.string.noMeals, Toast.LENGTH_LONG)
            .show();
        }

        return root;
    }
}

