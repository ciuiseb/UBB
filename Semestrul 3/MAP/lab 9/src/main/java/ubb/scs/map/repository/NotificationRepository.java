package ubb.scs.map.repository;

import ubb.scs.map.domain.entities.Notification;

public interface NotificationRepository extends Repository<Long, Notification> {
    Iterable<Notification> findAllForUser(Long userId);

}
