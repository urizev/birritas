package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Ingredients {
    public abstract ImmutableList<Hop> hops();
    public abstract ImmutableList<Yeast> yeasts();
    public abstract ImmutableList<Malt> malts();

    public static Ingredients create(ImmutableList<Hop> hops, ImmutableList<Yeast> yeasts, ImmutableList<Malt> malts) {
        return new AutoValue_Ingredients(hops, yeasts, malts);
    }
}
