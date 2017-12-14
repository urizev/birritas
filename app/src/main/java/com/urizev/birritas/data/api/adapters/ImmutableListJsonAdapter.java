package com.urizev.birritas.data.api.adapters;

import com.google.common.collect.ImmutableList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImmutableListJsonAdapter<T> extends JsonAdapter<ImmutableList<T>> {
    public static final Factory FACTORY = (type, annotations, moshi) -> {
        Class<?> rawType = Types.getRawType(type);
        if (!annotations.isEmpty()) return null;
        if (rawType != ImmutableList.class) {
            return null;
        }
        Type elementType = Types.collectionElementType(type, List.class);
        JsonAdapter<?> elementAdapter = moshi.adapter(elementType);
        return new ImmutableListJsonAdapter<>(elementAdapter);
    };
    private final JsonAdapter<T> elementAdapter;
    private ImmutableListJsonAdapter(JsonAdapter<T> elementAdapter) {
        this.elementAdapter = elementAdapter;
    }
    @Override public ImmutableList<T> fromJson(JsonReader reader)
            throws IOException {
        List<T> result = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            result.add(elementAdapter.fromJson(reader));
        }
        reader.endArray();
        return ImmutableList.copyOf(result);
    }
    @Override public void toJson(JsonWriter writer, ImmutableList<T> value) throws IOException {
        writer.beginArray();
        for (T element : value) {
            elementAdapter.toJson(writer, element);
        }
        writer.endArray();
    }
}
