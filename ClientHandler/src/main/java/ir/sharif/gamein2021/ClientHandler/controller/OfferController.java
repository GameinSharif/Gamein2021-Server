package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetOffersTransitModel;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.ClientHandler.db.Context;
import ir.sharif.gamein2021.core.domain.entity.Offer;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class OfferController extends Context {

    public String newOffer(Team team, OfferDto offer) {
        offerDBAccessor.add(new Offer(0 ,team,
                offer.getType(),
                offer.getVolume(),
                offer.getCostPerUnit(),
                offer.getEarliestExpectedArrival(),
                offer.getLatestExpectedArrival(),
                offer.getOfferDeadline()
        ));
        return "DONE";
    }

    public ArrayList<GetOffersTransitModel> getOffers() {

        ArrayList<Offer> offers = offerDBAccessor.all();
        ArrayList<GetOffersTransitModel> transmittingOffers = new ArrayList<>();

        for (Offer offer : offers) {
            transmittingOffers.add(new GetOffersTransitModel(
                    offer.getTeam().getTeamName(),
                    offer.getType(),
                    offer.getVolume(),
                    offer.getCostPerUnit(),
                    offer.getEarliestExpectedArrival(),
                    offer.getLatestExpectedArrival(),
                    offer.getOfferDeadline()
            ));
        }

        return transmittingOffers;

    }

}
