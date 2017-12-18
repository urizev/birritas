package com.urizev.birritas.app.base;

import android.support.v4.app.Fragment;

import com.urizev.birritas.app.App;


public class BaseFragment extends Fragment {
    private App mApp;

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected App getApp() {
        if (mApp == null) {
            BaseActivity activity = getBaseActivity();
            if (activity == null) {
                return null;
            }
            mApp = activity.getApp();
        }
        return mApp;
    }
}
