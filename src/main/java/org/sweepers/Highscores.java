package org.sweepers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.sweepers.models.Highscore;

/**
 * A singleton class that handles all of the high scores, including loading,
 * setting, and saving them.
 */
public class Highscores {
    private static Highscores instance = null;

    private Map<String, List<Highscore>> scores;

    private static final String DIRECTORY = System.getProperty("user.home") + File.separator + ".laossweeper" + File.separator;
    /** The max number of characters a name can be */
    public static final int MAX_NAME_LENGTH = 4;
    /** The separator between the name and time that is used in the files */
    public static final char SEPARATOR = ':';

    /**
     * Creates a new high score object.
     */
    public Highscores() {
        scores = new HashMap<>();
    }

    /**
     * @return the singleton instance of the class.
     */
    public static Highscores getInstance() {
        if (instance == null)
            instance = new Highscores();
        return instance;
    }

    /**
     * Gets a list of high scores, dependent on the key.
     * <p>
     * If the list isn't loaded into the hashmap, it looks at the file. If the file
     * exists it reads and returns those values. If the file doesn't exist it just
     * creates and returns an empty list.
     * </p>
     * 
     * @param key the key associated with the list, in the format "Size_Difficulty"
     * @return the list with the, up to, 5 best high scores
     * @throws FileNotFoundException shouldn't happen
     */
    public List<Highscore> getHighscore(String key) throws FileNotFoundException {
        if (!scores.containsKey(key)) {
            File file = new File(DIRECTORY + key);
            if (file.exists()) {
                List<Highscore> list = new ArrayList<>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    // line[0] = navn
                    // line[1] = tid
                    String[] line = scanner.nextLine().split(String.valueOf(SEPARATOR));
                    try {
                        if (line.length != 2 || line[0].length() > MAX_NAME_LENGTH
                                || !line[0].matches("[a-zA-Z0-9_.-]{0,4}"))
                            throw new IOException("Highscore is ill formatted.");
                        list.add(new Highscore(line[0], Long.parseLong(line[1])));
                    } catch (Exception e) {
                        continue;
                    }
                }
                scores.put(key, list);
                scanner.close();
            } else {
                scores.put(key, new ArrayList<>());
            }
        }

        return scores.get(key);
    }

    /**
     * Sets a new high score. The list is automatically sorted, and if there is over
     * 5 values, they are removed.
     * 
     * @param key   the key associated with the list, in the format
     *              "Size_Difficulty"
     * @param score the new high score to insert
     * @throws FileNotFoundException shouldn't happen
     */
    public void setHighscore(String key, Highscore score) throws FileNotFoundException {
        List<Highscore> list = getHighscore(key);
        list.add(score);
        list.sort((e1, e2) -> (int) (e1.time - e2.time));
        if (list.size() > 5)
            list.remove(5);
    }

    /**
     * Saves all the high scores currently loaded into the hashmap. It will create
     * new files, if they don't already exist, or overwrite them if they do. Other
     * files in the directory are left alone.
     * <p>
     * The directory for the files is the "home/.laossweeper/", where home is the
     * users home dir.
     * </p>
     * <p>
     * The files are structured, so there is a high score on every line, where the
     * line is "name:time".
     * </p>
     */
    public void saveHighscores() {
        // Create folder if it doesn't exist
        File dir = new File(DIRECTORY);
        dir.mkdirs();

        // Save all the currently loaded lists
        scores.forEach((key, value) -> {
            try {
                FileWriter file = new FileWriter(DIRECTORY + key);

                // Create the contents of the file
                StringBuilder sb = new StringBuilder();
                value.forEach(hs -> sb.append(hs.name).append(SEPARATOR).append(hs.time).append('\n'));

                file.write(sb.toString());
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        });
    }
}
