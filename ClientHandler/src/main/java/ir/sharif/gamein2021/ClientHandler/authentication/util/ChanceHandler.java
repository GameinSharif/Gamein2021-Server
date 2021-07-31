package ir.sharif.gamein2021.ClientHandler.authentication.util;

import java.util.HashMap;
import java.util.Random;

public class ChanceHandler {
    private static ChanceHandler instance = null;
    private HashMap<String, String> sessionIdToChanceMap = new HashMap<>();

    private ChanceHandler() {

    }

    public static ChanceHandler getInstance() {
        if (instance == null)
            instance = new ChanceHandler();
        return instance;
    }

    public String generateChance(String sessionId) {
        synchronized (this) {
            String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

            // create random string builder
            StringBuilder sb = new StringBuilder();

            // create an object of Random class
            Random random = new Random();

            // specify length of random string
            int length = 7;

            for (int i = 0; i < length; i++) {

                // generate random index number
                int index = random.nextInt(alphabet.length());

                // get character specified by index
                // from the string
                char randomChar = alphabet.charAt(index);

                // append the character to string builder
                sb.append(randomChar);
            }
            sessionIdToChanceMap.put(sessionId, sb.toString());
            return sb.toString();
        }
    }

    public String getChance(String sessionId) {
        return sessionIdToChanceMap.get(sessionId);
    }

    public void removeResponse(String sessionId) {
        sessionIdToChanceMap.remove(sessionId);
    }
}
