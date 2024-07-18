package FrontEnd.SubComponents.MusicArea;

import FrontEnd.SubComponents.FilePicker.FilePicker;
import BackEnd.MP3Player.MP3Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * Functional class of the MusicArea, the part in which the song and wait time are being chosen
 * It consists of a UI and an MP3Player
 */
public class MusicArea
{
    private final MP3Player _player;
    private final MusicAreaUI _ui;
    private final PropertyChangeSupport _support; //basically observable just newer

    private String _musicFolderPath;
    private String _songPath;

    /**
     * Constructor of the class
     */
    public MusicArea() {
        _player = MP3Player.getInstance();
        _ui = new MusicAreaUI();
        _support = new PropertyChangeSupport(this);
        _musicFolderPath = _player.getMusicFolderPath();
        _songPath = _player.getSongPath();

        _ui.setSongText(_songPath);
        addUIListeners();
    }

    /**
     * Adds the listeners of the components of MusicAreaUI
     */
    private void addUIListeners() {
        addTextFieldKeyListener();
        addTextFieldActionListener();
        addLoadSongButtonActionListener();
        addConfirmButtonActionListener();
        addBackButtonListener();
    }

    // ------------------ Listeners: Start --------------------------------------

    /**
     * KeyListener for the TextField, checking the text and
     * the resulting availability of the ConfirmButton
     */
    private void addTextFieldKeyListener() {
        //If the text gets changed it checks it anew and controls the availability of the button
        _ui.getTextField().addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                validateEntries();
            }
        });
    }

    /**
     * ActionListener for the TextField, doing the same as the ConfirmButton
     */
    private void addTextFieldActionListener() {
        // Shortcut for enter-key if the _confirmButton is enabled
        // doClick() automatically checks "isEnabled()" of the ConfirmButton
        _ui.getTextField().addActionListener(event -> _ui.getConfirmButton().doClick());
    }

    /**
     * ActionListener for the SongButton, opening a FilePicker to choose a song from your hard drive
     */
    private void addLoadSongButtonActionListener() {
        //Opens a JFileChooser when _loadSongButton is clicked
        _ui.getLoadSongButton().addActionListener(event -> {
            try {
                File songfile = FilePicker.pickFile(_player.getMusicFolderPath());

                String newSong = songfile.getPath();
                _player.changeDefaultSong(newSong);
                _ui.setSongText(newSong);
                validateEntries();

                _songPath = newSong;
                _musicFolderPath = newSong.substring(0,
                        Math.max(newSong.lastIndexOf("/"), newSong.lastIndexOf("\\")));
                _player.saveSongData();
            }
            catch (NullPointerException | FileNotFoundException e) {
                javax.swing.JOptionPane.showMessageDialog(new JFrame(),
                        "Unable to find the song file!");
            }
        });
    }

    /**
     * ActionListener for the ConfirmButton:
     * If all entries are valid and hence this can be activated, the song gets puts
     * on the list with given delay and the UI gets closed
     */
    private void addConfirmButtonActionListener() {
        //Actual action if the _confirmButton gets used and processes the entry
        _ui.getConfirmButton().addActionListener(event -> {
            String tmp = _ui.getTextField().getText();
            try {
                int number = Integer.parseInt(tmp);
                _ui.close();
                _player.startAlarmClock(number);
                _player.setSongPath(_songPath);
                _player.setMusicFolderPath(_musicFolderPath);
                _support.firePropertyChange("Test", 0, 1);
            }
            //should never happen, cause the textField-keyListener checks this
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "Entry is NaN and check textField-check was wrong");
            }
        });
    }

    /**
     * ActionListener for the BackButton, closes the window without action taken
     */
    private void addBackButtonListener() {
        _ui.getBackButton().addActionListener(event -> _ui.close());
    }

    // ------------------------- Listeners: End --------------------------------------

    /**
     * Checks if all requirements are fulfilled for the song to be played
     * aka the song is valid and the delay is valid
     */
    private void validateEntries() {
        String input = _ui.getTextField().getText();
        if (isValidEntry(input) && isValidSong(_songPath))
            _ui.enableConfirmButton();
        else
            _ui.disableConfirmButton();
    }


    /**
     * Checks if the input/given string is a number or a negative number
     * TODO: the other matches part (also matches with xx:xx)?
     *
     * @param tmp: checked entry
     * @return boolean: validity of the string
     */
    private boolean isValidEntry(String tmp) {
        return (tmp.matches("\\d+")); //|| tmp.matches("\\d{1,2}:\\d{2}")
    }

    /**
     * Checks if the given file is actually a song
     *
     * @return boolean: validity of the song
     */
    private boolean isValidSong(String songPath) {
        try {
            FileInputStream io = new FileInputStream(songPath);
            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * Allows listeners to be added
     *
     * @param pcl: the new listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        _support.addPropertyChangeListener(pcl);
    }

    public void showUI() {
        _ui.showUI();
    }

    /**
     * Sets the position of the UI (so that it always shows up on top of the main UI)
     *
     * @param p: The point describing the top left point of the UI
     */
    public void setUiPosition(Point p) {
        _ui.setPosition(p); // Sets position based on the mainframe
    }
}
