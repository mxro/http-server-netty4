/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty4.internal;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

import de.mxro.httpserver.netty4.ByteStreamHandler;
import de.mxro.sslutils.SslKeyStoreData;

/**
 * 
 * 
 * @author <a href="http://www.mxro.de/">Max Rohde</a>
 * 
 */
public final class RestServerPipelineFactory implements ChannelPipelineFactory {

    protected final boolean useSsl;
    protected final ByteStreamHandler handler;
    protected SslKeyStoreData sslKeyStore;

    @Override
    public ChannelPipeline getPipeline() throws Exception {

        final ChannelPipeline pipeline = pipeline();

        if (useSsl) {
            final SSLEngine engine = MxSslUtils.createContextForCertificate(sslKeyStore).createSSLEngine();

            engine.setUseClientMode(false);
            final SslHandler sslHandler = new SslHandler(engine);

            pipeline.addLast("ssl", sslHandler);
        }

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("deflater", new HttpContentCompressor());
        pipeline.addLast("aggregator", new HttpChunkAggregator(5242880));
        pipeline.addLast("httphandler", new HttpRequestAggregator(handler));

        return pipeline;
    }

    public RestServerPipelineFactory(final ByteStreamHandler handler, final boolean useSsl,
            final SslKeyStoreData sslKeyStore) {
        super();
        this.useSsl = useSsl;
        this.handler = handler;
        this.sslKeyStore = sslKeyStore;
    }

}
