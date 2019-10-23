package com.jacob.mealplan.ui.mealcomponents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MakeMealComponentDialogFragment extends DialogFragment {
    private ItemPass components;
    private MealComponentRecyclerViewAdapter recycler;
    private int position;
    public MealComponentsFragment fragment;
    private EditText textName;
    private Switch switchQuantifiable;
    private NumberPicker intAmount;

    MakeMealComponentDialogFragment(ItemPass theComponents, int thePosition, MealComponentsFragment theFragment){
        components = theComponents;
        fragment = theFragment;
        position = thePosition;
        Log.i("TAGTEST", theComponents.json.toString());
    }

    public MakeMealComponentDialogFragment(MealComponentRecyclerViewAdapter theRecycler, MealComponentsFragment theFragment){
        recycler = theRecycler;
        fragment = theFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_new_component, null);

        // Inputs
        textName = dialogView.findViewById(R.id.textMealName);
        switchQuantifiable = dialogView.findViewById(R.id.SwitchQuantifiable);
        intAmount = dialogView.findViewById(R.id.PickerAmount);
        intAmount.setMaxValue(1000);
        intAmount.setMinValue(0);

        // Setup values if we've been passed an existing component
        if(components != null){
            try {
                textName.setText(components.json.getString("Name"));
                switchQuantifiable.setChecked(components.json.getBoolean("Quantifiable"));
                intAmount.setValue(components.json.getInt("Amount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Change the ability to use the number scroller if the quantifiable switch changes
        dialogView.findViewById(R.id.SwitchQuantifiable).setOnClickListener(new Switch.OnClickListener() {
            @Override
            public void onClick(View view) {
                intAmount.setEnabled(((Switch) view).isChecked());
            }
        });

        builder.setView(dialogView)
                .setPositiveButton(((components != null) ? R.string.set : R.string.add), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Get Values
                        String name = (textName.getText().toString());
                        boolean quantifiable = switchQuantifiable.isChecked();
                        int amount = intAmount.getValue();
                        // Put the values into a json object
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Name", name);
                            jsonObject.put("Quantifiable", quantifiable);
                            jsonObject.put("Amount", amount);

                            // Modify the component if we were passed one, otherwise create a new one
                            if(components != null){
                                ItemStorage.getInstance().modifyComponentByPosition(new ItemPass(components.file, jsonObject), position);
                            } else {
                                ItemStorage.getInstance().addNewComponent(jsonObject, getContext());
                            }

                            // Tell the fragment to update the list
                            fragment.updateComponents();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
