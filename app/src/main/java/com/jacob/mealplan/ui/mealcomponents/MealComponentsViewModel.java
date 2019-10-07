package com.jacob.mealplan.ui.mealcomponents;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealComponentsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MealComponentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}