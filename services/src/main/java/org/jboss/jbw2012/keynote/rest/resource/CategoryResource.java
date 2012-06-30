package org.jboss.jbw2012.keynote.rest.resource ;

import java.util.List ;
import java.util.Map ;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.UriInfo ;

import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.rest.resource.api.CategoryAPI ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategoryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategoryMapDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategorySummaryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ItemDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.JaxRSUtils ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.resteasy.spi.NoLogWebApplicationException ;

@Transactional
@Singleton
public class CategoryResource implements CategoryAPI
{
    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private DTOUtils dtoUtils ;
    
    @Context
    private UriInfo uriInfo ;

    public List<CategoryDTO> getCategories()
    {
        return dtoUtils.getCategories(modelUtils.getCategories(), uriInfo.getBaseUri()) ;
    }

    public List<CategorySummaryDTO> getCategoriesSummary()
    {
        return dtoUtils.getCategoriesSummary(modelUtils.getCategories()) ;
    }

    public Map<String, CategoryMapDTO> getCategoriesMap()
    {
        return dtoUtils.getCategoriesMap(modelUtils.getCategories(), uriInfo.getBaseUri()) ;
    }

    public CategoryDTO getCategory(final long id)
    {
        return dtoUtils.getCategory(getVerifiedCategory(id), uriInfo.getBaseUri()) ;
    }

    public List<ItemDTO> getCategoryItems(final long id)
    {
        return dtoUtils.getItems(getVerifiedCategory(id).getItems(), uriInfo.getBaseUri()) ;
    }
    
    private Category getVerifiedCategory(final long id)
    {
        final Category category = modelUtils.getCategory(id) ;
        if (category != null)
        {
            return category ;
        }
        else
        {
            throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_CATEGORY)) ;
        }
    }
}
