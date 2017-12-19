package com.urizev.birritas.data.api.adapters;

import com.google.common.collect.ImmutableList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Types;
import com.urizev.birritas.data.api.data.BeerData;
import com.urizev.birritas.data.api.data.BreweryData;
import com.urizev.birritas.data.api.data.LiveData;

import java.io.IOException;
import java.util.Map;

public class LiveDataJsonAdapter extends JsonAdapter<LiveData> {
    public static final Factory FACTORY = (type, annotations, moshi) -> {
        Class<?> rawType = Types.getRawType(type);
        if (!annotations.isEmpty()) return null;
        if (rawType != LiveData.class) {
            return null;
        }

        return new LiveDataJsonAdapter(moshi.adapter(BeerData.class), moshi.adapter(BreweryData.class), moshi.adapter(Map.class));
    };

    private static final String KEY_TYPE = "type";

    private final JsonAdapter<BeerData> beerAdapter;
    private final JsonAdapter<BreweryData> breweryAdapter;
    private final JsonAdapter<Map> objectAdapter;

    private LiveDataJsonAdapter(JsonAdapter<BeerData> beerAdapter,
                                JsonAdapter<BreweryData> breweryAdapter,
                                JsonAdapter<Map> objectAdapter) {
        this.beerAdapter = beerAdapter;
        this.breweryAdapter = breweryAdapter;
        this.objectAdapter = objectAdapter;
    }

    @Override
    public LiveData fromJson(JsonReader reader) throws IOException {
        ImmutableList.Builder<BeerData> beers = new ImmutableList.Builder<>();
        ImmutableList.Builder<BreweryData> breweries = new ImmutableList.Builder<>();

        reader.beginArray();
        while (reader.hasNext()) {
            Map obj = objectAdapter.fromJson(reader);
            String type = (String) obj.get(KEY_TYPE);
            switch (type) {
                case LiveData.TYPE_BEER:
                    beers.add(beerAdapter.fromJsonValue(obj));
                    break;
                case LiveData.TYPE_BREWERY:
                    breweries.add(breweryAdapter.fromJsonValue(obj));
                    break;
            }
        }
        reader.endArray();

        return LiveData.create(beers.build(), breweries.build());
    }

    @Override public void toJson(JsonWriter writer, LiveData value) throws IOException {

    }
}
