/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.sysml.linker.tests.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * Utility class for tests.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class TestUtils {

    /**
     * Copy buffer size.
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Constructor.
     */
    private TestUtils() {
        // prevent instantiation
    }

    /**
     * Gets the content of the given {@link URI}.
     *
     * @param uriConverter
     *            the {@link URIConverter}
     * @param uri
     *            the {@link URI} to read from
     * @param charsetName
     *            The name of a supported {@link java.nio.charset.Charset}
     * @return a {@link CharSequence} of the content of the given {@link InputStream}
     * @throws IOException
     *             if the {@link InputStream} can't be read
     */
    public static String getContent(URIConverter uriConverter, URI uri, String charsetName) throws IOException {
        StringBuilder res = new StringBuilder(BUFFER_SIZE);
        try (InputStream stream = uriConverter.createInputStream(uri);
                InputStreamReader input = new InputStreamReader(new BufferedInputStream(stream), charsetName)) {
            char[] buffer = new char[BUFFER_SIZE];
            int length = input.read(buffer);
            while (length != -1) {
                res.append(buffer, 0, length);
                length = input.read(buffer);
            }
        }
        return res.toString();
    }

    /**
     * Sets the given content to the given {@link URI}.
     *
     * @param uriConverter
     *            the {@link URIConverter}
     * @param uri
     *            the {@link URI} to write to
     * @param charsetName
     *            the charset name
     * @param content
     *            the content to write
     * @throws UnsupportedEncodingException
     *             if the given charset is not supported
     * @throws IOException
     *             if the given stream can't be written to
     */
    public static void setContent(URIConverter uriConverter, URI uri, String charsetName, String content)
            throws UnsupportedEncodingException, IOException {
        try (OutputStream stream = uriConverter.createOutputStream(uri)) {
            stream.write(content.getBytes(charsetName));
        }
    }

    /**
     * Copies all bytes from a source {@link URI} to a destination {@link URI}.
     *
     * @param uriConverter
     *            the {@link URIConverter}
     * @param sourceURI
     *            the source {@link URI}
     * @param destURI
     *            the destination {@link URI}
     * @return the number of copied bytes
     * @throws IOException
     *             if the copy can't be done
     */
    public static long copy(URIConverter uriConverter, URI sourceURI, URI destURI) throws IOException {
        try (InputStream source = uriConverter.createInputStream(sourceURI);
                OutputStream dest = uriConverter.createOutputStream(destURI);) {
            long nread = 0L;
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = source.read(buf)) > 0) {
                dest.write(buf, 0, n);
                nread += n;
            }
            return nread;
        }
    }

    /**
     * Gets the portable version of the given {@link String}.
     *
     * @param textContent
     *            the text content
     * @return the portable version of the given {@link String}
     */
    public static String getPortableString(String textContent) {
        String res;

        // nsURI
        res = textContent.replaceFirst("xmlns:sysml=\"[^\"]*\"", "xmlns:sysml=\"\"");
        // UUID
        res = res.replaceAll("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", "00000000-0000-0000-0000-000000000000");

        return res;
    }

}
