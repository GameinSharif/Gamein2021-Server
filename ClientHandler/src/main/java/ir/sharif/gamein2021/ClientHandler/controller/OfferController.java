package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.ClientHandler.model.RFQ.GetOffersTransitModel;
import ir.sharif.gamein2021.ClientHandler.model.RFQ.NewOfferTransitModel;
import ir.sharif.gamein2021.core.db.Context;
import ir.sharif.gamein2021.core.entity.Offer;
import ir.sharif.gamein2021.core.entity.Team;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class OfferController extends Context {

    public String newOffer(Team team, NewOfferTransitModel offer) {
        offerDBAccessor.add(new Offer(
                team,
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
