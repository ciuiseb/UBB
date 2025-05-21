using System.Collections.Generic;

namespace Triathlon.Persistence.Interfaces
{
    public interface IRepository<TId, TEntity> where TEntity : class
    {
        /// <summary>
        /// Gets an entity by its id
        /// </summary>
        /// <param name="id">The entity id</param>
        /// <returns>The entity if found, otherwise null</returns>
        TEntity? GetById(TId id);

        /// <summary>
        /// Gets all entities
        /// </summary>
        /// <returns>A collection of all entities</returns>
        IEnumerable<TEntity> GetAll();

        /// <summary>
        /// Adds a new entity
        /// </summary>
        /// <param name="entity">The entity to add</param>
        /// <returns>The added entity</returns>
        TEntity Add(TEntity entity);

        /// <summary>
        /// Updates an existing entity
        /// </summary>
        /// <param name="entity">The entity to update</param>
        /// <returns>The updated entity</returns>
        TEntity Update(TEntity entity);

        /// <summary>
        /// Deletes an entity by its id
        /// </summary>
        /// <param name="id">The id of the entity to delete</param>
        /// <returns>True if deleted, false if not found</returns>
        bool Delete(TId id);

        /// <summary>
        /// Saves all changes
        /// </summary>
        /// <returns>Number of affected records</returns>
    }
}