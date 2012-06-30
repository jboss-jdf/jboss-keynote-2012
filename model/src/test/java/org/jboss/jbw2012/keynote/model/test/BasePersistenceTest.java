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

import java.util.concurrent.Callable ;

import javax.inject.Inject ;

import org.jboss.arquillian.container.test.api.Deployment ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.test.utils.ModelTestUtils ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.utils.TransactionalEntityManagerUtils ;
import org.jboss.shrinkwrap.api.ShrinkWrap ;
import org.jboss.shrinkwrap.api.spec.JavaArchive ;

public abstract class BasePersistenceTest
{
    @Deployment
    public static JavaArchive createDeployment()
    {
        return ShrinkWrap
                .create(JavaArchive.class, "test.jar")
                .addPackage(TransactionalEntityManagerUtils.class.getPackage())
                .addPackage(User.class.getPackage())
                .addPackage(ModelTestUtils.class.getPackage())
                .addPackage(ModelUtils.class.getPackage())
                .addClass(BasePersistenceTest.class)
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml") ;
    }

    @Inject
    protected ModelTestUtils modelTestUtils ;
    
    @Inject
    protected ModelUtils modelUtils ;
    
    protected <T> T executeInATransaction(final Callable<T> callable)
        throws Exception
    {
        modelTestUtils.startTransaction() ;
        boolean rollback = true ;
        try
        {
            final T result = callable.call() ;
            rollback = false ;
            return result ;
        }
        finally
        {
            modelTestUtils.endTransaction(rollback) ;
        }
    }
}
