package com.jacob.mealplan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ConfirmDeleteAllDialogFragment extends DialogFragment {
    boolean meal;
    boolean onMeal;
    MainActivity activity;
    ConfirmDeleteAllDialogFragment(boolean theMeal, boolean theOnMeal, MainActivity theActivity){
        meal = theMeal;
        activity = theActivity;
        onMeal = theOnMeal;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder .setTitle(getString(R.string.deleteAll, (meal?getString(R.string.meals):getString(R.string.components))))
                .setMessage(getString(R.string.areYouSure, (meal?getString(R.string.meals):getString(R.string.components))))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (meal){
                            ItemStorage.getInstance().deleteAllMeals();
                        } else {
                            ItemStorage.getInstance().deleteAllComponents();
                        }
                        activity.rebootActivity(onMeal);
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
