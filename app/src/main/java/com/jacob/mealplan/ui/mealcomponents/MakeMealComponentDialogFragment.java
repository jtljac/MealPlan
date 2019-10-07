package com.jacob.mealplan.ui.mealcomponents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class MakeMealComponentDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_newmeal, null);
        ((Switch) dialogView.findViewById(R.id.SwitchQuantifiable)).setOnClickListener(new Switch.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.findViewById(R.id.TextAmount).setEnabled(((Switch) view).isChecked());
            }
        });
        builder.setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = ((EditText) dialogView.findViewById(R.id.TextMealComponentName)).getText().toString();
                        boolean quantifiable = ((Switch) dialogView.findViewById(R.id.SwitchQuantifiable)).isChecked();
                        int amount = Integer.valueOf((((EditText) dialogView.findViewById(R.id.TextAmount)).getText().toString()));
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Name", name);
                            jsonObject.put("Quantifiable", quantifiable);
                            jsonObject.put("Amount", amount);
                            File dir = getContext().getDir("MealComponents", Context.MODE_PRIVATE);
                            File file = new File(dir, name);
                            int count = 0;
                            while(file.exists()){
                                count++;
                                file = new File(dir, name + count);
                            }
                            file.createNewFile();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                            writer.write(jsonObject.toString());
                            writer.flush();
                            writer.close();

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
