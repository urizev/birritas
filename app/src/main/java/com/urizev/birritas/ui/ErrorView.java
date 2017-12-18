package com.urizev.birritas.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urizev.birritas.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public class ErrorView extends LinearLayout {
    @BindView(R.id.icon) ImageView mIcon;
    @BindView(R.id.message) TextView mMessage;

    public ErrorView(Context context) {
        this(context, null, 0);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View.inflate(context, R.layout.view_error, this);
        ButterKnife.bind(this, this);
    }

    public void setMessage(String message) {
        this.mMessage.setText(message);
    }
}
