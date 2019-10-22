package com.jacob.mealplan;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
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

    public HorizontalNumberPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.horizontal_number_picker, this);

        number = findViewById(R.id.numberValue);
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
            if(max != null && tempValue > max){
                value = max;
                updateText();
            } else if (min != null && tempValue < min){
                value = min;
                updateText();
            } else {
                value = tempValue;
            }
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
}
