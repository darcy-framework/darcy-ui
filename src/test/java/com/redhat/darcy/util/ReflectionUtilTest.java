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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class ReflectionUtilTest {
    class TestClass {
        private final Boolean test1 = false;
        protected int test2;
        public String test3;
    };

    class TestSubClass extends TestClass {
        protected Object test4;
        private long test5;
    }

    interface Interface1 {}
    interface Interface2 {}
    interface Interface3 {}

    class TestInterfaces implements Interface1, Interface2 {}
    class TestSubClassInterfaces extends TestInterfaces implements Interface3 {}

    @Test
    public void shouldRetrieveAllNonSyntheticFieldsInAClass() {
        TestClass testClass = new TestClass();

        List<String> fieldNames = ReflectionUtil.getAllDeclaredFields(testClass)
                .stream()
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(fieldNames, containsInAnyOrder("test1", "test2", "test3"));
    }

    @Test
    public void shouldRetrieveAllNonSyntheticFieldsInEntireClassHierarchy() {
        TestSubClass testSubClass = new TestSubClass();

        List<String> fieldNames = ReflectionUtil.getAllDeclaredFields(testSubClass)
                .stream()
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(fieldNames, hasItems("test1", "test2", "test3", "test4", "test5"));
    }

    @Test
    public void shouldReturnFieldsWithAccessibilityFlagSet() {
        TestClass testClass = new TestClass();

        assertTrue(ReflectionUtil.getAllDeclaredFields(testClass)
                .stream()
                .allMatch(Field::isAccessible));
    }

    @Test
    public void shouldGetAllInterfacesInClassHierarchy() {
        TestSubClassInterfaces testSubClassInterfaces = new TestSubClassInterfaces();

        List<String> interfaceNames = ReflectionUtil.getAllInterfaces(testSubClassInterfaces)
                .stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toList());

        assertThat(interfaceNames, containsInAnyOrder("Interface1", "Interface2", "Interface3"));
    }
}
