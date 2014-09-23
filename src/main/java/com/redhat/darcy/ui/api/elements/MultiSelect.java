package com.redhat.darcy.ui.api.elements;

import com.redhat.darcy.ui.api.Locator;

import java.util.List;
import java.util.Optional;

public interface MultiSelect extends Disableable, Element {
    void select(Locator locator);
    List<SelectOption> getOptions();
    List<Optional<SelectOption>> getCurrentlySelectedOptions();
}
