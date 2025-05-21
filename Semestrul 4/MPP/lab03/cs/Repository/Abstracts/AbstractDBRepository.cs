using cs.Domain;
using cs.Repository.Interfaces;
using cs.Utils;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using System.Linq;

namespace cs.Repository.Abstracts;

public abstract class AbstractDBRepository<TId, TEntity> : IRepository<TId, TEntity> where TEntity : IEntity<TId>
{
    protected readonly string _tableName;
    protected readonly string _primaryKey;
    protected readonly SQLiteConnection _connection;

    protected AbstractDBRepository(string tableName, string primaryKey)
    {
        _tableName = tableName;
        _primaryKey = primaryKey;
        try
        {
            _connection = GetConnection();
        }
        catch (Exception ex)
        {
            // Logger.LogError(ex);
            throw new ApplicationException("Failed to establish database connection", ex);
        }
    }

    protected abstract string GetInsertParameters();

    protected abstract string GetUpdateParameters();

    protected abstract int GetUpdateParameterCount();

    /// <summary>
    /// Creates an entity from a database reader
    /// </summary>
    /// <param name="reader">The data reader containing entity data</param>
    /// <returns>The entity</returns>
    protected abstract TEntity CreateEntity(IDataReader reader);

    /// <summary>
    /// Sets parameters for the database command
    /// </summary>
    /// <param name="command">The SQLite command</param>
    /// <param name="entity">The entity to save</param>
    protected abstract void SetParameters(SQLiteCommand command, TEntity entity);

    public virtual TEntity Save(TEntity entity)
    {
        TEntity existingEntity = FindOne(entity.GetId());
        if (existingEntity != null)
            return entity;

        string sql = $"INSERT INTO {_tableName} VALUES {GetInsertParameters()}";
        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            SetParameters(command, entity);

            command.ExecuteNonQuery();
            return default;
        }
        catch (Exception ex)
        {
            throw new ApplicationException($"Failed to save entity to {_tableName}", ex);
        }
    }

    public virtual TEntity Delete(TId id)
    {
        TEntity entity = FindOne(id);
        if (entity == null)
            return default;

        string sql = $"DELETE FROM {_tableName} WHERE {_primaryKey} = @id";
        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@id", id);
            command.ExecuteNonQuery();

            return entity;
        }
        catch (Exception ex)
        {
            throw new ApplicationException($"Failed to delete entity from {_tableName}", ex);
        }
    }

    public virtual TEntity Update(TEntity entity)
    {
        string sql = $"UPDATE {_tableName} SET {GetUpdateParameters()} WHERE {_primaryKey} = @id";
        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            SetParameters(command, entity);
            command.Parameters.AddWithValue("@id", entity.GetId());

            int rowsAffected = command.ExecuteNonQuery();
            return rowsAffected > 0 ? entity : default;
        }
        catch (Exception ex)
        {
            throw new ApplicationException($"Failed to update entity in {_tableName}", ex);
        }
    }

    public virtual TEntity FindOne(TId id)
    {
        string sql = $"SELECT * FROM {_tableName} WHERE {_primaryKey} = @id";

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@id", id);

            using var reader = command.ExecuteReader();
            if (reader.Read())
            {
                return CreateEntity(reader);
            }
            return default;
        }
        catch (Exception ex)
        {
            throw new ApplicationException($"Failed to find entity in {_tableName}", ex);
        }
    }

    public virtual IEnumerable<TEntity> FindAll()
    {
        List<TEntity> entities = new();
        string sql = $"SELECT * FROM {_tableName}";

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            using var reader = command.ExecuteReader();

            while (reader.Read())
            {
                entities.Add(CreateEntity(reader));
            }
            return entities;
        }
        catch (Exception ex)
        {
            throw new ApplicationException($"Failed to retrieve entities from {_tableName}", ex);
        }
    }

    public int Count()
    {
        return FindAll().Count();
    }

    protected SQLiteConnection GetConnection()
    {
        return DatabaseConfig.GetConnection();
    }

    protected IEnumerable<TEntity> CreateList(IDataReader reader)
    {
        try
        {
            List<TEntity> list = new();
            while (reader.Read())
            {
                TEntity entity = CreateEntity(reader);
                list.Add(entity);
            }
            return list;
        }
        catch (Exception ex)
        {
            throw new DataException($"Error creating list from data reader: {ex.Message}", ex);
        }
    }
}