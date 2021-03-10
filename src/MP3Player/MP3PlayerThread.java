package MP3Player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;

public class MP3PlayerThread extends Thread
{
    private FileInputStream _song;
    private Player _player;
    private boolean run;
    
    public MP3PlayerThread(FileInputStream song)
    {
        run = true;
        _song = song;
        
    }
    
    public void run()
    {
        try
        {
            while (run)
            {
                _player = new Player(_song);
                _player.play();
            }
            //TimeUnit.MINUTES.sleep(delay);
            //change(1);
            
            //change(0);
        }
        catch (JavaLayerException e)
        {
            System.out.println(e);
        }
    }
    
    public void kill()
    {
        run = false;
        _player.close();
        this.interrupt();
    }
    
}
