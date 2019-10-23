package com.jacob.mealplan.ui.meals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.jacob.mealplan.HorizontalNumberPicker;
import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MakeMealUsedComponentRecyclerViewAdapter extends RecyclerView.Adapter<MakeMealUsedComponentRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<ViewHolder> viewHolders;
    private JSONObject components;

    // data is passed into the constructor
    public MakeMealUsedComponentRecyclerViewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        viewHolders = new ArrayList<>();
    }

    // data is passed into the constructor
    public MakeMealUsedComponentRecyclerViewAdapter(Context context, JSONObject theComponents) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        viewHolders = new ArrayList<>();
        components = theComponents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.makemealcomponentrecyclerview_row, parent, false);

        return new MakeMealUsedComponentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakeMealUsedComponentRecyclerViewAdapter.ViewHolder holder, int position) {
        String item = null;
        try {
            item = ItemStorage.getInstance().components.valueAt(position).json.getString("Name");

            viewHolders.add(holder);
            holder.name.setText(item);
            if(components != null && components.has(item)) {
                holder.picker.setValue(components.getInt(item));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ItemStorage.getInstance().components.size();
    }

    // convenience method for getting data at click position
    public ItemPass getItem(int id) {
        return ItemStorage.getInstance().components.valueAt(id);
    }

    public ViewHolder getViewHolder(int id){
        return viewHolders.get(id);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        HorizontalNumberPicker picker;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.componentName);
            picker = itemView.findViewById(R.id.quantitySelector);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
