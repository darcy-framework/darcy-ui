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

import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.elements.Elements;
import com.redhat.darcy.ui.elements.Label;

import org.junit.Test;

import java.util.concurrent.Callable;

public class AbstractViewSetContextTest {
    @Test(expected = MissingLoadConditionException.class)
    public void shouldThrowMissingLoadConditionExceptionIfNoLoadConditionIsPresent() {
        View testView = new AbstractView() {};
        
        testView.setContext(new NullContext());
    }
    
    @Test
    public void shouldInitializeLazyElements() {
        class TestView extends AbstractView {
            @Require
            private Label label = Elements.label(By.id("test"));
            
            public String getLabelText() {
                return label.readText();
            }
        };
        
        TestView testView = new TestView();
        
        testView.setContext(new DummyContext());
        
        assertEquals("Expected LazyElement to be initialized with dummy implementation.", 
                new AlwaysDisplayedLabel().readText(), testView.getLabelText());
    }
    
    @Test
    public void shouldAssignAndCastViewContextToFieldsAnnotatedWithContext() {
        class SpecificContext extends DummyContext {
            
        }
        
        class TestView extends AbstractView {
            @com.redhat.darcy.ui.annotations.Context
            private SpecificContext castedContext;
            
            protected Callable<Boolean> loadCondition() {
                return () -> true;
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
            
            protected Callable<Boolean> loadCondition() {
                return () -> true;
            }
        }
        
        TestView testView = new TestView();
        
        testView.setContext(new DummyContext());
    }
}
