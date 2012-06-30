package org.jboss.jbw2012.keynote.robots.rest;

import org.jboss.jbw2012.keynote.rest.resource.api.CategoryAPI ;
import org.jboss.jbw2012.keynote.rest.resource.api.ItemAPI ;
import org.jboss.jbw2012.keynote.rest.resource.api.OrderAPI ;
import org.jboss.jbw2012.keynote.rest.resource.api.ShoppingCartAPI ;
import org.jboss.jbw2012.keynote.rest.resource.api.StatisticsAPI ;
import org.jboss.jbw2012.keynote.rest.resource.api.UserAPI ;
import org.jboss.resteasy.client.ProxyFactory ;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin ;
import org.jboss.resteasy.spi.ResteasyProviderFactory ;


public class RestClient
{
    private static final RestClient INSTANCE = new RestClient() ;
    
    static
    {
        final ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance()  ;
        RegisterBuiltin.register(providerFactory) ;
        providerFactory.addClientErrorInterceptor(new RestClientErrorInterceptor()) ;
    }
    
    public static RestClient getRestClient()
    {
        return INSTANCE ;
    }

    public CategoryAPI getCategoryAPI(final String serverURL)
    {
        return ProxyFactory.create(CategoryAPI.class, serverURL) ;
    }

    public ItemAPI getItemAPI(final String serverURL)
    {
        return ProxyFactory.create(ItemAPI.class, serverURL) ;
    }

    public OrderAPI getOrderAPI(final String serverURL)
    {
        return ProxyFactory.create(OrderAPI.class, serverURL) ;
    }

    public ShoppingCartAPI getShoppingCartAPI(final String serverURL)
    {
        return ProxyFactory.create(ShoppingCartAPI.class, serverURL) ;
    }

    public StatisticsAPI getStatisticsAPI(final String serverURL)
    {
        return ProxyFactory.create(StatisticsAPI.class, serverURL) ;
    }

    public UserAPI getUserAPI(final String serverURL)
    {
        return ProxyFactory.create(UserAPI.class, serverURL) ;
    }
}
