package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.CoronaCoefficientRepository;
import ir.sharif.gamein2021.core.dao.CoronaRepository;
import ir.sharif.gamein2021.core.domain.dto.CoronaInfoDto;
import ir.sharif.gamein2021.core.domain.entity.CoronaInfo;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.Enums;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoronaService extends AbstractCrudService<CoronaInfoDto, CoronaInfo, Integer>
{
    private final ModelMapper modelMapper;
    private final CoronaRepository coronaRepository;
    private final CoronaCoefficientRepository coefficientRepository;
    private final DynamicConfigService dynamicConfigService;
    private final GameCalendar gameCalendar;

    public CoronaService(ModelMapper modelMapper,
                         CoronaRepository coronaRepository,
                         CoronaCoefficientRepository coefficientRepository,
                         DynamicConfigService dynamicConfigService, GameCalendar gameCalendar)
    {
        this.modelMapper = modelMapper;
        this.coronaRepository = coronaRepository;
        this.coefficientRepository = coefficientRepository;
        this.dynamicConfigService = dynamicConfigService;
        this.gameCalendar = gameCalendar;
        setRepository(coronaRepository);
    }

    @Transactional(readOnly = true)
    public boolean isCoronaStarted()
    {
        Integer currentWeek = gameCalendar.getCurrentWeek();
        Assert.notNull(currentWeek, "Current week is null!");
        Integer coronaStartingWeek = dynamicConfigService.getCoronaStartingWeek();
        Assert.notNull(coronaStartingWeek, "Corona week is null!");
        return coronaStartingWeek <= currentWeek;
    }

    @Transactional
    public void addDonatedMoneyToCoronaInfo(Float donatedAmount, CoronaInfoDto coronaInfo) {
        coronaInfo.setCurrentCollectedAmount(coronaInfo.getCurrentCollectedAmount() + donatedAmount);
        saveOrUpdate(coronaInfo);
    }

    @Transactional
    public CoronaInfoDto changeAndSaveCoronaStatusForCountry(Enums.Country country)
    {
        var coronaInfo = coronaRepository.findByCountry(country);
        if (checkIfCoronaStillExist(coronaInfo))
        {
            checkDonatedMoney(coronaInfo);
            var coronaDto = modelMapper.map(coronaInfo, CoronaInfoDto.class);
            return saveOrUpdate(coronaDto);
        }
        saveOrUpdate(modelMapper.map(coronaInfo, CoronaInfoDto.class));
        return null;
    }

    @Transactional(readOnly = true)
    public boolean checkCoronaForCountry(Enums.Country country)
    {
        return !findCoronaInfoWithCountry(country).isCoronaOver();
    }

    @Transactional(readOnly = true)
    public CoronaInfoDto findCoronaInfoWithCountry(Enums.Country country)
    {
        Assert.notNull(country, "This field can't be null!");
        var coronaInfo = coronaRepository.findByCountry(country);
        if (coronaInfo == null)
        {
            throw new EntityNotFoundException("CoronaInfos for country" + country.name() + " does not exist !");
        }
        return modelMapper.map(coronaInfo, CoronaInfoDto.class);
    }


    @Transactional(readOnly = true)
    public List<CoronaInfoDto> getCoronasInfoIfCoronaIsStarted()
    {
        List<CoronaInfoDto> coronaInfo = null;
        if (isCoronaStarted())
        {
            coronaInfo = list();
        }
        return coronaInfo;
    }

    @Transactional(readOnly = true)
    public Float calculateAvailableMoneyForDonate(Enums.Country country)
    {
        var coronaInfo = findCoronaInfoWithCountry(country);
        return coronaInfo.getAmountToBeCollect() - coronaInfo.getCurrentCollectedAmount();
    }

    @Transactional(readOnly = true)
    public Float getCoronaCoefficient()
    {
        Integer currentWeak = gameCalendar.getCurrentWeek();
        return coefficientRepository.findByWeek(currentWeak).getCoefficient();
    }

    private boolean checkIfCoronaStillExist(CoronaInfo coronaInfo)
    {
        return !coronaInfo.isCoronaOver() && isCoronaStarted();
    }

    private void checkDonatedMoney(CoronaInfo coronaInfo)
    {
        if (coronaInfo.getAmountToBeCollect().equals(coronaInfo.getCurrentCollectedAmount()))
        {
            coronaInfo.setCoronaOver(true);
        }
    }
}
