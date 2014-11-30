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

package com.redhat.darcy.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.HasElementContext;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.testing.doubles.AlwaysMetCondition;
import com.redhat.darcy.ui.testing.doubles.DummyContext;
import com.redhat.darcy.ui.testing.doubles.NullContext;
import com.redhat.synq.Condition;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class AbstractViewSetContextTest {
    @Test
    public void shouldSetContext() {
        ElementContext mockContext = mock(ElementContext.class);
        View testView = new AbstractView() {};

        testView.setContext(mockContext);

        assertSame(mockContext, testView.getContext());
    }

    @Test
    public void shouldSetContextOnFieldsWhosDeclaredTypeImplementsHasElementContext() {
        DummyContext dummyContext = new DummyContext();

        class TestView extends AbstractView {
            HasElementContext mockHasElementContext = mock(HasElementContext.class);
        };

        TestView testView = new TestView();
        testView.setContext(dummyContext);

        verify(testView.mockHasElementContext).setContext(dummyContext);
    }

    @Test
    public void shouldSetContextOnElementFieldsThatImplementHasElementContext() {
        DummyContext dummyContext = new DummyContext();

        class TestView extends AbstractView {
            Element mockElement = mock(ElementThatHasContext.class);
        };

        TestView testView = new TestView();
        testView.setContext(dummyContext);

        verify((HasElementContext) testView.mockElement).setContext(dummyContext);
    }

    @Test
    public void shouldSetContextOnListFieldsThatImplementHasElementContext() {
        DummyContext dummyContext = new DummyContext();

        class TestView extends AbstractView {
            List<Element> mockElementList = mock(ElementListThatHasContext.class);
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
        }

        TestView testView = new TestView();
        SpecificContext specificContext = new SpecificContext();

        testView.setContext(specificContext);

        assertEquals(testView.getContext(), testView.castedContext);
    }

    @Test(expected = DarcyException.class)
    public void shouldThrowExceptionIfAttemptToAssignContextToAnUnimplementedType() {
        class SpecificContext extends DummyContext {

        }

        class TestView extends AbstractView {
            @com.redhat.darcy.ui.annotations.Context
            private SpecificContext castedContext;
        }

        TestView testView = new TestView();

        testView.setContext(new DummyContext());
    }

    @Test
    public void shouldCallOnSetContext() {
        AbstractView testView = spy(new AbstractView() {});

        testView.setContext(new NullContext());

        verify(testView).onSetContext();
    }

    interface ElementThatHasContext extends Element, HasElementContext {}
    interface ElementListThatHasContext extends List<Element>, HasElementContext {}
}
