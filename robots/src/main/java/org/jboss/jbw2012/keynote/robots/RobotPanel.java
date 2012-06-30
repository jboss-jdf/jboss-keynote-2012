package org.jboss.jbw2012.keynote.robots;

import java.awt.BorderLayout ;
import java.text.ParseException ;

import javax.swing.BoxLayout ;
import javax.swing.JComponent ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JSlider ;
import javax.swing.JSpinner ;
import javax.swing.JSpinner.DefaultEditor ;
import javax.swing.SpinnerNumberModel ;
import javax.swing.SwingUtilities ;
import javax.swing.event.ChangeEvent ;
import javax.swing.event.ChangeListener ;

class RobotPanel extends JPanel implements RobotListener
{
    private final JLabel threadCount ;
    private final JLabel invocationCount ;
    
    RobotPanel(final Team team, final RobotPanelListener listener)
    {
        setLayout(new BorderLayout()) ;
        
        final JLabel label = new JLabel(team.description()) ;
        add(label, BorderLayout.WEST) ;
        
        final JPanel panel = new JPanel() ;
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)) ;
        final JSlider slider = new JSlider() ;
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent event) {
                listener.updateDelay(slider.getValue()) ;
            }
        }) ;
        listener.updateDelay(slider.getValue()) ;
        panel.add(slider) ;
        
        final JSpinner count = new JSpinner(new SpinnerNumberModel(0, 0, 500, 1)) ;
        count.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent event) {
                try {
                    count.commitEdit() ;
                    listener.updateThreadCount(((Number)count.getValue()).intValue()) ;
                } catch (final ParseException pe) {
                    final JComponent editor = count.getEditor() ;
                    if (editor instanceof DefaultEditor) {
                        ((DefaultEditor)editor).getTextField().setValue(count.getValue()) ;
                    }
                }
            }
        }) ;
        panel.add(count) ;
        
        add(panel, BorderLayout.EAST) ;
        
        final JPanel summaryPanel = new JPanel() ;
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS)) ;
        
        final JPanel threadCountPanel = new JPanel() ;
        threadCountPanel.add(new JLabel("Thread count: ")) ;
        threadCount = new JLabel("0") ;
        threadCountPanel.add(threadCount) ;
        
        summaryPanel.add(threadCountPanel) ;
        
        final JPanel invocationCountPanel = new JPanel() ;
        invocationCountPanel.add(new JLabel("Invocation count: ")) ;
        invocationCount = new JLabel("0") ;
        invocationCountPanel.add(invocationCount) ;

        summaryPanel.add(invocationCountPanel) ;
        
        add(summaryPanel, BorderLayout.SOUTH) ;
    }

    @Override
    public void notifyRobotThreadCount(final int threadCount)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RobotPanel.this.threadCount.setText(Integer.toString(threadCount)) ;
            }
        }) ;
    }

    @Override
    public void notifyRobotInvocationCount(final long invocationCount)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RobotPanel.this.invocationCount.setText(Long.toString(invocationCount)) ;
            }
        }) ;
    }
}
