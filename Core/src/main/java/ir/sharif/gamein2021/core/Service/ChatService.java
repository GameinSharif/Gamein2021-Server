package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.entity.Chat;
import ir.sharif.gamein2021.core.domain.entity.Message;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService extends AbstractCrudService<ChatDto, Chat, Integer> {

    private final MessageService messageService;
    private final TeamService teamService;

    public ChatService(MessageService messageService, TeamService teamService)
    {
        this.messageService = messageService;
        this.teamService = teamService;
    }

    public Chat save(ChatDto chatDto) {
        AssertionUtil.assertDtoNotNull(chatDto, Chat.class.getSimpleName());
        if (chatDto.getLatestMessageDate() == null) {
            chatDto.setLatestMessageDate(LocalDateTime.now());
        }
        var chat = toChat(chatDto);
        return getRepository().save(chat);
    }

    public Chat toChat(ChatDto chatDto) {
        ArrayList<Message> messages = new ArrayList<>();
        for (MessageDto messageDto : chatDto.getMessageDtos()) {
            messages.add(messageService.toMessage(messageDto));
        }
        ArrayList<Team> teams = new ArrayList<>();
        for (Integer teamId : chatDto.getTeamIds()) {
            teams.add(teamService.findById(teamId));
        }

        return Chat.builder()
                .id(chatDto.getId())
                .latestMessageDate(chatDto.getLatestMessageDate())
                .teams(teams)
                .messages(messages)
                .build();
    }

    public ChatDto toChatDto(Chat chat) {
        ArrayList<MessageDto> messageDtos = new ArrayList<>();
        for (Message message : chat.getMessages()) {
            messageDtos.add(messageService.toMessageDto(message));
        }
        ArrayList<Integer> teamIds = new ArrayList<>();
        for (Team team : chat.getTeams()) {
            teamIds.add(team.getId());
        }

        return ChatDto.builder()
                .id(chat.getId())
                .latestMessageDate(chat.getLatestMessageDate())
                .teamIds(teamIds)
                .messageDtos(messageDtos)
                .build();
    }

}
