package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.exception.MessageNotFoundInChatException;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.MessageRepository;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.entity.Message;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService extends AbstractCrudService<MessageDto, Message, Integer>
{
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final TeamService teamService;

    public MessageService(MessageRepository messageRepository, TeamService teamService, ChatService chatService)
    {
        this.messageRepository = messageRepository;
        this.teamService = teamService;
        this.chatService = chatService;
        setRepository(messageRepository);
    }

    public MessageDto getMessageByTextAndChatId(Integer chatId, Integer reportedTeamId, String messageText, LocalDateTime insertedAt)
    {
        ChatDto chatDto = chatService.loadById(chatId);
        for (MessageDto messageDto : chatDto.getMessages())
        {
            if (messageDto.getSenderTeamId().equals(reportedTeamId) && messageDto.getText().equals(messageText) && messageDto.getInsertedAt() == insertedAt)
            {
                return messageDto;
            }
        }
        return null;
    }

    public MessageDto addNewMessage(MessageDto messageDto)
    {
        AssertionUtil.assertDtoNotNull(messageDto, Message.class.getSimpleName());
        return saveOrUpdate(messageDto);
    }
}
