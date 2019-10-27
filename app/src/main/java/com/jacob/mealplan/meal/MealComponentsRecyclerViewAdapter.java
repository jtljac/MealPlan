package com.jacob.mealplan.meal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacob.mealplan.ItemPass;
import com.jacob.mealplan.ItemStorage;
import com.jacob.mealplan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MealComponentsRecyclerViewAdapter extends RecyclerView.Adapter<MealComponentsRecyclerViewAdapter.ViewHolder> {
    JSONObject items;
    ArrayList<String> keys = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public MealComponentsRecyclerViewAdapter(Context theContext, JSONObject theItems){
        items = theItems;
        Log.i("Test", theItems.toString());
        context = theContext;
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
        String key = keys.get(position);
        ItemPass item = ItemStorage.getInstance().components.get(Integer.valueOf(key));

        if(item != null) {
            try {
                holder.nameText.setText(item.json.getString("Name"));

                if (!items.optBoolean(key, false))
                    holder.amountText.setText(context.getString(R.string.unitsDisplay, String.valueOf(items.getInt(key)), item.json.optString("Units", "")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            holder.nameText.setText(context.getString(R.string.itemDeleted));
        }
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
