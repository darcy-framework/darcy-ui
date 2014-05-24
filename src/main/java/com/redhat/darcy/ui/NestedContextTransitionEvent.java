package com.redhat.darcy.ui;

import static com.redhat.darcy.ui.matchers.ViewMatchers.isLoadedInContext;
import static com.redhat.synq.HamcrestCondition.match;

import com.redhat.synq.DefaultPollEvent;
import com.redhat.synq.ForwardingPollEvent;

public class NestedContextTransitionEvent<T extends View> extends ForwardingPollEvent<T> {
    
    public NestedContextTransitionEvent(T destination, ElementContext context,
            Locator nestedContextLocator) {
        super(new DefaultPollEvent<>(match(
                destination,
                isLoadedInContext(ChainedElementContext.makeChainedElementContext(context,
                        nestedContextLocator)))));
    }
    
}
