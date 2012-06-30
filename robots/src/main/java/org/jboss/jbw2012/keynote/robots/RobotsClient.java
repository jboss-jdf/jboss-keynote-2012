package org.jboss.jbw2012.keynote.robots ;

import java.awt.Dimension ;
import java.awt.Toolkit ;

public class RobotsClient
{
    public static void main(final String[] args)
    {
        final String title = "JBossWorld Robots" ;
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", title) ;
        final Application application = new Application(title) ;
        application.pack() ;
        
        final Dimension size = application.getSize() ;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()  ;

        final int x = Math.max(0, (screenSize.width - size.width)/2) ;
        final int y = Math.max(0, (screenSize.height - size.height)/2) ;
        application.setLocation(x, y) ;
        application.setVisible(true) ;
    }
}
