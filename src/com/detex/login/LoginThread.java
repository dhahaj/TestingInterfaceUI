package com.detex.login;

//import com.detex.*;
//import com.detex.User.*;
//import com.detex.logging.*;
//import com.detex.Utils.utils.*;
import com.detex.logging.MyLogger;
import java.io.File;
import java.io.Serializable;
//import java.text.*;
import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.*;

/**
 * @file LoginThread.java
 *
 * @brief Displays a user login dialog window.
 */
public class LoginThread extends Thread implements Serializable {

    // UserLoader Class loads users from the users.json file.
    private UserLoader loader;
    private static final long serialVersionUID = 388622392452789817L;
    private boolean running, loggedIn;
    public User thisUser;
    private ArrayList<User> userArrayList = null;
    private String filename;// = tester_project_F1_1.dpath + File.separator + "users.dat";
    private String currentUser;
    final static int TOTAL_CNT = 0, FAILED_CNT = 1, PASSED_CNT = 2, PROG_CNT = 3;
    private Timer loginTimer;
    static Frame f = null;
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    //getLogger(this.getClass().getName());

    private void init() {
        userArrayList = (ArrayList) loader.getUserList();
    }

    // Constructor
    public LoginThread(File userFile) {
        loader = new UserLoader(userFile);
        filename = userFile.getAbsolutePath();
        // this.log = tester_project_d4.log;
        running = false;
        loggedIn = false;
        this.init();
    }

    public LoginThread(Logger log) {
        loader = new UserLoader(new File(filename));
        // this.log = log;
        running = false;
        loggedIn = false;
        // testCount=testFailed=testPassed=progCount=0;
        // userArrayList = new ArrayList<User>();
        this.init();
    }

    @Override
    public void start() {
        loggedIn = false;
        running = true; // Start the run loop
        super.start();
    }

    private ArrayList<User> loadUsers() {
        return userArrayList;
    }

    public ArrayList<String> getUserNames() {
        ArrayList<String> arrayList = new ArrayList<>();
        userArrayList.forEach((u) -> {
            arrayList.add(u.getName());
        });
        return arrayList;
    }

    /**
     * ** MAIN LOOP ****
     */
    @Override
    public void run() {
        //this.init();
        loginTimer = new Timer(1800000, (ActionEvent evt) -> {
            LOGGER.log(Level.INFO, "\nUser {0} auto-tester_project_F1_1.log off\t", getUsername());
            logout();
        });

        ArrayList<String> usernames = new ArrayList<>();

        userArrayList.forEach((u) -> {
            usernames.add(u.getName());
        });

        final JComboBox userid = new JComboBox(usernames.toArray(new String[usernames.size()]));
        int previousIndex = (Integer) Keys.LAST_USER.value();

        try { // try incase the index is out of bounds
            int index = Keys.getInt("LAST_USER"); //(Integer)Keys.LAST_USER.value();
            LOGGER.log(Level.INFO, "Index={0}", index);
            userid.setSelectedIndex(index);
        } catch (Exception e) {
        }

        // Outer Loop
        while (running) {
            // Inner Loop
            while (!loggedIn) {
                final JPasswordField pwd = new JPasswordField(10);
                pwd.addAncestorListener(new RequestFocusListener());
                final JPanel panel = new JPanel(new GridLayout(2, 2));
                panel.add(new JLabel("UserID:"));
                panel.add(userid);
                panel.add(new JLabel("Password:"));
                panel.add(pwd);
                int action = JOptionPane.showConfirmDialog(f, panel, "LOGIN", JOptionPane.OK_CANCEL_OPTION);

                if (action == JOptionPane.OK_OPTION) { // OK button pressed, verify the password

                    // Get the user selected from the list
                    final User selectedUser = userArrayList.get(userid.getSelectedIndex());

                    // Store selected user
                    Keys.LAST_USER.setPref(userid.getSelectedIndex());

                    // Check the password
                    final String pass = new String(pwd.getPassword());
                    if (selectedUser.checkPassword(pass)) { // Password Accepted!
                        thisUser = selectedUser;
                        MyLogger.User = thisUser.getName();
                        loggedIn = true;
                        loginTimer.start();
                        enableMenu(thisUser.isAdmin());
                        // com.detex.Utils.utils.cleanGarbage();
                        break;
                    } else { // Display a bad password notification
                        try {
                            SwingUtilities.invokeAndWait(() -> {
                                JOptionPane.showMessageDialog(f, "Incorrect Password!", "Error", JOptionPane.ERROR_MESSAGE);
                            });
                        } catch (InterruptedException | InvocationTargetException e) {
                        }
                    }
                } else { // Cancel or escape was pressed. Prompt to either login or close software.
                    try {
                        SwingUtilities.invokeAndWait(() -> {
                            int exitChoice = JOptionPane.showConfirmDialog(f, "Are you sure you want to exit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
                            if (exitChoice != JOptionPane.YES_OPTION) {
                                return;
                            }
                            LOGGER.info("Software closed");
                            quit();
                            System.exit(0);
                        });
                    } catch (InterruptedException | InvocationTargetException e) {
                    }
                }
            } // END_INNER_LOOP
        } // END_OUTER_LOOP
    }

    public boolean loggedin() {
        return loggedIn;
    }

    public void logout() {
        if (loggedIn) {
            synchronized (this) {
                LOGGER.log(Level.INFO, "\nUser {0} logged off\n\t*devices passed: {1}\n\t*devices failed: {2}\n\t*total devices tested: {3}\n\t*devices programmed: {4}\n\t*", new Object[]{thisUser.getName(), thisUser.getCount(PASSED_CNT), thisUser.getCount(FAILED_CNT), thisUser.getCount(TOTAL_CNT), thisUser.getCount(PROG_CNT)});
            }
            MyLogger.User = getUsername();
            loggedIn = false;
        }

        // Stop the timer if running
        if (loginTimer.isRunning()) {
            loginTimer.stop();
        }

        loggedIn = false;
        running = true;
        //    this.run();
    }

    public void restartTimer() {
        if (loggedIn) {
            loginTimer.restart();
        }
    }

    public String getUsername() {
        if (loggedIn && thisUser.getName() != null) {
            return thisUser.getName();
        } else {
            return null;
        }
    }

    public User getUser() {
        return this.thisUser;
    }

    // Our method that quits the thread
    public void quit() {
        // Stop the timer
        try {
            if (loginTimer.isRunning()) {
                loginTimer.stop();
            }
        } catch (Exception t) {
        }
        loggedIn = false;
        running = false;  // Setting running to false ends the loop in run()
        System.out.println("LoginThread is stopping.");
        // In case the thread is waiting. . .
        // interrupt();
    }

    public void addNewUser(String name, String pass, boolean admin) {
        User newUser = new User(name, pass, admin);
        userArrayList.add(newUser);
        loader.addUser(newUser);
    }

    public String removeUser(String username) {
        try {
            User u = null;
            for (User user : userArrayList) {
                if (user.getName().equals(username)) {
                    u = user;
                }
            }
            loader.removeUser(u);
        } catch (Exception e) {
        }
        return username;
    }

    public boolean isAdmin() {
        return thisUser.isAdmin();
    }

    public boolean running() {
        return running;
    }

    public class RequestFocusListener implements AncestorListener {

        private boolean removeListener;

        public RequestFocusListener() {
            this(true);
        }

        public RequestFocusListener(boolean removeListener) {
            this.removeListener = removeListener;
        }

        @Override
        public void ancestorAdded(AncestorEvent e) {
            JComponent component = e.getComponent();
            component.requestFocusInWindow();
            if (removeListener) {
                component.removeAncestorListener(this);
            }
        }

        @Override
        public void ancestorMoved(AncestorEvent e) {
        }

        @Override
        public void ancestorRemoved(AncestorEvent e) {
        }
    }

    public void enableMenu(boolean enable) {
        MenuBar mb = f.getMenuBar();
        Menu config = mb.getMenu(3);
        // println(config.getLabel());
        MenuItem adminMenu = config.getItem(config.getItemCount() - 1);
        adminMenu.setEnabled(enable);
    }
}
