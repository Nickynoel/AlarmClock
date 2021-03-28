package Alarmclock;

import MP3Player.MP3Player;
import MusicArea.MusicArea;

public class Alarmclock
{
    private AlarmclockUI _ui;
    private MP3Player _player;
    
    public Alarmclock()
    {
        _ui = new AlarmclockUI();
        _player = MP3Player.getInstance();
        addMP3PlayerListener();
        addUIListener();
    }
    
    /**
     * Adds the PropertyChangeListener for the MP3Player
     */
    private void addMP3PlayerListener()
    {
        _player.addPropertyChangeListener(evt ->
        {
            _ui.changeMusicStatus(_player.getStatus());
        });
    }
    
    /**
     * Adds the listeners of all components in the UI:
     * TitleButton.actionlistener: Opens TopicEditArea and gives it an observer
     * AddButton.actionlistener: Opens AddArea and gives it an observer
     * StopButton.actionlistener: Stops the song
     * TimerButton.actionlistener: Opens MusicArea and gives it an observer
     * SaveButton.actionlistener: Saves the topic-values onto the file TopicList.FILENAME
     * OptionButton.actionlistener: Closes window and opens OptionArea
     */
    private void addUIListener()
    {
        //listener to stop the song
        _ui.getStopButton().addActionListener(event ->
        {
            _player.quit();
            _ui.changeMusicStatus(_player.getStatus());
//            closeUI();
//            new Alarmclock();
        });

        //listener for the timer
        _ui.getTimerButton().addActionListener(event ->
        {
            final MusicArea area = new MusicArea(_player, _ui.getMainframe());
            area.addPropertyChangeListener(evt ->
            {
//                _player.addToQueue(number);
                _ui.setTimerLabelText(_player.getNextSongTime());
//                _ui.changeMusicStatus(_player.getStatus());
            });
            area.showUI();
        });
        
        //listener for closing
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
