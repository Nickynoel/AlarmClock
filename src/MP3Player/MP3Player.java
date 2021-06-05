package MP3Player;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
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

/**
 * Class that allows playing an mp3
 */

public class MP3Player
{
    //public static String DEFAULTSONG = "D:/Musik/Edge Of Paradise  - Fire2.mp3"; //Maybe load song from a file
    
    private String _songpath;
    private FileInputStream _song;
    private List<MP3PlayerThread> _songThreads;
    private float _volume;
    private int _status; //0: off, 1: on, 2: waiting
    private boolean _songValidity;
    
    private Calendar _nextSongTime;
    private PropertyChangeSupport _support; //basically observable just newer
    
    /**
     * Constructor for class MP3Player
     */
    private MP3Player()
    {
        _songThreads = new ArrayList<>();
        //loadPlayer();
        setOutputVolume(0.05f); //volume-control, should be set to 0.05f if you dont want your ears to die
        _status = 0;
        _support = new PropertyChangeSupport(this);
    }
    
    /**
     * Factory Method to generate an object of the class MP3Player
     */
    public static MP3Player getInstance()
    {
        return new MP3Player();
    }
    
    /**
     * Starts the Player with the Defaultsong
     */
    private void loadPlayer()
    {
//        try
//        {
//            _song = new FileInputStream(new File(DEFAULTSONG));
//            _songname = DEFAULTSONG;
//            _songValidity = true;
//        }
//        catch (FileNotFoundException e)
//        {
//            _songname = "No valid File!";
//            _songValidity = false;
//        }
    }
    
    /**
     * Sets the output-volume of the song, cause normally it's way too loud
     *
     * @param volume the volume as number between 0.0f and 1.0f
     */
    private void setOutputVolume(Float volume)
    {
        assert (volume >= 0 && volume <= 1) : "Volume v needs to be 0 <= v <= 1";
        _volume = volume;
        Info source = Port.Info.HEADPHONE;
        if (AudioSystem.isLineSupported(source))
        {
            try
            {
                Port outline = (Port) AudioSystem.getLine(source);
                outline.open();
                FloatControl volumeControl = (FloatControl) outline.getControl(FloatControl.Type.VOLUME);
                //System.out.println("  volume: " + volumeControl.getValue());
                volumeControl.setValue(_volume);
            }
            catch (LineUnavailableException ex)
            {
                System.err.println("source not supported");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Adds a time at which the song should be played to the queue with a given delay
     * (for now: only one request at a time)
     *
     * @param delay the downtime till the song should be played
     */
    public void addToQueue(int delay)
    {
        assert (delay >= 0) : "Delay must not be negative!";
        if (_status == 0)  //"For now only one songrequest at a time";
        {
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
    private void run(int delay)
    {
        assert (delay >= 0) : "Delay must not be negative!";
        MP3PlayerThread songThread = new MP3PlayerThread(delay, _song);
        change(2);
        _songThreads.add(songThread);
        //adds a Listener so the the class knows when the song is over
        songThread.addPropertyChangeListener(event ->
        {
            quit();
        });
        songThread.start();
    }
    
    /**
     * Changes the _status to 0,1,2 (stop, run, ready) and sends confirmation to other methods
     *
     * @param i 0,1,2 (stop, run, ready)
     */
    private void change(int i)
    {
        assert (i == 0 || i == 1 || i == 2) : "Only 3 viable _status-values";
        _status = i;
        confirmChange(i);
    }
    
    /**
     * Tells the PropertyChangeListeners that a change happens if number!=0
     *
     * @param number: the number typed into the textfield
     */
    private void confirmChange(int number)
    {
        assert (number == 0 || number == 1 || number == 2) : "Only 3 viable _status-values";
        _support.firePropertyChange("Test", -1, number);
    }
    
    /**
     * Allows listeners to be added
     *
     * @param pcl: the new listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        assert pcl != null : "Object is null!";
        _support.addPropertyChangeListener(pcl);
    }
    
    /**
     * GetA for the songname of the current song
     *
     * @return _songname
     */
    public String getSongname()
    {
        if (_song == null)
        {
            return "Choose a song!";
        }
        return _songpath;
    }
    
    /**
     * Returns a String of the time the next song is played
     *
     * @return time the next song is played as String
     */
    public String getNextSongTime()
    {
        String hours = Integer.toString(_nextSongTime.get(Calendar.HOUR_OF_DAY));
        String minutes = Integer.toString(_nextSongTime.get(Calendar.MINUTE));
        if (minutes.length() == 1)
        {
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }
    
    
    /**
     * GetA for _status
     *
     * @return _status
     */
    public int getStatus()
    {
        return _status;
    }
    
    /**
     * Method that changes the song that's gonna be played next
     *
     * @param songpath String with the Datapath of the sond
     * @throws FileNotFoundException Datapath has to be valid
     */
    public void setSong(String songpath) throws FileNotFoundException
    {
        _song = new FileInputStream(new File(songpath));
        _songpath = songpath;
        _songValidity = true;
    }
    
    /**
     * Quits the current thread and stops its song, if it is running already
     */
    public void quit()
    {
        if (_songThreads.size() > 0)
        {
            _songThreads.get(0).kill();
            _songThreads.remove(0);
        }
        change(0);
    }
    
    /**
     * Checks if the file is a playable song
     * @return boolean: Validity of the song
     */
    public boolean isValidSong()
    {
        return _songValidity;
    }
}


