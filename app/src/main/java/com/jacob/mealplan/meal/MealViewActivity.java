package com.jacob.mealplan.meal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MealViewActivity extends AppCompatActivity {
    int position;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);

        intent = getIntent();
        position = intent.getIntExtra("Position", 1);
        JSONObject json = ItemStorage.getInstance().meals.valueAt(position).json;

        TextView nameText = findViewById(R.id.mealName);
        TextView descriptionText = findViewById(R.id.mealDescription);

        RecyclerView componentView = findViewById(R.id.mealComponentsList);
        componentView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView stepView = findViewById(R.id.mealStepList);
        stepView.setLayoutManager(new LinearLayoutManager(this));

        try {

            nameText.setText(json.getString("Name"));
            descriptionText.setText(json.getString("Description"));

            MealComponentsRecyclerViewAdapter componentAdapter = new MealComponentsRecyclerViewAdapter(this, json.getJSONObject("Components"));
            componentView.setAdapter(componentAdapter);

            MealStepsRecyclerViewAdapter stepAdapter = new MealStepsRecyclerViewAdapter(this, json.getJSONArray("Steps"));
            stepView.setAdapter(stepAdapter);

            // Add lines in between each row
            componentView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            stepView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
