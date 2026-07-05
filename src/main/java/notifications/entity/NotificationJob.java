package notifications.entity;

import jakarta.persistence.*;

import java.time.Instant;

//@Entity
//@Table(name = "notification_jobs")
public class NotificationJob {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long tripId;

    private Long itineraryItemId;

    @Enumerated(EnumType.STRING)
    private TimelineItemType itemType;

    @Enumerated(EnumType.STRING)
    private ChannelType channel;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private Instant sendAt;

    public NotificationJob() {}

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getItineraryItemId() {
        return itineraryItemId;
    }

    public void setItineraryItemId(Long itineraryItemId) {
        this.itineraryItemId = itineraryItemId;
    }

    public TimelineItemType getItemType() {
        return itemType;
    }

    public void setItemType(TimelineItemType itemType) {
        this.itemType = itemType;
    }

    public ChannelType getChannel() {
        return channel;
    }

    public void setChannel(ChannelType channel) {
        this.channel = channel;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Instant getSendAt() {
        return sendAt;
    }

    public void setSendAt(Instant sendAt) {
        this.sendAt = sendAt;
    }
}
