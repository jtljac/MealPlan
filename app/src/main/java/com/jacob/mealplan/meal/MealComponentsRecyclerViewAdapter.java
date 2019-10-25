package com.jacob.mealplan.meal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MealComponentsRecyclerViewAdapter extends RecyclerView.Adapter<MealComponentsRecyclerViewAdapter.ViewHolder> {
    JSONObject items;
    ArrayList<String> keys = new ArrayList<>();
    private LayoutInflater mInflater;

    public MealComponentsRecyclerViewAdapter(Context context, JSONObject theItems){
        items = theItems;
        Log.i("Test", theItems.toString());
        mInflater = LayoutInflater.from(context);
        items.keys().forEachRemaining(keys::add);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.component_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = null;
        String amount = null;
        boolean quantifiable = false;
        try {
            String key = keys.get(position);
            Log.i("Test", key);
            item = ItemStorage.getInstance().components.get(Integer.valueOf(key)).json.getString("Name");
            if(items.optBoolean(key, false)) quantifiable = items.getBoolean(key);
            else amount = String.valueOf(items.getInt(key));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.nameText.setText(item);
        if (quantifiable) holder.amountText.setText(amount);
    }

    @Override
    public int getItemCount() {
        return items.length();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView amountText;

        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.componentName);
            amountText = itemView.findViewById(R.id.componentQuantity);
        }
    }
}
