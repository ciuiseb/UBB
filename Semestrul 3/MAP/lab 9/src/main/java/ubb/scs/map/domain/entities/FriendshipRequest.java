package ubb.scs.map.domain.entities;

import java.time.LocalDateTime;

public class FriendshipRequest extends Tuple<Long>{
    LocalDateTime sentDate;

    public FriendshipRequest(Long first, Long second, LocalDateTime friendsSince) {
        super(first, second);
        this.sentDate = friendsSince;
    }
    public FriendshipRequest(Long id, Long first, Long second, LocalDateTime friendsSince) {
        super(first, second);
        setId(id);
        this.sentDate = friendsSince;
    }
    public FriendshipRequest(Long sender, Long recipient) {
        super(sender, recipient);
        this.sentDate = LocalDateTime.now();
    }
    public long getSender(){
        return super.getFirst();
    }
    public long getReciver(){
        return super.getSecond();
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }
}
