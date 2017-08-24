/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detex.login;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Class which reads a json formatted file that contains all the configured user
 * and their associated passwords.
 *
 * @author dhahaj
 *
 */
// @SuppressWarnings("unused")
public class UserLoader {

    private final static String KEY_TOP = "users", KEY_USRNAME = "name", KEY_PSWD = "password", KEY_ADMIN = "admin";
    private JSONObject userObject = null;
    private JSONArray userArray = null;
    private List<User> users;
    private File jsonFile = null;
    PApplet parent = null;

    /**
     * Constructor
     *
     * @param path The <b>String</b> path to the configuration file.
     */
    public UserLoader(String path) {
        this(null, path);
        this.users = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param f The <b>File</b> containing the configuration info.
     */
    public UserLoader(File f) {
        this(null, f);
        this.users = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param parent A Processing <strong>PApplet</strong> instance.
     * @param path The <b>String</b> path to the configuration file.
     */
    public UserLoader(PApplet parent, String path) {
        this.users = new ArrayList<>();
        this.parent = parent;
        jsonFile = new File(path);
        init();
    }

    /**
     * Constructor
     *
     * @param parent A Processing <strong>PApplet</strong> instance.
     * @param file
     */
    public UserLoader(PApplet parent, File file) {
        this.users = new ArrayList<>();
        this.parent = parent;
        jsonFile = file;
        init();
    }

    /**
     * Initializes the <b>PApplet</b> if not provided and initializes the
     * <b>JSON</b> object and array instances.
     */
    private void init() {
        if (parent == null) {
            parent = new PApplet();
        }
        userObject = PApplet.loadJSONObject(jsonFile);
        userArray = userObject.getJSONArray(KEY_TOP);
        loadUsers();
    }

    /**
     * <p>
     * Loads the <b>User</b> objects from the json file.
     * </p>
     */
    private void loadUsers() {
        for (int i = 0; i < userArray.size(); i++) {
            JSONObject user = userArray.getJSONObject(i);
            String name = user.getString(KEY_USRNAME);
            String pass = user.getString(KEY_PSWD);
            boolean admin = user.getBoolean(KEY_ADMIN);
            User u = new User(name, pass, admin);
            // println(u.toString());
            users.add(u);
        }
    }

    /**
     * Returns all the <b>User</b> objects from the json file.
     *
     * @return <b>User[] User</b> array
     */
    public User[] getUsers() {
        return users.toArray(new User[0]);
    }

    /**
     * Returns the json configuration <b><em>File</em></b>.
     *
     * @return <b>File</b>
     */
    public File getJSONFile() {
        return jsonFile;
    }

    /**
     * Returns a <b><i>List</i></b> object containing the loaded users.
     *
     * @return List<<a>User</a>>
     */
    public List<User> getUserList() {
        return users;
    }

    /**
     * Adds a new <b>User</b> to the configuration. The config <b>File</b> is
     * then also updated and saved to include the new <b>User</b>.
     *
     * @param newUser The <b>User</b> to be added to the configuration data.
     */
    public void addUser(User newUser) {
        saveJSONFile();
        users.add(newUser);
    }

    /**
     * Removes a saved {@code User}.
     *
     * @param u The <code>User</code> to be removed.
     * @throws UserNotFoundException Thrown if the user is not found.
     */
    void removeUser(User u) throws Exception {
        if (!users.contains(u)) {
            throw new Exception("User not found.");
        } else {
            users.remove(u);
            saveJSONFile();
        }
    }

    /**
     * Saves the current <code>List</code> of Users to the JSON file.
     */
    private void saveJSONFile() {
        JSONArray values = new JSONArray();
        int i = 0;
        for (User u : users) {
            JSONObject user = new JSONObject();
            user.setBoolean(KEY_ADMIN, u.isAdmin());
            user.setString(KEY_USRNAME, u.getName());
            user.setString(KEY_PSWD, u.getPassword());
            values.setJSONObject(i++, user);
        }
        JSONObject json = new JSONObject();
        json.setJSONArray(KEY_TOP, values);
        parent.saveJSONObject(json, jsonFile.getPath());
    }

}
