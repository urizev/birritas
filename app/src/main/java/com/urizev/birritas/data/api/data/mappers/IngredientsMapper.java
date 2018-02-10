package com.urizev.birritas.data.api.data.mappers;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.data.api.data.HopData;
import com.urizev.birritas.data.api.data.IngredientsData;
import com.urizev.birritas.data.api.data.MaltData;
import com.urizev.birritas.data.api.data.YeastData;
import com.urizev.birritas.domain.entities.Hop;
import com.urizev.birritas.domain.entities.Ingredients;
import com.urizev.birritas.domain.entities.Malt;
import com.urizev.birritas.domain.entities.Yeast;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IngredientsMapper {
    private final HopMapper hopMapper;
    private final YeastMapper yeastMapper;
    private final MaltMapper maltMapper;

    @Inject
    IngredientsMapper(HopMapper hopMapper, YeastMapper yeastMapper, MaltMapper maltMapper) {
        this.hopMapper = hopMapper;
        this.yeastMapper = yeastMapper;
        this.maltMapper = maltMapper;
    }

    public Ingredients map(IngredientsData data) {
        ImmutableList.Builder<Hop> hops = new ImmutableList.Builder<>();
        ImmutableList.Builder<Yeast> yeasts = new ImmutableList.Builder<>();
        ImmutableList.Builder<Malt> malts = new ImmutableList.Builder<>();

        if (data != null) {
            ImmutableList<HopData> dataHops = data.hops();
            if (dataHops != null) {
                for (HopData hopData : dataHops) {
                    Hop hop = hopMapper.map(hopData);
                    if (hop != null) {
                        hops = hops.add(hop);
                    }
                }
            }

            ImmutableList<YeastData> dataYeasts = data.yeast();
            if (dataYeasts != null) {
                for (YeastData yeastData : dataYeasts) {
                    Yeast yeast = yeastMapper.map(yeastData);
                    if (yeast != null) {
                        yeasts = yeasts.add(yeast);
                    }
                }
            }

            ImmutableList<MaltData> dataMalts = data.malt();
            if (dataMalts != null) {
                for (MaltData maltData : dataMalts) {
                    Malt malt = maltMapper.map(maltData);
                    if (malt != null) {
                        malts = malts.add(malt);
                    }
                }
            }
        }

        return Ingredients.create(hops.build(), yeasts.build(), malts.build());
    }
}
