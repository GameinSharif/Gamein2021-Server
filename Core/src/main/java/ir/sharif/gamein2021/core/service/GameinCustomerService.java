package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.GameinCustomerRepository;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.domain.entity.GameinCustomer;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameinCustomerService extends AbstractCrudService<GameinCustomerDto, GameinCustomer, Integer>
{
    private final GameinCustomerRepository gameinCustomerRepository;
    private final ModelMapper modelMapper;

    public GameinCustomerService(GameinCustomerRepository gameinCustomerRepository, ModelMapper modelMapper)
    {
        this.gameinCustomerRepository = gameinCustomerRepository;
        this.modelMapper = modelMapper;
        setRepository(gameinCustomerRepository);
    }

    @Transactional(readOnly = true)
    public GameinCustomer findById(Integer id)
    {
        return getRepository().findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<GameinCustomerDto> list()
    {
        return super.list();
    }
}
