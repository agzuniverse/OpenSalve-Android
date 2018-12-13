package com.agzuniverse.agz.opensalve.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.agzuniverse.agz.opensalve.R;

public class CustomButton extends LinearLayout {
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_button, this);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickHandler();
            }
        });
    }

    private Runnable func;

    public void setButtonText(String s) {
        Button btn = findViewById(R.id.btn);
        btn.setText(s);
    }

    public void setOnClick(Runnable r) {
        this.func = r;
    }

    public void onClickHandler() {
        func.run();
    }
}
