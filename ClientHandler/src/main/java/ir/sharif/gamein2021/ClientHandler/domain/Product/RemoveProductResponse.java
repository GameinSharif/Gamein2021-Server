package ir.sharif.gamein2021.ClientHandler.domain.Product;

import ir.sharif.gamein2021.core.domain.dto.StorageDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class RemoveProductResponse extends ResponseObject implements Serializable {
    private String result;
    private StorageDto storageDto;

    public RemoveProductResponse(ResponseTypeConstant responseTypeConstant, StorageDto storageDto, String result) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.storageDto = storageDto;
        this.result = result;
    }
}
