package org.jboss.jbw2012.keynote.robots ;

import java.awt.Container ;
import java.awt.Dimension ;
import java.awt.Point ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.util.Set ;
import java.util.TreeSet ;
import java.util.concurrent.Executor ;
import java.util.concurrent.Executors ;
import java.util.concurrent.atomic.AtomicReference ;

import javax.swing.BoxLayout ;
import javax.swing.JButton ;
import javax.swing.JComboBox ;
import javax.swing.JDialog ;
import javax.swing.JFrame ;
import javax.swing.JLabel ;
import javax.swing.JMenu ;
import javax.swing.JMenuBar ;
import javax.swing.JMenuItem ;
import javax.swing.JOptionPane ;
import javax.swing.JPanel ;
import javax.swing.SwingUtilities ;
import javax.swing.border.TitledBorder ;
import javax.swing.plaf.basic.BasicComboBoxEditor ;

class Application extends JFrame
{
    private final JMenuItem connect ;
    private final JMenuItem disconnect ;
    private final JMenuItem start ;
    private final JMenuItem stop ;
    private final StatusPanel statusPanel ;

    private Set<String> serverURLs = new TreeSet<String>() ;
    
    private AtomicReference<Status> status = new AtomicReference<Status>(Status.DISCONNECTED) ;
    
    private final Executor executor = Executors.newSingleThreadExecutor() ;
    
    private final Robots robots = new Robots() ;
    
    Application(final String title)
    {
        super(title) ;
        setDefaultCloseOperation(EXIT_ON_CLOSE) ;
        
        final JMenuBar menuBar = new JMenuBar() ;
        
        final JMenu file = new JMenu("File") ;
        file.setMnemonic('F') ;
        
        connect = new JMenuItem("Connect ...") ;
        connect.setMnemonic('C') ;
        connect.addActionListener(new ConnectListener()) ;
        file.add(connect) ;
        
        disconnect = new JMenuItem("Disconnect") ;
        disconnect.setMnemonic('D') ;
        disconnect.addActionListener(new DisconnectListener()) ;
        file.add(disconnect) ;
        
        start = new JMenuItem("Start Robots") ;
        start.setMnemonic('R') ;
        start.addActionListener(new StartListener()) ;
        file.add(start) ;
        
        stop = new JMenuItem("Stop Robots") ;
        stop.setMnemonic('S') ;
        stop.addActionListener(new StopListener()) ;
        file.add(stop) ;
        
        menuBar.add(file) ;
        
        setJMenuBar(menuBar) ;
        
        final Container contentPane = getRootPane().getContentPane() ;
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS)) ;
        
        final JPanel buyers = new JPanel() ;
        buyers.setBorder(new TitledBorder("Buyers")) ;
        buyers.setLayout(new BoxLayout(buyers, BoxLayout.Y_AXIS)) ;
        
        buyers.add(getRobotPanel(Team.EAST, robots.getEastBuyer())) ;
        buyers.add(getRobotPanel(Team.WEST, robots.getWestBuyer())) ;
        
        final JPanel approvers = new JPanel() ;
        approvers.setBorder(new TitledBorder("Approvers")) ;
        approvers.setLayout(new BoxLayout(approvers, BoxLayout.Y_AXIS)) ;
        
        approvers.add(getRobotPanel(Team.EAST, robots.getEastApprover())) ;
        approvers.add(getRobotPanel(Team.WEST, robots.getWestApprover())) ;
        
        final JPanel vps = new JPanel() ;
        vps.setBorder(new TitledBorder("VPs")) ;
        vps.setLayout(new BoxLayout(vps, BoxLayout.Y_AXIS)) ;
        
        vps.add(getRobotPanel(Team.VP, robots.getVP())) ;

        statusPanel = new StatusPanel() ;
        
        contentPane.add(buyers) ;
        contentPane.add(approvers) ;
        contentPane.add(vps) ;
        contentPane.add(statusPanel) ;

        setResizable(false) ;
        
        refreshStatus() ;
        
        serverURLs.add("http://localhost:8080/jbossworld") ;
    }
    
    private RobotPanel getRobotPanel(final Team team, final Robot robot)
    {
        final RobotPanel robotPanel = new RobotPanel(team, robot) ;
        robot.setRobotListener(robotPanel) ;
        return robotPanel ;
    }
    
    private void refreshStatus()
    {
        final Status currentStatus = status.get() ;
        updateMenuItems(currentStatus) ;
        updateStatus(currentStatus) ;
    }
    
    private void updateMenuItems(final Status currentStatus)
    {
        connect.setEnabled(Status.DISCONNECTED == currentStatus) ;
        disconnect.setEnabled(Status.CONNECTED == currentStatus) ;
        start.setEnabled(Status.CONNECTED == currentStatus) ;
        stop.setEnabled(Status.STARTED == currentStatus) ;
    }
    
    private void updateStatus(final Status currentStatus)
    {
        switch(currentStatus)
        {
            case CONNECTED:
                statusPanel.setStatus("connected") ;
                break ;
            case DISCONNECTED:
                statusPanel.setStatus("disconnected") ;
                break ;
            case STOPPING:
                statusPanel.setStatus("stopping") ;
                break ;
            case STARTED:
                statusPanel.setStatus("running") ;
                break ;
        }
    }

    public class ConnectListener implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent event)
        {
            final JDialog dialog = new JDialog(Application.this, true) ;
            
            final Container contentPane = dialog.getRootPane().getContentPane() ;
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS)) ;
            
            final JPanel serverBase = new JPanel() ;
            serverBase.setLayout(new BoxLayout(serverBase, BoxLayout.X_AXIS)) ;
            serverBase.add(new JLabel("Server Base URL: ")) ;
            final JComboBox urlCombo = new JComboBox(serverURLs.toArray()) ;
            urlCombo.setEditable(true) ;
            urlCombo.setEditor(new BasicComboBoxEditor()) ;
            serverBase.add(urlCombo) ;
            
            contentPane.add(serverBase) ;
            
            final JPanel buttons = new JPanel() ;
            
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent event) {
                    dialog.setVisible(false) ;
                }
            } ;
            
            final JButton okButton = new JButton("OK") ;
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent event) {
                    final Object selectedItem = urlCombo.getSelectedItem() ;
                    executor.execute(new Runnable() {
                        public void run() {
                            final String serverURL = selectedItem.toString() ;
                            if (robots.validateServerURL(serverURL)) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        serverURLs.add(serverURL) ;
                                        robots.setServerURL(serverURL) ;
                                        dialog.setVisible(false) ;
                                        status.compareAndSet(Status.DISCONNECTED, Status.CONNECTED) ;
                                        refreshStatus() ;
                                    }
                                }) ;
                            } else {
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        JOptionPane.showMessageDialog(dialog, "Failed to validate Server URL") ;
                                    }
                                }) ;
                            }
                        }
                    }) ;
                }
            }) ;
            buttons.add(okButton) ;

            final JButton cancelButton = new JButton("Cancel") ;
            cancelButton.addActionListener(closeListener) ;
            buttons.add(cancelButton) ;
            
            contentPane.add(buttons) ;
            
            dialog.pack() ;
            final Dimension size = dialog.getSize() ;
            final Point ownerLocation = Application.this.getLocation() ;
            final Dimension ownerSize = Application.this.getSize() ;
            final int x = Math.max(0, ownerLocation.x + (ownerSize.width - size.width)/2) ;
            final int y = Math.max(0, ownerLocation.y + (ownerSize.height - size.height)/2) ;
            dialog.setLocation(x, y) ;
            dialog.setVisible(true) ;
        }
    }
    
    public class DisconnectListener implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent event)
        {
            status.compareAndSet(Status.CONNECTED, Status.DISCONNECTED) ;
            refreshStatus() ;
        }
    }
    
    public class StartListener implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent event)
        {
            status.compareAndSet(Status.CONNECTED, Status.STARTED) ;
            robots.start() ;
            refreshStatus() ;
        }
    }
    
    public class StopListener implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent event)
        {
            status.compareAndSet(Status.STARTED, Status.STOPPING) ;
            robots.stop() ;
            executor.execute(new Runnable() {
                public void run() {
                    robots.waitUntilStopped() ;
                    status.compareAndSet(Status.STOPPING, Status.CONNECTED) ;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            refreshStatus() ;
                        }
                    }) ;
                }
            }) ;
            refreshStatus() ;
        }
    }
}
