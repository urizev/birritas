package com.urizev.birritas.view.search;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
abstract class SearchViewState implements ViewState {
    public abstract ImmutableList<ViewState> items();

    public static SearchViewState create(ImmutableList<ViewState> items) {
        return new AutoValue_SearchViewState(items);
    }
}
