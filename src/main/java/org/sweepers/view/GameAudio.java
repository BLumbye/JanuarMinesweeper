package org.sweepers.view;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * A singleton class for playing the audio of the game. The background music
 * will automatically start when the class is instantiated.
 */
public class GameAudio {
    private static GameAudio instance = null;

    private AudioClip explosion, win;
    private MediaPlayer mp;
    private Media music;

    private GameAudio() {
        explosion = new AudioClip(getClass().getResource("/sfx/explosion.wav").toExternalForm());
        win = new AudioClip(getClass().getResource("/sfx/win.wav").toExternalForm());
        music = new Media(getClass().getResource("/sfx/music.wav").toExternalForm());
        mp = new MediaPlayer(music);
        mp.setVolume(Math.pow(0.5, 2));
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.play();
    }

    /**
     * @return the singleton instance of the class
     */
    public static GameAudio getInstance() {
        if (instance == null)
            instance = new GameAudio();
        return instance;
    }

    /**
     * Plays the explosion sound located in "/sfx/explosion.wav"
     */
    public void playExplosion() {
        explosion.play();
    }

    /**
     * Plays the win sound located in "/sfx/win.wav"
     */
    public void playWin() {
        win.play();
    }
}