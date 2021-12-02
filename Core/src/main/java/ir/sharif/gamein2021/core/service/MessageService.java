package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.MessageRepository;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.entity.Message;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends AbstractCrudService<MessageDto, Message, Integer>
{
    MessageRepository messageRepository;
    TeamService teamService;

    public MessageService(MessageRepository messageRepository, TeamService teamService)
    {
        this.messageRepository = messageRepository;
        this.teamService = teamService;
        setRepository(messageRepository);
    }

    public MessageDto addNewMessage(MessageDto messageDto)
    {
        AssertionUtil.assertDtoNotNull(messageDto, Message.class.getSimpleName());
        return saveOrUpdate(messageDto);
    }
}
