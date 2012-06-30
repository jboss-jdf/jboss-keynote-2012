package org.jboss.jbw2012.keynote.errai.events ;

public class UpdateTotal
{
    private final Long userId ;
    private final long ordered ;
    private final long approved ;
    private final long rejected ;

    public UpdateTotal(final Long userId, final long ordered, final long approved, final long rejected)
    {
        this.userId = userId ;
        this.ordered = ordered ;
        this.approved = approved ;
        this.rejected = rejected ;
    }

    public Long getUserId()
    {
        return userId ;
    }

    public long getOrdered()
    {
        return ordered ;
    }

    public long getApproved()
    {
        return approved ;
    }

    public long getRejected()
    {
        return rejected ;
    }
}
