/**
 * File-backed user database used by server
 *
 * Credentials are stored as plain-text username=password pairs because
 * the assignment only requires a simple text-based authentication check.
 */
package game.network.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUserDatabase implements UserDatabase {
    private final Path databasePath;
    private final Map<String, String> credentials;

    public FileUserDatabase(Path databasePath) throws IOException {
        this.databasePath = databasePath;
        this.credentials = new HashMap<>();
        initializeIfMissing();
        loadCredentials();
    }

    /**
     * Checks whether the username and password match the loaded credentials.
     *
     * @param username the username sent by the client
     * @param password the password sent by the client
     * @return true if the credentials are valid
     */
    @Override
    public boolean authenticate(String username, String password) {
        return password != null && password.equals(credentials.get(username));
    }

    /**
     * Creates a small default credential file when none exists
     *
     * @throws IOException if the file cannot be created
     */
    private void initializeIfMissing() throws IOException {
        if (Files.exists(databasePath)) {
            return;
        }
        Files.createDirectories(databasePath.getParent());
        Files.write(databasePath, List.of(
                "PlayerOne=password123",
                "Tester=test123",
                "VillageAdmin=admin123"
        ), StandardCharsets.UTF_8);
    }

    /**
     * Loads username and password pairs from the credential file.
     *
     * @throws IOException if the file cannot be read
     */
    private void loadCredentials() throws IOException {
        for (String line : Files.readAllLines(databasePath, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                continue;
            }
            String[] parts = trimmed.split("=", 2);
            credentials.put(parts[0].trim(), parts[1].trim());
        }
    }
}
