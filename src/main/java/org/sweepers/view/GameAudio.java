package org.sweepers.view;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameAudio {
    private static GameAudio instance = null;

    private AudioClip explosion, win;
    private MediaPlayer mp;
    private Media music;

    public GameAudio() {
        explosion = new AudioClip(getClass().getResource("/sfx/explosion.wav").toExternalForm());
        win = new AudioClip(getClass().getResource("/sfx/win.wav").toExternalForm());
        music = new Media(getClass().getResource("/sfx/music.wav").toExternalForm());
        mp = new MediaPlayer(music);
        mp.setVolume(Math.pow(0.5, 2));
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.play();
    }

    public static GameAudio getInstance() {
        if (instance == null) instance = new GameAudio();
        return instance;
    }

    public void playExplosion() {
        explosion.play();
    }

    public void playWin() {
        win.play();
    }
}