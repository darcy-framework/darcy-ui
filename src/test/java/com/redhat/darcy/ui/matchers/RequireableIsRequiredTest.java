package com.redhat.darcy.ui.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.redhat.darcy.ui.api.elements.Requireable;

public class RequireableIsRequiredTest {
    
    @Test
    public void shouldMatchWhenElementIsRequired() {
        RequireableIsRequired<TestElement> matcher = new RequireableIsRequired<TestElement>();
        TestElement mockElement = mock(TestElement.class);
        
        when(mockElement.isRequired()).thenReturn(true);

        assertTrue(matcher.matches(mockElement));
        
    }
    
    @Test
    public void shouldNotMatchWhenElementIsNotRequired() {
        RequireableIsRequired<TestElement> matcher = new RequireableIsRequired<TestElement>();
        TestElement mockElement = mock(TestElement.class);
        
        when(mockElement.isRequired()).thenReturn(false);
        
        assertFalse(matcher.matches(mockElement));
        
    }
    
    private interface TestElement extends Requireable {
        
    }
}
