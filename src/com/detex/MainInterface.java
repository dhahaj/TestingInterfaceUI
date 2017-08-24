package com.detex;

import com.detex.login.LoginThread;
import com.detex.login.User;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 *
 * @author dmh
 */
public class MainInterface {

   // static Preferences p = Preferences.userNodeForPackage(MainInterface.class);
    private static WindowFrame windowFrame;
    //    private static LoginThread loginThread;
    //    User user;

    private static void createGUI() {
        windowFrame = new WindowFrame();
      //  windowFrame.userBox.setSelectedIndex(p.getInt("LAST_INDEX", 0));
        windowFrame.setVisible(true);
       // loginThread = new LoginThread(new File("/com/detex/users.json"));
    }

    public static void main(String[] args) {

       /* InputStream stream = MainInterface.class.getClassLoader().getResourceAsStream("window.properties");
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(stream);
        } catch (Exception ex) {
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ex) {
            }
        }*/
        EventQueue.invokeLater(() -> MainInterface.createGUI());
    }
}
