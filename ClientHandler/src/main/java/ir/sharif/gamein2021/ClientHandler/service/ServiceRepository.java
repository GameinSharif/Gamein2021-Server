package ir.sharif.gamein2021.ClientHandler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceRepository {
    @Autowired
    public LoginService loginService;

    @Autowired
    public PushMessageService pushMessageService;

    @Autowired
    public SocketSessionService socketSessionService;

    @Autowired
    public EncryptDecryptService encryptDecryptService;

    @Autowired
    public RFQService rfqService;

}
