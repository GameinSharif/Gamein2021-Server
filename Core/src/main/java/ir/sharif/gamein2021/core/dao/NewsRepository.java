package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.dto.NewsDto;
import ir.sharif.gamein2021.core.domain.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findAllByWeekLessThanEqual(Integer week);
    List<News> findAllByWeek(Integer week);
}
