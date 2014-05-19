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

import com.redhat.darcy.DarcyException;
import com.redhat.darcy.ui.annotations.Context;
import com.redhat.darcy.ui.annotations.NotRequired;
import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.annotations.RequireAll;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.LazyElement;
import com.redhat.darcy.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * A partial implementation of View that initializes LazyElements in
 * {@link #setContext(ViewContext)}, and simplifies defining load conditions (via {@link Require},
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
     * All of these need to evaluate to true for the View to be considered loaded.
     */
    private final List<Callable<Boolean>> loadConditions = new ArrayList<>();
    
    // Initialize the load conditions
    {
        if (loadCondition() != null) {
            loadConditions.add(loadCondition());
        }
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
        } catch (NullContextException | MissingLoadConditionException e) {
            // Let this propagate in case of these exceptions
            throw e;
        } catch (Exception e) {
            // Otherwise log it and return false -- something went wrong in evaluating the load
            // condition so we'll assume it's just not loaded yet. (For instance, not finding an
            // element will throw an exception).
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public final View setContext(ViewContext context) {
        this.context = context;
        
        List<Field> fields = ReflectionUtil.getAllDeclaredFields(this);

        injectContexts(fields);
        initializeLazyElements(fields);
        readLoadConditionAnnotations(fields);
        
        if (loadConditions.isEmpty()) {
            throw new MissingLoadConditionException(this);
        }
        
        return this;
    }
    
    @Override
    public final ViewContext getContext() {
        return context;
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
    protected Callable<Boolean> loadCondition() {
        return null;
    }
    
    protected Transition transition() {
        return context.transition();
    }
    
    private void injectContexts(List<Field> fields) {
        fields.stream()
            .filter(f -> f.getAnnotation(Context.class) != null)
            .forEach(f -> {
                try {
                    f.set(this, f.getType().cast(context));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
    }
    
    private void initializeLazyElements(List<Field> fields) {
        fields.stream()
            .filter(f -> Element.class.isAssignableFrom(f.getType()) 
                    || List.class.isAssignableFrom(f.getType()))
            .forEach(f -> {
                try {
                    Object element = f.get(this);
                    
                    if (element instanceof LazyElement) {
                        ((LazyElement) element).setView(this);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
    }
    
    private void readLoadConditionAnnotations(List<Field> fields) {
        loadConditions.addAll(fields.stream()
            .filter(f -> Element.class.isAssignableFrom(f.getType()))
            .map(this::getLoadConditionForElementField)
            .filter(c -> c != null)
            .collect(Collectors.toList()));
    }
    
    private Callable<Boolean> getLoadConditionForElementField(Field field) {
        // Initialize it, and add a load condition for it if applicable.
        try {
            Object element = field.get(this);
            
            Callable<Boolean> callable;
            
            // Determine the applicable Callable for this type; prefer View over Element
            if (element instanceof View) {
                callable = ((View) element)::isLoaded;
            } else if (element instanceof Element) {
                callable = ((Element) element)::isDisplayed;
            } else {
                return null;
            }
            
            // Annotation logic
            if (field.getAnnotation(Require.class) != null
                    || (field.getDeclaringClass().getAnnotation(RequireAll.class) != null 
                    && field.getAnnotation(NotRequired.class) == null)) {
                return callable;
            }
            
            return null;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
