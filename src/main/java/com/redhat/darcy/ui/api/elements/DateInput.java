package com.redhat.darcy.ui.api.elements;

import java.time.LocalDate;

public interface DateInput extends Element {
    void setDate(LocalDate date);
    LocalDate getDate();
}
