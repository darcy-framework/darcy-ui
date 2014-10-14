package com.redhat.darcy.ui.api.elements;

import java.time.LocalDate;

public interface DateSelect extends Element, HasValue {
    void setDate(LocalDate date);
    LocalDate getDate();
}
