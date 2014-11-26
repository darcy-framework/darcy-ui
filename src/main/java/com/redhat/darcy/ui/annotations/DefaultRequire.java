package com.redhat.darcy.ui.annotations;

import java.lang.annotation.Annotation;

public class DefaultRequire implements Require {
    @Override
    public int atLeast() {
        return Require.DEFAULT_AT_LEAST;
    }

    @Override
    public int atMost() {
        return Require.DEFAULT_AT_MOST;
    }

    @Override
    public int exactly() {
        return Require.DEFAULT_EXACTLY;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Require.class;
    }
}
