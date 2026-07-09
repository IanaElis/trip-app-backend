package notifications.sender;

import notifications.entity.ChannelType;
import notifications.entity.Notification;

public interface NotificationSender {
    ChannelType supports();
    void send(Notification job);
}
