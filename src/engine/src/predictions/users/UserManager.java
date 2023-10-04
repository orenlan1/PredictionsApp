package predictions.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {
    private final Set<String> usersSet;

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public synchronized boolean addUser(String username) {
        if ( !usersSet.contains(username)) {
            usersSet.add(username);
            return true;
        }
        else
            return false;
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

}
