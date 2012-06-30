package org.jboss.jbw2012.keynote.rest.resource.dto.errors ;


public enum ErrorType
{
    INVALID_CATEGORY("Category does not exist"),
    INVALID_CREDENTIALS("Invalid credentials"),
    INVALID_ITEM("Item does not exist"),
    INVALID_ORDER("Order does not exist"),
    INVALID_ROLE("Invalid role; must be APPROVER or VP"),
    INVALID_STORE_STATUS("Invalid Store status"),
    INVALID_TEAM("Invalid team specified for user"),
    INVALID_USER("User does not exist"),
    MISSING_ROLE("No role specified in request"),
    ORDER_ALREADY_ASSIGNED("Order already assigned"),
    ORDER_ASSIGNED_TO_OTHER("Order is not assigned to current user"),
    STORE_CLOSED("Unfortunately the store is now closed"),
    STORE_VIP_ONLY("Unfortunately the store is closed, but will open soon", STORE_CLOSED.name()),
    UNASSIGNED_ORDER("Order is not assigned"),
    UNSUPPORTED_ROLE("Unsupported role"),
    USER_ALREADY_ASSIGNEE("Cannot assign more than one Order") ;

    private final String message ;
    private final String type ;
    
    private ErrorType(final String message)
    {
        this(message, null) ;
    }
    
    private ErrorType(final String message, final String type)
    {
        this.message = message ;
        this.type = type ;
    }
    
    public String getMessage()
    {
        return message ;
    }
    
    public String getType()
    {
        return (type != null ? type : name()) ;
    }
}