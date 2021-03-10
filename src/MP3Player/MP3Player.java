package MP3Player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;

/**
 * Class that allows playing an mp3
 * TODO: At this point, it can only be paused but not reset
 */

public class MP3Player
{
    public static String DEFAULTSONG = "D:/Musik/Archangel.mp3";
    
    private FileInputStream _song;
    private List<MP3PlayerThread> _songThreads;
    private float _volume;
    private int _status; //0: off, 1: on, 2: waiting
    private int _counter;
    
    private Calendar _nextSongTime;
    private PropertyChangeSupport _support; //basically observable just newer
    
    /**
     * Constructor for class MP3Player
     *
     * @param file Musicfile for the player
     */
    private MP3Player(FileInputStream file)
    {
        _song = file;
        _songThreads = new ArrayList<>();
        //_volume = 0; //volume-control, should be set to 0.05f
        setOutputVolume(0.05f);
        _status = 0;
        
        _support = new PropertyChangeSupport(this);
    }
    
    /**
     * Factory Method to generate an object of the class MP3Player, given a certain musicfile-path
     *
     * @param filepath the path to a musicfile as string
     * @return the object of class MP3Player
     * @throws FileNotFoundException if the String does not lead to a an actual file
     */
    public static MP3Player getInstance(String filepath) throws FileNotFoundException
    {
        return new MP3Player(new FileInputStream(new File(filepath)));
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
     * TODO: Delays in their own threads, but getting outside run() into inner class...
     *
     * @param delay the downtime till the song should be played
     */
    public void addToQueue(int delay)
    {
        assert (delay >= 0) : "Delay must not be negative!";
        assert (_status == 0) : "For now only one songrequest at a time";
        _nextSongTime = Calendar.getInstance();
        _nextSongTime.add(Calendar.MINUTE, delay);
        change(2);
        run(delay); //Delay needs to be in a thread as well !!!
    }
    
    //    /**
    //     * Plays the song on the given Thread and commits the change
    //     *
    //     * @param delay: delay until the song is to be played
    //     */
    //    private void run(int delay)
    //    {
    //        Thread songThread = new Thread(new Runnable()
    //        {
    //            @Override
    //            public void run()
    //            {
    //                try
    //                {
    //                    TimeUnit.MINUTES.sleep(delay);
    //                    Player playMP3 = new Player(_song);
    //                    change(1);
    //                    playMP3.play();
    //                    change(0);
    //                }
    //                catch (JavaLayerException | InterruptedException e)
    //                {
    //                    System.out.println(e);
    //                }
    //            }
    //        });
    //        _songThreads.add(songThread);
    //        songThread.start();
    //    }
    
    /**
     * Plays the song on the given Thread and commits the change
     *
     * @param delay: delay until the song is to be played
     */
    private void run(int delay)
    {
        MP3PlayerThread songThread = new MP3PlayerThread(_song);
        _songThreads.add(songThread);
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
        //System.out.println(_status);
        confirmChange(i);
    }
    
    /**
     * GetA for the Threads that play the songs
     *
     * @return _songThreads
     */
    public List<MP3PlayerThread> getSongThreads()
    {
        return _songThreads;
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
        if (minutes.length()==1)
        {
            minutes = "0" + minutes;
        }
        String time = hours + ":" + minutes;
        return time;
    }
    
    /**
     * Quits the currently running song, if any is running
     * TODO: still isnt able to reset...but at least move on from last time
     */
    public void quit()
    {
        if (_songThreads.size()>0)
        {
            //_songThreads.get(0).stop();
            _songThreads.get(0).kill();
            _songThreads.remove(0);
        }
        change(0);
    }
    
    /**
     * Tells the PropertyChangeListeners that a change happens if number!=0
     *
     * @param number: the number typed into the textfield
     */
    private void confirmChange(int number)
    {
        _support.firePropertyChange("Test", -1, number);
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
     * GetA for _status
     *
     * @return _status
     */
    public int getStatus()
    {
        return _status;
    }
}


