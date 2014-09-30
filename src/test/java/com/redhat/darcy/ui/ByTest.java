package com.redhat.darcy.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.WrapsElement;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.*;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
    public void shouldThrowLocatorNotSupportedExceptionForFindBylTextContentIfContextDoesNotSupportByTextContent() {
        Context mockContext = mock(Context.class);

        By.textContent("test").find(Locator.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllBylTextContentIfContextDoesNotSupportByTextContent() {
        Context mockContext = mock(Context.class);

        By.textContent("test").findAll(Locator.class, mockContext);
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

        By.partialTextContent("test").find(Locator.class, mockContext);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportedExceptionForFindAllByPartialTextContentIfContextDoesNotSupportByPartialTextContent() {
        Context mockContext = mock(Context.class);

        By.partialTextContent("test").findAll(Locator.class, mockContext);
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

    interface FindsByAll extends Context, FindsById, FindsByXPath, FindsByName, FindsByNested,
            FindsByLinkText, FindsByPartialTextContent, FindsByTextContent, FindsByView {}

    interface ElementWrapper extends Element, WrapsElement {};
}
