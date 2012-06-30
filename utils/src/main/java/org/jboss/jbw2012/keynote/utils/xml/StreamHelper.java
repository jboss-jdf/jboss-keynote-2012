/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. 
 * See the copyright.txt in the distribution for a full listing 
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package org.jboss.jbw2012.keynote.utils.xml;

import javax.xml.namespace.QName ;
import javax.xml.stream.XMLStreamConstants ;
import javax.xml.stream.XMLStreamException ;
import javax.xml.stream.XMLStreamReader ;

/**
 * Helper class for Stream operations.
 * @author kevin
 */
public class StreamHelper
{
    /**
     * Make sure the stream is at the start of the next element.
     * @param streamReader The current stream reader.
     * @throws XMLStreamException For parsing errors.
     */
    public static void skipToNextStartElement(final XMLStreamReader streamReader)
        throws XMLStreamException
    {
        if (streamReader.hasNext())
        {
            streamReader.nextTag() ;
            if (streamReader.isEndElement())
            {
                throw new XMLStreamException("Unexpected end element: " + streamReader.getName()) ;
            }
        }
        else
        {
            throw new XMLStreamException("Unexpected end of document reached") ;
        }
    }

    /**
     * Make sure the stream is at the start of an element.
     * @param streamReader The current stream reader.
     * @throws XMLStreamException For parsing errors.
     */
    public static void skipToStartElement(final XMLStreamReader streamReader)
        throws XMLStreamException
    {
        while (!streamReader.isStartElement())
        {
            if (streamReader.hasNext())
            {
                streamReader.next() ;
            }
            else
            {
                throw new XMLStreamException("Unexpected end of document reached") ;
            }
        }
    }

    /**
     * Check the next start tag is as expected.
     * @param streamReader The stream reader.
     * @param expected The expected qualified name.
     * @throws XMLStreamException For errors during parsing.
     */
    public static void checkNextStartTag(final XMLStreamReader streamReader, final QName expected)
        throws XMLStreamException
    {
        skipToNextStartElement(streamReader) ;
        checkTag(streamReader, expected) ;
    }
    
    /**
     * Check the end tag is as expected.
     * @param streamReader The stream reader.
     * @param expected The expected qualified name.
     * @throws XMLStreamException For errors during parsing.
     */
    public static void checkEndTag(final XMLStreamReader streamReader, final QName expected)
        throws XMLStreamException
    {
        streamReader.require(XMLStreamConstants.END_ELEMENT,
            QNameHelper.getNormalisedNamespaceURI(expected.getNamespaceURI()), expected.getLocalPart()) ;
    }
    
    /**
     * Compare the element tag with the expected qualified name.
     * @param streamReader The current stream reader.
     * @param expected The expected qualified name.
     * @throws XMLStreamException For errors during parsing.
     */
    public static void checkTag(final XMLStreamReader streamReader, final QName expected)
        throws XMLStreamException
    {
        final QName elementName = streamReader.getName() ;
        if (!expected.equals(elementName))
        {
            throw new XMLStreamException("Unexpected start element: " + elementName) ;
        }
    }

    /**
     * Check to see if the parent element is finished.
     * @param streamReader The stream reader.
     * @return true if it is finished, false otherwise.
     * @throws XMLStreamException For errors during parsing.
     */
    public static boolean checkParentFinished(final XMLStreamReader streamReader)
        throws XMLStreamException
    {
        return (streamReader.nextTag() == XMLStreamConstants.END_ELEMENT) ;
    }

    /**
     * Get the value of the specified attribute.
     * @param streamReader The current XML stream reader.
     * @param name The name of the attribute.
     * @return The value of the attribute.
     */
    public static String getAttributeValue(final XMLStreamReader streamReader, final QName name)
    {
        return streamReader.getAttributeValue(name.getNamespaceURI(), name.getLocalPart()) ;
    }
}
