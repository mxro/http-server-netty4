/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty4;

import java.io.ByteArrayOutputStream;

public interface ByteStreamHandler {

    public void processRequest(ByteArrayOutputStream receivedData, MessageEvent e);

}
