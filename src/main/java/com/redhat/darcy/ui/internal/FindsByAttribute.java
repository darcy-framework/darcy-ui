package com.redhat.darcy.ui.internal;

import java.util.List;

public interface FindsByAttribute {
    <T> List<T> findAllByAttribute(Class<T> type, String attribute, String value);
    <T> T findByAttribute(Class<T> type, String attribute, String value);
}
