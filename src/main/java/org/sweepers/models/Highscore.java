package org.sweepers.models;

/**
 * Stores a high score, with the name and time.
 */
public class Highscore {
    /** The name associated with the high score - max 4 characters */
    public String name;
    /** The time of the high score - in milliseconds */
    public long time;

    /**
     * Creates a new high score object.
     * @param name the name associated with the high score - max 4 characters
     * @param time the time of the high score - in milliseconds
     */
    public Highscore(String name, long time) {
        this.name = name;
        this.time = time;
    }
}
