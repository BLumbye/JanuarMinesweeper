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

public class Highscores {
    private static Highscores instance = null;

    private Map<String, List<Highscore>> scores;
    private final String DIRECTORY = System.getProperty("user.home") + File.separator + ".laossweeper" + File.separator;
    public final int MAX_NAME_LENGTH = 4;

    public Highscores() {
        scores = new HashMap<>();
    }

    public static Highscores getInstance() {
        if (instance == null) instance = new Highscores();
        return instance;
    }

    public List<Highscore> getHighscore(String key) throws FileNotFoundException {
        if (!scores.containsKey(key)) {
            File file = new File(DIRECTORY + key);
            if (file.exists()) {
                List<Highscore> list = new ArrayList<>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    // line[0] = navn
                    // line[1] = tid
                    String[] line = scanner.nextLine().split(":");
                    try {
                        if (line[0].length() > MAX_NAME_LENGTH) throw new IOException("Highscore is ill formatted.");
                        list.add(new Highscore(line[0], Long.parseLong(line[1])));
                    } catch (Exception e) {
                        e.printStackTrace();
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

    public void setHighscore(String key, Highscore score) throws FileNotFoundException {
        List<Highscore> list = getHighscore(key);
        list.add(score);
        list.sort((e1, e2) -> (int) (e1.time - e2.time));
        if (list.size() > 5) list.remove(5);
    }

    public void saveHighscores() {
        // Create folder if it doesn't exist
        File dir = new File(DIRECTORY);
        dir.mkdirs();

        scores.forEach((key, value) -> {
            try {
                FileWriter file = new FileWriter(DIRECTORY + key);
                String s = "";
                for (Highscore highscore : value) {
                    s += highscore.name + ":" + highscore.time + "\n";
                }
                file.write(s);
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        });
    }
}

