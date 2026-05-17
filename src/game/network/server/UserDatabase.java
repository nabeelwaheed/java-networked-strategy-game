/**
 * Abstraction for authenticating users against a server-side data source.
 */
package game.network.server;

public interface UserDatabase {
    /**
     * Authenticates a user against a backing data source.
     *
     * @param username the username sent by the client
     * @param password the password sent by the client
     * @return true if authentication succeeds
     */
    boolean authenticate(String username, String password);
}
