package org.jboss.jbw2012.keynote.rest.resource.api ;

import java.util.List ;
import java.util.Map ;

import javax.ws.rs.GET ;
import javax.ws.rs.Path ;
import javax.ws.rs.PathParam ;
import javax.ws.rs.Produces ;
import javax.ws.rs.core.MediaType ;

import org.jboss.jbw2012.keynote.rest.resource.dto.CategoryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategoryMapDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategorySummaryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ItemDTO ;
import org.jboss.resteasy.links.AddLinks ;
import org.jboss.resteasy.links.LinkResource ;
import org.jboss.resteasy.links.LinkResources ;

@Path("/category")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface CategoryAPI
{
    public static final String RESOURCE_PREFIX = CategoryAPI.class.getAnnotation(Path.class).value().substring(1);

    @GET
    @AddLinks
    @LinkResources({@LinkResource(CategoryDTO.class), @LinkResource(CategorySummaryDTO.class),
        @LinkResource(CategoryMapDTO.class)})
    public List<CategoryDTO> getCategories() ;

    @GET
    @AddLinks
    @LinkResources({@LinkResource(value=CategoryDTO.class, rel="categoriesSummary"), @LinkResource(value=CategorySummaryDTO.class, rel="categoriesSummary"),
        @LinkResource(value=CategoryMapDTO.class, rel="categoriesSummary")})
    @Produces({"application/vnd.categorysummary+json", "application/vnd.categorysummary+xml"})
    public List<CategorySummaryDTO> getCategoriesSummary() ;

    @GET
    @AddLinks
    @LinkResources({@LinkResource(value=CategoryDTO.class, rel="categoriesMap"), @LinkResource(value=CategorySummaryDTO.class, rel="categoriesMap"),
        @LinkResource(value=CategoryMapDTO.class, rel="categoriesMap")})
    @Produces({"application/vnd.categorymap+json", "application/vnd.categorymap+xml"})
    public Map<String, CategoryMapDTO> getCategoriesMap() ;

    @GET
    @Path("/{id}")
    @AddLinks
    @LinkResources({@LinkResource(CategoryDTO.class), @LinkResource(CategorySummaryDTO.class), @LinkResource(CategoryMapDTO.class)})
    public CategoryDTO getCategory(final @PathParam("id") long id) ;

    @GET
    @Path("/{id}/items")
    @AddLinks
    public List<ItemDTO> getCategoryItems(final @PathParam("id") long id) ;
}
