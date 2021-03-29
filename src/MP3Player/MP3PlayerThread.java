package MP3Player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

/**
 * Class that handles the playing of a song on an extra Thread
 * TODO: Statuschange
 */

public class MP3PlayerThread extends Thread
{
    private FileInputStream _song;
    private Player _player;
    private boolean _threadLife;
    private boolean _playerLife;
    private int _delay;
    
    /**
     * Constructor for the special Thread-class for each song
     * @param delay the delay until the song is supposed to be played
     * @param song the song to be played
     */
    public MP3PlayerThread(int delay,FileInputStream song)
    {
        _delay = delay;
        _threadLife = true;
        _playerLife = false;
        _song = song;
    }
    
    /**
     * run method, that will be used on a new thread by calling the start method
     */
    @Override
    public void run()
    {
        try
        {
            while (_threadLife)
            {
                TimeUnit.SECONDS.sleep(_delay);
                _playerLife = true;
                _player = new Player(_song);
                _player.play();
                _playerLife = false;
            }
            //TimeUnit.MINUTES.sleep(delay);
            //change(1);
            
            //change(0);
        }
        catch (JavaLayerException | InterruptedException e)
        {
            System.out.println(e);
        }
    }
    
    /**
     * Method that ends the thread after stopping the song if it is running
     */
    public void kill()
    {
        _threadLife = false;
        if (_playerLife)
        {
            _player.close();
        }
        this.interrupt();
    }
    
}
