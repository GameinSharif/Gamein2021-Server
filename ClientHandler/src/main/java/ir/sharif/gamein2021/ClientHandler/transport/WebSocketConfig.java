package ir.sharif.gamein2021.ClientHandler.transport;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Controller
@ComponentScan("ir.sharif.gamein2021")
public class WebSocketConfig implements WebSocketConfigurer
{
    private final SocketHandler socketHandler;

    public WebSocketConfig(SocketHandler socketHandler)
    {
        this.socketHandler = socketHandler;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(socketHandler, "/user");
    }
}