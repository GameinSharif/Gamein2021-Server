package ir.sharif.gamein2021.core.domain.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

public class ContractSupplierDetailDto
{
    private Integer id;
    private LocalDate contractDate;
    private Integer boughtAmount;
    private Integer pricePerUnit;
}
