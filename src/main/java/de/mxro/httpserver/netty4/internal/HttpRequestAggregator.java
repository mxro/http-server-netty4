/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty4.internal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

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
        if (msg instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse) msg;

            if (response.getStatus().code() == 200 && HttpHeaders.isTransferEncodingChunked(response)) {
                chunked = true;
            }
        }
        if (msg instanceof HttpContent) {
            final HttpContent chunk = (HttpContent) msg;

            if (chunk instanceof LastHttpContent) {

                chunked = false;
            } else {
                System.err.println(chunk.content().toString(CharsetUtil.UTF_8));
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
