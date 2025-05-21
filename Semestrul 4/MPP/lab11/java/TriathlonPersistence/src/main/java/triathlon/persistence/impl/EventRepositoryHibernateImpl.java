package triathlon.persistence.impl;

import jakarta.persistence.*;
import triathlon.model.Event;
import triathlon.model.EventEntity;
import triathlon.persistence.interfaces.EventRepository;
import triathlon.persistence.utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventRepositoryHibernateImpl implements EventRepository {

    private final EntityManagerFactory entityManagerFactory;
    private final Class<EventEntity> entityClass = EventEntity.class;

    public EventRepositoryHibernateImpl() {
        this.entityManagerFactory = HibernateUtils.getSessionFactory().unwrap(EntityManagerFactory.class);
    }

    @Override
    public Optional<Event> save(Event entity) {
        if (findOne(entity.getId()).isPresent())
            return Optional.of(entity);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            EventEntity eventEntity = toEntity(entity);
            entityManager.persist(eventEntity);

            transaction.commit();
            return Optional.empty();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Event> delete(Long id) {
        Optional<Event> entity = findOne(id);
        if (entity.isEmpty())
            return Optional.empty();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            EventEntity eventEntity = entityManager.find(entityClass, id);
            if (eventEntity != null) {
                entityManager.remove(eventEntity);
            }

            transaction.commit();

            return entity;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Event> update(Event entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            EventEntity eventEntity = toEntity(entity);
            eventEntity = entityManager.merge(eventEntity);

            transaction.commit();

            return Optional.of(toEvent(eventEntity));
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Event> findOne(Long id) {
        if (id == null)
            return Optional.empty();

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EventEntity eventEntity = entityManager.find(entityClass, id);

            if (eventEntity == null)
                return Optional.empty();

            return Optional.of(toEvent(eventEntity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Event> findAll() {

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<EventEntity> query = entityManager.createQuery(
                    "SELECT e FROM EventEntity e", EventEntity.class);

            List<EventEntity> entities = query.getResultList();
            List<Event> events = new ArrayList<>();

            for (EventEntity entity : entities) {
                events.add(toEvent(entity));
            }

            return events;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> findByRefereeId(Long refereeId) {

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Query query = entityManager.createNativeQuery(
                    "SELECT e.* FROM Events e " +
                            "JOIN Results r ON e.id = r.event_id " +
                            "WHERE r.referee_id = ?",
                    EventEntity.class);

            query.setParameter(1, refereeId);
            List<EventEntity> entities = query.getResultList();
            List<Event> events = new ArrayList<>();
            for (EventEntity entity : entities) {
                events.add(toEvent(entity));
            }

            return events;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private EventEntity toEntity(Event event) {
        if (event == null) return null;

        EventEntity entity = new EventEntity();
        entity.setId(event.getId());
        entity.setName(event.getName());
        entity.setDate(event.getDate());
        entity.setLocation(event.getLocation());
        entity.setDescription(event.getDescription());
        return entity;
    }

    private Event toEvent(EventEntity entity) {
        if (entity == null) return null;

        return new Event(
                entity.getId(),
                entity.getName(),
                entity.getDate(),
                entity.getLocation(),
                entity.getDescription()
        );
    }
}