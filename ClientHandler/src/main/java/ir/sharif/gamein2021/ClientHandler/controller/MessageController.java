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
import ir.sharif.gamein2021.core.Service.TeamService;
import ir.sharif.gamein2021.core.Service.UserService;
import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.entity.Chat;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class MessageController {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManager pushMessageManager;
    private final ChatService chatService;
    private final MessageService messageService;
    private final TeamService teamService;
    private final Gson gson = new Gson();

    public MessageController(PushMessageManager pushMessageManager,TeamService teamService, MessageService messageService, ChatService chatService)
    {
        this.chatService = chatService;
        this.messageService = messageService;
        this.teamService = teamService;
        this.pushMessageManager = pushMessageManager;
    }

    public void createNewOffer(ProcessedRequest request, NewMessageRequest newMessageRequest) {

        NewMessageResponse newMessageResponse;
        ChatDto chatDto = chatService.getChatByTeamId(
                newMessageRequest.getMessageDto().getSenderTeamId(),
                newMessageRequest.getMessageDto().getReceiverTeamId()
        );
        try
        {
            if (chatDto == null) {

                List<MessageDto> messageDtos = new ArrayList<>();
                ChatDto createdChatDto = ChatDto.builder()
                        .team1Id(newMessageRequest.getMessageDto().getSenderTeamId())
                        .team2Id(newMessageRequest.getMessageDto().getReceiverTeamId())
                        .messageDtos(messageDtos)
                        .build();
                ChatDto savedChatDto = chatService.save(createdChatDto);

                MessageDto createdMessageDto = MessageDto.builder()
                        .dateTime(LocalDateTime.now())
                        .chatId(savedChatDto.getId())
                        .senderTeamId(newMessageRequest.getMessageDto().getSenderTeamId())
                        .receiverTeamId(newMessageRequest.getMessageDto().getReceiverTeamId())
                        .text(newMessageRequest.getMessageDto().getText())
                        .build();

                MessageDto savedMessageDto = messageService.save(newMessageRequest.getMessageDto());
                savedChatDto.getMessageDtos().add(savedMessageDto);
                chatService.update(savedChatDto);

                newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, createdMessageDto, "OK");

            } else {
                if (chatDto.getMessageDtos().size() >= 20) {
                    chatDto.getMessageDtos().remove(0);
                }
                chatDto.getMessageDtos().add(newMessageRequest.getMessageDto());
                MessageDto savedMessageDto = messageService.save(newMessageRequest.getMessageDto());

                chatService.save(chatDto);
                savedMessageDto.setId(null);
                newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, savedMessageDto, "!!!!");
            }
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
