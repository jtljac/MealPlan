package com.jacob.mealplan.ui.mealcomponents;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.jacob.mealplan.R;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class MealComponentRecyclerViewAdapter extends RecyclerView.Adapter<MealComponentRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<componentPass> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity mActivity;
    public MealComponentsFragment fragment;

    private componentPass mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    // data is passed into the constructor
    public MealComponentRecyclerViewAdapter(Context context, List<componentPass> data, Activity activity, MealComponentsFragment theFragment) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mActivity = activity;
        fragment = theFragment;
    }

    public Context getContext() {
        return context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = null;
        try {
            animal = mData.get(position).json.getString("Name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.myTextView.setText(animal);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if(mData != null) return mData.size();
        return 0;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = getItem(position);
        mRecentlyDeletedItemPosition = position;
        mData.get(position).file.delete();
        mData.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = mActivity.findViewById(R.id.Main);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }

    public void setItems(List<componentPass> newList){
        mData = newList;
        notifyDataSetChanged();
    }

    private void undoDelete() {
        if(!mRecentlyDeletedItem.file.exists()) {
            mData.add(mRecentlyDeletedItemPosition,
                    mRecentlyDeletedItem);

            BufferedWriter writer = null;
            try {
                mRecentlyDeletedItem.file.createNewFile();
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mRecentlyDeletedItem.file)));
                writer.write(mRecentlyDeletedItem.json.toString());
                writer.flush();
                writer.close();
                notifyItemInserted(mRecentlyDeletedItemPosition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Error, File of that name already exists", Toast.LENGTH_SHORT).show();
        }
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.componentName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public componentPass getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}