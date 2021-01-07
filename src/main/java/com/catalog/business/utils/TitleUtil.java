package com.catalog.business.utils;

import com.catalog.model.Type;

import java.util.Map;

/**
 * Created by Igor on 11/28/2020.
 */
public class TitleUtil {

    public static Type getTypeForName(String name, Map<Long, Type> typeMap){
        Type tmp = null;
        for (Map.Entry<Long, Type> entry : typeMap.entrySet()) {
            if (entry.getValue().getName().toLowerCase().equals(name)) {
                tmp = entry.getValue();
                break;
            }
        }
        return tmp;
    }
}
