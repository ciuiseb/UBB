package ubb.scs.map.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private Long from;
    private Long to;
    private String message;
    private final LocalDateTime sentAt;
    private Long messageId = 0L;

    public Message(Long from, Long to, String message, LocalDateTime sentAt, Long messageId) {
        this.from = from;
        this.sentAt = sentAt;
        this.to = to;
        this.message = message;
        this.messageId = messageId;
    }

    public Message(Long from, Long to, String message, LocalDateTime sentAt) {
        this.from = from;
        this.sentAt = sentAt;
        this.to = to;
        this.message = message;
    }

    public Message(Long id, Long from, Long to, String message, LocalDateTime sentAt, Long messageId) {
        this.setId(id);
        this.from = from;
        this.sentAt = sentAt;
        this.to = to;
        this.message = message;
        this.messageId = messageId;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Long getReply() {
        return messageId;
    }

    public void setReply(Long reply) {
        this.messageId = reply;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
