package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.ChatRepository;
import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.entity.Chat;
import ir.sharif.gamein2021.core.domain.entity.Message;
import ir.sharif.gamein2021.core.domain.entity.Offer;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService extends AbstractCrudService<ChatDto, Chat, Integer> {

    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final TeamService teamService;

    public ChatService(ChatRepository chatRepository, MessageService messageService, TeamService teamService)
    {
        this.chatRepository = chatRepository;
        this.messageService = messageService;
        this.teamService = teamService;
        setRepository(chatRepository);
    }

    public ChatDto save(ChatDto chatDto) {
        AssertionUtil.assertDtoNotNull(chatDto, Chat.class.getSimpleName());
        if (chatDto.getLatestMessageDate() == null) {
            chatDto.setLatestMessageDate(LocalDateTime.now());
        }
        var chat = toChat(chatDto);
        return toChatDto(getRepository().save(chat));
    }

    @Transactional
    public ChatDto update(ChatDto newChatDto) {
        AssertionUtil.assertIdNotNull(newChatDto.getId(), ChatDto.class.getSimpleName());
        AssertionUtil.assertDtoNotNull(newChatDto, Chat.class.getSimpleName());
        Chat result = getRepository().save(toChat(newChatDto));
        return toChatDto(result);
    }

    public Chat toChat(ChatDto chatDto) {
        ArrayList<Message> messages = new ArrayList<>();
        for (MessageDto messageDto : chatDto.getMessageDtos()) {
            messages.add(messageService.toMessage(messageDto));
        }
        return Chat.builder()
                .id(chatDto.getId())
                .latestMessageDate(chatDto.getLatestMessageDate())
                .team1(teamService.findById(chatDto.getTeam1Id()))
                .team2(teamService.findById(chatDto.getTeam2Id()))
                .messages(messages)
                .build();
    }

    public ChatDto toChatDto(Chat chat) {
        ArrayList<MessageDto> messageDtos = new ArrayList<>();
        for (Message message : chat.getMessages()) {
            messageDtos.add(messageService.toMessageDto(message));
        }

        return ChatDto.builder()
                .id(chat.getId())
                .latestMessageDate(chat.getLatestMessageDate())
                .team1Id(chat.getTeam1().getId())
                .team2Id(chat.getTeam2().getId())
                .messageDtos(messageDtos)
                .build();
    }

    public ChatDto getChatByTeamId(Integer team1Id, Integer team2Id) {
        List<Chat> allChats = getRepository().findAll();
        for (Chat chat : allChats) {
            if (chat.getTeam1().getId().equals(team1Id) || chat.getTeam2().getId().equals(team1Id)) {
                if (chat.getTeam1().getId().equals(team2Id) || chat.getTeam2().getId().equals(team2Id)) {
                    return toChatDto(chat);
                }
            }
        }
        return null;
    }

}
