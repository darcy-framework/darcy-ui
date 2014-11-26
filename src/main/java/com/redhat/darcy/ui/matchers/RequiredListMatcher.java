package com.redhat.darcy.ui.matchers;

import com.redhat.darcy.util.Caching;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class RequiredListMatcher extends TypeSafeMatcher<List<?>> {
    private final int atLeast;
    private final int atMost;
    private final Matcher<?> matcher;

    public static RequiredListMatcher hasCorrectNumberOfItemsMatching(int atLeast,
                                                                      int atMost, Matcher<?> matcher) {
        return new RequiredListMatcher(atLeast, atMost, matcher);
    }

    public RequiredListMatcher(int atLeast, int atMost, Matcher<?> matcher) {
        this.atLeast = atLeast;
        this.atMost = atMost;
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(List<?> list) {
        long matchCount = fresh(list)
                .stream()
                .filter(matcher::matches)
                .count();

        return matchCount >= atLeast && matchCount <= atMost;
    }

    @Override
    public void describeTo(Description description) {
        boolean plural = atMost > 1;

        String element = plural ? "elements" : "element";
        String is = plural ? "are" : "is";

        description.appendText("a List with at least " + atLeast + " and at most " + atMost + " " +
                element + " that " + is);

        matcher.describeTo(description);
    }

    private List<?> fresh(List<?> list) {
        if (list instanceof Caching) {
            ((Caching) list).invalidateCache();
        }

        return list;
    }

}
