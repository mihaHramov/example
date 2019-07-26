package aaa.bbb.ccc.solidsnake.data.metricaSender;

import aaa.bbb.ccc.solidsnake.model.UserEvent;

public interface IMetricEventSender {
    void sendEvent(UserEvent event);
}
