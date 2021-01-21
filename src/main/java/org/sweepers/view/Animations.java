package org.sweepers.view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {
    /**
     * An animation that scales and rotates the target indefinetely.
     * @param target the node to animate
     */
    public static void titleAnimation(Node target) {
        // Set up timeline
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);

        // Create keyframes
        final KeyValue kvScaleX1 = new KeyValue(target.scaleXProperty(), 0.9, Interpolator.EASE_BOTH);
        final KeyValue kvScaleY1 = new KeyValue(target.scaleYProperty(), 0.9, Interpolator.EASE_BOTH);
        final KeyValue kvRotate1 = new KeyValue(target.rotateProperty(), -7, Interpolator.EASE_BOTH);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(0), kvScaleX1, kvScaleY1, kvRotate1);

        final KeyValue kvScaleX2 = new KeyValue(target.scaleXProperty(), 1.2, Interpolator.EASE_BOTH);
        final KeyValue kvScaleY2 = new KeyValue(target.scaleYProperty(), 1.2, Interpolator.EASE_BOTH);
        final KeyFrame kf2 = new KeyFrame(Duration.millis(2000), kvScaleX2, kvScaleY2);

        final KeyValue kvScaleX3 = new KeyValue(target.scaleXProperty(), 0.9, Interpolator.EASE_BOTH);
        final KeyValue kvScaleY3 = new KeyValue(target.scaleYProperty(), 0.9, Interpolator.EASE_BOTH);
        final KeyValue kvRotate3 = new KeyValue(target.rotateProperty(), 7, Interpolator.EASE_BOTH);
        final KeyFrame kf3 = new KeyFrame(Duration.millis(4000), kvScaleX3, kvScaleY3, kvRotate3);

        
        timeline.getKeyFrames().addAll(kf1, kf2, kf3);
        timeline.play();
    }
}
