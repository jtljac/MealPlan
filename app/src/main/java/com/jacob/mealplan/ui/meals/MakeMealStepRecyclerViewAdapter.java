package com.jacob.mealplan.ui.meals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jacob.mealplan.HorizontalNumberPicker;
import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MakeMealStepRecyclerViewAdapter extends RecyclerView.Adapter<MakeMealStepRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<ViewHolder> viewHolders;
    private int amount;
    private JSONArray steps;

    // data is passed into the constructor
    public MakeMealStepRecyclerViewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        viewHolders = new ArrayList<>();
        amount = 1;
    }

    // data is passed into the constructor
    public MakeMealStepRecyclerViewAdapter(Context context, JSONArray theSteps) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        viewHolders = new ArrayList<>();
        amount = 1;
        steps = theSteps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.meal_step_recyclerview_row, parent, false);
        return new MakeMealStepRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakeMealStepRecyclerViewAdapter.ViewHolder holder, int position) {
        viewHolders.add(holder);
        holder.name.setText(context.getString(R.string.stepFormatted, (position + 1)));
    }

    @Override
    public int getItemCount() {
        return amount;
    }

    public void addDescriptor(){
        amount++;
        notifyItemInserted(amount-1);
    }

    public void addDescriptor(String text){
        amount++;
        notifyItemInserted(amount-1);
        getViewHolder(amount-1).step.setText(text);
    }

    public void removeDescriptor(){
        if(amount > 1) {
            amount--;
            notifyItemRemoved(amount);
        } else {
            Toast.makeText(context, R.string.tooFewSteps, Toast.LENGTH_SHORT).show();
        }
    }

    public ViewHolder getViewHolder(int id){
        return viewHolders.get(id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView step;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.titleText);
            step = itemView.findViewById(R.id.stepText);
        }
    }
}
