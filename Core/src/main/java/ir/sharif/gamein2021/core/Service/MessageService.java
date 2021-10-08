package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.entity.Message;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService extends AbstractCrudService<MessageDto, Message, Integer> {

    TeamService teamService;

    public MessageService(TeamService teamService) {
        this.teamService = teamService;
    }

    public MessageDto save(MessageDto messageDto) {
        AssertionUtil.assertDtoNotNull(messageDto, Message.class.getSimpleName());
        messageDto.setDateTime(LocalDateTime.now());
        var message = toMessage(messageDto);
        Message result = getRepository().save(message);
        return toMessageDto(result);
    }

    public Message toMessage(MessageDto messageDto) {
        return Message.builder()
                .id(messageDto.getId())
                .text(messageDto.getText())
                .senderTeam(teamService.findById(messageDto.getSenderTeamId()))
                .receiverTeam(teamService.findById(messageDto.getReceiverTeamId()))
                .dateTime(messageDto.getDateTime())
                .build();
    }

    public MessageDto toMessageDto(Message message) {

    }

}
