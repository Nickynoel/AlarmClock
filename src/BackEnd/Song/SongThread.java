package BackEnd.Song;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

/**
 * Class that handles the playing of a song on an extra Thread
 */

public class SongThread extends Thread
{
    private final FileInputStream _song;
    private final int _delay;
    private final PropertyChangeSupport _support;

    private Player _player;
    private boolean _threadLife; // time the run-function is running, basically when the song is playing
    private boolean _playerLife; // time _player is alive - needs Thread to allow actions while song is playing


    /**
     * Constructor for the special Thread-class for each song
     *
     * @param delay the delay until the song is supposed to be played
     * @param song  the song to be played
     */
    public SongThread(FileInputStream song, int delay) {
        _delay = Math.max(0, delay);
        _threadLife = true;
        _playerLife = false;
        _song = song;
        _support = new PropertyChangeSupport(this);
        initiatePlayer();
    }

    private void initiatePlayer() {
        try {
            _player = new Player(_song);
        }
        catch (JavaLayerException e) {
            javax.swing.JOptionPane.showMessageDialog(new JFrame(),
                    "Error when loading the song file into the player!");
        }
    }

    /**
     * run method, that will be used on a new thread by calling the start method
     * has to be overwritten to work, cause start() triggers run()
     */
    @Override
    public void run() {
        try {
            while (_threadLife) {
                TimeUnit.MINUTES.sleep(_delay);

                _playerLife = true;
                _support.firePropertyChange("Player Running", -1, 1);
                _player.play(500);
                _threadLife = false;
            }
        }
//        catch (InterruptedException e) {
        catch (InterruptedException | JavaLayerException e) {
            // Do nothing
        }
        finally {
            //somehow doesn't trigger the text change // ToDo 1
            _support.firePropertyChange("Kill Thread", -1, 0);
            kill();
            return;
        }
    }

    /**
     * Method that ends the thread after stopping the song if it is running
     */
    public void kill() {
        _threadLife = false;
        if (_playerLife)
            _player.close();
        this.interrupt();
    }

    /**
     * Allows listeners to be added
     *
     * @param pcl: the new listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        _support.addPropertyChangeListener(pcl);
    }
}