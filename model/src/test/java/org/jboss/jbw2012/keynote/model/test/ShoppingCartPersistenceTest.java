/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jbw2012.keynote.model.test ;

import java.util.ArrayList ;
import java.util.List ;
import java.util.concurrent.Callable ;

import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.OrderItem ;
import org.jboss.jbw2012.keynote.model.Role ;
import org.jboss.jbw2012.keynote.model.ShoppingCart ;
import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.junit.Assert ;
import org.junit.Before ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
public class ShoppingCartPersistenceTest extends BasePersistenceTest
{
    @Before
    public void initUserAndCatalog()
        throws Exception
    {
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                modelUtils.createUser("TestBuyer", Role.BUYER, Team.EAST) ;
        
                final Category category1 = modelUtils.createCategory("category1") ;
        
                List<Item> initItems = new ArrayList<Item>() ;
                initItems.add(modelUtils.createItem(category1, "name1", "imageURL", "thumbnailURL", "description", 100)) ;
                initItems.add(modelUtils.createItem(category1, "name2", "imageURL", "thumbnailURL", "description", 200)) ;
                initItems.add(modelUtils.createItem(category1, "name3", "imageURL", "thumbnailURL", "description", 300)) ;
                category1.setItems(initItems) ;
        
                final Category category2 = modelUtils.createCategory("category2") ;
                
                initItems = new ArrayList<Item>() ;
                initItems.add(modelUtils.createItem(category2, "name4", "imageURL", "thumbnailURL", "description", 400)) ;
                initItems.add(modelUtils.createItem(category2, "name2", "imageURL", "thumbnailURL", "description", 100)) ;
                category2.setItems(initItems) ;
                return null ;
            }
        }) ;
    }
    
    @Test
    public void testModificationsToShoppingCart()
        throws Exception
    {
        final User user = executeInATransaction(new Callable<User>() {
            public User call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertNotNull("Users", users) ;
                Assert.assertEquals("User count", 1, users.size()) ;
                
                return users.get(0) ;
            }
        }) ;
        
        final List<Category> categories = executeInATransaction(new Callable<List<Category>>() {
            public List<Category> call() throws Exception {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertNotNull("Categories", categories) ;
                Assert.assertEquals("Category count", 2, categories.size()) ;
                return categories ;
            }
        }) ;
        
        final List<Item> cat1Items = executeInATransaction(new Callable<List<Item>>() {
            public List<Item> call() throws Exception {
                
                final List<Item> cat1Items = categories.get(0).getItems() ;
                Assert.assertNotNull("Category1 items", cat1Items) ;
                Assert.assertEquals("Item count", 3, cat1Items.size()) ;
                return cat1Items ;
            }
        }) ;
                
        final List<Item> cat2Items = executeInATransaction(new Callable<List<Item>>() {
            public List<Item> call() throws Exception {
                final List<Item> cat2Items = categories.get(1).getItems() ;
                Assert.assertNotNull("Category2 items", cat2Items) ;
                Assert.assertEquals("Item count", 2, cat2Items.size()) ;
                return cat2Items ;
            }
        }) ;
                
        final ShoppingCart shoppingCart = executeInATransaction(new Callable<ShoppingCart>() {
            public ShoppingCart call() throws Exception {
                final ShoppingCart shoppingCart = user.getShoppingCart() ;
                Assert.assertEquals("Buyer not set", user, shoppingCart.getBuyer()) ;
                
                modelUtils.addToCart(user, cat1Items.get(0)) ;
                return shoppingCart ;
            }
        }) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 1, orderItems.size()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(0).getQuantity()) ;
        
                modelUtils.addToCart(user, cat1Items.get(1)) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 2, orderItems.size()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(0).getQuantity()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(1).getQuantity()) ;
                
                modelUtils.addToCart(user, cat1Items.get(0)) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 2, orderItems.size()) ;
                Assert.assertEquals("Order Item quantity", 2, orderItems.get(0).getQuantity()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(1).getQuantity()) ;
                
                modelUtils.addToCart(user, cat2Items.get(0)) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 3, orderItems.size()) ;
                Assert.assertEquals("Order Item quantity", 2, orderItems.get(0).getQuantity()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(1).getQuantity()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(2).getQuantity()) ;
        
                modelUtils.removeFromCart(user, cat1Items.get(1)) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 2, orderItems.size()) ;
                Assert.assertEquals("Order Item quantity", 2, orderItems.get(0).getQuantity()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(1).getQuantity()) ;
        
                modelUtils.removeFromCart(user, cat1Items.get(0)) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 2, orderItems.size()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(0).getQuantity()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(1).getQuantity()) ;
        
                modelUtils.removeFromCart(user, cat1Items.get(0)) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 1, orderItems.size()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(0).getQuantity()) ;
        
                modelUtils.removeFromCart(user, cat2Items.get(0)) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 0, orderItems.size()) ;
        
                modelUtils.removeFromCart(user, cat1Items.get(0)) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 0, orderItems.size()) ;
                return null ;
            }
        }) ;
    }
}
