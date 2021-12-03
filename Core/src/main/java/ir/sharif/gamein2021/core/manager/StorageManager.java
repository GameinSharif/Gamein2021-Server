package ir.sharif.gamein2021.core.manager;


import ir.sharif.gamein2021.core.domain.dto.StorageDto;
import ir.sharif.gamein2021.core.domain.dto.StorageProductDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.StorageProduct;
import ir.sharif.gamein2021.core.service.DcService;
import ir.sharif.gamein2021.core.service.StorageService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class StorageManager {
    private final DcService dcService;
    private final TeamService teamService;
    private final StorageService storageService;

    public void MaintenanceCost(){
        List<TeamDto> teamDtos = teamService.findAllTeams();
        for(TeamDto teamDto : teamDtos){
            List<StorageDto> storageDtos = storageService.findAllStorageForTeam(teamDto);
            Float cost = (float)(0);
            for(StorageDto storageDto: storageDtos){
                List<StorageProductDto> storageProductDtos = storageDto.getProducts();
                for (StorageProductDto storageProductDto: storageProductDtos){
                    Product product = ReadJsonFilesManager.findProductById(storageProductDto.getProductId());
                    cost += product.getMaintenanceCostPerDay();
                }
            }
            teamDto.setCredit(teamDto.getCredit() - cost);
            teamService.saveOrUpdate(teamDto);
        }
    }

}
