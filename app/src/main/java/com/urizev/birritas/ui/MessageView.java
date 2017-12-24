package com.urizev.birritas.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
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

public class MessageView extends LinearLayout {
    @BindView(R.id.icon) ImageView mIcon;
    @BindView(R.id.message) TextView mMessage;

    public MessageView(Context context) {
        this(context, null, 0);
    }

    public MessageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View.inflate(context, R.layout.view_message, this);
        ButterKnife.bind(this, this);
    }

    public void setMessage(String message) {
        this.mMessage.setText(message);
    }

    public void setIcon(@DrawableRes int resId) {
        this.mIcon.setImageResource(resId);
    }
}