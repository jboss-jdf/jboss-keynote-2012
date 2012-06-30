package org.jboss.jbw2012.keynote.utils.xml;

/**
 * Simple class representing a text element.
 * This is used to compare XML documents.
 *
 * @author Kevin Conner
 */
public class Text implements Node
{
    /**
     * The text content.
     */
    private final String text ;
    
    /**
     * Construct the text element.
     * @param text The text value.
     */
    Text(final String text)
    {
        this.text = text ;
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
        
        if (obj instanceof Text)
        {
            final Text rhs = (Text)obj ;
            return (text.equals(rhs.text)) ;
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
        return text.hashCode() ;
    }
}
