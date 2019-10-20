package com.jacob.mealplan.ui.mealcomponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jacob.mealplan.R;

public class PartialSwipeToDeleteEditCallback extends ItemTouchHelper.SimpleCallback {
    // Icons and backgrounds
    private MealComponentRecyclerViewAdapter mAdapter;
    private Drawable iconDelete;
    private final ColorDrawable backgroundDelete;

    private Drawable iconEdit;
    private final ColorDrawable backgroundEdit;

    private Drawable iconAdd;
    private Drawable iconRemove;
    private final ColorDrawable backgroundCount;

    // Variables
    private boolean swipeBack;

    // Buttons
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 300;

    public PartialSwipeToDeleteEditCallback(MealComponentRecyclerViewAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;

        iconDelete = ContextCompat.getDrawable(mAdapter.getContext(),
                R.drawable.ic_delete);
        backgroundDelete = new ColorDrawable(Color.RED);

        iconEdit = ContextCompat.getDrawable(mAdapter.getContext(),
                R.drawable.ic_edit);
        backgroundEdit = new ColorDrawable(Color.GREEN);

        iconAdd = ContextCompat.getDrawable(mAdapter.getContext(),
                R.drawable.ic_add);
        iconRemove = ContextCompat.getDrawable(mAdapter.getContext(),
                R.drawable.ic_remove);
        backgroundCount = new ColorDrawable(Color.GRAY);
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        Log.i("TESTEST", String.valueOf(buttonShowedState));
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void setTouchListener(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if (dX > buttonWidth) buttonShowedState = ButtonsState.LEFT_VISIBLE;

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

        private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                    return false;
                }
            });
        }

        private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        PartialSwipeToDeleteEditCallback.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                        recyclerView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });
                        setItemsClickable(recyclerView, true);
                        swipeBack = false;
                        buttonShowedState = ButtonsState.GONE;
                    }
                    return false;
                }
            });
        }

        private void setItemsClickable(RecyclerView recyclerView,
        boolean isClickable) {
            for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                recyclerView.getChildAt(i).setClickable(isClickable);
            }
        }
}

enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}