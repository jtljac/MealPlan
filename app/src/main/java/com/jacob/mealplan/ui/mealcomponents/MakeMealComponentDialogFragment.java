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
    EditText textName;
    Switch switchQuantifiable;
    NumberPicker intAmount;
    MakeMealComponentDialogFragment(ItemPass theComponents, int thePosition, MealComponentsFragment theFragment){
        components = theComponents;
        fragment = theFragment;
        position = thePosition;
        Log.i("TAGTEST", theComponents.json.toString());
    }

    MakeMealComponentDialogFragment(MealComponentRecyclerViewAdapter theRecycler, MealComponentsFragment theFragment){
        recycler = theRecycler;
        fragment = theFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_newmeal, null);
        textName = dialogView.findViewById(R.id.TextMealComponentName);
        switchQuantifiable = dialogView.findViewById(R.id.SwitchQuantifiable);
        intAmount = dialogView.findViewById(R.id.PickerAmount);
        intAmount.setMaxValue(1000);
        intAmount.setMinValue(0);

        if(components != null){
            try {
                textName.setText(components.json.getString("Name"));
                switchQuantifiable.setChecked(components.json.getBoolean("Quantifiable"));
                intAmount.setValue(components.json.getInt("Amount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        dialogView.findViewById(R.id.SwitchQuantifiable).setOnClickListener(new Switch.OnClickListener() {
            @Override
            public void onClick(View view) {
                intAmount.setEnabled(((Switch) view).isChecked());
            }
        });
        builder.setView(dialogView)
                .setPositiveButton(((components != null) ? R.string.set : R.string.add), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = (textName.getText().toString());
                        boolean quantifiable = switchQuantifiable.isChecked();
                        int amount = intAmount.getValue();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Name", name);
                            jsonObject.put("Quantifiable", quantifiable);
                            jsonObject.put("Amount", amount);

                            if(components != null){
                                ItemStorage.getInstance().modifyComponentByPosition(new ItemPass(components.file, jsonObject), position);
                            } else {
                                ItemStorage.getInstance().addNewComponent(jsonObject, getContext());
                            }
                            fragment.updateComponents();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
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
