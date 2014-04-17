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

public class LoadConditionAnnotationReader {
    
    public static List<Callable<Boolean>> getLoadConditions(View view) {
        Class<?> viewClass = view.getClass();
        
        // We'll populate this with {@code Callable<Boolean>}s that can collectively help determine
        // if a view is loaded or not. We'll add these Callables based on annotated element fields
        // that we find within the view's class hierarchy. This is just a local list; if we find
        // some field within the class hierarchy that we can put these in, we'll do that. Otherwise
        // these won't have anywhere to be assigned and will be ignored.
        List<Callable<Boolean>> loadConditions = new ArrayList<>();
        
        // Loop through the class hierarchy
        while (viewClass != Object.class) {
            // Loop through the fields
            for (Field field : viewClass.getDeclaredFields()) {
                Class<?> fieldType = field.getType();
                
                // Is the field an element? It can also be a View, in the case of custom elements,
                // but it must at least implement Element to be considered for annotated load 
                // conditions.
                if (Element.class.isAssignableFrom(fieldType)) {
                    Callable<Boolean> loadCondition = getLoadConditionForElement(field, view);
                    
                    if (loadCondition != null) {
                        loadConditions.add(loadCondition);
                    }
                }
                
            }
            
            viewClass = viewClass.getSuperclass();
        }
        
        return loadConditions;
    }
    
    private static Callable<Boolean> getLoadConditionForElement(Field field, View view) {
        field.setAccessible(true);
        
        // Initialize it, and add a load condition for it if applicable.
        try {
            Object element = field.get(view);
            
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
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
