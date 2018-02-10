package com.urizev.birritas.view.common;

import com.google.common.collect.ImmutableList;
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

public class ViewStateAdapter extends ListDelegationAdapter<ImmutableList<? extends ViewState>> {
    public ViewStateAdapter(ImmutableList<ViewStateAdapterDelegate> delegates) {
        super();
        for (ViewStateAdapterDelegate delegate : delegates){
            //noinspection unchecked
            delegatesManager.addDelegate(delegate);
        }
    }
}
