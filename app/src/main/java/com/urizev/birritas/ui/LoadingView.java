package com.urizev.birritas.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.urizev.birritas.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingView extends LinearLayout {
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.message) TextView mMessage;

    public LoadingView(Context context) {
        this(context, null, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View.inflate(context, R.layout.view_loading, this);
        ButterKnife.bind(this, this);
    }
}
