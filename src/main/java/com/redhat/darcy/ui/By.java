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

import java.util.List;

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
    
    public static Locator linkText(String linkText) {
        return new ByLinkText(linkText);
    }
    
    public static Locator textContent(String textContent) {
        return new ByTextContent(textContent);
    }
    
    public static Locator partialTextContent(String partialTextContent) {
        return new ByPartialTextContent(partialTextContent);
    }
    
    public static Locator xpath(String xpath) {
        return new ByXPath(xpath);
    }
    
    public static Locator view(View view) {
        return new ByView(view);
    }
    
    public static Locator chained(Locator... locators) {
        return new ByChained(locators);
    }
    
    public static Locator nested(Element parent, Locator child) {
        return new ByNested(parent, child);
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
        
        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsById) context).findById(type, id);
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

        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByName) context).findByName(type, name);
        }
    }
    
    public static class ByLinkText implements Locator {
        private String linkText;
        
        public ByLinkText(String linkText) {
            this.linkText = linkText;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByLinkText) context).findAllByLinkText(type, linkText);
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByLinkText) context).findByLinkText(type, linkText);
        }
    }
    
    public static class ByTextContent implements Locator {
        private String textContent;
        
        public ByTextContent(String textContent) {
            this.textContent = textContent;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByTextContent) context).findAllByTextContent(type, textContent);
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByTextContent) context).findByTextContent(type, textContent);
        }
    }
    
    public static class ByPartialTextContent implements Locator {
        private String partialTextContent;
        
        public ByPartialTextContent(String partialTextContent) {
            this.partialTextContent = partialTextContent;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByPartialTextContent) context)
                    .findAllByPartialTextContent(type, partialTextContent);
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByPartialTextContent) context)
                    .findByPartialTextContent(type, partialTextContent);
        }
    }
    
    public static class ByXPath implements Locator {
        private String xpath;
        
        public ByXPath(String xpath) {
            this.xpath = xpath;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByXPath) context).findAllByXPath(type, xpath);
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByXPath) context).findByXPath(type, xpath);
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

        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByView) context).findByView(type, view);
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

        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByChained) context).findByChained(type, locators);
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

        @Override
        public <T> T find(Class<T> type, Context context) {
            return ((FindsByNested) context).findByNested(type, parent, child);
        }
    }
}
