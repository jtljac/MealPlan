package com.jacob.mealplan.ui.meals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.R;
import com.jacob.mealplan.ui.mealcomponents.MealComponentRecyclerViewAdapter;
import com.jacob.mealplan.ui.mealcomponents.MealComponentsFragment;

import org.json.JSONException;

public class MakeMealDialogFragment extends DialogFragment {
    private ItemPass components;
    private MealRecyclerViewAdapter recycler;
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
        final View dialogView = inflater.inflate(R.layout.dialog_newmeal, null);

        // Setup values if we've been passed an existing component
        if(components != null){
            try {
                textName.setText(components.json.getString("Name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return builder.create();
    }
}
