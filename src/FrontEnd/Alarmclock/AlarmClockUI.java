package FrontEnd.Alarmclock;

import javax.swing.*;
import java.awt.*;

/**
 * UI of AlarmClock
 */

public class AlarmClockUI
{
    private JLabel _timerLabel;
    private JButton _stopButton;
    private JButton _timerButton;
    private JButton _closeButton;

    private JFrame _mainframe;

    /**
     * Constructor of the UI
     */
    public AlarmClockUI() {
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
    private void createLabels() {
        _timerLabel = new JLabel("<html><span style='font-size:12px'>No song set:</span></html>");
    }

    /**
     * Creates the buttons
     * TODO: Pause/Unpause Buttons?
     */
    private void createButtons() {
        _stopButton = new JButton("Stop");
        _timerButton = new JButton("Start Timer!");
        _closeButton = new JButton("Close");
    }

    /**
     * Builds the JFrame
     */
    private void createWindow() {
        _mainframe = new JFrame();
        _mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _mainframe.setLocationRelativeTo(null);
        _mainframe.setTitle("Alarm Clock");
        _mainframe.setLayout(new GridLayout(2, 1));

        _mainframe.setSize(400, 150);

        _mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //_mainframe.setResizable(false);
    }

    /**
     * Adds panels onto the _mainframe
     */
    private void initializeWindow() {
        _mainframe.add(buildTimerPanel());
        _mainframe.add(buildClosePanel());
    }

    /**
     * The upper of the two main panels
     *
     * @return timerPanel
     */
    private JPanel buildTimerPanel() {
        JPanel timerPanel = new JPanel();
        timerPanel.setLayout(new FlowLayout());
        timerPanel.add(_timerLabel);
        timerPanel.add(_stopButton);
        return timerPanel;
    }

    /**
     * The lower of the two main panels
     *
     * @return closePanel
     */
    private JPanel buildClosePanel() {
        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout());
        closePanel.add(_timerButton);
        closePanel.add(_closeButton);

        return closePanel;
    }

    /**
     * Returns the stop button
     *
     * @return _stopButton
     */
    public JButton getStopButton() {
        return _stopButton;
    }

    /**
     * Returns the timer button
     *
     * @return _timerButton
     */
    public JButton getTimerButton() {
        return _timerButton;
    }

    /**
     * Returns the declining close button
     *
     * @return _closeButton
     */
    public JButton getCloseButton() {
        return _closeButton;
    }

    /**
     * Changes the UI depending on how the MP3-Player's status is
     * TODO: PauseButton???
     *
     * @param status: BackEnd.MP3Player._status, {0,1,2} at the moment
     */
    public void changeMusicStatus(int status) {
        assert (status == 0 || status == 1 || status == 2);
        switch (status) {
            case 0:
                disableStopButton();
                enableTimerButton();
                setTimerLabelText("No Timer Set");
                break;
            case 1:
            case 2:
                enableStopButton();
                disableTimerButton();
        }
    }

    /**
     * Sets the text for the _timerLabel signaling when the timer will start next
     *
     * @param text: text to be shown on the _timerLabel
     */
    public void setTimerLabelText(String text) {
        assert text != null : "text is null!";

        _timerLabel.setText("<html><span style='font-size:11px'>" + text + "</span></html>");
    }

    /**
     * Returns the top-left point of the UI, to place other UIs on top of it
     *
     * @return the top-left point of the UI
     */
    public Point getPosition() {
        return _mainframe.getLocation();
    }

    /**
     * Closes the UI
     */
    public void close() {
        _mainframe.dispose();
    }

    /**
     * Enables the _stopButton
     */
    private void enableStopButton() {
        _stopButton.setEnabled(true);
    }

    /**
     * Disables the _stopButton
     */
    private void disableStopButton() {
        _stopButton.setEnabled(false);
    }

    /**
     * Enables the _timerButton
     */
    private void enableTimerButton() {
        _timerButton.setEnabled(true);
    }

    /**
     * Disables the _timerButton
     */
    private void disableTimerButton() {
        _timerButton.setEnabled(false);
    }

}
