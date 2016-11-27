package se.ju.taun15a16.group5.mjilkmjecipes.backend;

/**
 * Created by kevin on 27.11.2016.
 */

public class AccountManager {

    // Singleton Variable
    private static AccountManager managerInstance = new AccountManager();

    // Private constructor
    private AccountManager(){
    }

    // Get AccountManager instance
    public static AccountManager getInstance() {
        return managerInstance;
    }
}
