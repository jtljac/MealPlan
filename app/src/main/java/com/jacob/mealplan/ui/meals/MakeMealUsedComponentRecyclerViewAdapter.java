package com.jacob.mealplan.ui.meals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.jacob.mealplan.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MakeMealUsedComponentRecyclerViewAdapter extends RecyclerView.Adapter<MakeMealUsedComponentRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private MealRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public MakeMealUsedComponentRecyclerViewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.makemealcomponentrecyclerview_row, parent, false);
        return new MakeMealUsedComponentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakeMealUsedComponentRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        Button pickerAdd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.componentName);
            picker = itemView.findViewById(R.id.quantitySelector);
        }

        @Override
        public void onClick(View view) {

        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}
