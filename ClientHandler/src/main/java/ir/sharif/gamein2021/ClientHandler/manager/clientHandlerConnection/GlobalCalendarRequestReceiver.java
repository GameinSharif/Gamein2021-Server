package ir.sharif.gamein2021.ClientHandler.manager.clientHandlerConnection;

import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.BaseClientHandlerRequest;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.UpdateCalendarRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = {"microservice"})
@Primary
@Component
@RabbitListener(queues = "#{calendarQueue.name}")
public class GlobalCalendarRequestReceiver {
    private GameCalendar gameCalendar;

    public GlobalCalendarRequestReceiver(GameCalendar gameCalendar) {
        this.gameCalendar = gameCalendar;
    }

    @RabbitHandler
    public void receive(BaseClientHandlerRequest request) throws InterruptedException {
        if(request instanceof UpdateCalendarRequest){
            System.out.println(((UpdateCalendarRequest) request).getNewDate());
            gameCalendar.setCurrentDate(((UpdateCalendarRequest) request).getNewDate());
        }
    }
}
