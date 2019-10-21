package com.jacob.mealplan.ui.mealcomponents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MealComponentsFragment extends Fragment implements MealComponentRecyclerViewAdapter.ItemClickListener{
    private MealComponentsViewModel mealComponentsViewModel;
    MealComponentRecyclerViewAdapter adapter;
    private MealComponentsFragment thisFragment = this;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Do view model stuff
        mealComponentsViewModel =
                ViewModelProviders.of(this).get(MealComponentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_meal_components, container, false);
        mealComponentsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        // Setup the action button
        FloatingActionButton fab = root.findViewById(R.id.fabAddComponent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment mealMaker = new MakeMealComponentDialogFragment(adapter, thisFragment);
                mealMaker.show(getFragmentManager(), "TAG");
            }
        });

        // Tell the user if no components exist
        if (ItemStorage.getInstance().components.size() < 1) {
            Toast.makeText(getContext(), R.string.noComponents, Toast.LENGTH_LONG)
                    .show();
        }

        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.componentsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MealComponentRecyclerViewAdapter(getContext(), this.getActivity(), this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // Add lines in between each row
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Add the touch helper to enable swiping
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteEditCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return root;
    }

    public void editComponent(int position){
        /*
         * Callback for creating component modifying dialog
         */
        DialogFragment mealMaker = new MakeMealComponentDialogFragment(adapter.getItem(position), position, this);
        mealMaker.show(getFragmentManager(), "TAG");
    }

    public void updateComponents(){
        /*
         * Tell the RecyclerView adapter to update its contents
         */
        adapter.updateItems();
    }

    @Override
    public void onItemClick(View view, int position) {
        /*
         * Callback for clicking on an item
         */
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}

