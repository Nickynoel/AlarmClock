package BackEnd.MP3Player;

import RowFileReader.RowFileReader;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javax.swing.*;

/**
 * Class that allows playing one mp3
 */

public class MP3Player
{
    private final static String SONGINFOFILE = "song.txt";
    private static MP3Player _singleton;

    /**
     * Factory Method to generate an object of the class BackEnd.MP3Player
     */
    public static MP3Player getInstance() {
        if (_singleton == null)
            _singleton = new MP3Player();
        return _singleton;
    }

    //    private List<Song> _songList;
    private String _songPath;
    private String _musicFolderPath;
    private FileInputStream _song;
    private final List<MP3PlayerThread> _songThreads;
    private float _volume;
    private int _status; // 0: off, 1: on, 2: waiting

    private Calendar _nextSongTime;

    private final PropertyChangeSupport _support; //basically observable just newer

    private MP3Player() {
        _songThreads = new ArrayList<>();
        //        _songList = new ArrayList<>();

        // Load Values???
        List<String> songData = loadSongFromFile(SONGINFOFILE);
        _musicFolderPath = songData.get(0);
        _songPath = songData.get(1);

        //volume-control, should be set to 0.05f if you don't want your ears to die
        setOutputVolume(0.05f); // ToDo: Manual setting + save value
        _status = 0;
        _support = new PropertyChangeSupport(this);
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
            data.add("No Song Found");
        }
        return data;
    }

    /**
     * Sets the output-volume of the song, cause normally it's way too loud
     *
     * @param volume the volume as number between 0.0f and 1.0f
     */
    private void setOutputVolume(Float volume) {
        volume = Math.max(0.0f, volume);
        volume = Math.min(1.0f, volume);
        _volume = volume;

        Info source = Port.Info.LINE_OUT; //alternative: HEADPHONE
        if (AudioSystem.isLineSupported(source)) {
            try {
                Port outline = (Port) AudioSystem.getLine(source);
                outline.open();
                FloatControl volumeControl = (FloatControl) outline.getControl(
                        FloatControl.Type.VOLUME);
                volumeControl.setValue(_volume);
            }
            catch (LineUnavailableException ex) {
                javax.swing.JOptionPane.showMessageDialog(new JFrame(), "Source not Supported!");
            }
        }
    }

    /**
     * Adds a time at which the song should be played to the queue with a given delay
     * (for now: only one request at a time)
     *
     * @param delay the downtime till the song should be played (has to be positive)
     */
    public void startAlarmClock(int delay) {
        //        addToSongList();
        //        updateNextSongTime();
        delay = Math.max(delay, 0);
        //"For now only one song-request at a time";
        if (_status == 0) {
            _nextSongTime = Calendar.getInstance();
            _nextSongTime.add(Calendar.MINUTE, delay);
            change(2);
            run(delay);
        }
    }

    /**
     * Plays the song on the given Thread and commits the change
     *
     * @param delay: delay until the song is to be played
     */
    private void run(int delay) {
        delay = Math.max(delay, 0);
        MP3PlayerThread songThread = new MP3PlayerThread(delay, _song);
        change(2);
        _songThreads.add(songThread);
        //adds a Listener so the class knows when the song is over
        songThread.addPropertyChangeListener(event -> quit());
        songThread.start();
    }

    /**
     * Changes the _status to 0,1,2 (stop, run, ready) and sends confirmation to other methods
     *
     * @param i 0,1,2 (stop, run, ready)
     */
    private void change(int i) {
        assert (i == 0 || i == 1 || i == 2) : "Only 3 viable _status-values";
        _status = i;
        confirmChange(i);
    }

    /**
     * Tells the PropertyChangeListeners that a change happens if number!=0
     *
     * @param number: the number typed into the textField
     */
    private void confirmChange(int number) {
        assert (number == 0 || number == 1 || number == 2) : "Only 3 viable _status-values";
        _support.firePropertyChange("Test", -1, number);
    }

    /**
     * Allows listeners to be added
     *
     * @param pcl: the new listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        _support.addPropertyChangeListener(pcl);
    }

    /**
     * GetA for the name of the current song
     *
     * @return _song
     */
    public String getSongPath() {
        return _songPath;
    }

    /**
     * Returns a String of the time the next song is played
     *
     * @return time the next song is played as String
     */
    public String getNextSongTime() {
        String hours = Integer.toString(_nextSongTime.get(Calendar.HOUR_OF_DAY));
        String minutes = Integer.toString(_nextSongTime.get(Calendar.MINUTE));
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }


    /**
     * GetA for _status
     *
     * @return _status
     */
    public int getStatus() {
        return _status;
    }

    /**
     * Method that changes the song that will be played next
     *
     * @param songPath String with the filepath of the song
     * @throws FileNotFoundException filepath has to be valid
     */
    public void setSong(String songPath) throws FileNotFoundException {
        _song = new FileInputStream(songPath);
        _songPath = songPath;
    }

    /**
     * Quits the current thread and stops its song, if it is running already
     */
    public void quit() {
        if (!_songThreads.isEmpty()) {
            _songThreads.get(0).kill();
            _songThreads.remove(0);
        }
        change(0);
    }

    public String getMusicFolderPath() {
        return _musicFolderPath;
    }

    public void setSongPath(String songPath) {
        _songPath = songPath;
    }

    public void setMusicFolderPath(String musicFolderPath) {
        _musicFolderPath = musicFolderPath;
    }

    public void saveSongData() {
        // ToDo: Save the _songPath and _musicFolderPath to the song.txt
    }
}


