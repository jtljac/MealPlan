package com.jacob.mealplan.meal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealStepsRecyclerViewAdapter extends RecyclerView.Adapter<MealStepsRecyclerViewAdapter.ViewHolder> {
    JSONArray items;
    private LayoutInflater mInflater;
    private Context context;

    public MealStepsRecyclerViewAdapter(Context theContext, JSONArray theItems){
        Log.i("Test", theItems.toString());
        context = theContext;
        mInflater = LayoutInflater.from(context);
        items = theItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.meal_view_step_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.stepText.setText(context.getString(R.string.stepFormatted, (position + 1)));
            holder.descriptionText.setText(items.getString(position));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.length();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepText;
        TextView descriptionText;

        ViewHolder(View itemView) {
            super(itemView);
            stepText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
        }
    }
}
