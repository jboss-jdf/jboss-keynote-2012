package org.jboss.jbw2012.keynote.utils.xml;

import java.io.Serializable ;
import java.util.ArrayList ;
import java.util.Comparator ;
import java.util.List ;
import java.util.Map ;
import java.util.TreeMap ;

import javax.xml.namespace.QName ;

import org.xml.sax.Attributes ;

/**
 * Simple class representing an element.
 * This is used to compare XML documents.
 *
 * @author Kevin Conner
 */
public class Element implements Node
{
    /**
     * The QName comparator.
     */
    private static final Comparator<QName> QNAME_COMPARATOR = new QNameComparator() ;
    
    /**
     * The name of the element.
     */
    private final QName name ;
    /**
     * Associated attributes.
     */
    private final Map<QName, String> attributes = new TreeMap<QName, String>(QNAME_COMPARATOR) ;
    /**
     * Children.
     */
    private final List<Node> children = new ArrayList<Node>() ;
    
    /**
     * Construct the element.
     * @param namespaceURI The namespace for the element.
     * @param localName The local name of the element.
     * @param attributes The associated attributes.
     */
    Element(final String namespaceURI, final String localName, final Attributes attributes)
    {
        name = new QName(namespaceURI, localName) ;
        final int numAttributes = attributes.getLength() ;
        for(int count = 0 ; count < numAttributes ; count++)
        {
            final String attrNamespaceURI = attributes.getURI(count) ;
            final String attrLocalName = attributes.getLocalName(count) ;
            final String attrValue = attributes.getValue(count) ;
            
            this.attributes.put(new QName(attrNamespaceURI, attrLocalName), attrValue) ;
        }
    }
    
    /**
     * Add a child node.
     * @param child The child node.
     */
    void addChild(final Node child)
    {
        children.add(child) ;
    }
    
    /**
     * Check for equality.
     * @param obj the object to test against.
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null)
        {
            return false ;
        }
        
        if (obj == this)
        {
            return true ;
        }
        
        if (obj instanceof Element)
        {
            final Element rhs = (Element)obj ;
            return (name.equals(rhs.name) && attributes.equals(rhs.attributes) &&
                    children.equals(rhs.children)) ;
        }
        
        return false ;
    }
    
    /**
     * Return a hash code for this element.
     * @return the element hash code.
     */
    @Override
    public int hashCode()
    {
        return name.hashCode() ^ attributes.hashCode() ^ children.hashCode() ;
    }
    
    /**
     * The QName comparator class.
     * @author kevin
     */
    private static final class QNameComparator implements Comparator<QName>, Serializable
    {
        /**
         * Serial version UID for this comparator.
         */
        private static final long serialVersionUID = -8711685004148549433L;

        /**
         * Compare the QNames.
         * @param name1 The first QName.
         * @param name2 The second QName.
         * @return 
         */
        public int compare(final QName name1, final QName name2)
        {
            final int uriComparator = name1.getNamespaceURI().compareTo(name2.getNamespaceURI()) ;
            if (uriComparator != 0)
            {
                return uriComparator ;
            }
            return name1.getLocalPart().compareTo(name2.getLocalPart());
        }
    }
}
