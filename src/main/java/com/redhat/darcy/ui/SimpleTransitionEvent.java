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

import static com.redhat.darcy.ui.matchers.ViewMatchers.isLoadedInContext;
import static com.redhat.synq.HamcrestCondition.match;

import com.redhat.synq.DefaultPollEvent;
import com.redhat.synq.Event;
import com.redhat.synq.ForwardingPollEvent;

public class SimpleTransitionEvent<T extends View> extends ForwardingPollEvent<T> implements TransitionEvent<T> {
    private final T destination;
    private final ViewContext context;
    
    public SimpleTransitionEvent(T destination, ViewContext context) {
        super(new DefaultPollEvent<>(match(destination, isLoadedInContext(context))));
        
        this.destination = destination;
        this.context = context;
    }
    
    @Override
    public Event<T> inNewContext() {
        return inNewContext(By.view(destination));
    }

    @Override
    public Event<T> inNewContext(Locator locator) {
        return new NewContextTransitionEvent<>(destination, context, locator);
    }

    @Override
    public Event<T> inNestedContext(Locator locator) {
        return new NestedContextTransitionEvent<>(destination, context, locator);
    }
}
