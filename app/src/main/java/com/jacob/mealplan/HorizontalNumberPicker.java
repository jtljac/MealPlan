package com.jacob.mealplan;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class HorizontalNumberPicker extends LinearLayout {
    private EditText number;
    private int value = 0;
    private Integer max, min;
    private int step = 1;
    private boolean wrap = true;

    private static String STATE_SELECTED_NUMBER = "SelectedNumber";

    private static String STATE_SUPER_CLASS = "SuperClass";

    public HorizontalNumberPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public HorizontalNumberPicker(Context context){
        super(context);
        setup(context);
    }

    public HorizontalNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context){
        inflate(context, R.layout.horizontal_number_picker, this);

        number = findViewById(R.id.numberValue);
        number.setText(String.valueOf(value));
        number.addTextChangedListener(new changeHandler());

        final ImageButton minus = findViewById(R.id.minusButton);
        final ImageButton add = findViewById(R.id.addButton);

        minus.setOnClickListener(new MinusHandler());
        add.setOnClickListener(new AddHandler());
    }

    private void updateText(){
        number.setText(String.valueOf(value));
    }

    private class AddHandler implements OnClickListener {

        @Override
        public void onClick(View view) {
            int tempValue = value + step;

            if(max != null && tempValue > max){
                if(wrap && min != null) {
                    value = min + (tempValue - max);
                }
            } else {
                value = tempValue;
            }
            updateText();
        }
    }

    private class MinusHandler implements OnClickListener{

        @Override
        public void onClick(View view) {
            int tempValue = value - step;

            if(min != null && tempValue < min){
                if(wrap && max != null) {
                    value = max - (min - tempValue);
                }
            } else {
                value = tempValue;
            }
            updateText();
        }
    }

    private class changeHandler implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int tempValue = Integer.valueOf(editable.toString());
            if(tempValue != value) setValue(tempValue);
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int tempValue) {
        if(max != null && tempValue > max){
            value = max;
        } else if (min != null && tempValue < min){
            value = min;
        } else {
            value = tempValue;
        }
        updateText();
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean isWrap() {
        return wrap;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putParcelable(STATE_SUPER_CLASS,
                super.onSaveInstanceState());
        bundle.putInt(STATE_SELECTED_NUMBER, value);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle)state;

            super.onRestoreInstanceState(bundle
                    .getParcelable(STATE_SUPER_CLASS));
            setValue(bundle.getInt(STATE_SELECTED_NUMBER));
        }
        else
            super.onRestoreInstanceState(state);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        // Makes sure that the state of the child views in the side
        // spinner are not saved since we handle the state in the
        // onSaveInstanceState.
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        // Makes sure that the state of the child views in the side
        // spinner are not restored since we handle the state in the
        // onSaveInstanceState.
        super.dispatchThawSelfOnly(container);
    }
}
