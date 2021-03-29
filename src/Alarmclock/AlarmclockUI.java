package Alarmclock;

import javax.swing.*;
import java.awt.*;

/**
 * UI of Alarmclock
 */

public class AlarmclockUI
{
    private JLabel _timerLabel;
    private JButton _stopButton;
    private JButton _timerButton;
    private JButton _closeButton;
    
    private JFrame _mainframe;
    
    /**
     * Constructor of the UI
     */
    public AlarmclockUI()
    {
        createLabels();
        createButtons();
        createWindow();
        
        initializeWindow();
        disableStopButton();
        _mainframe.setVisible(true);
    }
    
    /**
     * Creates the Labels
     * TODO: This Label
     */
    private void createLabels()
    {
        _timerLabel = new JLabel("<html><span style='font-size:12px'>Total Goaltime:</span></html>");
    }
    
    /**
     * Creates the buttons
     * TODO: Pause/Unpause Buttons?
     */
    private void createButtons()
    {
        _stopButton = new JButton("Stop");
        _timerButton = new JButton("Start Timer!");
        _closeButton = new JButton("Close");
    }
    
    /**
     * Builds the JFrame
     */
    private void createWindow()
    {
        _mainframe = new JFrame();
        _mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Schließt Fenster
        _mainframe.setLocationRelativeTo(null);
        _mainframe.setTitle("Alarmclock");
        _mainframe.setLayout(new GridLayout(2,1));
        
        _mainframe.setSize(400,150);
        
        _mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //_mainframe.setResizable(false);
    }
    
    /**
     * Adds panels onto the _mainframe
     */
    private void initializeWindow()
    {
        _mainframe.add(buildTimerPanel());
        _mainframe.add(buildClosePanel());
    }
    
    /**
     * The upper of the two mainpanels
     * @return timerPanel
     */
    private JPanel buildTimerPanel()
    {
        JPanel timerPanel = new JPanel();
        timerPanel.setLayout(new FlowLayout());
        timerPanel.add(_timerLabel);
        timerPanel.add(_stopButton);
        return timerPanel;
    }
    
    /**
     * The lower of the two mainpanels
     * @return closepanel
     */
    private JPanel buildClosePanel()
    {
        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout());
        closePanel.add(_timerButton);
        closePanel.add(_closeButton);
        
        return closePanel;
    }
    
    /**
     * Returns the stopbutton
     * @return _stopButton
     */
    public JButton getStopButton()
    {
        return _stopButton;
    }

    /**
     * Returns the timerbutton
     * @return _timerButton
     */
    public JButton getTimerButton()
    {
        return _timerButton;
    }
    
    /**
     * Returns the declining _closeButton
     * @return _closeButton
     */
    public JButton getCloseButton()
    {
        return _closeButton;
    }

    /**
     * Returns the mainframe
     * @return _mainframe
     */
    public JFrame getMainframe()
    {
        return _mainframe;
    }
    
    /**
     * Changes the UI depending on how the MP3-Player's status is
     * TODO: Pausebutton???
     * @param status: MP3Player._status, {0,1,2} at the moment
     */
    public void changeMusicStatus(int status)
    {
        assert (status == 0 || status == 1 || status == 2);
        switch (status)
        {
            case 0:
                disableStopButton();
                enableTimerButton();
                _timerLabel.setText("No Timer Set");
                break;
            case 1:
            case 2:
                enableStopButton();
                disableTimerButton();
        }
    }
    
    /**
     * Sets the text for the _timerLabel signaling when the timer is gonna start next
     *
     * @param text: text to be shown on the _timerLabel
     */
    public void setTimerLabelText(String text)
    {
        _timerLabel.setText(text);
    }
    
    /**
     * Closes the UI
     */
    public void close()
    {
        _mainframe.dispose();
    }
    
    /**
     * Enables the _stopButton
     */
    private void enableStopButton()
    {
        _stopButton.setEnabled(true);
    }
    
    /**
     * Disables the _stopbutton
     */
    private void disableStopButton()
    {
        _stopButton.setEnabled(false);
    }
    
    /**
     * Enables the _timerButton
     */
    private void enableTimerButton()
    {
        _timerButton.setEnabled(true);
    }
    
    /**
     * Disables the _timerButton
     */
    private void disableTimerButton()
    {
        _timerButton.setEnabled(false);
    }
    
}
