package ir.sharif.gamein2021.ClientHandler.manager;

import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewOfferRequest;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.Service.OfferService;
import ir.sharif.gamein2021.core.Service.UserService;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RequestDtoConversionManager {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final UserService userService;

    public RequestDtoConversionManager(UserService userService) {
        this.userService = userService;
    }

//    public OfferDto newOfferRequestToOfferDto(NewOfferRequest newOfferRequest) {
//        return OfferDto.builder()
//                .team(userService.findById(newOfferRequest.getTeamId()).getTeam())
//                .type(newOfferRequest.getType())
//                .volume(newOfferRequest.getVolume())
//                .costPerUnit(newOfferRequest.getCostPerUnit())
//                .earliestExpectedArrival(newOfferRequest.getEarliestExpectedArrival())
//                .latestExpectedArrival(newOfferRequest.getLatestExpectedArrival())
//                .offerDeadline(newOfferRequest.getOfferDeadline()).build();
//    }

}
