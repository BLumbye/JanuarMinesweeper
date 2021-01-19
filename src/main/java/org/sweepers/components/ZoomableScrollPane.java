package org.sweepers.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Class originally from https://stackoverflow.com/a/44314455.
 * Modified to fit the minesweeper program.
 */
public class ZoomableScrollPane extends ScrollPane {
    private DoubleProperty scaleValue = new SimpleDoubleProperty(0.8);
    private double zoomIntensity = 0.02;
    private StackPane target;
    private Group zoomNode;

    public ZoomableScrollPane(StackPane target) {
        super();
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(true);
        setFitToHeight(true); //center
        setFitToWidth(true); //center

        // Disable pan with left and right mouse button
        zoomNode.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.MIDDLE) event.consume();
        });

        // Update scale on resize
        widthProperty().addListener((obs, oldVal, newVal) -> updateScale(scaleValue.get()));
        heightProperty().addListener((obs, oldVal, newVal) -> updateScale(scaleValue.get()));
    }

    public void zoom(double difference) {
        updateScale(scaleValue.get() + difference);
    }

    public DoubleProperty getScaleValue() {
        return scaleValue;
    }

    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private boolean updateScale(double scale) {
        // Limit scale
        double ratio = Math.min(getHeight() / target.getHeight(), getWidth() / target.getWidth());
        scale = Math.max(ratio * 0.99, scale);
        scale = Math.min(1, scale);

        target.setScaleX(scale);
        target.setScaleY(scale);

        boolean changed = scale != scaleValue.get();
        scaleValue.set(scale);
        return changed;
    }

    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        // calculate pixel offsets from [0, 1] range
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        double oldScale = scaleValue.get();
        if (updateScale(scaleValue.get() * zoomFactor)) {
            this.layout(); // refresh ScrollPane scroll positions & target bounds
    
            // convert target coordinates to zoomTarget coordinates
            Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));
    
            // calculate adjustment of scroll position (pixels)
            Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(scaleValue.get() / oldScale - 1));
    
            // convert back to [0, 1] range
            // (too large/small values are automatically corrected by ScrollPane)
            Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
            this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
            this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
        }
    }
}
