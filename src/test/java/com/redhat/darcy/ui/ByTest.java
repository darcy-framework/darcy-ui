package com.redhat.darcy.ui;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;

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
    public void shouldHaveEquivalentByIdLocators() {
        assertThat(By.id("test"), not(equalTo(By.name("testing"))));
        assertThat(By.id("test"), equalTo(By.id("test")));
        assertThat(By.id("test"), not(equalTo(By.id("anotherTest"))));
        assertThat(By.id("test").hashCode(), equalTo(By.id("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByNameLocators() {
        assertThat(By.name("test"), not(equalTo(By.id("testing"))));
        assertThat(By.name("test"), equalTo(By.name("test")));
        assertThat(By.name("test"), not(equalTo(By.name("anotherTest"))));
        assertThat(By.name("test").hashCode(), equalTo(By.name("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByLinkTestLocators() {
        assertThat(By.linkText("test"), not(equalTo(By.name("testing"))));
        assertThat(By.linkText("test"), equalTo(By.linkText("test")));
        assertThat(By.linkText("test"), not(equalTo(By.linkText("anotherTest"))));
        assertThat(By.linkText("test").hashCode(), equalTo(By.linkText("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByTextContentLocators() {
        assertThat(By.textContent("test"), not(equalTo(By.name("testing"))));
        assertThat(By.textContent("test"), equalTo(By.textContent("test")));
        assertThat(By.textContent("test"), not(equalTo(By.textContent("anotherTest"))));
        assertThat(By.textContent("test").hashCode(), equalTo(By.textContent("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByPartialTextContentLocators() {
        assertThat(By.partialTextContent("test"), not(equalTo(By.name("testing"))));
        assertThat(By.partialTextContent("test"), equalTo(By.partialTextContent("test")));
        assertThat(By.partialTextContent("test"), not(equalTo(By.partialTextContent("anotherTest"))));
        assertThat(By.partialTextContent("test").hashCode(), equalTo(By.partialTextContent("test").hashCode()));
    }

    @Test
    public void shouldHaveEquivalentByXpathLocators() {
        assertThat(By.xpath("test"), not(equalTo(By.name("testing"))));
        assertThat(By.xpath("test"), equalTo(By.xpath("test")));
        assertThat(By.xpath("test"), not(equalTo(By.xpath("anotherTest"))));
        assertThat(By.xpath("test").hashCode(), equalTo(By.xpath("test").hashCode()));
    }

    @Test
    public void shouldHaveByAttributeEquivalence() {
        assertThat(By.attribute("attributeTest", "valueTest"), not(equalTo(By.id("test"))));
        assertThat(By.attribute("attributeTest", "valueTest"), equalTo(By.attribute("attributeTest", "valueTest")));
        assertThat(By.attribute("attributeTest", "valueTest"), not(equalTo(By.attribute("anotherAttributeTest", "anotherValueTest"))));
        assertThat(By.attribute("attributeTest", "valueTest").hashCode(), equalTo(By.attribute("attributeTest", "valueTest").hashCode()));
    }

    @Test
    public void shouldHaveByViewEquivalence() {
        View viewTest = mock(View.class);
        View anotherViewTest = mock(View.class);

        assertThat(By.view(viewTest), not(equalTo(By.id("test"))));
        assertThat(By.view(viewTest), equalTo(By.view(viewTest)));
        assertThat(By.view(viewTest), not(equalTo(By.view(anotherViewTest))));
        assertThat(By.view(viewTest).hashCode(), equalTo(By.view(viewTest).hashCode()));
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
        FindsByAllExceptFindsByAttribute mockContext = mock(FindsByAllExceptFindsByAttribute.class);

        By.attribute("value", "test").findAll(Element.class, mockContext);

        verify(mockContext).findAllByXPath(Element.class, ".//*[@value='test']");
    }

    @Test
    public void shouldUseFindByXPathIfContextDoesNotImplementFindsByAttribute() {
        FindsByAllExceptFindsByAttribute mockContext = mock(FindsByAllExceptFindsByAttribute.class);

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

    interface FindsByAll extends Context, FindsByAttribute, FindsById, FindsByXPath, FindsByName, FindsByNested,
            FindsByLinkText, FindsByPartialTextContent, FindsByTextContent, FindsByView {}

    interface FindsByAllExceptFindsByAttribute extends Context, FindsById, FindsByXPath, FindsByName, FindsByNested,
            FindsByLinkText, FindsByPartialTextContent, FindsByTextContent, FindsByView {}

    interface ElementWrapper extends Element, WrapsElement {};
}
