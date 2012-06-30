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
public class OrderTotalRejectPersistenceTest extends BasePersistenceTest
{
    @Before
    public void initUserAndCatalog()
        throws Exception
    {
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final User buyer = modelUtils.createUser("TestBuyer", Role.BUYER, Team.EAST) ;
                modelUtils.createUser("TestApprover", Role.APPROVER, Team.EAST) ;
        
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
                
                modelUtils.addToCart(buyer, item1) ;
                modelUtils.addToCart(buyer, item2) ;
                modelUtils.addToCart(buyer, item3) ;
                modelUtils.addToCart(buyer, item2) ;
                return null ;
            }
        }) ;
    }
    
    @Test
    public void testOrderTotalsApproval()
        throws Exception
    {
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertNotNull("Users", users) ;
                Assert.assertEquals("User count", 2, users.size()) ;
                
                final User buyerUser = users.get(0) ;
                final User approverUser = users.get(1) ;
                
                final ShoppingCart shoppingCart = buyerUser.getShoppingCart() ;
                
                List<OrderItem> cartOrderItems = shoppingCart.getOrderItems() ;
                Assert.assertNotNull("Cart Order Items", cartOrderItems) ;
                Assert.assertEquals("Cart Order Item count", 3, cartOrderItems.size()) ;
                Assert.assertEquals("Cart Order Item quantity", 1, cartOrderItems.get(0).getQuantity()) ;
                Assert.assertEquals("Cart Order Item quantity", 2, cartOrderItems.get(1).getQuantity()) ;
                Assert.assertEquals("Cart Order Item quantity", 1, cartOrderItems.get(2).getQuantity()) ;
                
                final Order order = modelUtils.checkout(buyerUser) ;
                
                Assert.assertEquals("Buyer total", order.getTotal(), buyerUser.getBuyerTotal().getTotalBought()) ;
                Assert.assertEquals("Approved total", 0, buyerUser.getApprovedTotal().getApproved()) ;
                Assert.assertEquals("Rejected total", 0, buyerUser.getApprovedTotal().getRejected()) ;
                Assert.assertEquals("Approver approved total", 0, approverUser.getApproverTotal().getApproved()) ;
                Assert.assertEquals("Approver rejected total", 0, approverUser.getApproverTotal().getRejected()) ;
                
                modelUtils.assign(approverUser, order) ;
        
                Assert.assertEquals("Buyer total", order.getTotal(), buyerUser.getBuyerTotal().getTotalBought()) ;
                Assert.assertEquals("Approved total", 0, buyerUser.getApprovedTotal().getApproved()) ;
                Assert.assertEquals("Rejected total", 0, buyerUser.getApprovedTotal().getRejected()) ;
                Assert.assertEquals("Approver approved total", 0, approverUser.getApproverTotal().getApproved()) ;
                Assert.assertEquals("Approver rejected total", 0, approverUser.getApproverTotal().getRejected()) ;
                
                final String rejectionMessage = "The rejection message" ;
                
                modelUtils.approve(approverUser, order, false, rejectionMessage) ;
                
                Assert.assertEquals("Buyer total", order.getTotal(), buyerUser.getBuyerTotal().getTotalBought()) ;
                Assert.assertEquals("Approved total", 0, buyerUser.getApprovedTotal().getApproved()) ;
                Assert.assertEquals("Rejected total", order.getTotal(), buyerUser.getApprovedTotal().getRejected()) ;
                Assert.assertEquals("Approver approved total", 0, approverUser.getApproverTotal().getApproved()) ;
                Assert.assertEquals("Approver rejected total", order.getTotal(), approverUser.getApproverTotal().getRejected()) ;
        
                Assert.assertEquals("Rejection message", rejectionMessage, order.getRejectionMessage()) ;
                return null ;
            }
        }) ;
    }
}
