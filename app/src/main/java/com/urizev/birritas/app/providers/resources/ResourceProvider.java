package com.urizev.birritas.app.providers.resources;

public interface ResourceProvider {
    String getString(int res, Object ... args);
    int getColor(int res);
}
