package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.UserRepository;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.Player;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractCrudService<UserDto, Player, Integer>
{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper)
    {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        setRepository(userRepository);
    }

    @Transactional(readOnly = true)
    public UserDto read(String username, String password)
    {
        Player result = userRepository.findUserByUsernameAndPassword(username, password);
        return modelMapper.map(result, UserDto.class);
    }

    @Override
    public UserDto loadById(Integer id)
    {
        return super.loadById(id);
    }
}
