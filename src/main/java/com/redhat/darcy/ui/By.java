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
import com.redhat.darcy.ui.api.elements.Element;
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
    
    public static Locator nested(Element parent, Locator child, Locator... additional) {
        return new ByNested(parent, child, additional);
    }
    
    public static class ById implements Locator {
        private String id;
        
        public ById(String id) {
            this.id = id;
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
        public boolean equals(Object object) {
            if (!(object instanceof ById)) {
                return false;
            }

            ById other = (ById) object;

            return id.equals(other.id);
        }
    }
    
    public static class ByName implements Locator {
        private String name;
        
        public ByName(String name) {
            this.name = name;
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
    }
    
    public static class ByLinkText implements Locator {
        private String linkText;
        
        public ByLinkText(String linkText) {
            this.linkText = linkText;
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
    }
    
    public static class ByTextContent implements Locator {
        private String textContent;
        
        public ByTextContent(String textContent) {
            this.textContent = textContent;
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
    }
    
    public static class ByPartialTextContent implements Locator {
        private String partialTextContent;
        
        public ByPartialTextContent(String partialTextContent) {
            this.partialTextContent = partialTextContent;
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
    }
    
    public static class ByXPath implements Locator {
        private String xpath;
        
        public ByXPath(String xpath) {
            this.xpath = xpath;
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
    }
    
    public static class ByView implements Locator {
        private View view;
        
        public ByView(View view) {
            this.view = view;
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
    }
    
    public static class ByChained implements Locator {
        private final Locator[] locators;
        
        public ByChained(Locator... locators) {
            this.locators = locators;
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
            if (!(object instanceof ByChained)) {
                return false;
            }

            ByChained other = (ByChained) object;

            return Arrays.equals(locators, other.locators);
        }
    }
    
    public static class ByNested implements Locator {
        private final Element parent;
        private final Locator child;
        
        public ByNested(Element parent, Locator child, Locator... additional) {
            Objects.requireNonNull(child, "child");
            Objects.requireNonNull(parent, "parent");

            if (additional != null) {
                Locator[] locators = new Locator[additional.length + 1];
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
                return ((FindsByNested) context).findAllByNested(type, parent, child);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }

        @Override
        public <T> T find(Class<T> type, Context context) {
            try {
                return ((FindsByNested) context).findByNested(type, parent, child);
            } catch (ClassCastException cce) {
                throw new LocatorNotSupportedException(this);
            }
        }
    }
}
