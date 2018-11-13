package ru.shpi0.snatrisx.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;

public class FileProcessor {

    private static final String DATA_FOLDER = "data/";
    private static final String SCORES_FILE = "scores.dat";
    private static final String PREFS_FILE = "prefs.dat";
    private static Gson gson = new Gson();

    private static FileHandle scoresFileHandle = Gdx.files.local(DATA_FOLDER).child(SCORES_FILE);
    private static FileHandle prefsFileHandle = Gdx.files.local(DATA_FOLDER).child(PREFS_FILE);

    public static boolean ifScoresFileExist() {
        return Gdx.files.local(DATA_FOLDER).child(SCORES_FILE).exists();
    }

    public static boolean ifPrefsFileExist() {
        return Gdx.files.local(DATA_FOLDER).child(PREFS_FILE).exists();
    }

    private static String toJSON(Object o) {
        return gson.toJson(o);
    }

    private static GamePreferences gamePreferencesFromJSON(String s) {
        return gson.fromJson(s, GamePreferences.class);
    }

    public static GamePreferences loadGamePreferencesFromFile() {
        String s = prefsFileHandle.readString();
        System.out.println(s);
        return gamePreferencesFromJSON(s);
    }

    public static void saveGamePreferencesToFile(GamePreferences gamePreferences) {
        prefsFileHandle.writeString(toJSON(gamePreferences), false);
    }

    public static void createGamePreferencesFile() {
        prefsFileHandle.writeString("null", false);
    }

}
