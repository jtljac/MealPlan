package com.jacob.mealplan.ui.mealcomponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.jacob.mealplan.R;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private MealComponentRecyclerViewAdapter mAdapter;
    private Drawable iconDelete;
    private final ColorDrawable backgroundDelete;
    private Drawable iconEdit;
    private final ColorDrawable backgroundEdit;


    public SwipeToDeleteCallback(MealComponentRecyclerViewAdapter adapter) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        iconDelete = ContextCompat.getDrawable(mAdapter.getContext(),
                R.drawable.ic_delete);
        backgroundDelete = new ColorDrawable(Color.RED);

        iconEdit = ContextCompat.getDrawable(mAdapter.getContext(),
                R.drawable.ic_edit);
        backgroundEdit = new ColorDrawable(Color.GREEN);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // used for up and down movements
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Log.i("TAGT", String.valueOf(direction));
        if(direction == 4) mAdapter.deleteItem(position);
        else if(direction == 8) {
            mAdapter.fragment.editComponent(position);
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView

        // Edit
        if (dX > 0) { // Swiping to the right
            int iconMargin = (itemView.getHeight() - iconEdit.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - iconEdit.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + iconEdit.getIntrinsicHeight();

            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + iconEdit.getIntrinsicWidth();
            iconEdit.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            backgroundEdit.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
            backgroundEdit.draw(c);
            iconEdit.draw(c);
        } else if (dX < 0) { // Swiping to the left
            int iconMargin = (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + iconDelete.getIntrinsicHeight();

            int iconLeft = itemView.getRight() - iconMargin - iconDelete.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            backgroundDelete.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            backgroundDelete.draw(c);
            iconDelete.draw(c);
        } else { // view is unSwiped
            backgroundEdit.setBounds(0, 0, 0, 0);
            backgroundDelete.setBounds(0, 0, 0, 0);
        }
    }
}
