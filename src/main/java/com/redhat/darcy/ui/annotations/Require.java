/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-ui.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.darcy.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In it's most common usage, indicates a {@link com.redhat.darcy.ui.api.elements.Findable},
 * {@link com.redhat.darcy.ui.api.elements.Element}, {@link com.redhat.darcy.ui.api.View}, or
 * {@link java.util.List Lists} of those types, is required as a part of a View's load condition
 * -- that is, the page will not be considered loaded until all required fields satisfy their load
 * condition. Each field type satisfies load conditions differently. For more details, see
 * {@link com.redhat.darcy.ui.matchers.LoadConditionMatcher}.
 * <p>
 * It is best to annotate as many elements as possible or write a specific load condition via
 * overriding {@link com.redhat.darcy.ui.api.View#isLoaded()} to ensure that every View's load
 * condition is unique.
 * <p>
 * For {@link com.redhat.darcy.ui.api.ViewElement ViewElements}, these annotations also communicate
 * how to calculate if such an element is "displayed" or "present."
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Require {
    int DEFAULT_AT_LEAST = 1;
    int DEFAULT_AT_MOST = Integer.MAX_VALUE;
    int DEFAULT_EXACTLY = -1;

    /**
     * Defaults to 1. Ignored for fields that are not {@link java.util.List Lists}. For list fields,
     * when evaluating conditions against required lists, the amount of elements in the list that
     * satisfy the condition must be at least this value.
     */
    int atLeast() default DEFAULT_AT_LEAST;

    /**
     * Defaults to {@link java.lang.Integer#MAX_VALUE}. Ignored for fields that are not
     * {@link java.util.List Lists}. For list fields, when evaluating conditions against required
     * lists, the amount of elements in the list that satisfy the condition must be at most this
     * value.
     */
    int atMost() default DEFAULT_AT_MOST;

    /**
     * Ignored by default and for fields that are not {@link java.util.List Lists}. If set to a
     * non-negative integer, {@link #atLeast} and {@link #atMost} are ignored, and the amount of
     * elements in the list that satisfy the condition must be exactly this value.
     */
    int exactly() default DEFAULT_EXACTLY;
}
