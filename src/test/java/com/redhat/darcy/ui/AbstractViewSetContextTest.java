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
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.elements.Elements;
import com.redhat.darcy.ui.elements.Label;
import com.redhat.darcy.ui.testing.doubles.AlwaysMetCondition;
import com.redhat.synq.Condition;

import org.junit.Test;

public class AbstractViewSetContextTest {
    @Test(expected = MissingLoadConditionException.class)
    public void shouldThrowMissingLoadConditionExceptionIfNoLoadConditionIsPresent() {
        View testView = new AbstractView() {};

        testView.setContext(new NullContext());
    }

    @Test
    public void shouldFindElementFieldsWithSetContext() {
        DummyContext mockContext = mock(DummyContext.class);
        when(mockContext.findById(Label.class, "test")).thenReturn(mock(Label.class));

        class TestView extends AbstractView {
            @Require
            Label label = Elements.label(By.id("test"));
        };

        TestView testView = new TestView();

        testView.setContext(mockContext);
        testView.label.readText(); // Do something on the element to prompt it to be found

        verify(mockContext).findById(anyObject(), anyString());
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
}
