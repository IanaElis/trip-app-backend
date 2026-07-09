package notifications.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "trip_id",nullable = false)
    private Long tripId;

    @Column(name = "item_id",nullable = false)
    private Long itineraryItemId;

    @Column(name = "item_type",nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TimelineItemType itemType;

    @Column(name = "channel_type",nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ChannelType channel;

    @Column(name = "status",nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(name = "send_at", nullable = false)
    private Instant sendAt;

    public Notification() {}

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
