package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;
import java.util.ArrayList ;
import java.util.List ;

import javax.persistence.CascadeType ;
import javax.persistence.Column ;
import javax.persistence.Entity ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.OneToMany ;

@Entity
public class Category implements Serializable
{
    private Long id ;
    private String name ;
    private List<Item> items ;
    
    public Category()
    {
    }

    public Category(final String name)
    {
        this.name = name ;
        items = new ArrayList<Item>() ;
    }
    
    @Id
    @GeneratedValue
    public Long getId()
    {
        return id ;
    }

    public void setId(final Long id)
    {
        this.id = id ;
    }
    
    @Column(unique=true)
    public String getName()
    {
        return name ;
    }
    
    public void setName(final String name)
    {
        this.name = name ;
    }
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "category")
    public List<Item> getItems()
    {
        return items ;
    }
    
    public void setItems(final List<Item> items)
    {
        this.items = items ;
    }
}
