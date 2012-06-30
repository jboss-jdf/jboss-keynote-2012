package org.jboss.jbw2012.keynote.utils.xml;

import java.util.ArrayList ;
import java.util.List ;

import org.xml.sax.Attributes ;
import org.xml.sax.SAXException ;
import org.xml.sax.helpers.DefaultHandler ;

/**
 * Simple SAX parser creating an identity document for the incoming XML.
 * Any leading and trailing whitespace is ignored in the document as are
 * namespace prefixes.
 *
 * @author Kevin Conner
 */
public class IdentitySAXHandler extends DefaultHandler
{
    /**
     * The root element.
     */
    private Element rootElement ;
    /**
     * The current element.
     */
    private Element currentElement ;
    /**
     * The stack of working elements.
     */
    private List<Element> stack = new ArrayList<Element>() ;
    /**
     * The current text value.
     */
    private StringBuilder currentText = new StringBuilder() ;

    @Override
    public void startElement(final String uri, final String localName,
        final String name, final Attributes attributes)
        throws SAXException
    {
        checkText() ;
        
        final Element element = new Element(uri, localName, attributes) ;
        if (rootElement == null)
        {
            rootElement = element ;
        }
        
        if (currentElement != null)
        {
            currentElement.addChild(element) ;
            stack.add(currentElement) ;
        }
        currentElement = element ;
    }
    
    @Override
    public void endElement(final String uri, final String localName,
        final String name)
        throws SAXException
    {
        checkText() ;
        
        final int lastIndex = (stack.size() - 1) ;
        if (lastIndex < 0)
        {
            currentElement = null ;
        }
        else
        {
            currentElement = stack.remove(lastIndex) ;
        }
    }
    
    @Override
    public void characters(final char[] ch, final int start, final int length)
        throws SAXException
    {
        currentText.append(ch, start, length) ;
    }
    
    private void checkText()
    {
        final int textLength = currentText.length() ;
        if (textLength > 0)
        {
            int start = 0 ;
            while((start < textLength) && isXMLWhitespace(currentText.charAt(start)))
            {
                start++ ;
            }
            
            int end = textLength-1 ;
            while((end >= start) && isXMLWhitespace(currentText.charAt(end)))
            {
                end-- ;
            }
            
            if (start <= end)
            {
                currentElement.addChild(new Text(currentText.substring(start, end+1))) ;
            }
            currentText.setLength(0) ;
            currentText.trimToSize() ;
        }
    }
    
    private boolean isXMLWhitespace(final char ch)
    {
        return ((ch == ' ') || (ch == '\t') || (ch == '\r') || (ch == '\n')) ;
    }
    
    public Element getRootElement()
    {
        return rootElement ;
    }
}
