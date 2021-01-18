package org.sweepers.view;

import javafx.scene.media.AudioClip;

public class GameAudio {
    private AudioClip explosion, win;

    public GameAudio() {
        explosion = new AudioClip(getClass().getResource("/explosion.wav").toExternalForm());
        win = new AudioClip(getClass().getResource("/win.wav").toExternalForm());
    }

    public void playExplosion() {
        explosion.play();
    }

    public void playWin() {
        win.play();
    }
}