package com.jacob.mealplan.ui.mealcomponents;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MealComponentRecyclerViewAdapter extends RecyclerView.Adapter<MealComponentRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity mActivity;
    public MealComponentsFragment fragment;

    private ItemPass mRecentlyDeletedItem;
    private int mRecentlyDeletedItemKey;

    public MealComponentRecyclerViewAdapter(Context context, Activity activity, MealComponentsFragment theFragment) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mActivity = activity;
        fragment = theFragment;
    }

    public Context getContext() {
        return context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.component_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = null;
        String amount = null;
        boolean quantifiable = false;
        try {
            item = ItemStorage.getInstance().components.valueAt(position).json.getString("Name");
            amount = ItemStorage.getInstance().components.valueAt(position).json.getString("Amount");
            quantifiable = ItemStorage.getInstance().components.valueAt(position).json.getBoolean("Quantifiable");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.nameText.setText(item);
        if (quantifiable) holder.amountText.setText(amount);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return ItemStorage.getInstance().components.size();
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = getItem(position);
        mRecentlyDeletedItemKey = ItemStorage.getInstance().components.keyAt(position);
        try {
            Log.i("Testes", mRecentlyDeletedItem.json.getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ItemStorage.getInstance().components.valueAt(position).file.delete();
        ItemStorage.getInstance().components.removeAt(position);
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



    private void undoDelete() {
        if(!mRecentlyDeletedItem.file.exists()) {
            ItemStorage.getInstance().components.append(mRecentlyDeletedItemKey,
                    mRecentlyDeletedItem);

            BufferedWriter writer = null;
            try {
                mRecentlyDeletedItem.file.createNewFile();
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mRecentlyDeletedItem.file)));
                writer.write(mRecentlyDeletedItem.json.toString());
                writer.flush();
                writer.close();
                notifyItemInserted(ItemStorage.getInstance().components.indexOfKey(mRecentlyDeletedItemKey));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Error, File of that name already exists", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateItems() {
        this.notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameText;
        TextView amountText;

        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.componentName);
            amountText = itemView.findViewById(R.id.componentQuantity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ItemPass getItem(int id) {
        return ItemStorage.getInstance().components.valueAt(id);
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