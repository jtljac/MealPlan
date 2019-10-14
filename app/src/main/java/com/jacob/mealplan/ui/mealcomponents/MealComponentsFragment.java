package com.jacob.mealplan.ui.mealcomponents;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jacob.mealplan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MealComponentsFragment extends Fragment implements MealComponentRecyclerViewAdapter.ItemClickListener{

    private MealComponentsViewModel mealComponentsViewModel;
    private File[] componentFiles;
    private ArrayList<componentPass> components;
    MealComponentRecyclerViewAdapter adapter;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        components = new ArrayList<>();
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
                DialogFragment mealMaker = new MakeMealComponentDialogFragment();
                mealMaker.show(getFragmentManager(), "TAG");
            }
        });

        componentFiles = root.getContext().getDir("MealComponents", Context.MODE_PRIVATE).listFiles();
        Log.i("MEALS", String.valueOf(componentFiles.length));

        if (componentFiles.length < 1){
            Toast.makeText(getContext(), R.string.noComponents, Toast.LENGTH_LONG)
                    .show();
        } else {
            for (File component:componentFiles) {
                try {
                    FileInputStream fin = new FileInputStream(component);
                    String ret = convertStreamToString(fin);
                    fin.close();
                    components.add(new componentPass(component, new JSONObject(ret)));
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.componentsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MealComponentRecyclerViewAdapter(getContext(), components);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        DialogFragment mealMaker = new MakeMealComponentDialogFragment(adapter.getItem(position));
        mealMaker.show(getFragmentManager(), "TAG");
    }
}

