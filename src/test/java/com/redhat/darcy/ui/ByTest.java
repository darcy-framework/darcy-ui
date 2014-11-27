package com.redhat.darcy.ui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anyObject;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    public void shouldHaveLocatorsThatHaveEquivalentStringLocators() {
        Class clazz = By.class;
        String one = "test", two = "test2";
        View mockView = mock(View.class);
        Locator otherLocator = By.view(mockView);
        String [] classes = {"id", "name", "linkText", "textContent", "partialTextContent", "xpath"};
        try {
            for(String className : classes) {
                Method method = clazz.getMethod(className, String.class);
                Locator locatorOne = (Locator) method.invoke(null, one);
                Locator locatorTwo = (Locator) method.invoke(null, two);
                Locator locatorThree = (Locator) method.invoke(null, one);

                assertThat(locatorOne, not(equalTo(otherLocator)));
                assertThat(locatorOne, equalTo(locatorOne));
                assertThat(locatorOne, not(equalTo(locatorTwo)));
                assertThat(locatorOne, equalTo(locatorThree));
                assertThat(locatorOne.hashCode(), notNullValue());
            }
        } catch (NoSuchMethodException e){
            e.printStackTrace();
        } catch (InvocationTargetException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    @Test
    public void shouldHaveByAttributeEquivalence() {
        String attributeOne = "attrTest", attributeTwo = "attrTest2";
        String valueOne = "valueTest", valueTwo = "valueTest2";
        Locator attrLocatorOne = By.attribute(attributeOne, valueOne);
        Locator attrLocatorTwo = By.attribute(attributeTwo, valueTwo);
        Locator attrLocatorThree = By.attribute(attributeOne, valueOne);
        Locator otherLocator = By.id("test");

        assertThat(attrLocatorOne, not(equalTo(otherLocator)));
        assertThat(attrLocatorOne, equalTo(attrLocatorOne));
        assertThat(attrLocatorOne, not(equalTo(attrLocatorTwo)));
        assertThat(attrLocatorOne, equalTo(attrLocatorThree));
        assertThat(attrLocatorOne.hashCode(), notNullValue());
    }

    @Test
    public void shouldHaveByViewEquivalence() {
        View view = mock(View.class);
        View view2 = mock(View.class);
        Locator viewLocatorOne = By.view(view);
        Locator viewLocatorTwo = By.view(view2);
        Locator viewLocatorThree = By.view(view);
        Locator otherLocator = By.id("test");

        assertThat(viewLocatorOne, not(equalTo(otherLocator)));
        assertThat(viewLocatorOne, equalTo(viewLocatorOne));
        assertThat(viewLocatorOne, not(equalTo(viewLocatorTwo)));
        assertThat(viewLocatorOne, equalTo(viewLocatorThree));
        assertThat(viewLocatorOne.hashCode(), notNullValue());
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
