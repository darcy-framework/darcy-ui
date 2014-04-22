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

import java.util.List;

public interface FindsByXPath {
    <T> List<T> findAllByXPath(Class<T> type, String xpath); 
    default <T> T findByXPath(Class<T> type, String xpath) {
        List<T> found = findAllByXPath(type, xpath);
        
        if (found.isEmpty()) {
            throw new NotFoundException(type, By.xpath(xpath));
        }
        
        return findAllByXPath(type, xpath).get(0);
    }
}