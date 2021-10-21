package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.domain.entity.Message;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.ChatRepository;
import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.domain.entity.Chat;
import ir.sharif.gamein2021.core.exception.ChatNotFoundException;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService extends AbstractCrudService<ChatDto, Chat, Integer>
{
    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;

    public ChatService(ChatRepository chatRepository, ModelMapper modelMapper)
    {
        this.chatRepository = chatRepository;
        this.modelMapper = modelMapper;

        setRepository(chatRepository);
    }

    @Transactional(readOnly = true)
    public Chat findById(Integer id)
    {
        return getRepository().findById(id).orElseThrow(ChatNotFoundException::new);
    }

    @Transactional
    public void addNewChat(ChatDto chatDto)
    {
        AssertionUtil.assertDtoNotNull(chatDto, Chat.class.getSimpleName());
        saveOrUpdate(chatDto);
    }

    @Transactional(readOnly = true)
    public ChatDto getChatByTeamId(Team team1, Team team2)
    {
        Chat chat = chatRepository.findByTeam1AndTeam2(team1, team2);
        if (chat == null)
        {
            chat = chatRepository.findByTeam1AndTeam2(team2, team1);
            if (chat == null)
            {
                return null;
            }
        }
        return modelMapper.map(chat, ChatDto.class);
    }

    @Transactional(readOnly = true)
    public List<ChatDto> getChatsByTeamId(Team team) {
        List<Chat> chats = new ArrayList<>();
        chats.addAll(chatRepository.findByTeam1(team));
        chats.addAll(chatRepository.findByTeam2(team));

        return chats.stream()
                .map(e -> modelMapper.map(e, ChatDto.class))
                .collect(Collectors.toList());
    }

}
