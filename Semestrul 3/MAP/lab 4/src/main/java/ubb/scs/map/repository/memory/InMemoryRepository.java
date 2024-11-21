package ubb.scs.map.repository.memory;


import ubb.scs.map.domain.entities.Entity;
import ubb.scs.map.domain.exceptions.DomainException;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.exceptions.ValidationException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Validator<E> validator;
    protected Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws RepoException if id is null.
     */
    @Override
    public Optional<E> findOne(ID id) {

        if (id == null) {
            throw new RepoException("ID cannot be null");
        }
        try {
            var e = entities.get(id);
            return Optional.ofNullable(e);
        } catch (Exception e) {
            throw new RepoException(e);
        }
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException if the entity is not valid
     * @throws RepoException       if the given entity is null.     *
     */
    @Override
    public Optional<E> save(E entity) throws ValidationException {
        if (entity == null)
            throw new RepoException("ENTITY CANNOT BE NULL");
        validator.validate(entity);
        if (entities.containsKey(entity.getId()))
            return Optional.of(entity);
        else {
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }


    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws RepoException if the given id is null.
     */
    @Override
    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new RepoException("ID CANNOT BE NULL");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (e.g id does not exist).
     * @throws RepoException       if the given entity is null.
     * @throws ValidationException if the entity is not valid.
     */
    @Override
    public Optional<E> update(E entity) {
        if (entity == null) {
            throw new RepoException("ENTITY CANNOT BE NULL");
        }
        validator.validate(entity);

        return entities.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(entity))
                .findFirst()
                .map(e -> {
                    entities.put(e.getKey(), entity);
                    return Optional.<E>empty();
                })
                .orElseGet(() -> Optional.of(entity));
    }
}
