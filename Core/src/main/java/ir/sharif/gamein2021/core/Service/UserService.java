package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.UserRepository;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.User;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractCrudService<UserDto, User, Integer>
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
        User result = userRepository.findUserByUsernameAndPassword(username, password);
        return modelMapper.map(result, UserDto.class);
    }

    @Override
    public UserDto loadById(Integer id)
    {
        return super.loadById(id);
    }
}
