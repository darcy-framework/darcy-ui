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

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Transition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AbstractViewTest {
    @Test
    public void shouldForwardTransitionToContext() {
        ElementContext mockContext = mock(ElementContext.class);
        Transition mockTransition = mock(Transition.class);

        when(mockContext.transition()).thenReturn(mockTransition);

        AbstractView testView = new AbstractView() {};
        testView.setContext(mockContext);

        assertSame(mockTransition, testView.transition());
    }

    @Test(expected = NullContextException.class)
    public void shouldThrowNullContextExceptionIfTransitionCalledWithoutAContext() {
        new AbstractView() {}.transition();
    }

    @Test
    public void shouldAllowOverridingOnSetContext() {
        new AbstractView() {
            @Override
            protected void onSetContext() {

            }
        };
    }
}
