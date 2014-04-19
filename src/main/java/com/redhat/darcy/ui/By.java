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

import java.util.List;

import com.redhat.darcy.ui.elements.Element;

/**
 * A helper class with static factories for {@link Locator}s, inspired by Selenium's By class.
 *
 */
public abstract class By {
    public static Locator id(String id) {
        return new ById(id);
    }

    public static Locator name(String name) {
        return new ByName(name);
    }
    
    public static Locator view(View view) {
        return new ByView(view);
    }
    
    public static Locator nested(Element parent, Locator child) {
        return new ByNested(parent, child);
    }
    
    public static Locator chained(Locator... locators) {
        return new ByChained(locators);
    }
    
    public static class ById implements Locator {
        private String id;
        
        public ById(String id) {
            this.id = id;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsById) context).findAllById(type, id);
        }
    }
    
    public static class ByName implements Locator {
        private String name;
        
        public ByName(String name) {
            this.name = name;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByName) context).findAllByName(type, name);
        }
    }
    
    public static class ByView implements Locator {
        private View view;
        
        public ByView(View view) {
            this.view = view;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByView) context).findAllByView(type, view);
        }
    }
    
    public static class ByChained implements Locator {
        private final Locator[] locators;
        
        public ByChained(Locator... locators) {
            this.locators = locators;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByChained) context).findAllByChained(type, locators);
        }
    }
    
    public static class ByNested implements Locator {
        private final Element parent;
        private final Locator child;
        
        public ByNested(Element parent, Locator child) {
            this.parent = parent;
            this.child = child;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByNested) context).findAllByNested(type, parent, child);
        }
    }
}
