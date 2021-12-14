package ir.sharif.gamein2021.ClientHandler.manager;

import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;

@Service
@Scope("singleton")
public class EncryptDecryptManager {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

//    private final RSAPublicKey publicKey;
//    private final RSAPrivateKey privateKey;

    // ----------------------------------------  constructor  ----------------------------------------
//    public EncryptDecryptManager() {
//        KeyPair keyPair = generateKeyPair();
//        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        this.publicKey = (RSAPublicKey) keyPair.getPublic();
//    }

    // ----------------------------------------  methods  ----------------------------------------
//    private KeyPair generateKeyPair() {
//
//        KeyPair keyPair = null;
//        try {
//
//            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//            generator.initialize(2048);
//            keyPair = generator.generateKeyPair();
//        } catch (Exception e) {
//            logger.debug(e);
//        }
//        return keyPair;
//    }

//    public String encryptMessage(String message) {
//
//        String encryptedMessage = null;
//        try {
//
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//            byte[] cipherTextBytes = cipher.doFinal(message.getBytes());
//            encryptedMessage = Base64.getEncoder().encodeToString(cipherTextBytes);
//
//        } catch (Exception e) {
//            logger.debug(e);
//        }
//        System.out.println(encryptedMessage);
//        return encryptedMessage;
//
//    }
//
//    public String decryptMessage(String encryptedMessage) {
//
//        String decryptedMessage = null;
//        try {
//
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.DECRYPT_MODE, privateKey);
//            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
//            decryptedMessage = new String(plainText);
//
//        } catch (Exception e) {
//            logger.debug(e);
//        }
//        return decryptedMessage;
//
//    }

//    public RSAPublicKey getPublicKey() {
//        return publicKey;
//    }


    private SecretKeySpec secretKey;
    private byte[] key;

    public EncryptDecryptManager() {
        MessageDigest sha = null;
        String myKey = "dafewiorhhcr234";
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String encryptMessage(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decryptMessage(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public SecretKeySpec getPublicKey() {
        return secretKey;
    }
}
