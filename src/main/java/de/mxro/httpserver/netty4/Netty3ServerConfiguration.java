package de.mxro.httpserver.netty4;

import mx.sslutils.SslKeyStoreData;
import de.mxro.httpserver.HttpService;

public abstract class Netty3ServerConfiguration {

	public abstract int getPort();

	public abstract boolean getUseSsl();

	public abstract SslKeyStoreData getSslKeyStore();

	public abstract HttpService getService();

}
