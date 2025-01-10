using lab_11.domain;

namespace lab_11.repo;

/// <summary>
/// Generic repository interface for CRUD operations
/// </summary>
/// <typeparam name="TEntity">Type of entity, must inherit from Entity base class</typeparam>
/// <typeparam name="TId">Type of entity's ID</typeparam>
public interface IRepository<TEntity, TId> where TEntity : Entity<TId>
{
    /// <summary>
    /// Finds an entity by its ID
    /// </summary>
    /// <param name="id">The ID of the entity to find</param>
    /// <returns>The found entity or null if not found</returns>
    /// <exception cref="ArgumentNullException">If id is null</exception>
    TEntity? FindOne(TId id);

    /// <summary>
    /// Retrieves all entities from the repository
    /// </summary>
    /// <returns>An enumerable collection of all entities</returns>
    IEnumerable<TEntity> FindAll();

    /// <summary>
    /// Saves a new entity to the repository
    /// </summary>
    /// <param name="entity">The entity to save</param>
    /// <returns>null if save was successful, or the entity if ID already exists</returns>
    /// <exception cref="ArgumentNullException">If entity is null</exception>
    /// <exception cref="ValidationException">If entity is invalid</exception>
    TEntity? Save(TEntity entity);

    /// <summary>
    /// Deletes an entity by its ID
    /// </summary>
    /// <param name="id">The ID of the entity to delete</param>
    /// <returns>The deleted entity or null if entity was not found</returns>
    /// <exception cref="ArgumentNullException">If id is null</exception>
    TEntity? Delete(TId id);

    /// <summary>
    /// Updates an existing entity
    /// </summary>
    /// <param name="entity">The entity to update</param>
    /// <returns>null if update was successful, or the entity if ID was not found</returns>
    /// <exception cref="ArgumentNullException">If entity is null</exception>
    /// <exception cref="ValidationException">If entity is invalid</exception>
    TEntity? Update(TEntity entity);
}