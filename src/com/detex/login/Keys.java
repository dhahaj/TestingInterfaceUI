/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detex.login;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.prefs.*;

public enum Keys {
    QW_PATH("/pictools/qw.exe"), QW_ARGS("/D16F627 /A /X"), QW_ARUN("/A"), QW_AEXIT("/X"), QW_CHIP("/D16F627"),
    FIRMWARE_FOLDER("C:\\Firmware"), LAST_SERIAL(0), // The last serial number
    // used

    EAX300_PATH("C:\\EAX_300_5.9\\EAX300_5.9.OBJ"), EAX300_FILE("EAX300_5.9.OBJ"), EAX300_EXT("_5.9.OBJ"),
    EAX500_PATH("C:\\EAX_500_v5.9.1\\EAX500_591.OBJ"), EAX500_FILE("EAX500_591.OBJ"), EAX500_EXT("_591.OBJ"),
    EAX2500_PATH("C:\\EAX_2500_V73\\EAX2500_73.OBJ"), EAX2500_FILE("EAX2500_73.OBJ"), EAX2500_EXT("_73.OBJ"),
    EAX3500_PATH("C:\\EAX_3500_A0\\EAX3500_A0.OBJ"), EAX3500_FILE("EAX3500_A0.OBJ"), EAX3500_EXT("_A0.OBJ"),
    V40_PATH("C:\\v40_ver4_0\\V40xx00_40.OBJ"), V40_FILE("V40xx00_54.OBJ"), V40_EXT("_54.OBJ"),
    WIDTH_SCALER(65), HEIGHT_SCALER(75), LAST_USER(10), LAST_DELAY(594), COM_PORT("COM 1"), COM(1);

    public Object value;
    public Object defaultValue;
    static Preferences prefs = null;

    static {
        prefs = Preferences.userRoot();
    }

    @SuppressWarnings("rawtypes")
    static Class klass = null;

    // Constructors:
    Keys(String defVal) {
        this.defaultValue = defVal;
        this.value = this.defaultValue;
        // this.value = (String) prefs.get(this.name(), defVal);
        // System.out.println("Loaded " + this.name() + ": " + this.value);
    }

    Keys(int defVal) {
        this.defaultValue = defVal;
        this.value = this.defaultValue;
        // this.value = ((Integer) (prefs.getInt(this.name(),
        // defVal))).intValue();
        // System.out.println("Loaded " + this.name() + ": " + this.value);
    }

    Keys(boolean defVal) {
        this.defaultValue = defVal;
        this.value = this.defaultValue;
        // this.value = ((Boolean) (prefs.getBoolean(this.name(),
        // defVal))).booleanValue();
        // System.out.println("Loaded " + this.name() + ": " + this.value);
    }

    public Object value() {
        return value;
    }

    @SuppressWarnings("rawtypes")
    public static void setLoggingClass(Class c) {
        klass = c;
        prefs = Preferences.userNodeForPackage(klass);
        initValues();
    }

    public static void setPrefs(Preferences p) {
        prefs = p;
        initValues();
    }

    public static String readPrefs() {
        StringBuilder sb = new StringBuilder();
        try {
            String[] strings = prefs.keys();
            for (String s : strings) {
                sb.append(MessageFormat.format("{0}: ", s));
                String val = prefs.get(s, "not found");
                sb.append(val).append("\n");
            }
        } catch (BackingStoreException e) {
        }
        return sb.toString();
    }

    public static void importPrefs(String path) {
        InputStream is = null;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
        }
        try {
            Preferences.importPreferences(is);
        } catch (IOException | InvalidPreferencesFormatException e) {
        }
    }

    private static void initValues() {
        String[] storedValues = {};
        try {
            storedValues = prefs.keys();
        } catch (BackingStoreException e) {
        }
        List<String> valueList = new ArrayList<>();
        valueList.addAll(Arrays.asList(storedValues));

        // Iterate thru the keys and assign values that were previously stored
        // in the preference node.
        for (Keys k : Keys.values()) {
            if (valueList.contains(k)) {
                int index = valueList.indexOf(k);
                k.value = valueList.get(index);
            }
        }
    }

    public static Preferences getPrefs() {
        // if (klass == null)
        // throw new Exception("Class for the Logger has not been set.");
        return prefs;
    }

    public void setPref(String value) {
        Keys.prefs.put(this.name(), value);
        this.value = value;
    }

    public void setPref(int value) {
        Keys.prefs.putInt(this.name(), value);
        this.value = value;
    }

    public static int getInt(String key) {
        return prefs.getInt(key, 0);
    }

    public static String getString(String key) {
        return prefs.get(key, null);
    }

    public void clearPref() {
        this.value = defaultValue;
        if (defaultValue instanceof Integer) {
            this.setPref((Integer) defaultValue);
        } else if (defaultValue instanceof String) {
            this.setPref((String) defaultValue);
        }
    }

    public static void clearAllPrefs() {
        for (Keys key : Keys.values()) {
            key.clearPref();
        }
    }

    @Override
    public String toString() {
        return this.name() + ": " + this.value;
    }

    public static void exportPrefs(String path) {
        try {
            prefs.flush();
            prefs.sync();
        } catch (BackingStoreException e1) {
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            Keys.prefs.exportNode(os);
        } catch (IOException | BackingStoreException e) {
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
