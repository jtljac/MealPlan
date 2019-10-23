package com.jacob.mealplan.ui.meals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.R;

import org.json.JSONException;

public class MakeMealDialogFragment extends DialogFragment {
    private ItemPass components;
    private MealRecyclerViewAdapter recycler;
    private MakeMealUsedComponentRecyclerViewAdapter adapter;
    private int position;
    public MealsFragment fragment;
    private EditText textName;

    MakeMealDialogFragment(ItemPass theComponents, int thePosition, MealsFragment theFragment){
        components = theComponents;
        fragment = theFragment;
        position = thePosition;
        Log.i("TAGTEST", theComponents.json.toString());
    }

    MakeMealDialogFragment(MealRecyclerViewAdapter theRecycler, MealsFragment theFragment){
        recycler = theRecycler;
        fragment = theFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_new_meal, null);

        // set up the RecyclerView
        RecyclerView recyclerView = dialogView.findViewById(R.id.componentsUsedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MakeMealUsedComponentRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // Add lines in between each row
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Setup values if we've been passed an existing component
        if(components != null){
            try {
                textName.setText(components.json.getString("Name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        builder.setView(dialogView)
                .setPositiveButton(((components != null) ? R.string.set : R.string.add), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();
    }
}
