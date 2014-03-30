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

import com.redhat.darcy.ui.elements.Element;

public interface By {
    public static By id(String id) {
        return new ById(id);
    }

    public static By name(String name) {
        return new ByName(name);
    }
    
    public static By view(View view) {
        return new ByView(view);
    }
    
    public static By nested(Element parent, By child) {
        return new ByNested(parent, child);
    }
    
    public static By chained(By... bys) {
        return new ByChained(bys);
    }
    
    /*
     * <T> List<T> findAll(Class<T> type, Context context);
     * default <T> T findFirst(Class<T> type, Context context) {
     *     return findAll(type, context).get(0);
     * }
     */
    
    <T> T find(Class<T> type, Context context);
    
    public static class ById implements By {
        private String id;
        
        public ById(String id) {
            this.id = id;
        }
        
        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsById) context).findById(type, id);
        }
    }
    
    public static class ByName implements By {
        private String name;
        
        public ByName(String name) {
            this.name = name;
        }
        
        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByName) context).findByName(type, name);
        }
    }
    
    public static class ByView implements By {
        private View view;
        
        public ByView(View view) {
            this.view = view;
        }
        
        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByView) context).findByView(type, view);
        }
    }
    
    public static class ByChained implements By {
        private final By[] bys;
        
        public ByChained(By... bys) {
            this.bys = bys;
        }
        
        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByChained) context).findByChained(type, bys);
        }
    }
    
    public static class ByNested implements By {
        private final Element parent;
        private final By child;
        
        public ByNested(Element parent, By child) {
            this.parent = parent;
            this.child = child;
        }
        
        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByNested) context).findByNested(type, parent, child);
        }
    }
}
