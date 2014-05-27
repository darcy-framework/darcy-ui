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

package com.redhat.darcy.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ReflectionUtil {
    public static List<Field> getAllDeclaredFields(Object object) {
        List<Field> allFields = new LinkedList<>();
        Class<?> objClass = object.getClass();
        
        // Loop through the class hierarchy
        while (objClass != Object.class) {
            allFields.addAll(Arrays.asList(objClass.getDeclaredFields()));
            
            objClass = objClass.getSuperclass();
        }
        
        allFields.forEach(f -> f.setAccessible(true));
        
        return allFields;
    }
    
    public static List<Class<?>> getAllInterfaces(Object object) {
        List<Class<?>> allInterfaces = new LinkedList<>();
        
        Class<?> objClass = object.getClass();
        
        // Loop through the class hierarchy
        while (objClass != Object.class) {
            allInterfaces.addAll(Arrays.asList(objClass.getInterfaces()));
            
            objClass = objClass.getSuperclass();
        }
        
        return allInterfaces;
    }
}
