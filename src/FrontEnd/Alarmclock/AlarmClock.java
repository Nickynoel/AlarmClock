package FrontEnd.Alarmclock;

import BackEnd.MP3Player.MP3Player;
import FrontEnd.MusicArea.MusicArea;

/**
 * Main functional class of the AlarmClock, consisting of:
 * _ui: UI to navigate
 * _player: BackEnd.MP3Player to play music
 */

public class AlarmClock
{
    private final AlarmClockUI _ui;
    private final MP3Player _player;

    /**
     * Constructor of the class AlarmClock
     */
    public AlarmClock() {
        _ui = new AlarmClockUI();
        addUIListener();
        _player = MP3Player.getInstance();
        addMP3PlayerListener();
    }

    /**
     * Adds the PropertyChangeListener for the MP3Player
     */
    private void addMP3PlayerListener() {
        _player.addPropertyChangeListener(event -> {
            _ui.changeMusicStatus(_player.getStatus());
            if (_player.getStatus() == 0) {
                _ui.setTimerLabelText("No song set:");
            }
            else if (_player.getStatus() == 2) {
                _ui.setTimerLabelText("Next song starts at: " + _player.getNextSongTime());
            }
        });
    }

    /**
     * Adds the listeners of all components in the UI:
     * StopButton.actionListener: Stops the song
     * TimerButton.actionListener: Opens MusicArea and gives it an observer
     * CloseButton.actionListener: Closes window
     */
    private void addUIListener() {
        addStopButtonListener();
        addTimerButtonListener();
        addCloseButtonListener();
    }

    /**
     * Listener to stop the song
     */
    private void addStopButtonListener() {
        _ui.getStopButton().addActionListener(event -> {
            _player.quit();
            _ui.changeMusicStatus(_player.getStatus());
        });
    }

    /**
     * Listener for the timer
     */
    private void addTimerButtonListener() {
        _ui.getTimerButton().addActionListener(event -> {
            final MusicArea area = new MusicArea(_player);
            area.setUiPosition(_ui.getPosition());
            addMusicAreaListener(area);

            area.showUI();
        });
    }

    /**
     * Adds the PropertyChangeListener to a MusicArea
     */
    private void addMusicAreaListener(MusicArea area) {
        assert area != null : "MusicArea is null";

        area.addPropertyChangeListener(
                evt -> _ui.setTimerLabelText("Next song starts at: " + _player.getNextSongTime()));
    }

    /**
     * Listener for the CloseButton
     */
    private void addCloseButtonListener() {
        _ui.getCloseButton().addActionListener(event -> closeUI());
    }

    /**
     * Close the UI to exit the program or move to different parts
     */
    private void closeUI() {
        _player.quit();
        _ui.close();
    }
}
