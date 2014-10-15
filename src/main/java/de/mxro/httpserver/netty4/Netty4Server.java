/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty4;

import de.mxro.async.Value;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.services.Services;
import de.mxro.server.ServerComponent;
import de.mxro.sslutils.SslKeyStoreData;

public class Netty4Server {

    /**
     * Secret must be supplied as URI path.
     * 
     * @param port
     * @param secret
     * @param operations
     * @return
     */
    public static void startShutdownServer(final int port, final String secret, final ServerComponent operations,
            final ValueCallback<Netty4ServerComponent> callback) {

        final Value<ServerComponent> ownServer = new Value<ServerComponent>(null);

        final Netty4ServerConfiguration conf = new Netty4ServerConfiguration() {

            @Override
            public boolean getUseSsl() {
                return false;
            }

            @Override
            public SslKeyStoreData getSslKeyStore() {
                return null;
            }

            @Override
            public HttpService getService() {

                return Services.shutdown(secret, operations, ownServer);
            }

            @Override
            public int getPort() {
                return port;
            }
        };

        start(conf, new ValueCallback<Netty4ServerComponent>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final Netty4ServerComponent value) {
                ownServer.set(value);
                callback.onSuccess(value);
            }
        });

    }

    public static void start(final Netty4ServerConfiguration conf, final ValueCallback<Netty4ServerComponent> callback) {

    }

    public static void start(final HttpService service, final int port,
            final ValueCallback<Netty4ServerComponent> callback) {
        final Netty4ServerConfiguration configuration = new Netty4ServerConfiguration() {

            @Override
            public boolean getUseSsl() {
                return false;
            }

            @Override
            public SslKeyStoreData getSslKeyStore() {
                return null;
            }

            @Override
            public HttpService getService() {
                return service;
            }

            @Override
            public int getPort() {
                return port;
            }
        };

        start(configuration, callback);
    }

}
