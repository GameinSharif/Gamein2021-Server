package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.ProviderRepository;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import ir.sharif.gamein2021.core.domain.entity.Provider;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderService extends AbstractCrudService<ProviderDto, Provider, Integer> {

    private final ProviderRepository providerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProviderService(ProviderRepository providerRepository, ModelMapper modelMapper){
        this.providerRepository = providerRepository;
        this.modelMapper = modelMapper;
        setRepository(providerRepository);
    }

    @Transactional(readOnly = true)
    public ProviderDto findProviderById(Integer id) {
        return modelMapper.map(getRepository().findById(id).orElseThrow(EntityNotFoundException::new), ProviderDto.class);
    }

    @Transactional(readOnly = true)
    public ArrayList<ProviderDto> findProvidersByTeam(Team userTeam) {
        ArrayList<ProviderDto> providerDtos = new ArrayList<>();
        List<Provider> providers = providerRepository.findAllByTeam(userTeam);
        for (Provider provider : providers) {
            providerDtos.add(modelMapper.map(provider, ProviderDto.class));
        }
        return providerDtos;
    }

    @Transactional(readOnly = true)
    public ArrayList<ProviderDto> findProvidersExceptTeam(Team userTeam) {
        ArrayList<ProviderDto> providerDtos = new ArrayList<>();
        List<Provider> providers = providerRepository.findAllByTeamIsNot(userTeam);
        for (Provider provider : providers) {
            providerDtos.add(modelMapper.map(provider, ProviderDto.class));
        }
        return providerDtos;
    }

    @Transactional
    public void deleteProvider(Integer providerId) {
        providerRepository.deleteById(providerId);
    }

    @Transactional
    public ProviderDto save(ProviderDto providerDto) {
        // TODO : Exception
        return saveOrUpdate(providerDto);
    }
}
