package org.jboss.jbw2012.keynote.rest.resource.init;

import java.io.InputStream ;
import java.util.List ;

import javax.annotation.PostConstruct ;
import javax.ejb.Singleton ;
import javax.ejb.Startup ;
import javax.inject.Inject ;
import javax.xml.stream.XMLStreamReader ;

import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.utils.ClassLoaderUtils ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.jbw2012.keynote.utils.xml.StreamHelper ;
import org.jboss.jbw2012.keynote.utils.xml.XMLUtils ;

@Singleton
@Startup
public class InitialiseData
{
    @Inject
    private ModelUtils modelUtils ;

    @PostConstruct
    @Transactional
    public void load()
        throws Exception
    {
        final List<Category> categories = modelUtils.getCategories()  ;
        if ((categories == null) || (categories.size() == 0))
        {
            final InputStream is = ClassLoaderUtils.getResourceAsStream("/jbossworld.xml", getClass())  ;
            final XMLStreamReader streamReader = XMLUtils.getXMLStreamReader(is)  ;
            StreamHelper.skipToNextStartElement(streamReader) ;
            final Configuration config = new Configuration(streamReader) ;
            config.persist(modelUtils) ;
        }
    }
}
