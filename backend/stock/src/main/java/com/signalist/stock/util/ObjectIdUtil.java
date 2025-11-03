package com.signalist.stock.util;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdUtil {
    public ObjectId fromStringSafe(String hex) {
        if (hex == null || hex.isBlank()) return null;
        return ObjectId.isValid(hex) ? new ObjectId(hex) : null;
    }
}
