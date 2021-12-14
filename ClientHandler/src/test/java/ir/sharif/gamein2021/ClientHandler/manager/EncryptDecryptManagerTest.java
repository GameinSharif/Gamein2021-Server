package ir.sharif.gamein2021.ClientHandler.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptDecryptManagerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void encryptTest(){
        EncryptDecryptManager manager = new EncryptDecryptManager();
        String encrypted = manager.encryptMessage("{\n" +
                "  \"requestTypeConstant\":0,\n" +
                "  \"username\":\"team1\",\n" +
                "  \"password\": \"12345\"\n" +
                "}");
        System.out.println(encrypted);

        String result = manager.decryptMessage(encrypted);
        System.out.println(result);
    }

    @Test
    public void decryptTest(){
        EncryptDecryptManager manager = new EncryptDecryptManager();
        String encryptedMessage = "pkpR07TYcf5h/GujZoYESAcHCRRwzG9YHqcTgC6UmNikFeorPQqUB2u9xpvWaQXk2XJZEDiLgO9w9GjY/++PxkTXFIXKppXRvL2fG2bEJd/TBE3qvp0cDZcJSQMs5Ss15NucTyDivr8XnU0bki98CDcsWQYPI5+Y/4fbzCJGxK0AiLwwOS9pVjtF8vhyq/CZAtdTqUT7i/2O5XcnaOiLSRturK7rl2a5UzVdCxJbu1rReso9mty++VeFSqwaNnNj8E9wwC5xb4ORbqp+N6et4WKImGCATpqO/jvK0Y5MNfMXJ6NsyXITL/jBjoE0fJ2dB01TnP2y3BGAnYpVprhVkg==";
        String result = manager.decryptMessage(encryptedMessage);
        System.out.println(result);
    }
}