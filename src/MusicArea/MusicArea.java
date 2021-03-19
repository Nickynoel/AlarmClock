package MusicArea;

import Dateieinleser.DateiEinleser;
import MP3Player.MP3Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;

public class MusicArea
{
    private MusicAreaUI _ui;
    private MP3Player _player;
    //    private String _activeSong;
    
    private PropertyChangeSupport _support; //basically observable just newer
    
    public MusicArea(MP3Player player, JFrame frame)
    {
        _player = player;
        //        _activeSong = MP3Player.DEFAULTSONG;
        _support = new PropertyChangeSupport(this);
        _ui = new MusicAreaUI();
        _ui.setPosition(new Point(frame.getLocation().x, frame.getLocation().y)); //Sets position based on the mainframe
        _ui.setSongText(MP3Player.DEFAULTSONG);
        addListener();
    }
    
    /**
     * Adds the listeners of the components of AddAreaUI:
     * BackButton.actionlistener: just closes
     * TextField.keylistener: checks the validity of the entry
     * Textfield.actionlistener: Shortcut to confirmButton
     * SongButton.actionlistener: Opens Filechooser to pick song
     * ConfirmButton.actionlistener: Processing of the entry
     */
    private void addListener()
    {
        //Leaves if just going back
        _ui.getBackButton().addActionListener(event ->
        {
            _ui.close();
        });
        
        //If the text gets changed it checks it anew and controls the availability of the button
        _ui.getTextfield().addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                super.keyReleased(e);
                String tmp = _ui.getTextfield().getText();
                if (isValid(tmp))
                {
                    _ui.enableConfirmButton();
                }
                else
                {
                    _ui.disableConfirmButton();
                }
                
            }
        });
        
        //Shortcut for enter-key if the _confirmButton is enabled
        _ui.getTextfield().addActionListener(event ->
        {
            _ui.getConfirmButton().doClick(); //doClick() automatically checks "isEnabled()"
        });
        
        //Opens a JFileChooser when _stopButton is clicked
        _ui.getSongButton().addActionListener(event ->
        {
            File bilddaten = DateiEinleser.liesBilddaten();
            //if (bilddaten != null)
            //{
            //_activeSong = ;
            try
            {
                String newSong = bilddaten.getPath();
                _player.setSong(newSong);
                System.out.println(newSong);
                _ui.setSongText(newSong);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            
            //_ui.setSongText(_activeSong);
            //            }
        });
        
        //Actual action if the _confirmButton gets used and processes the entry
        _ui.getConfirmButton().addActionListener(event ->
        {
            String tmp = _ui.getTextfield().getText();
            try
            {
                int number = Integer.parseInt(tmp);
                _ui.close();
                _player.addToQueue(number);
                confirmChange(1);
            }
            catch (NumberFormatException e) //should never happen, cause the textfield-keylistener checks this
            {
                JOptionPane.showMessageDialog(new JFrame(), "Entry is NaN and check textfield-check was wrong");
            }
        });
    }
    
    /**
     * Checks if the input/given string is a number or a negative number
     *
     * @param tmp: checked entry
     * @return boolean: validity of the string
     */
    private boolean isValid(String tmp)
    {
        return (tmp.matches("\\d+")); //|| tmp.matches("\\d{1,2}:\\d{2}")
//        {
//            return true;
//        }
//        return false;
    }
    
    /**
     * Tells the PropertyChangeListeners that a change happens if number!=0
     *
     * @param number: the number typed into the textfield
     */
    private void confirmChange(int number)
    {
        _support.firePropertyChange("Test", 0, number);
    }
    
    /**
     * Allows listeners to be added
     *
     * @param pcl: the new listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        _support.addPropertyChangeListener(pcl);
    }
    
    /**
     * Shows the AddAreaUI
     * Neccessary for observer Weekplan
     */
    public void showUI()
    {
        _ui.showUI();
    }
}
