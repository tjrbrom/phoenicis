/*
 * Copyright (C) 2015 Markus Ebner
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.utils.list;

import com.playonlinux.utils.filter.Filter;
import com.playonlinux.utils.filter.Filterable;
import com.playonlinux.utils.observer.AbstractObservableImplementation;
import com.playonlinux.utils.observer.Observable;
import com.playonlinux.utils.observer.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An observable,filterable list whose source itself is a filterable list, accessed with the given filter.
 *
 * @param <T> The type of the items within the list.
 */
public class FilterPromise<T> extends AbstractObservableImplementation implements Filterable<T>, Observer, Iterable<T> {

    private Filterable<T> source;
    private Filter<T> filter;
    private List<T> cache = null;

    public Filterable<T> getSource() {
        return source;
    }

    public Filter<T> getFilter() {
        return filter;
    }


    public FilterPromise(Filterable<T> source, Filter<T> filter) {
        this.source = source;
        this.filter = filter;
        source.addObserver(this);
        filter.addObserver(this);
    }


    @Override
    public Iterator<T> iterator() {
        updateCache();
        return cache.iterator();
    }

    @Override
    public List<T> getFiltered(Filter<T> filter) {
        updateCache();
        return new ArrayList<>(cache);
    }


    @Override
    public void update(Observable observable, Object argument) {
        cache = null;
        this.notifyObservers();
    }

    private void updateCache() {
        if (cache == null) {
            this.cache = new ArrayList<>(source.getFiltered(filter));
        }
    }


}
