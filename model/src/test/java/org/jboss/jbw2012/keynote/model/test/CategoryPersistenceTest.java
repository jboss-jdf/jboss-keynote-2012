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

import javax.persistence.PersistenceException ;
import javax.transaction.RollbackException ;

import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.test.utils.ModelTestUtils ;
import org.junit.Assert ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
public class CategoryPersistenceTest extends BasePersistenceTest
{
    @Test
    public void createAndDeleteCategories()
        throws Exception
    {
        final List<Category> initCategories = new ArrayList<Category>() ;
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Categories exist in clean database", 0, categories.size()) ;
                
                initCategories.add(modelUtils.createCategory("cat1")) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Incorrect category count", 1, categories.size()) ;
                ModelTestUtils.compareCategories("Failed to create correct Category", initCategories, categories) ;
        
                initCategories.add(modelUtils.createCategory("cat2")) ;
                return null ;
            }
        }) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Incorrect category count", 2, categories.size()) ;
                ModelTestUtils.compareCategories("Failed to create correct Category", initCategories, categories) ;
                
                Category category = initCategories.remove(0) ;
                modelUtils.deleteCategory(category.getId()) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Incorrect category count", 1, categories.size()) ;
                ModelTestUtils.compareCategories("Failed to delete correct Category", initCategories, categories) ;
                
                Category category = initCategories.remove(0) ;
                modelUtils.deleteCategory(category.getId()) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Incorrect category count", 0, categories.size()) ;
                return null ;
            }
        }) ;
    }
    
    @Test
    public void testUniqueName()
        throws Exception
    {
        final List<Category> initCategories = new ArrayList<Category>() ;
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Categories exist in clean database", 0, categories.size()) ;
                
                initCategories.add(modelUtils.createCategory("category")) ;
                return null ;
            }
        }) ;

        try
        {
            executeInATransaction(new Callable<Object>() {
                public Object call() {
                    final List<Category> categories = modelUtils.getCategories() ;
                    Assert.assertEquals("Incorrect category count", 1, categories.size()) ;
                    ModelTestUtils.compareCategories("Failed to create correct Category", initCategories, categories) ;
                    
                    modelUtils.createCategory("category") ;
                    return null ;
                }
            }) ;
            Assert.fail("Expected to receive a RollbackException containing a PersistenceException") ;
        }
        catch (final RollbackException re)
        {
            final Throwable cause = re.getCause() ;
            Assert.assertNotNull("Missing cause", cause) ;
            Assert.assertTrue("Unexpected cause", PersistenceException.class.isAssignableFrom(cause.getClass())) ;
        }

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                modelUtils.deleteCategory(initCategories.get(0).getId()) ;
                return null ;
            }
        }) ;
    }
    
    @Test
    public void createAndDeleteItems()
        throws Exception
    {
        final List<Item> initItems = new ArrayList<Item>() ;
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Items exist in clean database", 0, items.size()) ;
                
                initItems.add(modelUtils.createItem(null, "name1", "imageURL", "thumbnailURL", "description", 100)) ;
                return null ;
            }
        }) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Incorrect item count", 1, items.size()) ;
                ModelTestUtils.compareItems("Failed to create correct Item", initItems, items) ;
                
                initItems.add(modelUtils.createItem(null, "name2", "imageURL", "thumbnailURL", "description", 200)) ;
                return null ;
            }
        }) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Incorrect item count", 2, items.size()) ;
                ModelTestUtils.compareItems("Failed to create correct Item", initItems, items) ;
                
                final Item item = initItems.remove(0) ;
                modelUtils.deleteItem(item.getId()) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Incorrect item count", 1, items.size()) ;
                ModelTestUtils.compareItems("Failed to delete correct Item", initItems, items) ;
                
                Item item = initItems.remove(0) ;
                modelUtils.deleteItem(item.getId()) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Incorrect item count", 0, items.size()) ;
                return null ;
            }
        }) ;
    }
    
    @Test
    public void cascadeDeleteToItems()
        throws Exception
    {
        final List<Item> initItems = new ArrayList<Item>() ;
        final List<Category> initCategories = new ArrayList<Category>() ;
        final Category category  = executeInATransaction(new Callable<Category>() {
            public Category call() {
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Items exist in clean database", 0, items.size()) ;
        
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Categories exist in clean database", 0, categories.size()) ;
                
                final Category category = modelUtils.createCategory("category") ;
                initCategories.add(category) ;
        
                initItems.add(modelUtils.createItem(category, "name1", "imageURL", "thumbnailURL", "description", 100)) ;
                return category ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Incorrect item count", 1, items.size()) ;
                ModelTestUtils.compareItems("Failed to create correct Item", initItems, items) ;
                
                initItems.add(modelUtils.createItem(category, "name2", "imageURL", "thumbnailURL", "description", 200)) ;
                return null ;
            }
        }) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Incorrect item count", 2, items.size()) ;
                ModelTestUtils.compareItems("Failed to create correct Item", initItems, items) ;
                
                category.setItems(initItems) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Incorrect category count", 1, categories.size()) ;
                ModelTestUtils.compareCategories("Failed to create correct Category", initCategories, categories) ;
        
                modelUtils.deleteCategory(category.getId()) ;
                return null ;
            }
        }) ;

        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final List<Category> categories = modelUtils.getCategories() ;
                Assert.assertEquals("Incorrect category count", 0, categories.size()) ;
                
                final List<Item> items = modelUtils.getItems() ;
                Assert.assertEquals("Incorrect item count", 0, items.size()) ;
                return null ;
            }
        }) ;
    }
}
