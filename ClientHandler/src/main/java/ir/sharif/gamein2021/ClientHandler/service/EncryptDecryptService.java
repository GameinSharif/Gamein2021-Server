package ir.sharif.gamein2021.ClientHandler.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class EncryptDecryptService {

    public String encryptMessage(String message){
        //TODO
        return message;
    }

    public String decryptMessage(String encMessage){
        //TODO
        return encMessage;
    }
}
