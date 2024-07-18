package BackEnd.MP3Player;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Song
{
    private final String _filePath;
    private SongThread _songThread;
    private boolean _isStarted;
    private final int _delay; // 0 if actual MP3-Player

    private final PropertyChangeSupport _support;

    public Song(String path, int delay) throws FileNotFoundException {
        _filePath = path;
        _delay = delay;
        _isStarted = false;
        _songThread = new SongThread(new FileInputStream(path), delay);

        _support = new PropertyChangeSupport(this);
    }

    /**
     * Plays the song on the given Thread and commits the change
     */
    public void runThread() {
        //adds a Listener so the class knows when the song is over
        _songThread.addPropertyChangeListener(event -> stopThread());
        _isStarted = true;
        _songThread.start();
    }

    /**
     * Quits the current thread and stops its song, if it is running already
     */
    public void stopThread() {
        if (_isStarted) {
            _songThread.kill();
            _isStarted = false;
            _support.firePropertyChange("Test", -1, 0);
        }
    }

    public boolean getIsStarted(){
        return _isStarted;
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
