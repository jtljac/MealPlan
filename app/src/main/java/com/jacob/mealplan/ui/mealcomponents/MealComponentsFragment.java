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
    MealComponentsFragment thisFragment = this;


    public static String convertStreamToString(InputStream is) throws IOException {
        // http://www.java2s.com/Code/Java/File-Input-Output/ConvertInputStreamtoString.htm
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        Boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if(firstLine){
                sb.append(line);
                firstLine = false;
            } else {
                sb.append("\n").append(line);
            }
        }
        reader.close();
        return sb.toString();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mealComponentsViewModel =
                ViewModelProviders.of(this).get(MealComponentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_meal_components, container, false);
        mealComponentsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fabAddComponent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment mealMaker = new MakeMealComponentDialogFragment(adapter, thisFragment);
                mealMaker.show(getFragmentManager(), "TAG");
            }
        });

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

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteEditCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return root;
    }

    public void editComponent(int position){
        DialogFragment mealMaker = new MakeMealComponentDialogFragment(adapter.getItem(position), position, this);
        mealMaker.show(getFragmentManager(), "TAG");
    }

    public void updateComponents(){
        adapter.updateItems();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}

