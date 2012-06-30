package org.jboss.jbw2012.keynote.utils;

import java.io.ByteArrayOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.net.URL ;

public class ClassLoaderUtils
{
    public static InputStream getResourceAsStream(final String resourceName, final Class<?> caller)
    {
        final String resource ;
        if (resourceName.startsWith("/"))
        {
                resource = resourceName.substring(1) ;
        }
        else
        {
                final Package callerPackage = caller.getPackage() ;
                if (callerPackage != null)
                {
                        resource = callerPackage.getName().replace('.', '/') + '/' + resourceName ;
                }
                else
                {
                        resource = resourceName ;
                }
        }
        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader() ;
        if (threadClassLoader != null)
        {
                final InputStream is = threadClassLoader.getResourceAsStream(resource) ;
                if (is != null)
                {
                        return is ;
                }
        }
        
        final ClassLoader classLoader = caller.getClassLoader() ;
        if (classLoader != null)
        {
                final InputStream is = classLoader.getResourceAsStream(resource) ;
                if (is != null)
                {
                        return is ;
                }
        }
        
        return ClassLoader.getSystemResourceAsStream(resource) ;
    }
    
    public static URL getResource(final String resourceName, final Class<?> caller)
    {
        final String resource ;
        if (resourceName.startsWith("/"))
        {
            resource = resourceName.substring(1) ;
        }
        else
        {
            final Package callerPackage = caller.getPackage() ;
            if (callerPackage != null)
            {
                    resource = callerPackage.getName().replace('.', '/') + '/' + resourceName ;
            }
            else
            {
                resource = resourceName ;
            }
        }
        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader() ;
        if (threadClassLoader != null)
        {
            final URL url = threadClassLoader.getResource(resource) ;
            if (url != null)
            {
                return url ;
            }
        }
        
        final ClassLoader classLoader = caller.getClassLoader() ;
        if (classLoader != null)
        {
            final URL url = classLoader.getResource(resource) ;
            if (url != null)
            {
                return url ;
            }
        }
        
        return ClassLoader.getSystemResource(resource) ;
    }
    
    public static byte[] readStream(final InputStream is)
        throws IOException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
        final byte[] buffer = new byte[256] ;
        while(true)
        {
            final int count = is.read(buffer) ;
            if (count <= 0)
            {
                break ;
            }
            baos.write(buffer, 0, count) ;
        }
        return baos.toByteArray() ;
    }

    public static String getResourceAsString(final String resourceName, final Class<?> caller, final String charset)
        throws IOException
    {
        return new String(readStream(getResourceAsStream(resourceName, caller)), charset) ;
    }
}
