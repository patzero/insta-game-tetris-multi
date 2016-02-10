/**
 * This file is part of WANTED: Bad-ou-Alyve.
 *
 * WANTED: Bad-ou-Alyve is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WANTED: Bad-ou-Alyve is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WANTED: Bad-ou-Alyve.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.badoualy.badoualyve.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

import java.util.concurrent.locks.ReentrantLock;

import rx.Single;

/** Convenience and beauty wrapper for NetworkCallable **/
public class NetworkOperation<T> {

    private final Class<T> clazz; // One of the multiple way to get T's class
    private final Net.HttpRequest request;

    public NetworkOperation(String url, Class<T> clazz) {
        this(url, null, clazz);
    }

    public NetworkOperation(String url, String parameter, Class<T> clazz) {
        this(new HttpRequestBuilder().newRequest()
                                     .method(Net.HttpMethods.GET)
                                     .url(parameter != null && parameter.length() > 0 ? String.format(url, parameter) : url)
                                     .build(), clazz);
    }

    public NetworkOperation(Net.HttpRequest request, Class<T> clazz) {
        this.clazz = clazz;
        this.request = request;
    }

    public Single<T> execute() {
        return new NetworkCallable<T>(request, clazz).toObservable();
    }
}
