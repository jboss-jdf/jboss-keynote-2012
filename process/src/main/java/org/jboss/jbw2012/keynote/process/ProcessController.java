package org.jboss.jbw2012.keynote.process;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.concurrent.locks.Lock ;
import java.util.concurrent.locks.ReentrantLock ;

import javax.annotation.PostConstruct ;
import javax.annotation.PreDestroy ;
import javax.annotation.Resource ;
import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.persistence.EntityManager ;
import javax.persistence.EntityManagerFactory ;
import javax.persistence.PersistenceUnit ;
import javax.persistence.Query ;
import javax.transaction.TransactionManager ;
import javax.transaction.UserTransaction ;

import org.drools.KnowledgeBase ;
import org.drools.WorkingMemory;
import org.drools.agent.KnowledgeAgent ;
import org.drools.agent.KnowledgeAgentConfiguration ;
import org.drools.agent.KnowledgeAgentFactory ;
import org.drools.builder.KnowledgeBuilder ;
import org.drools.builder.KnowledgeBuilderFactory ;
import org.drools.builder.ResourceType ;
import org.drools.command.impl.CommandBasedStatefulKnowledgeSession;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.event.AgendaEventListener ;
import org.drools.event.DefaultAgendaEventListener ;
import org.drools.event.RuleFlowGroupActivatedEvent;
import org.drools.event.process.ProcessEventListener ;
import org.drools.impl.EnvironmentFactory ;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.io.ResourceChangeScanner ;
import org.drools.io.ResourceChangeScannerConfiguration ;
import org.drools.io.ResourceFactory ;
import org.drools.persistence.jpa.JPAKnowledgeService ;
import org.drools.runtime.Environment ;
import org.drools.runtime.EnvironmentName ;
import org.drools.runtime.StatefulKnowledgeSession ;
import org.drools.runtime.process.ProcessInstance ;
import org.drools.runtime.process.WorkItemManager ;
import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.utils.TransactionalEntityManagerUtils ;
import org.jboss.logging.Logger ;
import org.jbpm.integration.console.shared.GuvnorConnectionUtils ;
import org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler ;
import org.jbpm.task.AccessType ;
import org.jbpm.task.Content ;
import org.jbpm.task.Group ;
import org.jbpm.task.OrganizationalEntity ;
import org.jbpm.task.Status ;
import org.jbpm.task.Task ;
import org.jbpm.task.TaskData ;
import org.jbpm.task.service.ContentData ;
import org.jbpm.task.service.TaskService ;
import org.jbpm.task.service.TaskServiceSession ;
import org.jbpm.task.service.UserGroupCallback ;
import org.jbpm.task.service.UserGroupCallbackManager ;
import org.jbpm.task.service.local.LocalHumanTaskService ;
import org.jbpm.task.service.local.LocalTaskService ;
import org.jbpm.task.utils.ContentMarshallerContext ;
import org.jbpm.task.utils.ContentMarshallerHelper ;

@Singleton
public class ProcessController
{
    private Logger logger = Logger.getLogger(getClass()) ;
    
    @Resource(mappedName="java:/TransactionManager")
    private TransactionManager tm ;
    @Inject
    private UserTransaction ut ;
    
    @Inject
    private UserGroupCallback userGroupCallback ;
    @Inject
    private ProcessEventListener processEventListener ;
    
    @Inject
    private ModelUtils modelUtils ;
    
    @PersistenceUnit(unitName="org.jbpm.persistence.jpa")
    private EntityManagerFactory emf ;
    @Inject
    private TransactionalEntityManagerUtils transactionalEntityManagerUtils ;
    
    private KnowledgeBase knowledgeBase ;
    
    private StatefulKnowledgeSession session ;
    
    private LocalTaskService localTaskService ;
    
    private final ContentMarshallerContext marshallerContext = new ContentMarshallerContext() ;
    
    private List<Status> readyStatus ;
    
    private boolean startedScannerService ;
    private boolean startedNotifierService ;

    private final Lock processLock = new ReentrantLock(true) ;
    
    @Inject
    private TaskManager taskManager ;

    @PostConstruct
    public void initialise()
    {
        readyStatus = Arrays.asList(Status.Ready) ;
        
        knowledgeBase = createKnowledgeBase() ;
        
        final Environment env = EnvironmentFactory.newEnvironment() ;
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf) ;
        env.set(EnvironmentName.TRANSACTION_MANAGER, tm) ;
        env.set(EnvironmentName.TRANSACTION, ut);
        
        session = JPAKnowledgeService.newStatefulKnowledgeSession(knowledgeBase, null, env) ;
        final AgendaEventListener agendaEventListener = new DefaultAgendaEventListener() {
            public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event, WorkingMemory workingMemory) {
                workingMemory.fireAllRules();
            }
        } ;
        ((StatefulKnowledgeSessionImpl)  ((KnowledgeCommandContext) ((CommandBasedStatefulKnowledgeSession) session)
            .getCommandService().getContext()).getStatefulKnowledgesession() )
            .session.addEventListener(agendaEventListener) ;

        final TaskService taskService = LocalHumanTaskService.getService(env) ;
        
        final GenericHTWorkItemHandler humanTaskHandler = new GenericHTWorkItemHandler(session) ;
        localTaskService = new JBWLocalTaskService(taskService) ;
        humanTaskHandler.setClient(localTaskService) ;
        humanTaskHandler.setLocal(true) ;
        humanTaskHandler.setIpAddress("127.0.0.1") ;
        humanTaskHandler.setPort(9999) ;
        final WorkItemManager workItemManager = session.getWorkItemManager()  ;
        workItemManager.registerWorkItemHandler("Human Task", humanTaskHandler) ;
        workItemManager.registerWorkItemHandler("Log", new LogWorkItemHandler());
        
        UserGroupCallbackManager.getInstance().setCallback(userGroupCallback) ;
        
        session.addEventListener(processEventListener) ;
        
        taskManager.setTaskManagerCallback(new ProcessControllerCallback()) ;
        
        initialiseTransactionalResource(taskService) ;
    }
    
    private void initialiseTransactionalResource(final TaskService taskService)
    {
        int count = 0 ;
        final int increment = 10 ;
        
        try
        {
            tm.begin() ;
            try
            {
                final TaskServiceSession taskServiceSession = taskService.createSession()  ;
                taskServiceSession.addUser(new org.jbpm.task.User("Administrator")) ;
                taskServiceSession.dispose() ;
                
                do
                {
                    final List<User> users = modelUtils.getUsersWithAssignedOrders(count, increment) ;
                    if (users == null)
                    {
                        break ;
                    }
                    for(User assignee: users)
                    {
                        final Order order = assignee.getAssignedOrder() ;
                        if (order != null)
                        {
                            final Long taskId = order.getTaskId() ;
                            if (taskId != null)
                            {
                                final Task task = getTask(taskId) ;
                                if (task != null)
                                {
                                    taskManager.startTask(task) ;
                                }
                            }
                        }
                    }
                    if (users.size() < increment)
                    {
                        break ;
                    }
                }
                while(true) ;
            }
            finally
            {
                tm.commit() ;
            }
        }
        catch (final Exception ex)
        {
            logger.info("Failed to retrieve initial task state") ;
        }
    }
    
    @PreDestroy
    public void destroy()
    {
        taskManager.shutdown() ;
        try
        {
            session.dispose() ;
        }
        finally
        {
            shutdownGuvnor() ;
        }
    }
    
    public void lock()
    {
        processLock.lock() ;
    }
    
    public void unlock()
    {
        processLock.unlock() ;
    }
    
    public void startProcess(final Order order)
    {
        final Map<String, Object> params = new HashMap<String, Object>() ;
        params.put("orderId", Long.toString(order.getId())) ;
        params.put("team", order.getBuyer().getTeam().name()) ;
        params.put("total", order.getTotal() / 100.00F) ;
        params.put("order", order);
        final ProcessInstance processInstance = session.createProcessInstance("org.jboss.jbw2012.keynote", params)  ;
        final long processInstanceId = processInstance.getId()  ;
        order.setProcessInstanceId(processInstanceId) ;
        session.startProcessInstance(processInstanceId) ;
    }
    
    // Uses a separate Entity Manager in order to be able to restrict result list
    public List<Order> getOpenOrders(final User user, final int startPosition, final int maxResults)
    {
        final List<Long> taskIds = getTaskList(user.getTeam().name(), "en-UK", startPosition, maxResults) ;
        if ((taskIds == null) || (taskIds.size() == 0))
        {
            return null ;
        }
        final List<Order> orders = new ArrayList<Order>() ;
        for(Long taskId: taskIds)
        {
            final Task task = localTaskService.getTask(taskId) ;
            if (task != null)
            {
                final Order order = getOrder(task) ;
                if (order != null)
                {
                    orders.add(order) ;
                }
            }
        }
        return orders ;
    }
    
    // Uses a separate Entity Manager in order to be able to restrict result list
    public Order getNextOrder(final User user)
    {
        final List<Long> taskIds = getTaskList(user.getTeam().name(), "en-UK", 0, 1) ;
        if ((taskIds == null) || (taskIds.size() == 0))
        {
            return null ;
        }
        else
        {
            final Long taskId = taskIds.get(0) ;
            final String userId = user.getId().toString() ;

            localTaskService.claim(taskId, userId) ;
            localTaskService.start(taskId, userId) ;
            
            final Task task = localTaskService.getTask(taskId) ;
            taskManager.startTask(task) ;
            
            final Order order = getOrder(task) ;
            modelUtils.assign(user, order) ;
            return order ;
        }
    }
    
    public void assign(final User user, final Order order)
    {
        final long taskId = order.getTaskId() ;
        final String userId = user.getId().toString() ;
        
        localTaskService.claim(taskId, userId) ;
        localTaskService.start(taskId, userId) ;
        
        final Task task = localTaskService.getTask(taskId) ;
        taskManager.startTask(task) ;
        
        modelUtils.assign(user, order) ;
    }
    
    public void release(final User user, final Order order)
    {
        final long taskId = order.getTaskId() ;
        final String userId = user.getId().toString() ;
        
        localTaskService.release(taskId, userId) ;
        modelUtils.release(user, order) ;
    }
    
    public void complete(final User user, final Order order, final Map<String, Object> properties)
    {
        final long taskId = order.getTaskId() ;
        final Task task = localTaskService.getTask(taskId) ;
        taskManager.completeTask(task) ;
        
        localTaskService.complete(taskId, user.getId().toString(),
            ContentMarshallerHelper.marshal(properties, marshallerContext, null)) ;
    }

    public void approve(final User user, final Order order, final boolean approved, final String message)
    {
        modelUtils.approve(user, order, approved, message) ;
        final Map<String, Object> params = new HashMap<String, Object>() ;
        params.put("approved", approved) ;
        complete(user, order, params) ;
    }

    public void signOff(final User user, final Order order, final boolean signedOff, final String message)
    {
        modelUtils.signOff(user, order, signedOff, message) ;
        final Map<String, Object> params = new HashMap<String, Object>() ;
        params.put("signedOff", signedOff) ;
        complete(user, order, params) ;
    }
    
    public Task getTask(final long taskId)
    {
        return localTaskService.getTask(taskId) ;
    }

    public ProcessInstance getProcessInstance(final long processInstanceId)
    {
        return session.getProcessInstance(processInstanceId) ;
    }
    
    private KnowledgeBase createKnowledgeBase()
    {
        KnowledgeBase kbase = null ;
        final GuvnorConnectionUtils guvnorUtils = new GuvnorConnectionUtils() ;
        if (guvnorUtils.guvnorExists())
        {
            try
            {
                final ResourceChangeScanner scannerService = ResourceFactory.getResourceChangeScannerService()  ;
                final ResourceChangeScannerConfiguration scannerConfig = scannerService.newResourceChangeScannerConfiguration() ;
                scannerConfig.setProperty("drools.resource.scanner.interval", "10") ;
                scannerService.configure(scannerConfig) ;
                scannerService.start() ;
                startedScannerService = true ;
                
                ResourceFactory.getResourceChangeNotifierService().start() ;
                startedNotifierService = true ;
                
                final KnowledgeAgentConfiguration agentConf = KnowledgeAgentFactory.newKnowledgeAgentConfiguration() ;
                agentConf.setProperty("drools.agent.newInstance", "false") ;
                final KnowledgeAgent knowledgeAgent = KnowledgeAgentFactory.newKnowledgeAgent("Guvnor default", agentConf) ;
                knowledgeAgent.applyChangeSet(ResourceFactory.newReaderResource(guvnorUtils.createChangeSet())) ;
                kbase = knowledgeAgent.getKnowledgeBase() ;
                logger.info("Connected to Guvnor.") ;
            }
            catch (final Throwable t)
            {
                logger.error("Could not load processes from Guvnor: " + t.getMessage(), t) ;
                shutdownGuvnor() ;
            }
        }
        else
        {
            logger.warn("Could not connect to Guvnor.") ;
        }

        // Create a kbase if we couldn't do that with Guvnor
        if (kbase == null)
        {
            final KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder() ;
            builder.add(ResourceFactory.newClassPathResource("jbossworld2012.pkg"), ResourceType.PKG) ;
            kbase = builder.newKnowledgeBase() ;
            logger.info("Using local process definition.") ;
        }

        return kbase ;
    }
    
    private void shutdownGuvnor()
    {
        if (startedNotifierService)
        {
            ResourceFactory.getResourceChangeNotifierService().stop() ;
            startedNotifierService = false ;
        }
        if (startedScannerService)
        {
            ResourceFactory.getResourceChangeScannerService().stop() ;
            startedScannerService = false ;
        }
    }
    
    private Order getOrder(final Task task)
    {
        final TaskData taskData = task.getTaskData() ;
        if (taskData.getDocumentAccessType() == AccessType.Inline)
        {
            final Content content = localTaskService.getContent(taskData.getDocumentContentId())  ;
            @SuppressWarnings("unchecked")
            final Map<String, Object> parameters = (Map<String, Object>)ContentMarshallerHelper.unmarshall(taskData.getDocumentType(), content.getContent(), marshallerContext, null) ;
            try
            {
                final String orderId = (String) parameters.get("orderId") ;
                final long id = Long.parseLong(orderId) ;
                return modelUtils.getOrder(id) ;
            }
            catch (final NumberFormatException nfe) {} // Fall through
        }
        return null ;
    }
    
    private List<Long> getTaskList(final String groupId, final String language, final int startPosition, final int maxResults)
    {
        final EntityManager em = transactionalEntityManagerUtils.getEntityManager(emf) ;
        
        final Query query = em.createNamedQuery("TaskIdsAssignedAsPotentialOwnerByStatusByGroup") ;
        query.setParameter("groupId", groupId) ;
        query.setParameter("status", readyStatus) ;

        query.setFirstResult(startPosition) ;
        query.setMaxResults(maxResults) ;
        
        @SuppressWarnings("unchecked")
        final List<Long> response = query.getResultList() ;
        return response ;
    }

    private class JBWLocalTaskService extends LocalTaskService
    {
        public JBWLocalTaskService(final TaskService taskService)
        {
            super(taskService) ;
        }
        
        @Override
        public void addTask(Task task, ContentData content)
        {
            super.addTask(task, content) ;
            final Order order = getOrder(task) ;
            order.setTaskId(task.getId()) ;
            @SuppressWarnings("unchecked")
            final Map<String, Object> parameters = (Map<String, Object>)ContentMarshallerHelper.unmarshall(task.getTaskData().getDocumentType(), content.getContent(), marshallerContext, null) ;
            final Boolean exceedsLimit = (Boolean)parameters.get("exceedsLimit") ;
            if (exceedsLimit != null)
            {
                order.setExceedsLimit(exceedsLimit) ;
            }
            final List<OrganizationalEntity> potentialOwners = task.getPeopleAssignments().getPotentialOwners() ;
            if ((potentialOwners != null) && (potentialOwners.size() == 1))
            {
                final OrganizationalEntity organizationalEntity = potentialOwners.get(0) ;
                if (organizationalEntity instanceof Group)
                {
                    final Group group = (Group)organizationalEntity ;
                    order.setTaskTeam(Team.valueOf(group.getId())) ;
                }
            }
        }
    }
    
    private class ProcessControllerCallback implements TaskManagerCallback
    {
        public void expireTasks(final List<Long> expiredTasks)
        {
            for (Long taskId: expiredTasks)
            {
                try
                {
                    tm.begin() ;
                    try
                    {
                        final Task task = getTask(taskId) ;
                        if (task != null)
                        {
                            final Order order = getOrder(task) ;
                            if ((order != null) && taskId.equals(order.getTaskId()))
                            {
                                release(order.getAssignee(), order) ;
                            }
                        }
                    }
                    finally
                    {
                        tm.commit() ;
                    }
                }
                catch (final Exception ex)
                {
                    logger.info("Failed to release task " + taskId) ;
                }
            }
        }
    }
}
