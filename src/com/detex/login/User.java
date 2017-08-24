/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detex.login;

/**
 * User.java
 */
import java.io.Serializable;
import java.util.Objects;
//import java.util.ArrayList;

/**
 * <b>User</b> class to handle the login process and to keep track of the user's
 * counts.
 *
 * @author dhahaj
 *
 */
public class User implements Serializable {

    private static final long serialVersionUID = -7641865726502498527L;
    static final int PASSED_CNT = 0;
    static final int FAILED_CNT = 1;
    static final int PROG_CNT = 2;
    static final int TOTAL_CNT = 3;
    private String username = null;
    private String password = null;
    private boolean admin = false;
    private int passed_count = 0;
    private int failed_count = 0;
    private int pgm_count = 0;
    private int total_count = 0;

    /**
     * Constructor
     *
     * @param username The username.
     * @param password The password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.admin = false;
    }

    /**
     * Constructor
     *
     * @param username The username.
     * @param password The password.
     * @param admin If the user is an administrator.
     */
    public User(String username, String password, boolean admin) {
        this(username, password);
        this.admin = admin;
    }

    /**
     * incrementCount
     *
     * @param which Which count to increment.
     */
    public void incrementCount(int which) {
        switch (which) {
            case PASSED_CNT:
                this.passed_count++;
                break;
            case FAILED_CNT:
                this.failed_count++;
                break;
            case PROG_CNT:
                this.pgm_count++;
                break;
            case TOTAL_CNT:
                this.total_count++;
                break;
        }
    }

    /**
     * Returns the counts for the logged in <b>User</b>.
     *
     * @param which Which count to return.
     * @return int The requested count number.
     */
    public int getCount(int which) {
        switch (which) {
            case PASSED_CNT:
                return this.passed_count;
            case FAILED_CNT:
                return this.failed_count;
            case PROG_CNT:
                return this.pgm_count;
            case TOTAL_CNT:
                return this.total_count;
        }
        return 0;
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return this.username + " " + this.password + " " + this.admin;
    }

    /**
     * isAdmin
     *
     * @return boolean True if the user is an administrator.
     */
    public boolean isAdmin() {
        return this.admin;
    }

    /**
     * Returns username for the logged in <b>User</b>.
     *
     * @return String The username.
     */
    public String getName() {
        return this.username;
    }

    /**
     * Verify the password for a given user.
     *
     * @param p The password to check (ignores case).
     * @return True if the password matches this users password.
     */
    public boolean checkPassword(String p) {
        return p.equalsIgnoreCase(this.password);
    }

    /**
     * Returns the password for the logged in <b>User</b>.
     *
     * @return The password.
     */
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u = (User) obj;
            boolean equal = this.username.equalsIgnoreCase(u.username) && this.admin == u.admin && this.password.equalsIgnoreCase(u.password);
            return equal;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.username);
        hash = 47 * hash + Objects.hashCode(this.password);
        hash = 47 * hash + (this.admin ? 1 : 0);
        return hash;
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        super.clone();
        return new User(this.username, this.password, this.admin);
    }
}
