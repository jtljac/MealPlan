package com.jacob.mealplan.ui.meals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MakeMealDialogFragment extends DialogFragment {
    private ItemPass components;
    private MealRecyclerViewAdapter recycler;
    private MakeMealUsedComponentRecyclerViewAdapter componentAdapter;
    private MakeMealStepRecyclerViewAdapter stepAdapter;
    private int position;
    public MealsFragment fragment;
    private EditText textName;
    private EditText textDescription;

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
        final Button buttonAdder = dialogView.findViewById(R.id.stepAddButton);
        final Button buttonRemover = dialogView.findViewById(R.id.stepRemoveButton);
        textName = dialogView.findViewById(R.id.textMealName);
        textDescription = dialogView.findViewById(R.id.textMealDescription);

        // set up the RecyclerView
        RecyclerView componentRecyclerView = dialogView.findViewById(R.id.componentsUsedRecyclerView);
        componentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        componentAdapter = new MakeMealUsedComponentRecyclerViewAdapter(getContext());
        componentRecyclerView.setAdapter(componentAdapter);
        componentRecyclerView.setItemViewCacheSize(componentAdapter.getItemCount());

        // Add lines in between each row
        componentRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        RecyclerView stepRecyclerView = dialogView.findViewById(R.id.stepsRecyclerView);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stepAdapter = new MakeMealStepRecyclerViewAdapter(getContext());
        stepRecyclerView.setAdapter(stepAdapter);

        stepRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        buttonAdder.setOnClickListener(new buttonAdd());
        buttonRemover.setOnClickListener(new buttonRemove());

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
                        String name = textName.getText().toString();
                        String description = textDescription.getText().toString();
                        try {
                            JSONObject json = new JSONObject();
                            JSONObject jsonComponents = new JSONObject();
                            JSONArray jsonSteps = new JSONArray();

                            json    .put("Name", name)
                                    .put("Description", description);
                            MakeMealUsedComponentRecyclerViewAdapter.ViewHolder componentAmount;
                            for (int i = 0; i < componentAdapter.getItemCount() - 1; i++) {
                                componentAmount = componentAdapter.getViewHolder(i);
                                if(componentAmount.picker.getValue() > 0){
                                    jsonComponents.put(ItemStorage.getInstance().components.valueAt(i).json.getString("ID"), componentAmount.picker.getValue());
                                }
                            }
                            Log.i("JSON", jsonComponents.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();
    }

    private class buttonAdd implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            stepAdapter.addDescriptor();
        }
    }

    private class buttonRemove implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            stepAdapter.removeDescriptor();
        }
    }
}
