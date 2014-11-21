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

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.WrapsElement;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.FindsByAttribute;
import com.redhat.darcy.ui.internal.FindsById;
import com.redhat.darcy.ui.internal.FindsByLinkText;
import com.redhat.darcy.ui.internal.FindsByName;
import com.redhat.darcy.ui.internal.FindsByNested;
import com.redhat.darcy.ui.internal.FindsByPartialTextContent;
import com.redhat.darcy.ui.internal.FindsByTextContent;
import com.redhat.darcy.ui.internal.FindsByView;
import com.redhat.darcy.ui.internal.FindsByXPath;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A helper class with static factories for {@link com.redhat.darcy.ui.api.Locator}s, inspired by
 * Selenium's By class.
 */
public abstract class By {
    public static ById id(String id) {
        return new ById(id);
    }

    public static ByName name(String name) {
        return new ByName(name);
    }
    
    public static ByLinkText linkText(String linkText) {
        return new ByLinkText(linkText);
    }
    
    public static ByTextContent textContent(String textContent) {
        return new ByTextContent(textContent);
    }
    
    public static ByPartialTextContent partialTextContent(String partialTextContent) {
        return new ByPartialTextContent(partialTextContent);
    }
    
    public static ByXPath xpath(String xpath) {
        return new ByXPath(xpath);
    }
    
    public static ByView view(View view) {
        return new ByView(view);
    }
    
    public static ByChained chained(Locator... locators) {
        return new ByChained(locators);
    }
    
    public static ByNested nested(Element parent, Locator child, Locator... additional) {
        return new ByNested(parent, child, additional);
    }

    public static ByAttribute attribute(String attribute, String value) {
        return new ByAttribute(attribute, value);
    }
    
    public static class ById implements Locator {
        private String id;
        
        public ById(String id) {
            this.id = Objects.requireNonNull(id, "id");
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
                return ((FindsById) context).findAllById(type, id);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }
        
        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsById) context).findById(type, id);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof ById)) {
                return false;
            }

            ById other = (ById) object;

            return id.equals(other.id);
        }

        @Override
        public String toString() {
            return "ById: {" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    public static class ByAttribute implements Locator {
        private String attribute;
        private String value;

        public ByAttribute(String attribute, String value) {
            this.attribute = Objects.requireNonNull(attribute, "attribute");
            this.value = Objects.requireNonNull(value, "value");
        }

        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            if (context instanceof FindsByAttribute) {
                return ((FindsByAttribute) context).findAllByAttribute(type, attribute, value);
            } else if (context instanceof FindsByXPath) {
                return ((FindsByXPath) context).findAllByXPath(type, ".//*[@" + attribute + "='" + value + "']");
            }

            throw new LocatorNotSupportedException(this);
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            if (context instanceof FindsByAttribute) {
                return ((FindsByAttribute) context).findByAttribute(type, attribute, value);
            } else if (context instanceof FindsByXPath) {
                return ((FindsByXPath) context).findByXPath(type, ".//*[@" + attribute + "='" + value + "']");
            }

            throw new LocatorNotSupportedException(this);
        }

        @Override
        public int hashCode() {
            return Objects.hash(attribute, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ByAttribute byAttribute = (ByAttribute) o;

            return attribute.equals(byAttribute.attribute)
                    && value.equals(byAttribute.value);
        }

        @Override
        public String toString() {
            return "ByAttribute: {" +
                    "attribute='" + attribute + "', " +
                    "value='" + value + "'" +
                    '}';
        }
    }
    
    public static class ByName implements Locator {
        private String name;
        
        public ByName(String name) {
            this.name = Objects.requireNonNull(name, "name");
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
                return ((FindsByName) context).findAllByName(type, name);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByName) context).findByName(type, name);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ByName byName = (ByName) o;

            return name.equals(byName.name);
        }

        @Override
        public String toString() {
            return "ByName: {" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
    
    public static class ByLinkText implements Locator {
        private String linkText;
        
        public ByLinkText(String linkText) {
            this.linkText = Objects.requireNonNull(linkText, "linkText");
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
                return ((FindsByLinkText) context).findAllByLinkText(type, linkText);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByLinkText) context).findByLinkText(type, linkText);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ByLinkText that = (ByLinkText) o;

            return linkText.equals(that.linkText);

        }

        @Override
        public int hashCode() {
            return Objects.hash(linkText);
        }

        @Override
        public String toString() {
            return "ByLinkText: {" +
                    "linkText='" + linkText + '\'' +
                    '}';
        }
    }
    
    public static class ByTextContent implements Locator {
        private String textContent;
        
        public ByTextContent(String textContent) {
            this.textContent = Objects.requireNonNull(textContent, "textContent");
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
                return ((FindsByTextContent) context).findAllByTextContent(type, textContent);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByTextContent) context).findByTextContent(type, textContent);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ByTextContent that = (ByTextContent) o;

            return textContent.equals(that.textContent);

        }

        @Override
        public int hashCode() {
            return Objects.hash(textContent);
        }

        @Override
        public String toString() {
            return "ByTextContent: {" +
                    "textContent='" + textContent + '\'' +
                    '}';
        }
    }
    
    public static class ByPartialTextContent implements Locator {
        private String partialTextContent;
        
        public ByPartialTextContent(String partialTextContent) {
            this.partialTextContent = Objects.requireNonNull(partialTextContent,
                    "partialTextContent");
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
                return ((FindsByPartialTextContent) context)
                        .findAllByPartialTextContent(type, partialTextContent);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByPartialTextContent) context)
                        .findByPartialTextContent(type, partialTextContent);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ByPartialTextContent that = (ByPartialTextContent) o;

            return partialTextContent.equals(that.partialTextContent);

        }

        @Override
        public int hashCode() {
            return Objects.hash(partialTextContent);
        }

        @Override
        public String toString() {
            return "ByPartialTextContent: {" +
                    "partialTextContent='" + partialTextContent + '\'' +
                    '}';
        }
    }
    
    public static class ByXPath implements Locator {
        private String xpath;
        
        public ByXPath(String xpath) {
            this.xpath = Objects.requireNonNull(xpath, "xpath");
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
                return ((FindsByXPath) context).findAllByXPath(type, xpath);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByXPath) context).findByXPath(type, xpath);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ByXPath that = (ByXPath) o;

            return xpath.equals(that.xpath);

        }

        @Override
        public int hashCode() {
            return Objects.hash(xpath);
        }

        @Override
        public String toString() {
            return "ByXPath: {" +
                    "xpath='" + xpath + '\'' +
                    '}';
        }
    }
    
    public static class ByView implements Locator {
        private final View view;
        
        public ByView(View view) {
            this.view = Objects.requireNonNull(view, "view");
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
                return ((FindsByView) context).findAllByView(type, view);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByView) context).findByView(type, view);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ByView that = (ByView) o;

            return view.equals(that.view);

        }

        @Override
        public int hashCode() {
            return Objects.hash(view);
        }

        @Override
        public String toString() {
            return "ByView: {" +
                    "view='" + view + '\'' +
                    '}';
        }
    }
    
    public static class ByChained implements Locator {
        private final Locator[] locators;
        
        public ByChained(Locator... locators) {
            this.locators = Objects.requireNonNull(locators, "locators");

            if (locators.length == 0) {
                throw new IllegalArgumentException("Cannot chain 0 locators.");
            }
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
                return ((FindsByNested) context).findAllByChained(type, locators);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByNested) context).findByChained(type, locators);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            ByChained other = (ByChained) object;

            return Arrays.equals(locators, other.locators);
        }

        @Override
        public int hashCode() {
            return Objects.hash(locators);
        }

        @Override
        public String toString() {
            return "ByChained: {" +
                    "locators='" + Arrays.toString(locators) + '\'' +
                    '}';
        }
    }
    
    public static class ByNested implements Locator {
        private final Element parent;
        private final Locator child;
        
        public ByNested(Element parent, Locator child, Locator... additional) {
            Objects.requireNonNull(child, "child");
            Objects.requireNonNull(parent, "parent");

            if (additional.length > 0) {
                Locator[] locators = new Locator[additional.length + 1];
                locators[0] = child;
                System.arraycopy(additional, 0, locators, 1, additional.length);
                this.child = By.chained(locators);
            } else {
                this.child = child;
            }

            this.parent = parent;
        }
        
        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            try {
            	
                return ((FindsByNested) context).findAllByNested(type, parent(), child);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByNested) context).findByNested(type, parent(), child);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ByNested byNested = (ByNested) o;

            return child.equals(byNested.child) && parent.equals(byNested.parent);

        }

        @Override
        public int hashCode() {
            return Objects.hash(parent, child);
        }

        @Override
        public String toString() {
            return "ByNested: {" +
                    "parent=" + parent +
                    ", child=" + child +
                    '}';
        }
        
        private Element parent() {
        	return (parent instanceof WrapsElement)
                    ? ((WrapsElement) parent).getWrappedElement()
                    : parent;
        }
    }
}
