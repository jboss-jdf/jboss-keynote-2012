package org.jboss.jbw2012.keynote.rest.resource.test;

import java.io.File ;
import java.net.URL ;

import javax.ws.rs.ApplicationPath ;
import javax.ws.rs.HttpMethod ;
import javax.ws.rs.core.MediaType ;
import javax.ws.rs.core.MultivaluedMap ;

import org.jboss.arquillian.container.test.api.Deployment ;
import org.jboss.arquillian.test.api.ArquillianResource ;
import org.jboss.jbw2012.keynote.rest.JBWApplication ;
import org.jboss.jbw2012.keynote.rest.resource.CategoryResource ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategoryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.init.InitialiseData ;
import org.jboss.jbw2012.keynote.rest.resource.utils.InitialiseUpdateManager ;
import org.jboss.jbw2012.keynote.rest.retry.HibernateRetryHandler ;
import org.jboss.jbw2012.keynote.utils.ClassLoaderUtils ;
import org.jboss.jbw2012.keynote.utils.xml.XMLUtils ;
import org.jboss.resteasy.client.ClientRequest ;
import org.jboss.resteasy.client.ClientResponse ;
import org.jboss.shrinkwrap.api.ShrinkWrap ;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive ;
import org.jboss.shrinkwrap.api.spec.WebArchive ;
import org.junit.Assert ;

public abstract class BaseResourceTest
{
    protected static final String RESOURCE_PREFIX ;

    @Deployment(testable = false)
    public static EnterpriseArchive createDeployment()
    {
        final WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(CategoryResource.class.getPackage())
                .addPackage(CategoryDTO.class.getPackage())
                .addAsLibraries(getTestLibs("resteasy-links.jar", "jboss-el.jar", "jbwdemo-process.jar", "jbwdemo-services-api.jar"))
                // jBPM5 libraries
                .addAsLibraries(getTestLibs("drools-core.jar", "knowledge-api.jar", "drools-compiler.jar",
                    "drools-persistence-jpa.jar", "knowledge-internal-api.jar",
                    "protobuf-java.jar", "ecj.jar", "mvel2.jar", "antlr-runtime.jar",
                    "jbpm-bpmn2.jar", "jbpm-human-task-core.jar", "jbpm-persistence-jpa.jar",
                    "jbpm-flow.jar", "jbpm-flow-builder.jar", "jbpm-workitems.jar",
                    "jbpm-gwt-shared.jar", "commons-codec.jar"))
                .addPackage(InitialiseData.class.getPackage())
                .addPackage(InitialiseUpdateManager.class.getPackage())
                .addPackage(HibernateRetryHandler.class.getPackage())
                .addClasses(JBWApplication.class)
                .addAsResource("jbossworld-test.xml", "jbossworld.xml")
                .addAsWebInfResource("WEB-INF/beans.xml", "beans.xml")
                .addAsResource(getProcessResource("META-INF/JBPMorm-JPA2.xml"), "META-INF/JBPMorm-JPA2.xml")
                .addAsResource(getProcessResource("META-INF/Taskorm.xml"), "META-INF/Taskorm.xml") ;
        return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
            .addAsModule(testWar)
            .addAsLibraries(getTestLibs("jbwdemo-utils.jar", "jbwdemo-model.jar",
                "jbwdemo-errai-model.jar", "jbwdemo-admin-model.jar")) ;
    }

    @ArquillianResource
    protected URL deploymentUrl ;
    
    private static File[] getTestLibs(final String ... jars)
    {
        return getFiles("target/test-libs/", jars) ;
    }
    
    private static File getProcessResource(final String resource)
    {
        final File[] resources = getFiles("target/jbpm-resources/", resource) ;
        return (resources == null ? null : resources[0]) ;
    }
    
    private static File[] getFiles(final String base, final String ... names)
    {
        if (names != null)
        {
            final File[] files = new File[names.length] ;
            int count = 0 ;
            for(String file: names)
            {
                files[count++] = new File(base + file) ;
            }
            return files ;
        }
        else
        {
            return null ;
        }
    }
    
    protected void runXMLGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams)
        throws Exception
    {
        runXMLGetTest(prefix, path, queryParams, null, 204) ;
    }
    
    protected void runXMLGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams, final String resource)
        throws Exception
    {
        runXMLGetTest(prefix, path, queryParams, resource, 200) ;
    }
    
    protected void runXMLGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams, final String resource, final String mediaType)
        throws Exception
    {
        runXMLGetTest(prefix, path, queryParams, resource, mediaType, 200) ;
    }
    
    protected void runXMLGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams, final String resource, int status)
        throws Exception
    {
        runXMLGetTest(prefix, path, queryParams, resource, MediaType.APPLICATION_XML, status) ;
    }
    
    protected void runXMLGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams, final String resource, final String mediaType, final int status)
        throws Exception
    {
        final ClientRequest request = new ClientRequest(deploymentUrl.toString() + prefix + path) ;
        request.header("Accept", mediaType) ;
        
        if (queryParams != null)
        {
            request.getQueryParameters().putAll(queryParams) ;
        }

        final ClientResponse<String> response = request.get(String.class) ;

        Assert.assertEquals(status, response.getStatus()) ;
        
        if (resource != null)
        {
            final String expected = ClassLoaderUtils.getResourceAsString(resource, getClass(), "UTF-8") ;
            final String xmlResponse = response.getEntity() ;
            
            final boolean equals = XMLUtils.compareXMLContent(expected, xmlResponse) ;
            if (!equals)
            {
                Assert.fail("Invalid response, expected: " + expected + " but received " + xmlResponse) ;
            }
        }
    }
    
    protected void runXMLPostTest(final String prefix, final String path, final String requestResource)
        throws Exception
    {
        runXMLPostTest(prefix, path, requestResource, null, 204) ;
    }
    
    protected void runXMLPostTest(final String prefix, final String path, final String requestResource, final String responseResource)
        throws Exception
    {
        runXMLPostTest(prefix, path, requestResource, responseResource, 200) ;
    }
    
    protected void runXMLPostTest(final String prefix, final String path, final String requestResource, final String responseResource, final int status)
        throws Exception
    {
        runXMLPostTest(prefix, path, requestResource, null, responseResource, status) ;
    }
    
    protected void runXMLPostTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource)
        throws Exception
    {
        runXMLPostTest(prefix, path, requestResource, queryParams, responseResource, 200) ;
    }
    
    protected void runXMLPostTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource, final int status)
        throws Exception
    {
        runXMLPostPutDeleteTest(prefix, path, requestResource, queryParams, responseResource, status, HttpMethod.POST) ;
    }
    
    protected void runXMLPutTest(final String prefix, final String path, final String requestResource)
        throws Exception
    {
        runXMLPutTest(prefix, path, requestResource, null, 204) ;
    }
    
    protected void runXMLPutTest(final String prefix, final String path, final String requestResource, final String responseResource)
        throws Exception
    {
        runXMLPutTest(prefix, path, requestResource, responseResource, 200) ;
    }
    
    protected void runXMLPutTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource)
        throws Exception
    {
        runXMLPutTest(prefix, path, requestResource, queryParams, responseResource, 200) ;
    }
    
    protected void runXMLPutTest(final String prefix, final String path, final String requestResource, final String responseResource, final int status)
        throws Exception
    {
        runXMLPutTest(prefix, path, requestResource, null, responseResource, status) ;
    }
    
    protected void runXMLPutTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource, final int status)
        throws Exception
    {
        runXMLPostPutDeleteTest(prefix, path, requestResource, queryParams, responseResource, status, HttpMethod.PUT) ;
    }
    
    protected void runXMLDeleteTest(final String prefix, final String path, final String requestResource)
        throws Exception
    {
        runXMLDeleteTest(prefix, path, requestResource, null, 204) ;
    }
    
    protected void runXMLDeleteTest(final String prefix, final String path, final String requestResource, final String responseResource)
        throws Exception
    {
        runXMLDeleteTest(prefix, path, requestResource, responseResource, 200) ;
    }
    
    protected void runXMLDeleteTest(final String prefix, final String path, final String requestResource, final String responseResource, final int status)
        throws Exception
    {
        runXMLDeleteTest(prefix, path, requestResource, null, responseResource, status) ;
    }
    
    protected void runXMLDeleteTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource, final int status)
            throws Exception
    {
        runXMLPostPutDeleteTest(prefix, path, requestResource, null, responseResource, status, HttpMethod.DELETE) ;
    }
    
    protected void runXMLPostPutDeleteTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource, final int status, final String method)
        throws Exception
    {
        final ClientRequest request = new ClientRequest(deploymentUrl.toString() + prefix + path) ;
        request.header("Accept", MediaType.APPLICATION_XML) ;
        
        if (queryParams != null)
        {
            request.getQueryParameters().putAll(queryParams) ;
        }

        final String body = ClassLoaderUtils.getResourceAsString(requestResource, getClass(), "UTF-8") ;
        request.body(MediaType.APPLICATION_XML, body) ;
        
        ClientResponse<?> response = null ;
        if (HttpMethod.POST.equals(method))
        {
            response = request.post() ;
        }
        else if (HttpMethod.PUT.equals(method))
        {
            response = request.put() ;
        }
        else if (HttpMethod.DELETE.equals(method))
        {
            response = request.delete() ;
        }
        else
        {
            Assert.fail("Unsupported HttpMethod " + method) ;
        }

        if (responseResource != null)
        {
            final String expected = ClassLoaderUtils.getResourceAsString(responseResource, getClass(), "UTF-8") ;
            final String xmlResponse = response.getEntity(String.class) ;
    
            Assert.assertEquals(status, response.getStatus()) ;
            
            final boolean equals = XMLUtils.compareXMLContent(expected, xmlResponse) ;
            if (!equals)
            {
                Assert.fail("Invalid response, expected: " + expected + " but received " + xmlResponse) ;
            }
        }
    }
    
    protected void runJSONGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams)
            throws Exception
    {
        runJSONGetTest(prefix, path, queryParams, null, 204) ;
    }
    
    protected void runJSONGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams, final String resource)
            throws Exception
    {
        runJSONGetTest(prefix, path, queryParams, resource, 200) ;
    }
    
    protected void runJSONGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams, final String resource, final String mediaType)
            throws Exception
    {
        runJSONGetTest(prefix, path, queryParams, resource, mediaType, 200) ;
    }
    
    protected void runJSONGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams, final String resource, int status)
        throws Exception
    {
        runJSONGetTest(prefix, path, queryParams, resource, MediaType.APPLICATION_JSON, status) ;
    }
        
    protected void runJSONGetTest(final String prefix, final String path, final MultivaluedMap<String, String> queryParams, final String resource, final String mediaType, final int status)
        throws Exception
    {
        final ClientRequest request = new ClientRequest(deploymentUrl.toString() + prefix + path) ;
        request.header("Accept", mediaType) ;
        
        if (queryParams != null)
        {
            request.getQueryParameters().putAll(queryParams) ;
        }

        final ClientResponse<String> response = request.get(String.class) ;
        Assert.assertEquals(status, response.getStatus()) ;
        
        if (resource != null)
        {
            final String expected = ClassLoaderUtils.getResourceAsString(resource, getClass(), "UTF-8").trim() ;
    
            Assert.assertEquals(expected, response.getEntity()) ;
        }
    }
    
    protected void runJSONPostTest(final String prefix, final String path, final String requestResource)
        throws Exception
    {
        runJSONPostTest(prefix, path, requestResource, null, 204) ;
    }
    
    protected void runJSONPostTest(final String prefix, final String path, final String requestResource, final String responseResource)
        throws Exception
    {
        runJSONPostTest(prefix, path, requestResource, responseResource, 200) ;
    }
    
    protected void runJSONPostTest(final String prefix, final String path, final String requestResource, final String responseResource, final int status)
        throws Exception
    {
        runJSONPostTest(prefix, path, requestResource, null, responseResource, status) ;
    }
    
    protected void runJSONPostTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource)
        throws Exception
    {
        runJSONPostTest(prefix, path, requestResource, queryParams, responseResource, 200) ;
    }
    
    protected void runJSONPostTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource, final int status)
        throws Exception
    {
        runJSONPostPutDeleteTest(prefix, path, requestResource, queryParams, responseResource, status, HttpMethod.POST) ;
    }
    
    protected void runJSONPutTest(final String prefix, final String path, final String requestResource)
        throws Exception
    {
        runJSONPutTest(prefix, path, requestResource, null, 204) ;
    }
    
    protected void runJSONPutTest(final String prefix, final String path, final String requestResource, final String responseResource)
        throws Exception
    {
        runJSONPutTest(prefix, path, requestResource, responseResource, 200) ;
    }
    
    protected void runJSONPutTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource)
        throws Exception
    {
        runJSONPutTest(prefix, path, requestResource, queryParams, responseResource, 200) ;
    }
    
    protected void runJSONPutTest(final String prefix, final String path, final String requestResource, final String responseResource, final int status)
        throws Exception
    {
        runJSONPutTest(prefix, path, requestResource, null, responseResource, status) ;
    }
    
    protected void runJSONPutTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource, final int status)
        throws Exception
    {
        runJSONPostPutDeleteTest(prefix, path, requestResource, queryParams, responseResource, status, HttpMethod.PUT) ;
    }
    
    protected void runJSONDeleteTest(final String prefix, final String path, final String requestResource)
        throws Exception
    {
        runJSONDeleteTest(prefix, path, requestResource, null, 204) ;
    }
    
    protected void runJSONDeleteTest(final String prefix, final String path, final String requestResource, final String responseResource)
        throws Exception
    {
        runJSONDeleteTest(prefix, path, requestResource, responseResource, 200) ;
    }
    
    protected void runJSONDeleteTest(final String prefix, final String path, final String requestResource, final String responseResource, final int status)
        throws Exception
    {
        runJSONDeleteTest(prefix, path, requestResource, null, responseResource, status) ;
    }
    
    protected void runJSONDeleteTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource, final int status)
            throws Exception
    {
        runJSONPostPutDeleteTest(prefix, path, requestResource, null, responseResource, status, HttpMethod.DELETE) ;
    }
    
    protected void runJSONPostPutDeleteTest(final String prefix, final String path, final String requestResource, final MultivaluedMap<String, String> queryParams, final String responseResource, final int status, final String method)
        throws Exception
    {
        final ClientRequest request = new ClientRequest(deploymentUrl.toString() + prefix + path) ;
        request.header("Accept", MediaType.APPLICATION_JSON) ;
        
        if (queryParams != null)
        {
            request.getQueryParameters().putAll(queryParams) ;
        }

        final String body = ClassLoaderUtils.getResourceAsString(requestResource, getClass(), "UTF-8") ;
        request.body(MediaType.APPLICATION_JSON, body) ;
        
        ClientResponse<?> response = null ;
        if (HttpMethod.POST.equals(method))
        {
            response = request.post() ;
        }
        else if (HttpMethod.PUT.equals(method))
        {
            response = request.put() ;
        }
        else if (HttpMethod.DELETE.equals(method))
        {
            response = request.delete() ;
        }
        else
        {
            Assert.fail("Unsupported HttpMethod " + method) ;
        }

        if (responseResource != null)
        {
            final String expected = ClassLoaderUtils.getResourceAsString(responseResource, getClass(), "UTF-8").trim() ;
    
            Assert.assertEquals(status, response.getStatus()) ;            
            Assert.assertEquals(expected, response.getEntity(String.class).trim()) ;
        }
    }
    
    static
    {
        final String applicationPath = JBWApplication.class.getAnnotation(ApplicationPath.class).value().substring(1) ;
        RESOURCE_PREFIX = (applicationPath.length() > 0 ? applicationPath + "/" : applicationPath) ;
    }
}