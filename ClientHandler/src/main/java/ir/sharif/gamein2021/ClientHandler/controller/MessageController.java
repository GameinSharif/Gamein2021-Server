package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Messenger.NewMessageRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Messenger.NewMessageResponse;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetOffersResponse;
import ir.sharif.gamein2021.ClientHandler.manager.EncryptDecryptManager;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.manager.SocketSessionManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.Service.ChatService;
import ir.sharif.gamein2021.core.Service.MessageService;
import ir.sharif.gamein2021.core.Service.UserService;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageController {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManager pushMessageManager;
    private final MessageService messageService;
    private final ChatService chatService;
    private final Gson gson = new Gson();

    public MessageController(PushMessageManager pushMessageManager, MessageService messageService, ChatService chatService)
    {
        this.chatService = chatService;
        this.pushMessageManager = pushMessageManager;
        this.messageService = messageService;
    }

    public void createNewOffer(ProcessedRequest request, NewMessageRequest newMessageRequest) {
        MessageDto messageDto = newMessageRequest.getMessageDto();
        if (messageDto.getId() == null) {

        }
        NewMessageResponse newMessageResponse;
        try
        {
            MessageDto sendingMessageDto = messageService.save(newMessageRequest.getMessageDto());
            newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, sendingMessageDto, "OK");
        }
        catch (Exception e)
        {
            logger.debug(e);
            newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, null, "Failed to Send the Message!");
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newMessageResponse));
        pushMessageManager.sendMessageByTeamId(newMessageResponse.getMessageDto().getReceiverTeamId().toString(), gson.toJson(newMessageResponse));
    }

}
