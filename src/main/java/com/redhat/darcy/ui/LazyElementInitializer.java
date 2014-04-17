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

import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.LazyElement;

/**
 * Static helper class to take a View from an invalid, default state, to an initialized, ready to
 * use state. Only applies if Views employ proxy elements and/or annotations.
 * 
 * @author ahenning
 * 
 */
public abstract class LazyElementInitializer {
    
    /**
     * Views may employee lazily loaded elements who are defined via static factories that produce
     * proxy elements. These static factories have no reference to the View the elements are a part
     * of, and therefore the elements at their initial state have no reference to the
     * {@link ViewContext} that they are in. This tells the elements what View they are apart of so
     * they can be worked with.
     * 
     * @param view
     * @see com.redhat.darcy.ui.elements.LazyElement
     */
    public static <T extends View> void initLazyElements(T view) {
        Class<?> viewClass = view.getClass();
        
        // Loop through the class hierarchy
        while (viewClass != Object.class) {
            // Loop through the fields
            for (Field field : viewClass.getDeclaredFields()) {
                Class<?> fieldType = field.getType();
                
                // Is the field a LazyElement? It can also be a View, in the case of custom elements,
                // but it must at least implement LazyElement to indicate it needs to be initialized
                // in this way.
                if (Element.class.isAssignableFrom(fieldType)) {
                    field.setAccessible(true);
                    
                    // Initialize it
                    try {
                        Object element = field.get(view);
                        
                        if (element instanceof LazyElement) {
                            ((LazyElement) field.get(view)).setView(view);
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
            
            viewClass = viewClass.getSuperclass();
        }
    }
}
