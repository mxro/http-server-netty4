package de.mxro.httpserver.netty4;

import de.mxro.httpserver.HttpService;
import de.mxro.sslutils.SslKeyStoreData;

public abstract class Netty4ServerConfiguration {

	public abstract int getPort();

	public abstract boolean getUseSsl();

	public abstract SslKeyStoreData getSslKeyStore();

	public abstract HttpService getService();

}
