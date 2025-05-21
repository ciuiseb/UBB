package triathlon.persistence.impl;

import jakarta.persistence.*;
import triathlon.model.Participant;
import triathlon.model.ParticipantEntity;
import triathlon.persistence.utils.HibernateUtils;
import triathlon.persistence.interfaces.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticipantRepositoryHibernateImpl implements ParticipantRepository {

    private final EntityManagerFactory entityManagerFactory;
    private final Class<ParticipantEntity> entityClass = ParticipantEntity.class;

    public ParticipantRepositoryHibernateImpl() {
        this.entityManagerFactory = HibernateUtils.getSessionFactory().unwrap(EntityManagerFactory.class);
    }


    @Override
    public Optional<Participant> findOne(Long id) {
        if (id == null)
            return Optional.empty();

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            ParticipantEntity participantEntity = entityManager.find(entityClass, id);

            if (participantEntity == null)
                return Optional.empty();

            return Optional.of(toParticipant(participantEntity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Participant> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<ParticipantEntity> query = entityManager.createQuery(
                    "SELECT p FROM ParticipantEntity p", ParticipantEntity.class);

            List<ParticipantEntity> entities = query.getResultList();
            List<Participant> participants = new ArrayList<>();

            for (ParticipantEntity entity : entities) {
                participants.add(toParticipant(entity));
            }

            return participants;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Participant> save(Participant entity) {
        if (findOne(entity.getId()).isPresent())
            return Optional.of(entity);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            ParticipantEntity participantEntity = toEntity(entity);
            entityManager.persist(participantEntity);

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
    public Optional<Participant> delete(Long id) {
        Optional<Participant> entity = findOne(id);
        if (entity.isEmpty())
            return Optional.empty();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            ParticipantEntity participantEntity = entityManager.find(entityClass, id);
            if (participantEntity != null) {
                entityManager.remove(participantEntity);
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
    public Optional<Participant> update(Participant entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            ParticipantEntity participantEntity = toEntity(entity);
            participantEntity = entityManager.merge(participantEntity);

            transaction.commit();

            return Optional.of(toParticipant(participantEntity));
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    private ParticipantEntity toEntity(Participant participant) {
        if (participant == null) return null;

        ParticipantEntity entity = new ParticipantEntity();
        entity.setId(participant.getId());
        entity.setFirstName(participant.getFirstName());
        entity.setLastName(participant.getLastName());
        entity.setAge(participant.getAge());
        entity.setGender(participant.getGender());
        return entity;
    }

    private Participant toParticipant(ParticipantEntity entity) {
        if (entity == null) return null;

        return new Participant(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAge(),
                entity.getGender()
        );
    }
}