package org.jboss.jbw2012.keynote.process.test ;

import java.io.File ;
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.TreeMap ;
import java.util.concurrent.Callable ;

import javax.inject.Inject ;

import org.drools.runtime.process.ProcessInstance ;
import org.jboss.arquillian.container.test.api.Deployment ;
import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.Role ;
import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.test.utils.ModelTestUtils ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.process.JBWProcessEventListener ;
import org.jboss.jbw2012.keynote.process.LogWorkItemHandler ;
import org.jboss.jbw2012.keynote.process.ProcessController ;
import org.jboss.jbw2012.keynote.process.ProcessStatistics ;
import org.jboss.jbw2012.keynote.process.ProcessStatisticsManager ;
import org.jboss.jbw2012.keynote.process.TaskManager ;
import org.jboss.jbw2012.keynote.process.TaskManagerCallback ;
import org.jboss.jbw2012.keynote.process.TaskManagerConfiguration ;
import org.jboss.jbw2012.keynote.process.test.utils.ProcessTestUtils ;
import org.jboss.jbw2012.keynote.utils.TransactionalEntityManagerUtils ;
import org.jboss.shrinkwrap.api.ShrinkWrap ;
import org.jboss.shrinkwrap.api.spec.WebArchive ;
import org.jbpm.task.Task ;
import org.junit.Assert ;
import org.junit.Before ;

public abstract class BaseProcessControllerTest
{
    private static final String START_NODE = "_0311431E-90BC-49B9-982E-8DDF38F08933" ;
    private static final String APPROVAL_NODE = "_C38E5983-89D6-4C32-BB9D-542E8681873D" ;
    private static final String END_NODE = "_3E200A54-C599-4B55-A8AD-B2007D3D543C" ;
    
    @Deployment
    public static WebArchive createDeployment()
    {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("META-INF/beans.xml", "beans.xml")
                .addAsResource("META-INF/JBPMorm-JPA2.xml")
                .addAsResource("META-INF/Taskorm.xml")
                .addAsResource("jbossworld2012.pkg")
                .addAsResource("jbpm.console.properties")
                .addPackage(TransactionalEntityManagerUtils.class.getPackage())
                .addClasses(ProcessController.class, LogWorkItemHandler.class,
                    JBWProcessEventListener.class, ProcessStatistics.class,
                    ProcessStatisticsManager.class, ProcessTestUtils.class,
                    TaskManager.class, TaskManagerCallback.class, TaskManagerConfiguration.class)
                .addPackage(BaseProcessControllerTest.class.getPackage())
                .addAsLibraries(getTestLibs("jbwdemo-model.jar", "jbwdemo-model-tests.jar",
                    "drools-core.jar", "knowledge-api.jar", "drools-compiler.jar",
                    "drools-persistence-jpa.jar", "knowledge-internal-api.jar",
                    "protobuf-java.jar", "ecj.jar", "mvel2.jar", "antlr-runtime.jar",
                    "jbpm-bpmn2.jar", "jbpm-human-task-core.jar", "jbpm-persistence-jpa.jar",
                    "jbpm-flow.jar", "jbpm-flow-builder.jar", "jbpm-workitems.jar",
                    "jbpm-gwt-shared.jar", "commons-codec.jar"
            )) ;
    }
    
    @Inject ModelTestUtils modelTestUtils ;
    
    @Inject
    protected ModelUtils modelUtils ;
    
    @Inject
    protected ProcessTestUtils processTestUtils ;

    @Inject
    private ProcessController processController ;

    @Inject
    private ProcessStatisticsManager processStatisticsManager ;
    
    private long approverId ;
    private long orderId ;
    
    @Before
    public void prepareProcessTest()
        throws Exception
    {
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final User approver = modelUtils.createUser("TestApprover", Role.APPROVER, Team.EAST) ;
                approverId = approver.getId() ;
                
                final User user = modelUtils.createUser("TestBuyer", Role.BUYER, Team.EAST) ;
    
                final Category category1 = modelUtils.createCategory("category1") ;
                final Item item1 = modelUtils.createItem(category1, "name1", "imageURL", "thumbnailURL", "description", 100) ;
                final Item item2 = modelUtils.createItem(category1, "name2", "imageURL", "thumbnailURL", "description", 200) ;
                
                List<Item> initItems = new ArrayList<Item>() ;
                initItems.add(item1) ;
                initItems.add(item2) ;
                initItems.add(modelUtils.createItem(category1, "name3", "imageURL", "thumbnailURL", "description", 300)) ;
                category1.setItems(initItems) ;
                
                modelUtils.addToCart(user, item1) ;
                modelUtils.addToCart(user, item2) ;
                modelUtils.addToCart(user, item2) ;
                
                final Order order = modelUtils.checkout(user) ;
                orderId = order.getId() ;
                return null ;
            }
        }) ;
    }
    
    protected void startProcess()
        throws Exception
    {
        ProcessStatistics processStatistics = processStatisticsManager.getProcessStatistics() ;
        Assert.assertNotNull("Null process statistics returned", processStatistics) ;
        Assert.assertEquals("Invalid process started count", 0, processStatistics.getProcessStartedCount()) ;
        Assert.assertEquals("Invalid process completed count", 0, processStatistics.getProcessCompletedCount()) ;
        Assert.assertNull("Invalid node count", processStatistics.getNodeCounts()) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final Order order = modelUtils.getOrder(orderId) ;
                processController.startProcess(order) ;
                return null ;
            }
        }) ;

        processStatistics = processStatisticsManager.getProcessStatistics() ;
        Assert.assertNotNull("Null process statistics returned", processStatistics) ;
        Assert.assertEquals("Invalid process started count", 1, processStatistics.getProcessStartedCount()) ;
        Assert.assertEquals("Invalid process completed count", 0, processStatistics.getProcessCompletedCount()) ;
        final Map<String, Long> nodeCounts = processStatistics.getNodeCounts() ;
        Assert.assertNotNull("Invalid node count", nodeCounts) ;
        Assert.assertEquals("Invalid node count size", 2, nodeCounts.size()) ;
        final Map<String, Long> expectedNodeCounts = createNodeCounts(1, 1) ;
        Assert.assertEquals("Invalid node counts", expectedNodeCounts, nodeCounts) ;
    }

    protected void verifyFirstOpenOrder()
        throws Exception
    {
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final User approver = modelUtils.getUser(approverId) ;
                final Order order = modelUtils.getOrder(orderId) ;
                
                final List<Order> openOrders = processController.getOpenOrders(approver, 0, 10)  ;
                Assert.assertNotNull("Open orders", openOrders) ;
                Assert.assertEquals("Open order count", 1, openOrders.size()) ;
                final Order openOrder = openOrders.get(0) ;
                Assert.assertEquals("Order id", order.getId(), openOrder.getId()) ;
                return null ;
            }
        }) ;

        final ProcessStatistics processStatistics = processStatisticsManager.getProcessStatistics() ;
        Assert.assertNotNull("Null process statistics returned", processStatistics) ;
        Assert.assertEquals("Invalid process started count", 1, processStatistics.getProcessStartedCount()) ;
        Assert.assertEquals("Invalid process completed count", 0, processStatistics.getProcessCompletedCount()) ;
        final Map<String, Long> nodeCounts = processStatistics.getNodeCounts() ;
        Assert.assertNotNull("Invalid node count", nodeCounts) ;
        Assert.assertEquals("Invalid node count size", 2, nodeCounts.size()) ;
        final Map<String, Long> expectedNodeCounts = createNodeCounts(1, 1) ;
        Assert.assertEquals("Invalid node counts", expectedNodeCounts, nodeCounts) ;
    }
    
    protected void assignFromOpenOrder()
        throws Exception
    {
        final Long taskId = executeInATransaction(new Callable<Long>() {
            public Long call() {
                final User approver = modelUtils.getUser(approverId) ;
                
                final List<Order> openOrders = processController.getOpenOrders(approver, 0, 10)  ;
                Assert.assertNotNull("Open orders", openOrders) ;
                Assert.assertEquals("Invalid order count", 1, openOrders.size()) ;
                final Order openOrder = openOrders.get(0) ;
                processController.assign(approver, openOrder) ;
                return openOrder.getTaskId() ;
            }
        }) ;
        
        Assert.assertNotNull("Invalid task id", taskId) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final User approver = modelUtils.getUser(approverId) ;
                
                final List<Order> openOrders = processController.getOpenOrders(approver, 0, 10)  ;
                Assert.assertNull("Open orders", openOrders) ;
                
                final Task task = processController.getTask(taskId) ;
                Assert.assertEquals("Task assignment", approver.getId().toString(), task.getTaskData().getActualOwner().getId()) ;
                return null ;
            }
        }) ;
        
        final ProcessStatistics processStatistics = processStatisticsManager.getProcessStatistics() ;
        Assert.assertNotNull("Null process statistics returned", processStatistics) ;
        Assert.assertEquals("Invalid process started count", 1, processStatistics.getProcessStartedCount()) ;
        Assert.assertEquals("Invalid process completed count", 0, processStatistics.getProcessCompletedCount()) ;
        final Map<String, Long> nodeCounts = processStatistics.getNodeCounts() ;
        Assert.assertNotNull("Invalid node count", nodeCounts) ;
        Assert.assertEquals("Invalid node count size", 2, nodeCounts.size()) ;
        final Map<String, Long> expectedNodeCounts = createNodeCounts(1, 1) ;
        Assert.assertEquals("Invalid node counts", expectedNodeCounts, nodeCounts) ;
    }
    
    protected void assignNextOrder()
        throws Exception
    {
        final Long taskId = executeInATransaction(new Callable<Long>() {
            public Long call() {
                final User approver = modelUtils.getUser(approverId) ;
                
                Order nextOrder = processController.getNextOrder(approver)  ;
                Assert.assertNotNull("Next orders", nextOrder) ;
                return nextOrder.getTaskId() ;
            }
        }) ;
        
        Assert.assertNotNull("Invalid task id", taskId) ;
        
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final User approver = modelUtils.getUser(approverId) ;
                
                final List<Order> openOrders = processController.getOpenOrders(approver, 0, 10)  ;
                Assert.assertNull("Open orders", openOrders) ;
                
                final Task task = processController.getTask(taskId) ;
                Assert.assertEquals("Task assignment", approver.getId().toString(), task.getTaskData().getActualOwner().getId()) ;
                return null ;
            }
        }) ;
        
        final ProcessStatistics processStatistics = processStatisticsManager.getProcessStatistics() ;
        Assert.assertNotNull("Null process statistics returned", processStatistics) ;
        Assert.assertEquals("Invalid process started count", 1, processStatistics.getProcessStartedCount()) ;
        Assert.assertEquals("Invalid process completed count", 0, processStatistics.getProcessCompletedCount()) ;
        final Map<String, Long> nodeCounts = processStatistics.getNodeCounts() ;
        Assert.assertNotNull("Invalid node count", nodeCounts) ;
        Assert.assertEquals("Invalid node count size", 2, nodeCounts.size()) ;
        final Map<String, Long> expectedNodeCounts = createNodeCounts(1, 1) ;
        Assert.assertEquals("Invalid node counts", expectedNodeCounts, nodeCounts) ;
    }

    protected void approveOrder(final boolean approved)
        throws Exception
    {
        executeInATransaction(new Callable<Object>() {
            public Object call() {
                final User approver = modelUtils.getUser(approverId) ;
                final Order order = modelUtils.getOrder(orderId) ;
                
                final Task task = processController.getTask(order.getTaskId()) ;
                Assert.assertEquals("Task approval", approver.getId().toString(), task.getTaskData().getActualOwner().getId()) ;
                
                order.setApproved(approved) ;
                
                final HashMap<String, Object> map = new HashMap<String, Object>() ;
                map.put("approved", order.getApproved()) ;
                
                processController.complete(approver, order, map) ;
              
                final ProcessInstance processInstance = processController.getProcessInstance(order.getProcessInstanceId())  ;
                Assert.assertNull("Process terminated", processInstance) ;
                return null ;
            }
        }) ;

        final ProcessStatistics processStatistics = processStatisticsManager.getProcessStatistics() ;
        Assert.assertNotNull("Null process statistics returned", processStatistics) ;
        Assert.assertEquals("Invalid process started count", 1, processStatistics.getProcessStartedCount()) ;
        Assert.assertEquals("Invalid process completed count", 1, processStatistics.getProcessCompletedCount()) ;
        final Map<String, Long> nodeCounts = processStatistics.getNodeCounts() ;
        Assert.assertNotNull("Invalid node count", nodeCounts) ;
        Assert.assertEquals("Invalid node count size", 3, nodeCounts.size()) ;
        final Map<String, Long> expectedNodeCounts = createNodeCounts(1, 0, 1) ;
        Assert.assertEquals("Invalid node counts", expectedNodeCounts, nodeCounts) ;
    }
    
    private <T> T executeInATransaction(final Callable<T> callable)
        throws Exception
    {
        processTestUtils.startTransaction() ;
        boolean rollback = true ;
        try
        {
            final T result = callable.call() ;
            rollback = false ;
            return result ;
        }
        finally
        {
            processTestUtils.endTransaction(rollback) ;
        }
    }
    
    private static Map<String, Long> createNodeCounts(final long ... counts)
    {
        final Map<String, Long> nodeCounts = new TreeMap<String, Long>() ;
        if (counts != null)
        {
            if (counts.length > 0)
            {
                nodeCounts.put(START_NODE, counts[0]) ;
            }
            if (counts.length > 1)
            {
                nodeCounts.put(APPROVAL_NODE, counts[1]) ;
            }
            if (counts.length > 2)
            {
                nodeCounts.put(END_NODE, counts[2]) ;
            }
        }
        return nodeCounts ;
    }

    private static File[] getTestLibs(String ... jars)
    {
        if (jars != null)
        {
            final File[] testLibs = new File[jars.length] ;
            int count = 0 ;
            for(String jar: jars)
            {
                testLibs[count++] = new File("target/test-libs/" + jar) ;
            }
            return testLibs ;
        }
        else
        {
            return null ;
        }
    }
}