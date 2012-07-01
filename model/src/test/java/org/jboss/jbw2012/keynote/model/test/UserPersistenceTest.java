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
import org.jboss.jbw2012.keynote.model.Role ;
import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.test.utils.ModelTestUtils ;
import org.junit.Assert ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
public class UserPersistenceTest extends BasePersistenceTest
{
    @Test
    public void createAndDeleteUsers()
        throws Exception
    {
        final List<User> initUsers = new ArrayList<User>() ;
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertEquals("Users exist in clean database", 0, users.size()) ;
                
                initUsers.add(modelUtils.createUser("user1", Role.APPROVER, Team.EAST)) ;
                return null ;
            }
        }) ;
    
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertEquals("Incorrect user count", 1, users.size()) ;
                ModelTestUtils.compareUsers("Failed to create correct User",initUsers, users) ;
        
                initUsers.add(modelUtils.createUser("user2", Role.BUYER, Team.WEST)) ;
                return null ;
            }
        }) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertEquals("Incorrect user count", 2, users.size()) ;
                ModelTestUtils.compareUsers("Failed to create correct User",initUsers, users) ;
                
                final User user = initUsers.remove(0) ;
                modelUtils.deleteUser(user.getId()) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertEquals("Incorrect user count", 1, users.size()) ;
                ModelTestUtils.compareUsers("Failed to create correct User",initUsers, users) ;
                
                final User user = initUsers.remove(0) ;
                modelUtils.deleteUser(user.getId()) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertEquals("Incorrect user count", 0, users.size()) ;
                return null ;
            }
        }) ;
    }
    
    @Test
    public void testDuplicateNames()
        throws Exception
    {
        final List<User> initUsers = new ArrayList<User>() ;
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertEquals("Users exist in clean database", 0, users.size()) ;
                
                initUsers.add(modelUtils.createUser("user", Role.APPROVER, Team.EAST)) ;
                return null ;
            }
        }) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertEquals("Incorrect user count", 1, users.size()) ;
                ModelTestUtils.compareUsers("Failed to create correct User",initUsers, users) ;
                
                initUsers.add(modelUtils.createUser("user", Role.BUYER, Team.WEST)) ;
                return null ;
            }
        }) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() throws Exception {
                final List<User> users = modelUtils.getUsers(0, 10) ;
                Assert.assertEquals("Incorrect user count", 2, users.size()) ;
                ModelTestUtils.compareUsers("Failed to create correct User",initUsers, users) ;
                return null ;
            }
        }) ;
    }
}
