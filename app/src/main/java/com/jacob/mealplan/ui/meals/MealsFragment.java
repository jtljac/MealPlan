package com.jacob.mealplan.ui.meals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;
import com.jacob.mealplan.ui.mealcomponents.MakeMealComponentDialogFragment;
import com.jacob.mealplan.ui.mealcomponents.MealComponentRecyclerViewAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MealsFragment extends Fragment implements MealRecyclerViewAdapter.ItemClickListener{

    private MealsViewModel mealsViewModel;
    MealRecyclerViewAdapter adapter;
    private MealsFragment thisFragment = this;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                DialogFragment mealMaker = new MakeMealDialogFragment(adapter, thisFragment);
                mealMaker.show(getFragmentManager(), "TAG");
            }
        });

        if (ItemStorage.getInstance().meals.size() < 1){
            Toast.makeText(getContext(), R.string.noMeals, Toast.LENGTH_LONG)
            .show();
        }

        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.componentsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MealRecyclerViewAdapter(getContext(), this.getActivity(), this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // Add lines in between each row
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}

