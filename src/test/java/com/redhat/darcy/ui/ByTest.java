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

package com.redhat.darcy.ui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.WrapsElement;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Findable;
import com.redhat.darcy.ui.internal.FindsByAttribute;
import com.redhat.darcy.ui.internal.FindsById;
import com.redhat.darcy.ui.internal.FindsByLinkText;
import com.redhat.darcy.ui.internal.FindsByName;
import com.redhat.darcy.ui.internal.FindsByNested;
import com.redhat.darcy.ui.internal.FindsByPartialTextContent;
import com.redhat.darcy.ui.internal.FindsByTextContent;
import com.redhat.darcy.ui.internal.FindsByTitle;
import com.redhat.darcy.ui.internal.FindsByView;
import com.redhat.darcy.ui.internal.FindsByXPath;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class ByTest {
    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByIdIfContextDoesNotSupportById() {
        Context mockContext = mock(Context.class);

        By.id("test").find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByIdIfContextDoesNotSupportById() {
        Context mockContext = mock(Context.class);

        By.id("test").findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByIdToFindById() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.id("test").find(Element.class, mockContext);

        verify(mockContext).findById(Element.class, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnZeroLocators() {
        Locator locator = By.chained(new Locator[]{});
    }

    @Test
    public void shouldUseFindByIdToFindAllById() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.id("test").findAll(Element.class, mockContext);

        verify(mockContext).findAllById(Element.class, "test");
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByXpathIfContextDoesNotSupportByXpath() {
        Context mockContext = mock(Context.class);

        By.xpath("test").find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByXpathIfContextDoesNotSupportByXpath() {
        Context mockContext = mock(Context.class);

        By.xpath("test").findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByXpathToFindByXpath() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.xpath("test").find(Element.class, mockContext);

        verify(mockContext).findByXPath(Element.class, "test");
    }

    @Test
    public void shouldUseFindByXpathToFindAllByXpath() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.xpath("test").findAll(Element.class, mockContext);

        verify(mockContext).findAllByXPath(Element.class, "test");
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByNameIfContextDoesNotSupportByName() {
        Context mockContext = mock(Context.class);

        By.name("test").find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByNameIfContextDoesNotSupportByName() {
        Context mockContext = mock(Context.class);

        By.name("test").findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByNameToFindByName() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.name("test").find(Element.class, mockContext);

        verify(mockContext).findByName(Element.class, "test");
    }

    @Test
    public void shouldUseFindByNameToFindAllByXpath() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.name("test").findAll(Element.class, mockContext);

        verify(mockContext).findAllByName(Element.class, "test");
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByAttributeIfContextDoesNotSupportByAttributeOrByXpath() {
        Context mockContext = mock(Context.class);

        By.attribute("value", "test").find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByAttributeIfContextDoesNotSupportByAttributeOrByXpath() {
        Context mockContext = mock(Context.class);

        By.attribute("value", "test").findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByAttributeToFindByAttribute() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.attribute("value", "test").find(Element.class, mockContext);

        verify(mockContext).findByAttribute(Element.class, "value", "test");
    }

    @Test
    public void shouldUseFindByAttributeToFindAllByAttribute() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.attribute("value", "test").findAll(Element.class, mockContext);

        verify(mockContext).findAllByAttribute(Element.class, "value", "test");
    }

    @Test
    public void shouldUseFindAllByXPathIfContextDoesNotImplementFindsByAttribute() {
        FindsByAllExceptAttribute mockContext = mock(FindsByAllExceptAttribute.class);

        By.attribute("value", "test").findAll(Element.class, mockContext);

        verify(mockContext).findAllByXPath(Element.class, ".//*[@value='test']");
    }

    @Test
    public void shouldUseFindByXPathIfContextDoesNotImplementFindsByAttribute() {
        FindsByAllExceptAttribute mockContext = mock(FindsByAllExceptAttribute.class);

        By.attribute("value", "test").find(Element.class, mockContext);

        verify(mockContext).findByXPath(Element.class, ".//*[@value='test']");
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByNestedIfContextDoesNotSupportByNested() {
        Context mockContext = mock(Context.class);
        Locator mockLocator = mock(Locator.class);
        Element mockElement = mock(Element.class);

        By.nested(mockElement, mockLocator).find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByNestedIfContextDoesNotSupportByNested() {
        Context mockContext = mock(Context.class);
        Locator mockLocator = mock(Locator.class);
        Element mockElement = mock(Element.class);

        By.nested(mockElement, mockLocator).findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByNestedToFindByNested() {
        FindsByAll mockContext = mock(FindsByAll.class);
        Locator mockLocator = mock(Locator.class);
        Element mockElement = mock(Element.class);

        By.nested(mockElement, mockLocator).find(Element.class, mockContext);

        verify(mockContext).findByNested(Element.class, mockElement, mockLocator);
    }

    @Test
    public void shouldUseFindAllByNestedToFindByNested() {
        FindsByAll mockContext = mock(FindsByAll.class);
        Locator mockLocator = mock(Locator.class);
        Element mockElement = mock(Element.class);

        By.nested(mockElement, mockLocator).findAll(Element.class, mockContext);

        verify(mockContext).findAllByNested(Element.class, mockElement, mockLocator);
    }

    @Test
    public void shouldUnwrapWrappedElementsInByNested() {
        Element mockElement = mock(ElementWrapper.class);
        FindsByAll mockContext = mock(FindsByAll.class);

        Element realElement = new AlwaysDisplayedLabel();
        when(((WrapsElement) mockElement).getWrappedElement()).thenReturn(realElement);

        new By.ByNested(mockElement, By.id("child")).find(Element.class, mockContext);

        verify(mockContext).findByNested(Element.class, realElement, By.id("child"));
    }

    @Test
    public void shouldUnwrapWrappedElementsInByNestedWhenFindingAll() {
        Element mockElement = mock(ElementWrapper.class);
        FindsByAll mockContext = mock(FindsByAll.class);

        Element realElement = new AlwaysDisplayedLabel();
        when(((WrapsElement) mockElement).getWrappedElement()).thenReturn(realElement);

        new By.ByNested(mockElement, By.id("child")).findAll(Element.class, mockContext);

        verify(mockContext).findAllByNested(Element.class, realElement, By.id("child"));
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByChainedIfContextDoesNotSupportByChained() {
        Context mockContext = mock(Context.class);
        Locator[] mockLocators = new Locator[] { mock(Locator.class), mock(Locator.class) };

        By.chained(mockLocators).find(Element.class, mockContext);
    }

    @Test
    public void shouldAllowChainingLocatorsInByNested() {
        Element mockElement = mock(Element.class);
        FindsByAll mockContext = mock(FindsByAll.class);

        By.ByNested byNested = new By.ByNested(mockElement, By.id("first"), By.id("second"));
        byNested.find(Element.class, mockContext);

        verify(mockContext).findByNested(Element.class, mockElement,
                By.chained(By.id("first"), By.id("second")));
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByChainedIfContextDoesNotSupportByChained() {
        Context mockContext = mock(Context.class);
        Locator[] mockLocators = new Locator[] { mock(Locator.class), mock(Locator.class) };

        By.chained(mockLocators).findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByChainedToFindByChained() {
        FindsByAll mockContext = mock(FindsByAll.class);
        Locator[] mockLocators = new Locator[] { mock(Locator.class), mock(Locator.class) };

        By.chained(mockLocators).find(Element.class, mockContext);

        verify(mockContext).findByChained(Element.class, mockLocators);
    }

    @Test
    public void shouldUseFindByChainedToFindByAllChained() {
        FindsByAll mockContext = mock(FindsByAll.class);
        Locator[] mockLocators = new Locator[] { mock(Locator.class), mock(Locator.class) };

        By.chained(mockLocators).findAll(Element.class, mockContext);

        verify(mockContext).findAllByChained(Element.class, mockLocators);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByLinkTextIfContextDoesNotSupportByLinkText() {
        Context mockContext = mock(Context.class);

        By.linkText("test").find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByLinkTextIfContextDoesNotSupportByLinkText() {
        Context mockContext = mock(Context.class);

        By.linkText("test").findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByLinkTextToFindByLinkText() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.linkText("test").find(Element.class, mockContext);

        verify(mockContext).findByLinkText(Element.class, "test");
    }

    @Test
    public void shouldUseFindAllByLinkTextToFindAllByLinkText() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.linkText("test").findAll(Element.class, mockContext);

        verify(mockContext).findAllByLinkText(Element.class, "test");
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByTextContentIfContextDoesNotSupportByTextContent() {
        Context mockContext = mock(Context.class);

        By.textContent("test").find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByTextContentIfContextDoesNotSupportByTextContent() {
        Context mockContext = mock(Context.class);

        By.textContent("test").findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByTextContentToFindByTextContent() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.textContent("test").find(Element.class, mockContext);

        verify(mockContext).findByTextContent(Element.class, "test");
    }

    @Test
    public void shouldUseFindAllByTextContentToFindAllByTextContent() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.textContent("test").findAll(Element.class, mockContext);

        verify(mockContext).findAllByTextContent(Element.class, "test");
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByPartialTextContentIfContextDoesNotSupportByPartialTextContent() {
        Context mockContext = mock(Context.class);

        By.partialTextContent("test").find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByPartialTextContentIfContextDoesNotSupportByPartialTextContent() {
        Context mockContext = mock(Context.class);

        By.partialTextContent("test").findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByPartialTextContentToFindByPartialTextContent() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.partialTextContent("test").find(Element.class, mockContext);

        verify(mockContext).findByPartialTextContent(Element.class, "test");
    }

    @Test
    public void shouldUseFindAllByPartialTextContentToFindAllByPartialTextContent() {
        FindsByAll mockContext = mock(FindsByAll.class);

        By.partialTextContent("test").findAll(Element.class, mockContext);

        verify(mockContext).findAllByPartialTextContent(Element.class, "test");
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindByViewIfContextDoesNotSupportByView() {
        Context mockContext = mock(Context.class);
        View mockView = mock(View.class);

        By.view(mockView).find(Element.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByViewIfContextDoesNotSupportByView() {
        Context mockContext = mock(Context.class);
        View mockView = mock(View.class);

        By.view(mockView).findAll(Element.class, mockContext);
    }

    @Test
    public void shouldUseFindByViewToFindByView() {
        FindsByAll mockContext = mock(FindsByAll.class);
        View mockView = mock(View.class);

        By.view(mockView).find(Element.class, mockContext);

        verify(mockContext).findByView(Element.class, mockView);
    }

    @Test
    public void shouldUseFindAllByViewToFindAllByView() {
        FindsByAll mockContext = mock(FindsByAll.class);
        View mockView = mock(View.class);

        By.view(mockView).findAll(Element.class, mockContext);

        verify(mockContext).findAllByView(Element.class, mockView);
    }

    @Test
    public void shouldHaveEquivalentByIdLocators() {
        By.ById byId_test_ = By.id("test");
        assertThat(byId_test_, not(equalTo(By.name("test"))));
        assertThat(byId_test_, equalTo(By.id("test")));
        assertThat(byId_test_, not(equalTo(By.id("anotherTest"))));
        assertThat(byId_test_, equalTo(byId_test_));
        assertThat(byId_test_.hashCode(), equalTo(By.id("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByNameLocators() {
        By.ByName byName_test_ = By.name("test");

        assertThat(byName_test_, not(equalTo(By.id("test"))));
        assertThat(byName_test_, equalTo(By.name("test")));
        assertThat(byName_test_, not(equalTo(By.name("anotherTest"))));
        assertThat(byName_test_, equalTo(byName_test_));
        assertThat(byName_test_.hashCode(), equalTo(By.name("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByLinkTestLocators() {
        By.ByLinkText byLinkText_test_ = By.linkText("test");

        assertThat(byLinkText_test_, not(equalTo(By.name("testing"))));
        assertThat(byLinkText_test_, equalTo(By.linkText("test")));
        assertThat(byLinkText_test_, not(equalTo(By.linkText("anotherTest"))));
        assertThat(byLinkText_test_, equalTo(byLinkText_test_));
        assertThat(byLinkText_test_.hashCode(), equalTo(By.linkText("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByTextContentLocators() {
        By.ByTextContent byTextContent_test_ = By.textContent("test");

        assertThat(byTextContent_test_, not(equalTo(By.name("testing"))));
        assertThat(byTextContent_test_, equalTo(By.textContent("test")));
        assertThat(byTextContent_test_, not(equalTo(By.textContent("anotherTest"))));
        assertThat(byTextContent_test_, equalTo(byTextContent_test_));
        assertThat(byTextContent_test_.hashCode(), equalTo(By.textContent("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByPartialTextContentLocators() {
        By.ByPartialTextContent byPartialTextContent_test_ = By.partialTextContent("test");

        assertThat(byPartialTextContent_test_, not(equalTo(By.name("testing"))));
        assertThat(byPartialTextContent_test_, equalTo(By.partialTextContent("test")));
        assertThat(byPartialTextContent_test_, not(equalTo(By.partialTextContent("anotherTest"))));
        assertThat(byPartialTextContent_test_, equalTo(byPartialTextContent_test_));
        assertThat(byPartialTextContent_test_.hashCode(), equalTo(By.partialTextContent
                ("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByXpathLocators() {
        By.ByXPath byXPath_test_ = By.xpath("test");

        assertThat(byXPath_test_, not(equalTo(By.name("testing"))));
        assertThat(byXPath_test_, equalTo(By.xpath("test")));
        assertThat(byXPath_test_, not(equalTo(By.xpath("anotherTest"))));
        assertThat(byXPath_test_, equalTo(byXPath_test_));
        assertThat(byXPath_test_.hashCode(), equalTo(By.xpath("test").hashCode()));
    }

    @Test
    public void shouldHaveByAttributeEquivalence() {
        By.ByAttribute byAttribute_attributeTest_valueTest_ =
                By.attribute("attributeTest", "valueTest");

        assertThat(byAttribute_attributeTest_valueTest_, not(equalTo(By.id("test"))));
        assertThat(byAttribute_attributeTest_valueTest_,
                equalTo(By.attribute("attributeTest", "valueTest")));
        assertThat(byAttribute_attributeTest_valueTest_, not(equalTo(By.attribute("1", "2"))));
        assertThat(byAttribute_attributeTest_valueTest_,
                equalTo(byAttribute_attributeTest_valueTest_));
        assertThat(byAttribute_attributeTest_valueTest_.hashCode(),
                equalTo(By.attribute("attributeTest", "valueTest").hashCode()));
    }

    @Test
    public void shouldHaveByViewEquivalence() {
        View testView = mock(View.class);
        View differentTestView = mock(View.class);

        By.ByView byView_testView_ = By.view(testView);

        assertThat(byView_testView_, not(equalTo(By.id("test"))));
        assertThat(byView_testView_, equalTo(By.view(testView)));
        assertThat(byView_testView_, not(equalTo(By.view(differentTestView))));
        assertThat(byView_testView_, equalTo(byView_testView_));
        assertThat(byView_testView_.hashCode(), equalTo(By.view(testView).hashCode()));
    }

    @Test
    public void shouldCorrectlyImplementEqualsForByNested() {
        Element parent = new AlwaysDisplayedLabel();
        Element differentParent = new AlwaysDisplayedLabel();

        By.ByNested nestedUnderParentByNameTest = By.nested(parent, By.name("test"));

        assertEquals(nestedUnderParentByNameTest, By.nested(parent, By.name("test")));
        assertEquals(nestedUnderParentByNameTest, nestedUnderParentByNameTest);
        assertNotEquals(nestedUnderParentByNameTest, By.nested(parent, By.id("id")));
        assertNotEquals(nestedUnderParentByNameTest, By.nested(differentParent, By.name("test")));
        assertNotEquals(nestedUnderParentByNameTest, By.id("something else entirely"));
        assertEquals(nestedUnderParentByNameTest.hashCode(),
                By.nested(parent, By.name("test")).hashCode());
    }

    @Test
    public void shouldCorrectlyImplementEqualsForByChained() {
        Locator idOf_parent_ = By.id("parent");
        Locator nameOf_child_ = By.name("child");

        By.ByChained underId_parent_andName_child_ = By.chained(idOf_parent_, nameOf_child_);

        assertEquals(underId_parent_andName_child_, By.chained(By.id("parent"), By.name("child")));
        assertEquals(underId_parent_andName_child_, underId_parent_andName_child_);
        assertNotEquals(underId_parent_andName_child_, By.chained(By.id("parent"), By.id("diff")));
        assertNotEquals(underId_parent_andName_child_, By.chained(By.name("diff"), By.name("child")));
        assertNotEquals(underId_parent_andName_child_, By.id("something else entirely"));
        assertEquals(underId_parent_andName_child_.hashCode(),
                By.chained(By.id("parent"), By.name("child")).hashCode());
    }

    @Test
    public void shouldCorrectlyImplementEqualsForByTitle() {
        By.ByTitle byTitleFoo = By.title("foo");
        assertEquals(byTitleFoo, By.title("foo"));
        assertEquals(byTitleFoo, byTitleFoo);
        assertNotEquals(byTitleFoo, By.title("bar"));
        assertEquals(byTitleFoo.hashCode(), By.title("foo").hashCode());
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionWhenTryingToFindByTitleWhenNotSupported() {
        Context context = mock(Context.class);
        By.title("foo").find(Findable.class, context);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionWhenTryingToFindAllByTitleWhenNotSupported() {
        Context context = mock(Context.class);
        By.title("foo").findAll(Findable.class, context);
    }

    @Test
    public void shouldFindByTitleWhenUsingFindByTitle() {
        FindsByAll mockContext = mock(FindsByAll.class);
        Element el = mock(Element.class);
        when(mockContext.findByTitle(Element.class, "foo")).thenReturn(el);
        assertSame(el, By.title("foo").find(Element.class, mockContext));
    }

    @Test
    public void shouldFindAllByTitleWhenUsingFindAllByTitle() {
        FindsByAll mockContext = mock(FindsByAll.class);
        Element el1 = mock(Element.class);
        Element el2 = mock(Element.class);
        when(mockContext.findAllByTitle(Element.class, "foo")).thenReturn(Arrays.asList(el1, el2));

        assertThat(By.title("foo").findAll(Element.class, mockContext),
                contains(sameInstance(el1), sameInstance(el2)));
    }

    interface FindsByAll extends Context, FindsByAttribute, FindsById, FindsByXPath, FindsByName, FindsByNested,
            FindsByLinkText, FindsByPartialTextContent, FindsByTextContent, FindsByView, FindsByTitle {}

    interface FindsByAllExceptAttribute extends Context, FindsById, FindsByXPath, FindsByName, FindsByNested,
            FindsByLinkText, FindsByPartialTextContent, FindsByTextContent, FindsByView, FindsByTitle {}

    interface ElementWrapper extends Element, WrapsElement {};
}
