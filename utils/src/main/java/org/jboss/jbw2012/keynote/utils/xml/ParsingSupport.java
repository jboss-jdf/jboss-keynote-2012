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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Utility class providing support for parsing.
 * @author kevin
 */
public abstract class ParsingSupport
{
    /**
     * Is the configuration of this element valid?
     * @return true if valid, false otherwise.
     */
    public boolean isValid()
    {
        return true ;
    }
    
    /**
     * Parse the contents of this element from the input stream.
     * @param in The input stream.
     */
    protected final void parse(final XMLStreamReader in)
        throws XMLStreamException
    {
        final int attributeCount = in.getAttributeCount() ;
        for(int count = 0 ; count < attributeCount ; count++)
        {
            final QName attributeName = in.getAttributeName(count) ;
            final String attributeValue = in.getAttributeValue(count) ;
            putAttribute(in, attributeName, attributeValue) ;
        }
        
        final QName name = in.getName() ;
        boolean finished = false ;
        
        do
        {
            final int type = in.next() ;
            switch(type)
            {
                case XMLStreamConstants.START_ELEMENT:
                    final QName startElementName = in.getName() ;
                    putElement(in, startElementName) ;
                    StreamHelper.checkEndTag(in, startElementName) ;
                    break ;
                case XMLStreamConstants.END_ELEMENT:
                    StreamHelper.checkEndTag(in, name) ;
                    finished = true ;
                    break ;
                case XMLStreamConstants.CDATA:
                case XMLStreamConstants.CHARACTERS:
                    putValue(in, in.getText()) ;
                    break ;
                case XMLStreamConstants.COMMENT:
                case XMLStreamConstants.DTD:
                case XMLStreamConstants.ENTITY_DECLARATION:
                case XMLStreamConstants.ENTITY_REFERENCE:
                case XMLStreamConstants.SPACE:
                case XMLStreamConstants.NOTATION_DECLARATION:
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    // Ignore these
                    break ;
                case XMLStreamConstants.ATTRIBUTE:
                case XMLStreamConstants.START_DOCUMENT:
                case XMLStreamConstants.END_DOCUMENT:
                case XMLStreamConstants.NAMESPACE:
                    throw new XMLStreamException("Encountered unexpected event type: " + type) ;
            }
        }
        while(!finished) ;
    }
    
    /**
     * Set the text value of this element.
     * @param in The current input stream.
     * @param value The text value of this element.
     * @throws XMLStreamException For errors during parsing.
     */
    protected void putValue(final XMLStreamReader in, final String value)
        throws XMLStreamException
    {
    }
    
    /**
     * Add the attribute value.
     * @param in The current input stream.
     * @param attributeName The qualified attribute name.
     * @param attributeValue The qualified attribute value.
     * @throws XMLStreamException For errors during parsing.
     */
    protected void putAttribute(final XMLStreamReader in,
        final QName attributeName, final String attributeValue)
        throws XMLStreamException
    {
    }
    
    /**
     * Add the element.
     * @param in The current input stream.
     * @param elementName The qualified element name.
     * @throws XMLStreamException For errors during parsing.
     */
    protected abstract void putElement(final XMLStreamReader in,
        final QName elementName)
        throws XMLStreamException ;
}
