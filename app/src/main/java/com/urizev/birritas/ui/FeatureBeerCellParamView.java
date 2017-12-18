package com.urizev.birritas.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urizev.birritas.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeatureBeerCellParamView extends LinearLayout {
    @BindView(R.id.param_name) TextView mParamName;
    @BindView(R.id.param_value) TextView mParamValue;

    public FeatureBeerCellParamView(Context context) {
        this(context, null, 0);
    }

    public FeatureBeerCellParamView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeatureBeerCellParamView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View.inflate(context, R.layout.view_featured_cell_value, this);
        ButterKnife.bind(this, this);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.FeatureBeerCellParamView,
                    0, 0);

            try {
                mParamName.setText(a.getString(R.styleable.FeatureBeerCellParamView_nameText));
                mParamValue.setText(a.getString(R.styleable.FeatureBeerCellParamView_valueText));
            } finally {
                a.recycle();
            }
        }
    }

    public void setValueText(String valueText) {
        this.mParamValue.setText(valueText);
    }
}
