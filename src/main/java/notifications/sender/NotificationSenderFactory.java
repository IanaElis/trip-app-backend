package notifications.sender;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import notifications.entity.ChannelType;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class NotificationSenderFactory {

    private final Map<ChannelType, NotificationSender> senders = new HashMap<>();

    public void initializeMap(){
        senders.put(ChannelType.EMAIL, new EmailSender());
    }

    @Inject
    public NotificationSenderFactory(EmailSender emailSender) {
        senders.put(ChannelType.EMAIL, emailSender);
    }

    public NotificationSender get(ChannelType type) {
        if(type == null){
            return null;
        }
        return senders.get(type);
    }
}
