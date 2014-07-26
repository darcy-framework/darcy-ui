/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy.

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

package com.redhat.darcy.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.api.HasElementContext;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.InheritsContext;
import com.redhat.darcy.ui.testing.doubles.AlwaysMetCondition;
import com.redhat.darcy.ui.testing.doubles.DummyContext;
import com.redhat.darcy.ui.testing.doubles.NullContext;
import com.redhat.synq.Condition;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class AbstractViewSetContextTest {
    @Test(expected = MissingLoadConditionException.class)
    public void shouldThrowMissingLoadConditionExceptionIfNoLoadConditionIsPresent() {
        View testView = new AbstractView() {};

        testView.setContext(new NullContext());
    }

    @Test
    public void shouldSetContextOnElementFieldsThatImplementLazyElement() {
        DummyContext dummyContext = new DummyContext();

        class TestView extends AbstractView {
            @Require
            Element mockElement = mock(ElementThatIsLazy.class);
        };

        TestView testView = new TestView();
        testView.setContext(dummyContext);

        verify((HasElementContext) testView.mockElement).setContext(dummyContext);
    }

    @Ignore("issue #13 prevents this test from passing: " +
            "https://github.com/darcy-framework/darcy-ui/issues/13")
    @Test
    public void shouldSetContextOnListFieldsThatImplementLazyElement() {
        DummyContext dummyContext = new DummyContext();

        class TestView extends AbstractView {
            @Require
            List<Element> mockElementList = mock(ElementListThatIsLazy.class);
        };

        TestView testView = new TestView();
        testView.setContext(dummyContext);

        verify((HasElementContext) testView.mockElementList).setContext(dummyContext);
    }

    @Test
    public void shouldAssignAndCastViewContextToFieldsAnnotatedWithContext() {
        class SpecificContext extends DummyContext {

        }

        class TestView extends AbstractView {
            @com.redhat.darcy.ui.annotations.Context
            private SpecificContext castedContext;

            // We need a load condition for this view to be valid
            @Override
            protected Condition<?> loadCondition() {
                return new AlwaysMetCondition<>();
            }
        }

        TestView testView = new TestView();
        SpecificContext specificContext = new SpecificContext();

        testView.setContext(specificContext);

        assertEquals(testView.getContext(), testView.castedContext);
    }

    @Test(expected = ClassCastException.class)
    public void shouldThrowExceptionIfAttemptToAssignContextToAnUnimplementedType() {
        class SpecificContext extends DummyContext {

        }

        class TestView extends AbstractView {
            @com.redhat.darcy.ui.annotations.Context
            private SpecificContext castedContext;

            // We need a load condition for this view to be valid
            @Override
            protected Condition<?> loadCondition() {
                return new AlwaysMetCondition<>();
            }
        }

        TestView testView = new TestView();

        testView.setContext(new DummyContext());
    }

    @Test
    public void shouldCallOnSetContext() {
        AbstractView testView = spy(new AbstractView() {
            // We need a load condition for this view to be valid
            @Override
            protected Condition<?> loadCondition() {
                return new AlwaysMetCondition<>();
            }
        });

        testView.setContext(new NullContext());

        verify(testView).onSetContext();
    }

    interface ElementThatIsLazy extends Element, InheritsContext {}
    interface ElementListThatIsLazy extends List<Element>, InheritsContext {}
}
