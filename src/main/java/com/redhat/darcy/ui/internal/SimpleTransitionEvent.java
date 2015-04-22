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

package com.redhat.darcy.ui.internal;

import static com.redhat.darcy.ui.matchers.DarcyMatchers.loadedInContext;

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.ParentContext;
import com.redhat.darcy.ui.api.TransitionEvent;
import com.redhat.darcy.ui.api.View;
import com.redhat.synq.Event;
import com.redhat.synq.ForwardingPollEvent;
import com.redhat.synq.Synq;

public class SimpleTransitionEvent<T extends View> extends ForwardingPollEvent<T> implements TransitionEvent<T> {
    private final T destination;
    private final ElementContext context;
    
    public SimpleTransitionEvent(T destination, ElementContext context) {
        super(Synq.expect(destination, loadedInContext(context)));
        
        this.destination = destination;
        this.context = context;
    }
    
    @Override
    public Event<T> inNewContext() {
        return inNewContext(By.view(destination));
    }

    @Override
    public Event<T> inNewContext(Locator locator) {
        if (!(context instanceof ParentContext)) {
            throw new UnsupportedOperationException("Context not capable of finding another context"
                    + " for View: " + context);
        }
        
        return new NewContextTransitionEvent<>(destination, (ParentContext) context, locator);
    }
}
