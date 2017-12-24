package com.urizev.birritas.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

        switch(getOrientation()) {
            case HORIZONTAL:
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                params.setMarginEnd((int) context.getResources().getDimension(R.dimen.view_param_view_internal_horizontal_margin));
                this.mParamName.setLayoutParams(params);
                this.mParamName.setGravity(Gravity.START);
                this.mParamValue.setGravity(Gravity.END);
                break;
            case VERTICAL:
                setGravity(Gravity.CENTER);
                break;
        }
    }

    public void setValueText(String valueText) {
        this.mParamValue.setText(valueText);
    }
}
