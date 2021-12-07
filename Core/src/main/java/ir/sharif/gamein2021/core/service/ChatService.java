package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.ChatRepository;
import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.domain.entity.Chat;
import ir.sharif.gamein2021.core.exception.ChatNotFoundException;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService extends AbstractCrudService<ChatDto, Chat, Integer>
{
    private final ChatRepository chatRepository;
    private final TeamService teamService;
    private final ModelMapper modelMapper;

    public ChatService(ChatRepository chatRepository, TeamService teamService, ModelMapper modelMapper)
    {
        this.chatRepository = chatRepository;
        this.teamService = teamService;
        this.modelMapper = modelMapper;

        setRepository(chatRepository);
    }

    @Transactional(readOnly = true)
    public Chat findById(Integer id)
    {
        return getRepository().findById(id).orElseThrow(ChatNotFoundException::new);
    }

    @Transactional
    public ChatDto addNewChat(ChatDto chatDto)
    {
        AssertionUtil.assertDtoNotNull(chatDto, Chat.class.getSimpleName());
        return saveOrUpdate(chatDto);
    }

    @Transactional(readOnly = true)
    public ChatDto getChatByTeamId(Integer team1Id, Integer team2Id)
    {
        Chat chat = chatRepository.findByTeam1AndTeam2(teamService.findTeamById(team1Id), teamService.findTeamById(team2Id));
        if (chat == null)
        {
            chat = chatRepository.findByTeam1AndTeam2(teamService.findTeamById(team2Id), teamService.findTeamById(team1Id));
            if (chat == null)
            {
                return null;
            }
        }
        return modelMapper.map(chat, ChatDto.class);
    }

    @Transactional(readOnly = true)
    public List<ChatDto> getChatsByTeam(Team team) {
        List<Chat> chats = new ArrayList<>();
        chats.addAll(chatRepository.findByTeam1(team));
        chats.addAll(chatRepository.findByTeam2(team));

        return chats.stream()
                .map(e -> modelMapper.map(e, ChatDto.class))
                .collect(Collectors.toList());
    }

}
