package com.sbplat.inventorysearcher.configuration;

import java.awt.Color;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Config {

    private String configPath;

    private boolean searchLore;
    private boolean coverBorders;
    private Color highlightColor;
    private int highlightAlpha;

    public Config(String configPath) {
        this.configPath = configPath;
        reload();
    }

    public void reload() {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> map = null;
        try {
            map = gson.fromJson(new FileReader(getConfigFile()), mapType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (map == null) {
            map = new HashMap<>();
        }
        searchLore = Boolean.parseBoolean(map.getOrDefault("searchLore", "false"));
        coverBorders = Boolean.parseBoolean(map.getOrDefault("coverBorders", "false"));
        try {
            highlightColor = Color.decode(map.getOrDefault("highlightColor", "0x000000"));
        } catch (NumberFormatException e) {
            highlightColor = Color.BLACK;
        }
        highlightAlpha = Math.max(0, Math.min(255, Integer.parseInt(map.getOrDefault("highlightAlpha", "128"))));
        save();
    }

    public boolean getSearchLore() {
        return searchLore;
    }

    public boolean getCoverBorders() {
        return coverBorders;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public int getHighlightAlpha() {
        return highlightAlpha;
    }

    public void setSearchLore(boolean searchLore) {
        this.searchLore = searchLore;
        save();
    }

    public void setCoverBorders(boolean coverBorders) {
        this.coverBorders = coverBorders;
        save();
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
        save();
    }

    public void setHighlightAlpha(int highlightAlpha) {
        this.highlightAlpha = Math.max(0, Math.min(255, highlightAlpha));
        save();
    }

    public void save() {
        Map<String, String> map = new HashMap<>();
        map.put("searchLore", Boolean.toString(searchLore));
        map.put("coverBorders", Boolean.toString(coverBorders));
        map.put("highlightColor", "0x" + Integer.toHexString(highlightColor.getRGB()).substring(2).toUpperCase());
        map.put("highlightAlpha", Integer.toString(highlightAlpha));
        try (FileWriter writer = new FileWriter(getConfigFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getConfigFile() {
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return configFile;
    }
}
