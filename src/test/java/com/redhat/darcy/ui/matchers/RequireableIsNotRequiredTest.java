package com.redhat.darcy.ui.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.redhat.darcy.ui.api.elements.Requireable;

public class RequireableIsNotRequiredTest {
    
    @Test
    public void shouldMatchWhenElementIsNotRequired() {
        RequireableIsNotRequired<TestElement> matcher = new RequireableIsNotRequired<TestElement>();
        TestElement mockElement = mock(TestElement.class);
        
        when(mockElement.isRequired()).thenReturn(false);

        assertTrue(matcher.matches(mockElement));
        
    }
    
    @Test
    public void shouldNotMatchWhenElementIsRequired() {
        RequireableIsNotRequired<TestElement> matcher = new RequireableIsNotRequired<TestElement>();
        TestElement mockElement = mock(TestElement.class);
        
        when(mockElement.isRequired()).thenReturn(true);
        
        assertFalse(matcher.matches(mockElement));
        
    }
    
    private interface TestElement extends Requireable {
        
    }
}
