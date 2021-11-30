package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.OfferRepository;
import ir.sharif.gamein2021.core.dao.ReportRepository;
import ir.sharif.gamein2021.core.domain.dto.ReportDto;
import ir.sharif.gamein2021.core.domain.entity.Report;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ReportService extends AbstractCrudService<ReportDto, Report, Integer> {

    private final ReportRepository reportRepository;
    private final ModelMapper modelMapper;

    public ReportService(ReportRepository reportRepository, ModelMapper modelMapper)
    {
        this.reportRepository = reportRepository;
        this.modelMapper = modelMapper;
        setRepository(reportRepository);
    }



}
