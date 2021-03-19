package Alarmclock;

import Dateieinleser.DateiEinleser;
import MP3Player.MP3Player;
import MusicArea.MusicArea;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Alarmclock
{
    private AlarmclockUI _ui;
    private MP3Player _player;
    
    public Alarmclock()
    {
        _ui = new AlarmclockUI();
        loadPlayer();
        addMP3PlayerListener();
        addUIListener();
    }
    
    /**
     * Initializes the MP3Player
     */
    private void loadPlayer()
    {
        try
        {
            _player = MP3Player.getInstance(MP3Player.DEFAULTSONG);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Adds the PropertyChangeListener for the MP3Player
     */
    private void addMP3PlayerListener()
    {
        _player.addPropertyChangeListener(evt ->
        {
            //_ui.changeMusicStatus(_player.getStatus());
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
            closeUI();
            new Alarmclock();
        });

        //listener for the timer
        _ui.getTimerButton().addActionListener(event ->
        {
            final MusicArea area = new MusicArea(_player, _ui.getMainframe());
            area.addPropertyChangeListener(evt ->
            {
                //_player.addToQueue(number);
                _ui.setTimerLabelText(_player.getNextSongTime());
                _ui.changeMusicStatus(_player.getStatus());
            });
            area.showUI();
        });
        
        //listener to open file, for tests
//        _ui.getTestButton().addActionListener(event ->
//        {
//            File bilddaten = DateiEinleser.liesBilddaten();
//            if (bilddaten != null)
//            {
//                try
//                {
//                    MP3Player player = MP3Player.getInstance(bilddaten.getPath());
//                    player.addToQueue(0);
//                }
//                catch (FileNotFoundException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println(bilddaten.getPath());
//        });
        
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
