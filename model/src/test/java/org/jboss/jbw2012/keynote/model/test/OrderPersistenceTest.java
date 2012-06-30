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
import org.jboss.jbw2012.keynote.model.Order ;
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
public class OrderPersistenceTest extends BasePersistenceTest
{
    @Before
    public void initUserAndCatalog()
        throws Exception
    {
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final User user = modelUtils.createUser("TestBuyer", Role.BUYER, Team.EAST) ;
        
                final Category category1 = modelUtils.createCategory("category1") ;
                final Item item1 = modelUtils.createItem(category1, "name1", "imageURL", "thumbnailURL", "description", 100) ;
                final Item item2 = modelUtils.createItem(category1, "name2", "imageURL", "thumbnailURL", "description", 200) ;
                
                List<Item> initItems = new ArrayList<Item>() ;
                initItems.add(item1) ;
                initItems.add(item2) ;
                initItems.add(modelUtils.createItem(category1, "name3", "imageURL", "thumbnailURL", "description", 300)) ;
                category1.setItems(initItems) ;
                
                final Category category2 = modelUtils.createCategory("category2") ;
                final Item item3 = modelUtils.createItem(category2, "name4", "imageURL", "thumbnailURL", "description", 400) ;
                
                initItems = new ArrayList<Item>() ;
                initItems.add(item3) ;
                initItems.add(modelUtils.createItem(category2, "name2", "imageURL", "thumbnailURL", "description", 100)) ;
                category2.setItems(initItems) ;
                
                modelUtils.addToCart(user, item1) ;
                modelUtils.addToCart(user, item2) ;
                modelUtils.addToCart(user, item3) ;
                modelUtils.addToCart(user, item2) ;
                return null ;
            }
        }) ;
    }
    
    @Test
    public void testOrderPersistence()
        throws Exception
    {
        final Long buyerUserId = executeInATransaction(new Callable<Long>() {
            public Long call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertNotNull("Users", users) ;
                Assert.assertEquals("User count", 1, users.size()) ;
                
                return users.get(0).getId() ;
            }
        }) ;
                
        final Long shoppingCartId = executeInATransaction(new Callable<Long>() {
            public Long call() throws Exception {
                final User buyerUser = modelUtils.getUser(buyerUserId) ;
                return buyerUser.getShoppingCart().getId() ;
            }
        }) ;
                
        final Long orderId = executeInATransaction(new Callable<Long>() {
            public Long call() throws Exception {
                final User buyerUser = modelUtils.getUser(buyerUserId) ;
                final ShoppingCart shoppingCart = modelUtils.getShoppingCart(shoppingCartId) ;
                final List<OrderItem> cartOrderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Cart Order Items", cartOrderItems) ;
                Assert.assertEquals("Cart Order Item count", 3, cartOrderItems.size()) ;
                Assert.assertEquals("Cart Order Item quantity", 1, cartOrderItems.get(0).getQuantity()) ;
                Assert.assertEquals("Cart Order Item quantity", 2, cartOrderItems.get(1).getQuantity()) ;
                Assert.assertEquals("Cart Order Item quantity", 1, cartOrderItems.get(2).getQuantity()) ;
                
                return modelUtils.checkout(buyerUser).getId() ;
            }
        }) ;
                
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final ShoppingCart shoppingCart = modelUtils.getShoppingCart(shoppingCartId) ;
                final Order order = modelUtils.getOrder(orderId) ;
                final List<OrderItem> cartOrderItems  = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Cart Order Items", cartOrderItems) ;
                Assert.assertEquals("Cart Order Item count", 0, cartOrderItems.size()) ;
                
                final List<OrderItem> orderItems = order.getOrderItems() ;
                Assert.assertNotNull("Order Items", orderItems) ;
                Assert.assertEquals("Order Item count", 3, orderItems.size()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(0).getQuantity()) ;
                Assert.assertEquals("Order Item quantity", 2, orderItems.get(1).getQuantity()) ;
                Assert.assertEquals("Order Item quantity", 1, orderItems.get(2).getQuantity()) ;
        
                List<Order> orders = modelUtils.getOrders(0, 10) ;
                Assert.assertNotNull("Orders", orders) ;
                Assert.assertEquals("Order count", 1, orders.size()) ;
                Assert.assertEquals("Order id", order.getId(), orders.get(0).getId()) ;
                return null ;
            }
        }) ;
    }
}
