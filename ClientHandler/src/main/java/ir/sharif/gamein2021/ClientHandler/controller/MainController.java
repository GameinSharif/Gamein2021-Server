package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.service.PushMessageService;
import ir.sharif.gamein2021.ClientHandler.service.ServiceRepository;
import ir.sharif.gamein2021.ClientHandler.service.SocketSessionService;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@Component
public class MainController
{

    private final ServiceRepository serviceRepository;
    private final Gson gson;

    public MainController(ServiceRepository serviceRepository)
    {
        this.serviceRepository = serviceRepository;
        this.gson = new Gson();
    }

    public void HandleMessage(ProcessedRequest request)
    {
        switch (request.requestType)
        {
            case LOGIN:
                LoginRequest loginRequest = gson.fromJson(request.requestData, LoginRequest.class);
                serviceRepository.loginService.authenticate(request, loginRequest);
                break;

            default:
                System.out.println("Request type is invalid.");
        }
    }
}
