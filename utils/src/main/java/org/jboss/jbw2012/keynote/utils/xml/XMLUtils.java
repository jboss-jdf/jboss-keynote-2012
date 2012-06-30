package org.jboss.jbw2012.keynote.utils.xml ;

import java.io.IOException ;
import java.io.InputStream ;
import java.io.Reader ;
import java.io.StringReader ;

import javax.xml.parsers.ParserConfigurationException ;
import javax.xml.parsers.SAXParser ;
import javax.xml.parsers.SAXParserFactory ;
import javax.xml.stream.XMLInputFactory ;
import javax.xml.stream.XMLStreamException ;
import javax.xml.stream.XMLStreamReader ;
import javax.xml.transform.Source ;

import org.xml.sax.InputSource ;
import org.xml.sax.SAXException ;

public class XMLUtils
{
    /**
     * The XML input factory.
     */
    private static final XMLInputFactory XML_INPUT_FACTORY  ;
    
    public static boolean compareXMLContent(final String content1, final String content2)
        throws ParserConfigurationException, SAXException, IOException
    {
        return compareXMLContent(new InputSource(new StringReader(content1)), new InputSource(new StringReader(content2))) ;
    }

    public static boolean compareXMLContent(final InputSource content1, final InputSource content2)
        throws ParserConfigurationException, SAXException, IOException
    {
        final SAXParserFactory parserFactory = SAXParserFactory.newInstance() ;
        parserFactory.setNamespaceAware(true) ;
        parserFactory.setValidating(false) ;

        final SAXParser parser = parserFactory.newSAXParser() ;

        final IdentitySAXHandler handler1 = new IdentitySAXHandler() ;
        parser.parse(content1, handler1) ;

        final IdentitySAXHandler handler2 = new IdentitySAXHandler() ;
        parser.parse(content2, handler2) ;

        return (handler1.getRootElement().equals(handler2.getRootElement())) ;
    }
    
    /**
     * Get the XML stream reader.
     * @param reader The input reader.
     * @return The XML stream reader.
     * @throws XMLStreamException For errors obtaining an XML stream reader.
     */
    public static XMLStreamReader getXMLStreamReader(final Reader reader)
        throws XMLStreamException
    {
        return XML_INPUT_FACTORY.createXMLStreamReader(reader) ;
    }

    /**
     * Get the XML stream reader.
     * @param is The input stream.
     * @return The XML stream reader.
     * @throws XMLStreamException For errors obtaining an XML stream reader.
     */
    public static XMLStreamReader getXMLStreamReader(final InputStream is)
        throws XMLStreamException
    {
        return XML_INPUT_FACTORY.createXMLStreamReader(is) ;
    }

    /**
     * Get the XML stream reader.
     * @param is The input stream.
     * @param encoding The input stream encoding.
     * @return The XML stream reader.
     * @throws XMLStreamException For errors obtaining an XML stream reader.
     */
    public static XMLStreamReader getXMLStreamReader(final InputStream is, final String encoding)
        throws XMLStreamException
    {
        return XML_INPUT_FACTORY.createXMLStreamReader(is, encoding) ;
    }

    /**
     * Get the XML stream reader.
     * @param source The source.
     * @return The XML stream reader.
     * @throws XMLStreamException For errors obtaining an XML stream reader.
     */
    public static XMLStreamReader getXMLStreamReader(final Source source)
        throws XMLStreamException
    {
        return XML_INPUT_FACTORY.createXMLStreamReader(source) ;
    }
    
    static
    {
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance() ;
        xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE) ;
        XML_INPUT_FACTORY = xmlInputFactory ;
    }
}
