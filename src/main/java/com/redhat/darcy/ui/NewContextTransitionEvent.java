package com.redhat.darcy.ui;

import static com.redhat.darcy.ui.matchers.ViewMatchers.isLoadedInContext;
import static com.redhat.synq.HamcrestCondition.match;

import com.redhat.synq.DefaultPollEvent;
import com.redhat.synq.Event;
import com.redhat.synq.ForwardingPollEvent;

public class NewContextTransitionEvent<T extends View> extends ForwardingPollEvent<T> implements Event<T> {

    public NewContextTransitionEvent(T destination, ParentContext parentContext, 
            Locator contextLocator) {
        super(new DefaultPollEvent<>(match(
                destination, isLoadedInContext(parentContext.findContext(contextLocator)))));
    }
    
}
