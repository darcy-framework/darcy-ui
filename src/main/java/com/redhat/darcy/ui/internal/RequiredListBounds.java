package com.redhat.darcy.ui.internal;

import com.redhat.darcy.ui.annotations.DefaultRequire;
import com.redhat.darcy.ui.annotations.Require;

import java.lang.reflect.Field;
import java.util.Optional;

public class RequiredListBounds {
    private final int atLeast;
    private final int atMost;

    public RequiredListBounds(Field field) {
        Require annotation = Optional.ofNullable(field.getAnnotation(Require.class))
                .orElse(new DefaultRequire());

        if (annotation.exactly() >= 0) {
            atLeast = atMost = annotation.exactly();
        } else {
            atLeast = annotation.atLeast();
            atMost = annotation.atMost();
        }
    }

    public int atLeast() {
        return atLeast;
    }

    public int atMost() {
        return atMost;
    }
}
