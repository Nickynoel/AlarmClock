package MusicArea;

import javax.swing.*;
import java.awt.*;

/**
 * UI of MusicArea
 */

public class MusicAreaUI
{
    private JLabel _timeLabel;
    private JTextField _timeField;
    private JLabel _songLabel;
    private JButton _songButton;
    private JButton _confirmButton;
    private JButton _backButton;
    
    private JPanel _topPanel;
    private JPanel _centerPanel;
    private JPanel _botPanel;
    
    private JDialog _dialog;
    
    /**
     * Initializing the UI
     */
    public MusicAreaUI()
    {
        createLabels();
        createTextField();
        createButtons();
        createPanels();
        
        createWindow();
        initializeWindow();
    }
    
    /**
     * Initialization of the Labels
     */
    private void createLabels()
    {
        _timeLabel = new JLabel("Time in minutes/for timer to go:");
        _songLabel = new JLabel("Songtitle: ");
    }
    
    /**
     * Creates the buttons
     */
    private void createButtons()
    {
        _confirmButton = new JButton("confirm");
        _confirmButton.setEnabled(false);
        
        _songButton = new JButton("Load Song!");
        
        _backButton = new JButton("back");
    }
    
    /**
     * Creates the textfield
     */
    private void createTextField()
    {
        _timeField = new JTextField("", 5); //number of colomns or dimension
    }
    
    /**
     * Creates the panels
     */
    private void createPanels()
    {
        _topPanel = new JPanel();
        _topPanel = generateToppanel();
        
        _centerPanel = new JPanel();
        _centerPanel = generateCenterpanel();
        
        _botPanel = new JPanel();
        _botPanel = generateBotpanel();
    }
    
    /**
     * Initialization of the _topPanel, containing the Label describing the action
     * @return the _topPanel
     */
    private JPanel generateToppanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        JPanel panel1 = new JPanel();
        panel1.add(_timeLabel);
        panel.add(panel1);
        
        JPanel panel2 = new JPanel();
        panel2.add(_timeField);
        panel.add(panel2);
        
        return panel;
    }
    
    /**
     * Initialization of the _centerPanel, containing the textfield for the input
     * @return the _centerPanel
     */
    private JPanel generateCenterpanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        JPanel panel1 = new JPanel();
        panel1.add(_songLabel);
        panel.add(panel1);
        
        JPanel panel2 = new JPanel();
        panel2.add(_songButton);
        panel.add(panel2);
        
        return panel;
    }
    
    /**
     * Initialization of the _botPanel, containing the two buttons for confirming and declining
     * @return the _botPanel
     */
    private JPanel generateBotpanel()
    {
        JPanel panel1 = new JPanel();
        panel1.add(_confirmButton);
        JPanel panel2 = new JPanel();
        panel2.add(_backButton);
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(panel1);
        panel.add(panel2);
        
        return panel;
    }
    
    /**
     * Builds the JDialog
     */
    private void createWindow()
    {
        _dialog = new JDialog();
        _dialog.setTitle("Set Timer");
        _dialog.setLayout(new BorderLayout());
        
        _dialog.setSize(350,200);
        
        _dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        _dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        _dialog.setResizable(false);
    }
    
    /**
     * insert the panels onto the _mainFrame
     */
    private void initializeWindow()
    {
        _dialog.add(_topPanel,BorderLayout.PAGE_START);
        _dialog.add(_centerPanel);
        _dialog.add(_botPanel,BorderLayout.PAGE_END);
    }
    
    /**
     * Sets the UI to a certain point
     * @param p: Point for positioning the JDialog
     */
    public void setPosition(Point p)
    {
        _dialog.setLocation(p);
    }
    
    /**
     * Returns the timefield of the UI
     * @return _timeField
     */
    public JTextField getTextfield()
    {
        return _timeField;
    }
    
    /**
     * Sets the text of the next song onto the label and removes the path
     * @param title: Title of the chosen song
     */
    public void setSongText(String title)
    {
        String song = title;
        while(song.contains("/")|| song.contains("\\"))
        {
            int index = Math.max(song.indexOf("/"),song.indexOf("\\"));
            song = song.substring(index+1);
        }
        _songLabel.setText("Songtitle: "+ song);
    }
    
    /**
     * Returns the confirmationbutton
     * @return _confirmButton
     */
    public JButton getConfirmButton()
    {
        return _confirmButton;
    }
    
    /**
     * Enables the _confirmButton
     */
    public void enableConfirmButton()
    {
        _confirmButton.setEnabled(true);
    }
    
    /**
     * Disables the _confirmButton
     */
    public void disableConfirmButton()
    {
        _confirmButton.setEnabled(false);
    }
    
    /**
     * Returns the declining _backButton
     * @return _backButton
     */
    public JButton getBackButton()
    {
        return _backButton;
    }
    
    /**
     * Sets the UI's visibility to true
     */
    public void showUI()
    {
        _dialog.setVisible(true);
    }
    
    /**
     * Closes the UI
     */
    public void close()
    {
        _dialog.dispose();
    }
    
    /**
     * GetA for the _songButton
     * @return _songButton
     */
    public JButton getSongButton()
    {
        return _songButton;
    }
}