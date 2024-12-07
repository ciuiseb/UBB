package ubb.scs.map.domain.entities;

import java.time.LocalDateTime;


public class Notification  extends Entity<Long>{
    private Long userId;
    private String type;
    private String message;
    private LocalDateTime dateTime;

    public Notification(Long userId, String type, String message, LocalDateTime dateTime) {
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.dateTime = dateTime;
    }
    public Notification(Long id, Long userId, String type, String message, LocalDateTime dateTime) {
        this.setId(id);
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
