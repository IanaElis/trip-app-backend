package notifications.sender;

import notifications.entity.ChannelType;
import notifications.entity.NotificationJob;

public interface NotificationSender {
    ChannelType supports();
    void send(NotificationJob job);
}
