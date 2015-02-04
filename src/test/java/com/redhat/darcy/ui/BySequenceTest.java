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

import static com.redhat.darcy.ui.matchers.DarcyMatchers.present;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Label;
import com.redhat.darcy.ui.internal.FindsById;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
import com.redhat.darcy.ui.testing.doubles.NeverFoundElement;
import com.redhat.darcy.ui.testing.doubles.NeverFoundLabel;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@RunWith(JUnit4.class)
public class BySequenceTest {
    @Test
    public void shouldKeepFindingElementsInSequenceUntilNextIsNotPresentStartingFromSomeIndex() {
        TestContext mockContext = mock(TestContext.class);

        Label label1 = new AlwaysDisplayedLabel();
        Label label2 = new AlwaysDisplayedLabel();
        Label label3 = new AlwaysDisplayedLabel();
        Label label4 = new AlwaysDisplayedLabel();

        when(mockContext.findById(Label.class, "1")).thenReturn(label1);
        when(mockContext.findById(Label.class, "2")).thenReturn(label2);
        when(mockContext.findById(Label.class, "3")).thenReturn(label3);
        when(mockContext.findById(Label.class, "4")).thenReturn(label4);
        when(mockContext.findById(Label.class, "5")).thenReturn(new NeverFoundLabel());

        By.BySequence bySequence = By.sequence(i -> By.id(i.toString()), 1);

        List<Label> found = bySequence.findAll(Label.class, mockContext);

        assertThat(found, contains(label1, label2, label3, label4));
    }

    @Test
    public void shouldStartWithIndexOf0ByDefault() {
        TestContext mockContext = mock(TestContext.class);

        when(mockContext.findById(Label.class, "0")).thenReturn(new NeverFoundLabel());

        By.BySequence bySequence = By.sequence(i -> By.id(i.toString()));

        List<Label> found = bySequence.findAll(Label.class, mockContext);

        // do something to trigger actually looking up the elements
        found.size();

        verify(mockContext).findById(Label.class, "0");
        verifyNoMoreInteractions(mockContext);
    }

    @Test
    public void shouldFindFirstInSequenceWhenFindingSingleElementRegardlesOfIfItIsPresent() {
        TestContext mockContext = mock(TestContext.class);

        Label shouldBeFound = new NeverFoundLabel();

        when(mockContext.findById(Label.class, "5")).thenReturn(shouldBeFound);

        By.BySequence bySequence = By.sequence(i -> By.id(i.toString()), 5);

        Label actuallyFound = bySequence.find(Label.class, mockContext);

        assertThat(actuallyFound, equalTo(shouldBeFound));
        assertThat(actuallyFound, is(not(present())));
    }

    @Test
    public void shouldReturnLazilyEvaluatedList() {
        TestContext mockContext = mock(TestContext.class);

        when(mockContext.findById(Label.class, "0")).thenReturn(new NeverFoundLabel());

        By.BySequence bySequence = By.sequence(i -> By.id(i.toString()));

        List<Label> found = bySequence.findAll(Label.class, mockContext);

        verifyZeroInteractions(mockContext);
    }

    @Test
    public void shouldBeEquivalentToBySequencesThatShareEquivalentSequencesAndStartingIndexes() {
        By.BySequence thingSequence = By.sequence(new IdCounter("thing_"), 1);

        assertThat(thingSequence, equalTo(thingSequence));
        assertThat(thingSequence, equalTo(By.sequence(new IdCounter("thing_"), 1)));
    }

    @Test
    public void shouldNotBeEquivalentToAnythingElse() {
        By.BySequence thingSequence = By.sequence(new IdCounter("thing_"), 1);

        assertThat(thingSequence, not(equalTo(By.id("huh"))));
        assertThat(thingSequence, not(equalTo(By.sequence(new IdCounter("thing_"), 0))));
        assertThat(thingSequence, not(equalTo(By.sequence(i -> By.id(i.toString()), 1))));
    }

    @Test
    public void shouldHaveEquivalentHashCodesForEquivalentSequences() {
        By.BySequence thingSequence = By.sequence(new IdCounter("thing_"), 1);

        assertThat(thingSequence.hashCode(),
                equalTo(By.sequence(new IdCounter("thing_"), 1).hashCode()));
    }

    private static class IdCounter implements Function<Integer, Locator> {
        private final String prefix;

        private IdCounter(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Locator apply(Integer integer) {
            return By.id(prefix + integer);
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != IdCounter.class) {
                return false;
            }

            IdCounter that = (IdCounter) o;

            return Objects.equals(that.prefix, this.prefix);
        }

        @Override
        public int hashCode() {
            return prefix != null ? prefix.hashCode() : 0;
        }
    }

    private interface TestContext extends Context, FindsById {}
}
