package ir.sharif.gamein2021.ClientHandler.domain.Product;

import ir.sharif.gamein2021.core.domain.dto.StorageDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class RemoveProductResponse extends ResponseObject implements Serializable {
    private String result;
    private StorageDto storage;

    public RemoveProductResponse(ResponseTypeConstant responseTypeConstant, StorageDto storage, String result) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.storage = storage;
        this.result = result;
    }
}
