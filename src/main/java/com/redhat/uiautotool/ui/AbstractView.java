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

package com.redhat.uiautotool.ui;
    
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A partial implementation of View that initializes LazyElements in
 * {@link #setContext(ViewContext)}, simplifies defining load conditions (via {@link Require},
 * {@link RequireAll}, {@link NotRequired}, and {@link #loadCondition()}.
 * 
 * @author ahenning
 * 
 */
public abstract class AbstractView implements View {
    /**
     * The ViewContext for this View, managed by AbstractView.
     */
    private ViewContext context;
    
    /**
     * List of Callable<Boolean> "load conditions" -- if all of these return true, then the page is
     * considered loaded.
     */
    @LoadConditions
    private final List<Callable<Boolean>> loadConditions = new ArrayList<>();
    
    // Add the custom defined load condition to the list if there is one defined.
    // @see #loadCondition()
    {
        if (loadCondition() != null) {
            loadConditions.add(loadCondition());
        }
    }
    
    /**
     * Used by {@link #isLoaded()}. When the Callable.call evaluates to true, the page should be
     * loaded.
     * <P>
     * This condition will be considered in addition to any elements annotated with {@link Require}.
     * <P>
     * By default this returns null. Subclasses should override this method if necessary to define a
     * more specific load condition. If the simple visibility of some elements is all that is
     * required, then simply use {@link Require} or {@link RequireAll} annotations.
     * 
     * @return Null if not explicitly overridden by a subclass.
     */
    public Callable<Boolean> loadCondition() {
        return null;
    }
    
    @Override
    public final boolean isLoaded() {
        if (context == null) {
            throw new NullContextException();
        }
        
        if (loadConditions.isEmpty()) {
            throw new MissingLoadConditionException(this);
        }
        
        try {
            for (Callable<Boolean> condition : loadConditions) {
                if (!condition.call()) {
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public final View setContext(ViewContext context) {
        if (this.context == null) {
            // First call. Initialize elements. At the moment it is unnecessary to repeat this step
            // if the context changes because elements are tied to a View, not a context directly,
            // and so knowing only about the View is sufficient. It will reference whatever is the
            // current context of that View. This behavior may change.
            ViewInitializer.initView(this);
        }
        
        this.context = context;
        
        return this;
    }
    
    @Override
    public final ViewContext getContext() {
        return context;
    }
}
