package com.jacob.mealplan.ui.meals;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONException;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MealRecyclerViewAdapter extends RecyclerView.Adapter<MealRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private MealRecyclerViewAdapter.ItemClickListener mClickListener;
    private Activity mActivity;
    public MealsFragment fragment;

    private ItemPass mRecentlyDeletedItem;
    private int mRecentlyDeletedItemKey;

    // data is passed into the constructor
    public MealRecyclerViewAdapter(Context context, Activity activity, MealsFragment theFragment) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mActivity = activity;
        fragment = theFragment;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mealrecyclerview_row, parent, false);
        return new MealRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = null;
        try {
            item = ItemStorage.getInstance().meals.valueAt(position).json.getString("Name");

            //TODO: Logic for if quantities aren't great enough
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.nameText.setText(item);
    }

    @Override
    public int getItemCount() {
        return ItemStorage.getInstance().meals.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameText;
        CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.mealName);
            card = itemView.findViewById(R.id.theCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ItemPass getItem(int id) {
        return ItemStorage.getInstance().meals.valueAt(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MealRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
