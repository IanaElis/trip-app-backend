package notifications.entity;

import jakarta.persistence.*;

//@Entity
//@Table(name = "notification_rules")
public class NotificationRule {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TimelineItemType itemType;

    private Integer offsetMinutes;

    private boolean enabled;

    public NotificationRule() {}

    public Long getId() {
        return id;
    }

    public TimelineItemType getItemType() {
        return itemType;
    }

    public void setItemType(TimelineItemType itemType) {
        this.itemType = itemType;
    }

    public Integer getOffsetMinutes() {
        return offsetMinutes;
    }

    public void setOffsetMinutes(Integer offsetMinutes) {
        this.offsetMinutes = offsetMinutes;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
