package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Messenger.NewMessageRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Messenger.NewMessageResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.service.ChatService;
import ir.sharif.gamein2021.core.service.MessageService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager pushMessageManager;
    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;
    private final TeamService teamService;
    private final Gson gson = new Gson();

    public MessageController(LocalPushMessageManager pushMessageManager, TeamService teamService, MessageService messageService, ChatService chatService, UserService userService)
    {
        this.chatService = chatService;
        this.messageService = messageService;
        this.teamService = teamService;
        this.pushMessageManager = pushMessageManager;
        this.userService = userService;
    }

    public void addNewChatMessage(ProcessedRequest request, NewMessageRequest newMessageRequest)
    {
        Integer id = newMessageRequest.playerId;
        UserDto userDto = userService.loadById(id);
        Integer senderTeamId = userDto.getTeam().getId();

        MessageDto messageDto = MessageDto.builder()
                .insertedAt(newMessageRequest.getInsertedAt())
                .senderTeamId(senderTeamId)
                .receiverTeamId(newMessageRequest.getReceiverTeamId())
                .text(newMessageRequest.getText())
                .build();

        NewMessageResponse newMessageResponse;

        ChatDto chatDto = chatService.getChatByTeamId(
                teamService.findTeamById(senderTeamId),
                teamService.findTeamById(newMessageRequest.getReceiverTeamId())
        );

        try
        {
            if (chatDto == null) //initialize the chat
            {
                List<MessageDto> messageDtos = new ArrayList<>();
                messageDtos.add(messageDto);

                chatService.addNewChat(ChatDto.builder()
                        .team1Id(messageDto.getSenderTeamId())
                        .team2Id(messageDto.getReceiverTeamId())
                        .messages(messageDtos)
                        .latestMessageDate(messageDto.getInsertedAt())
                        .build()
                );

                newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, messageDto, "OK");
            }
            else
            {
                try
                {
                    if (chatDto.getMessages().size() >= GameConstants.ChatMaxMessagesCount)
                    {
                        chatDto.getMessages().remove(0);
                    }
                    chatDto.getMessages().add(messageDto);

                    chatService.addNewChat(chatDto);

                    newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, messageDto, "OK");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, null, "Failed to Send the Message!");
                }
            }
        } catch (Exception e)
        {
            logger.debug(e);
            System.out.println(e);
            e.printStackTrace();
            newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, null, "Failed to Send the Message!");
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newMessageResponse));
        pushMessageManager.sendMessageByTeamId(newMessageResponse.getMessageDto().getReceiverTeamId().toString(), gson.toJson(newMessageResponse));
    }

}
