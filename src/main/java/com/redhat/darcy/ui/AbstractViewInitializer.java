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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.LazyElement;

/**
 * Static helper class to take a View from an invalid, default state, to an initialized, ready to
 * use state. Only applies if Views employ proxy elements and/or annotations.
 * 
 * @author ahenning
 * 
 */
public abstract class AbstractViewInitializer {
    /**
     * Views may employee annotations or lazily loaded elements to make them more maintainable and
     * portable. In order to use a View like this, it must be initialized with some reflection magic
     * fairy dust.
     * 
     * @param view
     * @return The view, now in an appropriate state to be used.
     * @see #sprinkleFairyDust(View)
     * @see AbstractView
     */
    public static <T extends View> T initView(T view) {
        return sprinkleFairyDust(view);
    }
    
    /**
     * Views may employee lazily loaded elements who are defined via static factories that produce
     * proxy elements. These static factories have no reference to the View the elements are a part
     * of, and therefore the elements at their initial state have no reference to the
     * {@link ViewContext} that they are in. This tells the elements what View they are apart of so
     * they can be worked with.
     * <P>
     * Additionally, while we're reflectively iterating over fields, we'll look out for a 
     * {@code List} of {@code Callable<Boolean>} annotated with {@code @LoadConditions}. If we find
     * it, we'll populate it with load conditions as determined from annotated elements and the 
     * view itself.
     * 
     * @param view
     * @return
     * @see com.redhat.darcy.ui.elements.LazyElement
     * @see com.redhat.darcy.ui.Require
     * @see com.redhat.darcy.ui.RequireAll
     * @see com.redhat.darcy.ui.NotRequired
     * @see com.redhat.darcy.ui.LoadConditions
     */
    @SuppressWarnings("unchecked")
    private static <T extends View> T sprinkleFairyDust(T view) {
        Class<?> viewClass = view.getClass();
        
        // We'll populate this with {@code Callable<Boolean>}s that can collectively help determine
        // if a view is loaded or not. We'll add these Callables based on annotated element fields
        // that we find within the view's class hierarchy. This is just a local list; if we find
        // some field within the class hierarchy that we can put these in, we'll do that. Otherwise
        // these won't have anywhere to be assigned and will be ignored.
        List<Callable<Boolean>> elementLoadConditions = new ArrayList<>();
        
        // A reference to a load conditions field if we find one.
        List<Callable<Boolean>> loadConditionsField = null;
        
        // Loop through the class hierarchy
        while (viewClass != Object.class) {
            // Loop through the fields
            for (Field field : viewClass.getDeclaredFields()) {
                Class<?> fieldType = field.getType();
                
                // Is the field an element or a view?
                if (Element.class.isAssignableFrom(fieldType)
                        || View.class.isAssignableFrom(fieldType)) {
                    initializeElement(field, view, elementLoadConditions);
                    
                // Is the field a list of load conditions? (annotated with @LoadCondition)
                // Do we already have a load conditions field determined?
                } else if(loadConditionsField == null 
                        && field.getAnnotation(LoadConditions.class) != null) {
                    try {
                        field.setAccessible(true);
                        loadConditionsField = (List<Callable<Boolean>>) field.get(view);
                    } catch (ClassCastException | IllegalArgumentException 
                            | IllegalAccessException e) {
                        // Something went wrong... ignore the load conditions field
                    }
                }
                
            }
            
            viewClass = viewClass.getSuperclass();
        }
        
        if (loadConditionsField != null) {
            loadConditionsField.addAll(elementLoadConditions);
        }
        
        return view;
    }
    
    private static void initializeElement(Field field, View view, 
            List<Callable<Boolean>> loadConditions) {
        field.setAccessible(true);
        
        // Initialize it, and add a load condition for it if applicable.
        try {
            Object element = field.get(view);
            
            if (element instanceof LazyElement) {
                ((LazyElement) element).setView(view);
            }
            
            Callable<Boolean> conditionForElement = getCallableFromAnnotatedField(
                    field, element);
            
            if (conditionForElement != null) {
                loadConditions.add(conditionForElement);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Read annotations from a field and its declaring class and spit back out a Callable<Boolean>
     * to represent it.
     * 
     * @param field
     * @param element
     * @return Null if field is not annotated with Require and class is not annotated with
     *         RequireAll, is annotated with Ignore, or is not an instance of View or Element.
     */
    private static Callable<Boolean> getCallableFromAnnotatedField(Field field, 
            final Object element) {
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
    }
}
