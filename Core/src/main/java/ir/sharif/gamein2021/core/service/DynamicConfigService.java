package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.DynamicConfigRepository;
import ir.sharif.gamein2021.core.domain.dto.DynamicConfigDto;
import ir.sharif.gamein2021.core.domain.entity.DynamicConfig;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.models.GameStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DynamicConfigService extends AbstractCrudService<DynamicConfigDto, DynamicConfig, String> {
    private final static String CURRENT_DATE_KEY = "currentDate";
    private final static String GAME_STATUS_KEY = "gameStatus";
    private final static String CURRENT_WEEK_KEY = "currentWeek";

    private final DynamicConfigRepository repository;

    public DynamicConfigService(DynamicConfigRepository repository) {
        setRepository(repository);
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public DynamicConfig getConfig(String configKey){
        return repository.findById(configKey).orElse(null);
    }

    @Transactional
    public void setConfig(String key, String value){
        DynamicConfigDto config = new DynamicConfigDto();
        config.setKey(key);
        config.setValue(value);
        saveOrUpdate(config);
    }

    @Transactional(readOnly = true)
    public LocalDate getCurrentDate() {
        DynamicConfig currentDateConfig = getConfig(CURRENT_DATE_KEY);
        if (currentDateConfig == null) {
            return null;
        }

        return LocalDate.parse(currentDateConfig.getValue());
    }

    @Transactional
    public void setCurrentDate(LocalDate currentDate) {
        setConfig(CURRENT_DATE_KEY, currentDate.toString());
    }

    @Transactional(readOnly = true)
    public GameStatus getGameStatus() {
        DynamicConfig gameStatusConfig = getConfig(GAME_STATUS_KEY);
        if (gameStatusConfig == null) {
            return null;
        }

        return GameStatus.valueOf(gameStatusConfig.getValue());
    }

    @Transactional
    public void setGameStatus(GameStatus gameStatus) {
        setConfig(GAME_STATUS_KEY, gameStatus.toString());
    }

    @Transactional(readOnly = true)
    public Integer getCurrentWeek() {
        DynamicConfig currentWeekConfig = getConfig(CURRENT_WEEK_KEY);
        if (currentWeekConfig == null) {
            return null;
        }

        return Integer.parseInt(currentWeekConfig.getValue());
    }

    @Transactional
    public void setCurrentWeek(Integer currentWeek) {
        setConfig(CURRENT_WEEK_KEY, currentWeek.toString());
    }
}
