
package com.idk.shit.utils;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
    private final String fileName;
    private Map<Integer, Integer> highScores;

    public ScoreManager(String fileName) {
        this.fileName = fileName;
        highScores = new HashMap<>();
        loadScores();
    }

    private void loadScores() {
        File file = new File(fileName);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    int level = Integer.parseInt(parts[0]);
                    int score = Integer.parseInt(parts[1]);
                    highScores.put(level, score);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Integer, Integer> entry : highScores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateScore(int level, int newScore) {
        System.out.println("Updating level " + level + " with score " + newScore);
    
        int currentHighScore = highScores.getOrDefault(level, 0);
        if (newScore > currentHighScore) {
            highScores.put(level, newScore);
            System.out.println("New high score for level " + level + ": " + newScore);
            saveScores();
        }
    }

    public int getHighScore(int level) {
        return highScores.getOrDefault(level, -1);
    }
}

/*
package com.idk.shit.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
    private final String fileName;
    private Map<Integer, Integer> highScores;

    public ScoreManager(String fileName) {
        String currentDir = System.getProperty("user.dir");
        String dataDir = currentDir + File.separator + "data";
        this.fileName = dataDir + File.separator + fileName;
        this.highScores = new HashMap<>();

        // Создайте папку, если её нет
        new File(dataDir).mkdirs();

        System.out.println("Файл с рекордами: " + this.fileName);
        loadScores();
    }

    private void loadScores() {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Файл с рекордами не найден: " + fileName);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    int level = Integer.parseInt(parts[0]);
                    int score = Integer.parseInt(parts[1]);
                    highScores.put(level, score);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            System.err.println("Ошибка при чтении рекордов из файла: " + fileName);
        }
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Integer, Integer> entry : highScores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при сохранении рекордов в файл: " + fileName);
        }
    }

    public void updateScore(int level, int newScore) {
        System.out.println("Updating level " + level + " with score " + newScore);

        int currentHighScore = highScores.getOrDefault(level, 0);
        if (newScore > currentHighScore) {
            highScores.put(level, newScore);
            System.out.println("New high score for level " + level + ": " + newScore);
            saveScores();
        }
    }

    public int getHighScore(int level) {
        return highScores.getOrDefault(level, -1);
    }
}*/
