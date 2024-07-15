package FrontEnd.MusicArea;

import FrontEnd.Dateieinleser.FilePicker;
import BackEnd.MP3Player.MP3Player;
import RowFileReader.RowFileReader;
import RowFileWriter.RowFileWriter;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;


/**
 * Functional class of the MusicArea, the part in which the song and time are being chosen,
 * consisting of a UI and an MP3Player
 */
public class MusicArea
{
    private final static String SONGINFOFILE = "song.txt";

    private final MusicAreaUI _ui;
    private final MP3Player _player;
    private final List<String> _songs;

    private final PropertyChangeSupport _support; //basically observable just newer

    /**
     * Constructor of the class
     *
     * @param player MP3Player that plays the song
     */
    public MusicArea(MP3Player player) {
        _songs = loadSongFromFile(SONGINFOFILE);
        _player = player;
        _support = new PropertyChangeSupport(this);
        _ui = new MusicAreaUI();
        _ui.setSongText(_player.getSongName());

        initiateSongData();
        addListener();
    }

    /**
     * Loads the song.txt file and reads the given path and song of the last title
     *
     * @param songDataFile: the file with the song data, given in a file
     * @return [path, songPath]
     */
    private List<String> loadSongFromFile(String songDataFile) {
        RowFileReader reader = RowFileReader.getInstance(songDataFile);
        List<String> data = reader.getList();
        if (data.size() != 2) {
            data.clear();
            data.add("C:/");
            data.add("NoSongFound");
        }
        return data;
    }

    /**
     * Method that sets the saved song as initial song
     */
    private void initiateSongData() {
        try {
            _player.setSong(_songs.get(1));
            _ui.setSongText(_songs.get(1));
        }
        catch (FileNotFoundException e) {
            javax.swing.JOptionPane.showMessageDialog(new JFrame(),
                    "Unable to find the song file!");
        }
    }

    /**
     * Adds the listeners of the components of AddAreaUI:
     * BackButton.actionListener: just closes
     * TextField.keyListener: checks the validity of the entry
     * TextField.actionListener: Shortcut to confirmButton
     * SongButton.actionListener: Opens FileChooser to pick song
     * ConfirmButton.actionListener: Processing of the entry
     */
    private void addListener() {
        addBackButtonListener();
        addTextFieldKeyListener();
        addTextFieldActionListener();
        addSongButtonActionListener();
        addConfirmButtonActionListener();
    }

    /**
     * ActionListener for the BackButton, closes the window without action taken
     */
    private void addBackButtonListener() {
        _ui.getBackButton().addActionListener(event -> _ui.close());
    }

    /**
     * KeyListener for the TextField, checking the text and the resulting availability of the ConfirmButton
     */
    private void addTextFieldKeyListener() {
        //If the text gets changed it checks it anew and controls the availability of the button
        _ui.getTextField().addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                checkButton();
            }
        });
    }

    /**
     * ActionListener for the TextField, doing the same as the ConfirmButton
     */
    private void addTextFieldActionListener() {
        //Shortcut for enter-key if the _confirmButton is enabled
        _ui.getTextField().addActionListener(event -> {
            _ui.getConfirmButton()
                    .doClick(); //doClick() automatically checks "isEnabled()" of the ConfirmButton
        });
    }

    /**
     * ActionListener for the SongButton, opening a FilePicker to choose a song from your hard drive
     * TODO: Save the paths of folder and song on an external file to get back later
     */
    private void addSongButtonActionListener() {
        //Opens a JFileChooser when _stopButton is clicked
        _ui.getSongButton().addActionListener(event -> {
            addFilePickerListener();

            _ui.setSongText(_player.getSongName());
            RowFileWriter writer = RowFileWriter.getInstance(_songs, new File(SONGINFOFILE));
            writer.saveFile();
        });
    }

    /**
     * Adds a listener to the FilePicker to get the chosen filepath
     */
    private void addFilePickerListener() {
        try {
            File songfile = FilePicker.pickFile(_songs.get(0));

            String newSong = songfile.getPath();
            _player.setSong(newSong);
            _ui.setSongText(newSong);
            checkButton();

            _songs.set(1, newSong);
            _songs.set(0, newSong.substring(0,
                    Math.max(newSong.lastIndexOf("/"), newSong.lastIndexOf("\\"))));
        }
        catch (NullPointerException | FileNotFoundException e) {
            javax.swing.JOptionPane.showMessageDialog(new JFrame(),
                    "Unable to find the song file!");
        }
    }

    /**
     * ActionListener for the ConfirmButton:
     * If all entries are valid and hence this can be activated, the song gets puts on the list with given delay
     * and the UI gets closed
     */
    private void addConfirmButtonActionListener() {
        //Actual action if the _confirmButton gets used and processes the entry
        _ui.getConfirmButton().addActionListener(event -> {
            String tmp = _ui.getTextField().getText();
            try {
                int number = Integer.parseInt(tmp);
                _ui.close();
                _player.addToQueue(number);
                confirmChange(1);
            }
            //should never happen, cause the textField-keyListener checks this
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "Entry is NaN and check textField-check was wrong");
            }
        });
    }

    /**
     * Checks if all requirements are fulfilled for the song to be played
     * aka the song is valid and the delay is valid
     */
    private void checkButton() {
        String tmp = _ui.getTextField().getText();
        if (isValidEntry(tmp) && isValidSong()) {
            _ui.enableConfirmButton();
        }
        else {
            _ui.disableConfirmButton();
        }
    }


    /**
     * Checks if the input/given string is a number or a negative number
     * TODO: the other matches part
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
    private boolean isValidSong() {
        return _player.isValidSong();
    }

    /**
     * Tells the PropertyChangeListeners that a change happens if number!=0
     *
     * @param number: the number typed into the textField
     */
    private void confirmChange(int number) {
        _support.firePropertyChange("Test", 0, number);
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
        _ui.setPosition(p); //Sets position based on the mainframe
    }
}
