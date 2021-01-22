package org.sweepers;

/**
 * Only serves to launch the game. This class is necessary since the game is
 * served as a fat jar.
 */
public class Launcher {
    /**
     * Just calls Main.main
     * @param args not used
     */
    public static void main(String[] args) {
        Main.main(args);
    }
}
