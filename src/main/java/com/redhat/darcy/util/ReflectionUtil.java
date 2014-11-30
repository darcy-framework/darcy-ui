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

package com.redhat.darcy.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class ReflectionUtil {
    /**
     * Returns all fields of any scope, throughout the class hierarchy of the object, excluding
     * synthetic fields (like references to outer classes). All returned fields have accessibility
     * override flag set.
     */
    public static List<Field> getAllDeclaredFields(Object object) {
        List<Field> allFields = new ArrayList<>();
        Class<?> objClass = object.getClass();
        
        // Loop through the class hierarchy
        while (objClass != Object.class) {
            allFields.addAll(Arrays.asList(objClass.getDeclaredFields()));
            
            objClass = objClass.getSuperclass();
        }

        allFields.removeIf(Field::isSynthetic);
        allFields.forEach(f -> f.setAccessible(true));
        
        return allFields;
    }
    
    public static List<Class<?>> getAllInterfaces(Object object) {
        List<Class<?>> allInterfaces = new ArrayList<>();
        
        Class<?> objClass = object.getClass();
        
        // Loop through the class hierarchy
        while (objClass != Object.class) {
            allInterfaces.addAll(Arrays.asList(objClass.getInterfaces()));
            
            objClass = objClass.getSuperclass();
        }
        
        return allInterfaces;
    }

    // TODO: Add unit test
    public static Class<?> getGenericTypeOfCollectionField(Field field) {
        Type genericType = field.getGenericType();

        if (!(genericType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Field is not a ParameterizedType.");
        }

        Type[] genericTypes = ((ParameterizedType) genericType).getActualTypeArguments();


        if (genericTypes.length != 1) {
            throw new IllegalArgumentException("Field does not have a single generic type.");
        }

        if (!(genericTypes[0] instanceof Class)) {
            throw new IllegalArgumentException("Field's generic type is not a class?");
        }

        return (Class<?>) genericTypes[0];
    }
}
