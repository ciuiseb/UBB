package triathlon.persistence.interfaces;


import triathlon.model.Entity;

import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    Optional<E> findOne(ID id);

    /**
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     * - the entity (id already exists
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    Optional<E> save(E entity);


    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return an {@code Optional}
     * - null if there is no entity with the given id,
     * - the removed entity, otherwise
     * @throws IllegalArgumentException if the given id is null.
     */
    Optional<E> delete(ID id);

    /**
     * @param entity entity must not be null
     * @return an {@code Optional}
     * <p>
     * - null if the entity was updated - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given entity is null
     */
    Optional<E> update(E entity);
}
