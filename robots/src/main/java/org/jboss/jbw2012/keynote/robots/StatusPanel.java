package org.jboss.jbw2012.keynote.robots;

import java.awt.BorderLayout ;

import javax.swing.JLabel ;
import javax.swing.JPanel ;

class StatusPanel extends JPanel
{
    private static final String MESSAGE = "Status: " ;
    private final JLabel status ;
    
    StatusPanel()
    {
        setLayout(new BorderLayout()) ;
        status = new JLabel(MESSAGE) ;
        add(status, BorderLayout.CENTER) ;
    }
    
    void setStatus(final String status)
    {
        this.status.setText(MESSAGE + status) ;
    }
}
