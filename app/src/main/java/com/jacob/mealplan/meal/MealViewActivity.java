package com.jacob.mealplan.meal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONException;

public class MealViewActivity extends AppCompatActivity {
    RecyclerView componentView;
    int position;
    MealComponentsRecyclerViewAdapter adapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        position = intent.getIntExtra("Position", 1);
        setContentView(R.layout.activity_meal_view);
        componentView = findViewById(R.id.mealComponentsList);
        componentView.setLayoutManager(new LinearLayoutManager(this));
        try {
            adapter = new MealComponentsRecyclerViewAdapter(this, ItemStorage.getInstance().meals.valueAt(position).json.getJSONObject("Components"));
            componentView.setAdapter(adapter);

            // Add lines in between each row
            componentView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
