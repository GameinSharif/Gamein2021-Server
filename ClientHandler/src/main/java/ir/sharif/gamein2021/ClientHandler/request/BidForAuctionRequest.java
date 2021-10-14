package ir.sharif.gamein2021.ClientHandler.request;

import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class BidForAuctionRequest extends RequestObject implements Serializable {
//    TODO check if we need higherPrice ;
    private final Integer factoryId;

    public Integer getFactoryId() {
        return factoryId;
    }
}
