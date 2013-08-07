/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.xml.stax;

import javanet.staxutils.XMLEventStreamWriter;
import javanet.staxutils.XMLStreamEventReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;

/**
 * Various tools related to StAX API.
 * 
 * @version $Id$
 * @since 5.2M1
 */
public final class StAXUtils
{
    /**
     * Utility class.
     */
    private StAXUtils()
    {
    }

    /**
     * Extract or create an instance of {@link XMLStreamReader} from the provided {@link Source}.
     * 
     * @param source the source
     * @return the {@link XMLStreamReader}
     * @throws XMLStreamException when failing to extract xml stream reader
     */
    public static XMLStreamReader getXMLStreamReader(Source source) throws XMLStreamException
    {
        XMLStreamReader xmlStreamReader;

        if (source instanceof StAXSource) {
            // StAXSource is not supported by standard XMLInputFactory
            StAXSource staxSource = (StAXSource) source;
            if (staxSource.getXMLStreamReader() != null) {
                xmlStreamReader = staxSource.getXMLStreamReader();
            } else {
                // TODO: add support for XMLStreamReader -> XMLEventReader
                throw new XMLStreamException("XMLEventReader is not supported as source");
            }
        } else {
            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(source);
        }

        return xmlStreamReader;
    }

    /**
     * Extract or create an instance of {@link XMLEventReader} from the provided {@link Source}.
     * 
     * @param source the source
     * @return the {@link XMLEventReader}
     * @throws XMLStreamException when failing to extract xml event reader
     */
    public static XMLEventReader getXMLEventReader(Source source) throws XMLStreamException
    {
        XMLEventReader xmlEventReader;

        if (source instanceof StAXSource) {
            // StAXSource is not supported by standard XMLInputFactory
            StAXSource staxSource = (StAXSource) source;
            if (staxSource.getXMLEventReader() != null) {
                xmlEventReader = staxSource.getXMLEventReader();
            } else {
                xmlEventReader = new XMLStreamEventReader(staxSource.getXMLStreamReader());
            }
        } else {
            xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(source);
        }

        return xmlEventReader;
    }

    /**
     * Extract or create an instance of {@link XMLStreamWriter} from the provided {@link Result}.
     * 
     * @param result the result
     * @return the {@link XMLStreamWriter}
     * @throws XMLStreamException when failing to extract xml stream writer
     */
    public static XMLStreamWriter getXMLStreamWriter(Result result) throws XMLStreamException
    {
        XMLStreamWriter xmlStreamWriter;

        if (result instanceof SAXResult) {
            // SAXResult is not supported by the standard XMLOutputFactory
            xmlStreamWriter = new XMLEventStreamWriter(new SAXEventWriter(((SAXResult) result).getHandler()));
        } else if (result instanceof StAXResult) {
            // XMLEventWriter is not supported as result of XMLOutputFactory#createXMLStreamWriter
            StAXResult staxResult = (StAXResult) result;
            if (staxResult.getXMLStreamWriter() != null) {
                xmlStreamWriter = staxResult.getXMLStreamWriter();
            } else {
                xmlStreamWriter = new XMLEventStreamWriter(staxResult.getXMLEventWriter());
            }
        } else {
            xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(result);
        }

        return xmlStreamWriter;
    }
}
