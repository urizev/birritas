package com.urizev.birritas.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.urizev.birritas.R;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapMessageView extends CardView {
    @BindView(R.id.map_message_progress) ProgressBar progressBar;
    @BindView(R.id.map_message_text) TextView textView;
    @BindView(R.id.map_message_button) Button button;
    private WeakReference<MapMessageViewListener> listener;

    public MapMessageView(Context context) {
        this(context, null, 0);
    }

    public MapMessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.listener = new WeakReference<>(null);
        View.inflate(context, R.layout.view_map_message, this);
        ButterKnife.bind(this);
    }

    public void setLoading () {
        this.progressBar.setVisibility(View.VISIBLE);
        this.textView.setText(R.string.loading_);
        this.button.setVisibility(GONE);
    }

    public void setError(String error) {
        this.progressBar.setVisibility(GONE);
        this.textView.setText(error);
        this.button.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.map_message_button)
    public void onRetryClicked() {
        MapMessageViewListener l = this.listener.get();
        if (l != null) {
            l.onRetryClicked(this);
        }
    }

    public void setListener(MapMessageViewListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    public interface MapMessageViewListener {
        void onRetryClicked(MapMessageView view);
    }
}
