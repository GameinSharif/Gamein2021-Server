package ir.sharif.gamein2021.ClientHandler.domain.Product;

import ir.sharif.gamein2021.core.domain.dto.StorageDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class GetStorageProductsResponse extends ResponseObject implements Serializable {
    private String result;
    private List<StorageDto> storages;

    public GetStorageProductsResponse(ResponseTypeConstant responseTypeConstant, List<StorageDto> storages, String result) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.storages = storages;
        this.result = result;
    }
}
