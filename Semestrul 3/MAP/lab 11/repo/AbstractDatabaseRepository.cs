using System.Data;
using lab_11.domain;
using Npgsql;

namespace lab_11.repo;
/// <summary>
/// Base repository implementation for database operations
/// </summary>
/// <typeparam name="TId">Type of the entity's ID</typeparam>
/// <typeparam name="TEntity">Type of entity, must inherit from Entity and have a parameterless constructor</typeparam>
/// <param name="connectionString">Database connection string</param>
/// <param name="tableName">Name of the database table</param>
public abstract class AbstractDatabaseRepository<TId, TEntity>(string connectionString, string tableName)
    : IRepository<TEntity, TId>
    where TEntity : Entity<TId>, new()
{
    protected readonly string _tableName = tableName;

    public virtual TEntity? FindOne(TId id)
    {
        if (id == null) throw new ArgumentNullException(nameof(id));

        using var conn = new NpgsqlConnection(connectionString);
        conn.Open();
        using var cmd = new NpgsqlCommand($"SELECT * FROM {_tableName} WHERE id = @id", conn);
        cmd.Parameters.AddWithValue("@id", id);
        using var reader = cmd.ExecuteReader();
        if (reader.Read())
        {
            return MapEntity(reader);
        }

        return default;
    }

    public virtual IEnumerable<TEntity> FindAll()
    {
        var entities = new List<TEntity>();
        using var conn = new NpgsqlConnection(connectionString);
        conn.Open();
        using var cmd = new NpgsqlCommand($"SELECT * FROM {_tableName}", conn);
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            entities.Add(MapEntity(reader));
        }

        return entities;
    }

    public virtual TEntity? Save(TEntity entity)
    {
        if (entity == null) throw new ArgumentNullException(nameof(entity));

        using var conn = new NpgsqlConnection(connectionString);
        conn.Open();
        using var cmd = new NpgsqlCommand(GetInsertQuery(), conn);
        AddParameters(cmd, entity);
        try
        {
            var id = (TId)cmd.ExecuteScalar()!;
            entity.Id = id;
            return null; 
        }
        catch (PostgresException)
        {
            return entity; 
        }
    }

    public virtual TEntity? Delete(TId id)
    {
        if (id == null) throw new ArgumentNullException(nameof(id));

        var entity = FindOne(id);
        if (entity == null) return null;

        using var conn = new NpgsqlConnection(connectionString);
        conn.Open();
        using var cmd = new NpgsqlCommand($"DELETE FROM {_tableName} WHERE id = @id", conn);
        cmd.Parameters.AddWithValue("@id", id);
        cmd.ExecuteNonQuery();
        return entity;
    }

    public virtual TEntity? Update(TEntity entity)
    {
        if (entity == null) throw new ArgumentNullException(nameof(entity));

        using var conn = new NpgsqlConnection(connectionString);
        conn.Open();
        using var cmd = new NpgsqlCommand(GetUpdateQuery(), conn);
        AddParameters(cmd, entity);
        cmd.Parameters.AddWithValue("@id", entity.Id);
        var rowsAffected = cmd.ExecuteNonQuery();
        return rowsAffected == 0 ? entity : null;
    }

    /// <summary>
    /// Maps a database record to an entity
    /// </summary>
    /// <param name="reader">Data reader containing the current record</param>
    /// <returns>New entity instance populated with data from the reader</returns>
    protected abstract TEntity MapEntity(IDataReader reader);

    /// <summary>
    /// Adds entity-specific parameters to the command
    /// </summary>
    /// <param name="cmd">The command to add parameters to</param>
    /// <param name="entity">The entity containing the values for the parameters</param>
    protected abstract void AddParameters(NpgsqlCommand cmd, TEntity entity);

    /// <summary>
    /// Gets the SQL query for inserting a new entity
    /// </summary>
    /// <returns>INSERT query string with parameter placeholders</returns>
    protected abstract string GetInsertQuery();

    /// <summary>
    /// Gets the SQL query for updating an existing entity
    /// </summary>
    /// <returns>UPDATE query string with parameter placeholders</returns>
    protected abstract string GetUpdateQuery();
}
