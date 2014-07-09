package com.redhat.darcy.ui;

import static com.redhat.darcy.ui.elements.Elements.element;
import static org.junit.Assert.assertEquals;

import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.LazyElement;
import com.redhat.darcy.ui.testing.doubles.AlwaysMetCondition;
import com.redhat.darcy.ui.testing.doubles.DummyContext;
import com.redhat.synq.Condition;

import org.junit.Test;

public class LazyViewTest {
    interface ContextInterface {
        String getTestString();
    }
    
    interface CustomElement extends Element {
        ContextInterface getCastedContext();
    }
    
    @Test
    public void shouldBeAbleToAccessInterfacesOfParentViewContextViaAtContextAnnotation() {
        class ParentContext extends DummyContext implements ContextInterface {
            public String getTestString() {
                return "test";
            }
        }
        
        class TestCustomElement extends AbstractView implements CustomElement {
            @com.redhat.darcy.ui.annotations.Context
            ContextInterface parentContext;

            @Override
            public boolean isPresent() {
                return isLoaded();
            }

            @Override
            public boolean isDisplayed() {
                return isLoaded();
            }

            @Override
            public ContextInterface getCastedContext() {
                return parentContext;
            }
            
            // At least one load condition is necessary to be a valid view
            protected Condition<?> loadCondition() {
                return new AlwaysMetCondition<>();
            }
        }
        
        ParentContext parentContext = new ParentContext();
        CustomElement customElement = element(CustomElement.class, By.id("test"), 
                new TestCustomElement());
        ((LazyElement) customElement).setContext(parentContext);
        
        
        assertEquals("Custom element's @Context field should have been assigned a proxy to parent "
                + "view's context.", 
                parentContext.getTestString(), 
                customElement.getCastedContext().getTestString());
    }
}
