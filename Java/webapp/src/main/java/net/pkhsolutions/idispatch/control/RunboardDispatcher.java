package net.pkhsolutions.idispatch.control;

import net.pkhsolutions.idispatch.boundary.events.DispatchNotificationSent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by peholmst on 2014-05-06.
 */
@Component
class RunboardDispatcher implements ApplicationListener<DispatchNotificationSent> {

    @Override
    @Async
    public void onApplicationEvent(DispatchNotificationSent event) {

    }
}
