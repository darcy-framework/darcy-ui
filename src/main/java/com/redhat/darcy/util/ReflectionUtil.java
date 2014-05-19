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
