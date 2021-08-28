package ir.sharif.gamein2021.ClientHandler.service;

import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;

@Service
@Scope("singleton")
public class EncryptDecryptService {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    // ----------------------------------------  constructor  ----------------------------------------
    public EncryptDecryptService() {
        KeyPair keyPair = generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    // ----------------------------------------  methods  ----------------------------------------
    private KeyPair generateKeyPair() {

        KeyPair keyPair = null;
        try {

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            keyPair = generator.generateKeyPair();

        } catch (Exception e) {
            logger.debug(e);
        }
        return keyPair;

    }

    public String encryptMessage(String message){

        String encryptedMessage = null;
        try {

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherTextBytes = cipher.doFinal(message.getBytes());
            encryptedMessage = Base64.getEncoder().encodeToString(cipherTextBytes);

        } catch (Exception e) {
            logger.debug(e);
        }
        System.out.println(encryptedMessage);
        return encryptedMessage;

    }

    public String decryptMessage(String encryptedMessage){

        String decryptedMessage = null;
        try {

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
            decryptedMessage = new String(plainText);

        } catch (Exception e) {
            logger.debug(e);
        }
        return decryptedMessage;

    }

    public PublicKey getPublicKey() {
        return publicKey;
    }


}
