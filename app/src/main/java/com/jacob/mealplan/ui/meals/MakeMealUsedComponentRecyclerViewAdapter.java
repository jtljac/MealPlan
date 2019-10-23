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

import java.util.ArrayList;

public class MakeMealUsedComponentRecyclerViewAdapter extends RecyclerView.Adapter<MakeMealUsedComponentRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<ViewHolder> viewHolders;
    private MealRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public MakeMealUsedComponentRecyclerViewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        viewHolders = new ArrayList<>();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.name.setText(item);
        viewHolders.add(holder);
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
            picker = itemView.findViewById(R.id.quantityPicker);
        }

        @Override
        public void onClick(View view) {

        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

    // allows clicks events to be caught
    public void setClickListener(MealRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
