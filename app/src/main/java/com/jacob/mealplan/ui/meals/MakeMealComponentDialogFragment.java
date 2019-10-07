package com.jacob.mealplan.ui.meals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.jacob.mealplan.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MakeMealComponentDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_newmeal, null);
        builder.setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = ((EditText) dialogView.findViewById(R.id.TextMealComponentName)).getText().toString();
                        boolean quantifiable = ((Switch) dialogView.findViewById(R.id.SwitchQuantifiable)).isChecked();
                        JSONObject jsonObject = new JSONObject();


                        try {
                            jsonObject.put("Name", name);
                            jsonObject.put("Quantifiable", quantifiable);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
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
