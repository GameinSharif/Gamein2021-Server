package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Messenger.*;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.dto.ReportDto;
import ir.sharif.gamein2021.core.domain.entity.Report;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.service.ChatService;
import ir.sharif.gamein2021.core.service.MessageService;
import ir.sharif.gamein2021.core.service.ReportService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManagerInterface pushMessageManager;
    private final ChatService chatService;
    private final ReportService reportService;
    private final MessageService messageService;
    private final TeamService teamService;
    private final Gson gson = new Gson();

    public MessageController(PushMessageManagerInterface pushMessageManager, ReportService reportService, TeamService teamService, ChatService chatService, MessageService messageService)
    {
        this.chatService = chatService;
        this.teamService = teamService;
        this.reportService = reportService;
        this.messageService = messageService;
        this.pushMessageManager = pushMessageManager;
    }

    public void reportMessage(ProcessedRequest request, ReportMessageRequest reportMessageRequest)
    {
        ReportMessageResponse reportMessageResponse;
        try
        {
            ChatDto chatDto = chatService.loadById(reportMessageRequest.getChatId());
            MessageDto messageDto = messageService.getMessageByTextAndChatId(chatDto.getId(), reportMessageRequest.getReportedTeamId(), reportMessageRequest.getMessageText(), reportMessageRequest.getInsertedAt());

            if (messageDto == null)
            {
                reportMessageResponse = new ReportMessageResponse(ResponseTypeConstant.REPORT_MESSAGE, "NOT Successful", null);
            }
            else if (!isChatAndTeamIdsValid(reportMessageRequest.getChatId(), request.teamId, reportMessageRequest.getReportedTeamId()))
            {
                reportMessageResponse = new ReportMessageResponse(ResponseTypeConstant.REPORT_MESSAGE, "NOT Successful 2", null);
            }
            else if (isReportDuplicated(messageDto, reportMessageRequest, request.teamId))
            {
                reportMessageResponse = new ReportMessageResponse(ResponseTypeConstant.REPORT_MESSAGE, "NOT Successful 3", null);
            }
            else
            {
                ReportDto reportDto = ReportDto.builder()
                        .messageText(reportMessageRequest.getMessageText())
                        .chatId(chatDto.getId())
                        .reportedTeamId(reportMessageRequest.getReportedTeamId())
                        .reporterTeamId(request.teamId)
                        .reportedAt(LocalDateTime.now())
                        .sentAt(messageDto.getInsertedAt())
                        .build();
                reportService.saveOrUpdate(reportDto);
                reportMessageResponse = new ReportMessageResponse(ResponseTypeConstant.REPORT_MESSAGE, "Message Reported!", messageDto);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            reportMessageResponse = new ReportMessageResponse(ResponseTypeConstant.REPORT_MESSAGE, "An Error Occurred!", null);
        }

        if (reportMessageResponse.getMessage() == null)
        {
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(reportMessageResponse));
        }
        else
        {
            pushMessageManager.sendMessageByTeamId(request.teamId.toString(), gson.toJson(reportMessageResponse));
        }
    }

    public void addNewChatMessage(ProcessedRequest request, NewMessageRequest newMessageRequest)
    {
        Integer senderTeamId = request.teamId;

        MessageDto messageDto = MessageDto.builder()
                .insertedAt(LocalDateTime.now())
                .senderTeamId(senderTeamId)
                .receiverTeamId(newMessageRequest.getReceiverTeamId())
                .text(newMessageRequest.getText())
                .build();

        NewMessageResponse newMessageResponse;

        ChatDto chatDto = chatService.getChatByTeamId(senderTeamId, newMessageRequest.getReceiverTeamId());

        try
        {
            if (chatDto == null) //initialize the chat
            {
                chatDto = chatService.addNewChat(ChatDto.builder()
                        .team1Id(messageDto.getSenderTeamId())
                        .team2Id(messageDto.getReceiverTeamId())
                        .messages(new ArrayList<>())
                        .latestMessageDate(messageDto.getInsertedAt())
                        .build()
                );

                messageDto.setChatId(chatDto.getId());
                chatDto.getMessages().add(messageDto);
                chatService.saveOrUpdate(chatDto);

                newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, chatDto, messageDto, "OK");
            }
            else
            {
                try
                {
                    messageDto.setChatId(chatDto.getId());
                    if (chatDto.getMessages().size() >= GameConstants.ChatMaxMessagesCount)
                    {
                        chatDto.getMessages().remove(0);
                    }
                    chatDto.getMessages().add(messageDto);
                    chatDto.setLatestMessageDate(messageDto.getInsertedAt());

                    chatDto = chatService.addNewChat(chatDto);

                    newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, chatDto, messageDto, "OK");
                } catch (Exception e)
                {
                    e.printStackTrace();
                    newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, null, null, "Failed to Send the Message!");
                    pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(newMessageResponse));
                    return;
                }
            }
            pushMessageManager.sendMessageByTeamId(newMessageResponse.getMessage().getReceiverTeamId().toString(), gson.toJson(newMessageResponse));
            pushMessageManager.sendMessageByTeamId(newMessageResponse.getMessage().getSenderTeamId().toString(), gson.toJson(newMessageResponse));
        } catch (Exception e)
        {
            logger.debug(e);
            System.out.println(e.getMessage());
            newMessageResponse = new NewMessageResponse(ResponseTypeConstant.NEW_MESSAGE, null, null, "Failed to Send the Message!");
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(newMessageResponse));
        }
    }

    public void getAllChats(ProcessedRequest request, GetAllChatsRequest getAllChatsRequest)
    {
        GetAllChatsResponse getAllChatsResponse;
        try
        {
            Team team = teamService.findTeamById(request.teamId);
            List<ChatDto> chatDtos = chatService.getChatsByTeam(team);
            getAllChatsResponse = new GetAllChatsResponse(ResponseTypeConstant.GET_ALL_CHATS, chatDtos);
        } catch (Exception e)
        {
            logger.debug(e);
            getAllChatsResponse = new GetAllChatsResponse(ResponseTypeConstant.GET_ALL_CHATS, null);
        }
        pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(getAllChatsResponse));
    }

    private boolean isChatAndTeamIdsValid(Integer chatId, Integer reporterTeamId, Integer reportedTeamId)
    {
        return chatService.getChatByTeamId(reportedTeamId, reporterTeamId).getId().equals(chatId);
    }

    private boolean isReportDuplicated(MessageDto messageDto, ReportMessageRequest reportMessageRequest, Integer reporterTeamId)
    {
        List<Report> reports = reportService.getReports(reportMessageRequest.getMessageText(), reporterTeamId, reportMessageRequest.getReportedTeamId());
        if (reports.size() == 0)
        {
            return false;
        }
        for (Report report : reports)
        {
            if (report.getSentAt().equals(messageDto.getInsertedAt()))
            {
                return true;
            }
        }
        return false;
    }
}
