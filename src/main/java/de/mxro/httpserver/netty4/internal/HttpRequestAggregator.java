/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty4.internal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.io.ByteArrayOutputStream;

import de.mxro.httpserver.netty4.ByteStreamHandler;

/**
 * 
 * @author <a href="http://www.mxro.de/">Max Rohde</a>
 * 
 */
public class HttpRequestAggregator extends ChannelInboundHandlerAdapter {

    protected final ByteStreamHandler byteStreamHandler;

    private final ByteArrayOutputStream receivedData;
    private boolean chunked;

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        if (msg instanceof HttpRequest) {

            if (!chunked) {
                final HttpRequest request = (HttpRequest) msg;

                final ChannelBuffer buffer = request.getContent();
                receivedData.write(buffer.array());

                if (!request.isChunked()) {
                    byteStreamHandler.processRequest(receivedData, e);
                } else {
                    chunked = true;

                }

            } else {
                final HttpChunk chunk = (HttpChunk) e.getMessage();
                final ChannelBuffer buffer = chunk.getContent();
                receivedData.write(buffer.array());

                if (chunk.isLast()) {
                    byteStreamHandler.processRequest(receivedData, e);
                }
            }
        }

    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        ctx.close();
        throw new RuntimeException(cause.getCause());
    }

    public HttpRequestAggregator(final ByteStreamHandler byteStreamHandler) {
        super();
        this.byteStreamHandler = byteStreamHandler;

        this.chunked = false;
        this.receivedData = new ByteArrayOutputStream();
    }

}
