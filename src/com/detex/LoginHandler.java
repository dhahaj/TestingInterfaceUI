package com.detex;

/**
 *
 * @author Daniel
 */
public class LoginHandler {

    private LoginHandler() {
    }

    public static LoginHandler getInstance() {
        return LoginHandlerHolder.INSTANCE;
    }

    private static class LoginHandlerHolder {

        private static final LoginHandler INSTANCE = new LoginHandler();
    }
}
