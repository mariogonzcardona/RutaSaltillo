package com.map.develop.rutasaltillov2.JSonParsers;

import com.map.develop.rutasaltillov2.SearchRoute.Route;

import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface RutasFinder {
    void onRutasFinderStart();
    void onRutasFinder(List<Rutas> route);
}
