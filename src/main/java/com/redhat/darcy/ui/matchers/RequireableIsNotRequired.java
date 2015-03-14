package com.redhat.darcy.ui.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.redhat.darcy.ui.api.elements.Requireable;

public class RequireableIsNotRequired<T extends Requireable> extends TypeSafeMatcher<T> {

    @Override
    protected boolean matchesSafely(T requireable) {
        return !requireable.isRequired();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(" an element that is not required ");
    }
    
    @Override
    public void describeMismatchSafely(T requireable, Description mismatchDescription) {
        mismatchDescription.appendText(" isRequired was ").appendValue(requireable.isRequired());
    }

}
