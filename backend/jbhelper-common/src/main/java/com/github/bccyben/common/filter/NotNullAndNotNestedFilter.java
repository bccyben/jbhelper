package com.github.bccyben.common.filter;

import java.util.List;

public class NotNullAndNotNestedFilter {
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return true;
        }
        if (!(obj instanceof String)
                && !(obj instanceof Integer)
                && !(obj instanceof Double)
                && !(obj instanceof List)) {
            return true;
        }
        return false;
    }
}
