package Alarmclock;

import MP3Player.MP3Player;
import MusicArea.MusicArea;

/**
 * Main functional class of the Alarmclock, consisting of:
 * _ui: UI to navigate
 * _player: MP3Player to play music
 */

public class Alarmclock
{
    private AlarmclockUI _ui;
    private MP3Player _player;
    
    /**
     * Constructor of the class Alarmclock
     */
    public Alarmclock()
    {
        _ui = new AlarmclockUI();
        addUIListener();
        _player = MP3Player.getInstance();
        addMP3PlayerListener();
    }
    
    /**
     * Adds the PropertyChangeListener for the MP3Player
     */
    private void addMP3PlayerListener()
    {
        _player.addPropertyChangeListener(event ->
        {
            _ui.changeMusicStatus(_player.getStatus());
            if (_player.getStatus() == 0)
            {
                _ui.setTimerLabelText("No song set:");
            }
            else if (_player.getStatus() == 2)
            {
                _ui.setTimerLabelText("Next song starts at: " + _player.getNextSongTime());
            }
        });
    }
    
    /**
     * Adds the listeners of all components in the UI:
     * StopButton.actionlistener: Stops the song
     * TimerButton.actionlistener: Opens MusicArea and gives it an observer
     * CloseButton.actionlistener: Closes window
     */
    private void addUIListener()
    {
        addStopButtonListener();
        addTimerButtonListener();
        addCloseButtonListener();
    }
    
    /**
     * Listener to stop the song
     */
    private void addStopButtonListener()
    {
        _ui.getStopButton().addActionListener(event ->
        {
            _player.quit();
            _ui.changeMusicStatus(_player.getStatus());
            //            closeUI();
            //            new Alarmclock();
        });
    }
    
    /**
     * Listener for the timer
     */
    private void addTimerButtonListener()
    {
        _ui.getTimerButton().addActionListener(event ->
        {
            final MusicArea area = new MusicArea(_player);
            area.setUiPosition(_ui.getPosition());
            addMusicAreaListener(area);
            
            area.showUI();
        });
    }
    
    /**
     * Adds the Propertychangelistener to a Musicarea
     *
     * @param area
     */
    private void addMusicAreaListener(MusicArea area)
    {
        assert area != null : "Musicarea is null";
        area.addPropertyChangeListener(evt ->
        {
            _ui.setTimerLabelText("Next song starts at: " + _player.getNextSongTime());
        });
    }
    
    /**
     * Listener for the CloseButton
     */
    private void addCloseButtonListener()
    {
        _ui.getCloseButton().addActionListener(event ->
        {
            closeUI();
        });
    }
    
    /**
     * Close the UI to exit the program or move to different parts
     */
    private void closeUI()
    {
        _player.quit();
        _ui.close();
    }
}
