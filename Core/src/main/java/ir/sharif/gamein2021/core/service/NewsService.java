package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.NewsRepository;
import ir.sharif.gamein2021.core.domain.dto.NewsDto;
import ir.sharif.gamein2021.core.domain.entity.News;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class NewsService extends AbstractCrudService<NewsDto, News, Integer> {
    private final NewsRepository newsRepository;
    private final ModelMapper modelMapper;

    public NewsService(NewsRepository newsRepository, ModelMapper modelMapper)
    {
        this.newsRepository = newsRepository;
        this.modelMapper = modelMapper;
        setRepository(newsRepository);
    }

    @Transactional(readOnly = true)
    public NewsDto findById(Integer id) {
        return modelMapper.map(getRepository().findById(id).orElseThrow(EntityNotFoundException::new), NewsDto.class);
    }

    public List<NewsDto> findAllLessThanCurrentWeek(Integer week){
        List<News> news = newsRepository.findAllByWeekLessThanEqual(week);
        return news.stream()
                .map(e -> modelMapper.map(e, NewsDto.class))
                .collect(Collectors.toList());
    }

    public List<NewsDto> findByWeek(Integer week){
        List<News> news = newsRepository.findAllByWeek(week);
        return news.stream()
                .map(e -> modelMapper.map(e, NewsDto.class))
                .collect(Collectors.toList());
    }
}
