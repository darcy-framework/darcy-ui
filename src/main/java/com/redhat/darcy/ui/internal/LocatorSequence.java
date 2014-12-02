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

package com.redhat.darcy.ui.internal;

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Findable;

import java.util.AbstractList;
import java.util.Optional;
import java.util.function.Function;

public class LocatorSequence<T> extends AbstractList<T> {
    private final Class<T> type;
    private final Context context;
    private final Function<Integer, Locator> sequence;
    private final int start;

    public LocatorSequence(Class<T> type, Context context, Function<Integer, Locator> sequence,
            int start) {
        this.type = type;
        this.context = context;
        this.sequence = sequence;
        this.start = start;
    }

    @Override
    public T get(int index) {
        return sequence.apply(start + index).find(type, context);
    }

    @Override
    public int size() {
        // Compute the size every call because it may change over time.
        int i = start;

        while(safeGet(type, context, i).isPresent()) {
            i++;
        }

        return i - start;
    }

    private Optional<T> safeGet(Class<T> type, Context context, int index) {
        try {
            T it = sequence.apply(index).find(type, context);

            if (it instanceof Findable && !((Findable) it).isPresent()) {
                return Optional.empty();
            }

            return Optional.of(it);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
