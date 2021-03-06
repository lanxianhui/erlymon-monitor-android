/*
 * Copyright (c) 2016, Sergey Penkovsky <sergey.penkovsky@gmail.com>
 *
 * This file is part of Erlymon Monitor.
 *
 * Erlymon Monitor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Erlymon Monitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Erlymon Monitor.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.erlymon.monitor.dagger.module;

import com.appunite.websocket.rx.object.RxObjectWebSockets;
import com.google.gson.Gson;

import org.erlymon.monitor.api.RestApi;
import org.erlymon.monitor.mvp.NetworkService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sergey on 17.03.17.
 */

@Module(includes = {RestApiModule.class, WebSocketModule.class})
public class NetworkModule {
    @Provides
    @Singleton
    public NetworkService provideNetworkService(RestApi restApi, RxObjectWebSockets rxObjectWebSockets, Gson gson) {
        return new NetworkService(restApi, rxObjectWebSockets, gson);
    }
}
