using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using System.Linq;
using Triathlon.Model;
using Triathlon.Persistence.Interfaces;
using Triathlon.Persistence.Utils;

namespace Triathlon.Persistence.Abstracts
{
    public abstract class AbstractDbRepository<TId, TEntity> : IRepository<TId, TEntity> where TEntity : class
    {
        protected readonly string TableName;
        protected readonly string PrimaryKey;
        protected readonly SQLiteConnection Connection;

        protected AbstractDbRepository(string tableName, string primaryKey)
        {
            TableName = tableName;
            PrimaryKey = primaryKey;
            try
            {
                Connection = GetConnection();
            }
            catch (Exception e)
            {
                throw new Exception("Failed to establish database connection", e);
            }
        }

        protected abstract string GetInsertParameters();
        protected abstract string GetUpdateParameters();
        protected abstract int GetUpdateParameterCount();

        protected abstract TEntity CreateEntity(SQLiteDataReader data);

        protected abstract void SaveEntity(SQLiteCommand command, TEntity entity);

        public TEntity Add(TEntity entity)
        {
            var id = GetEntityId(entity);
            if (GetById(id) != null)
                return entity;

            string sql = $"INSERT INTO {TableName} VALUES {GetInsertParameters()}";
            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != ConnectionState.Open)
                        Connection.Open();

                    SaveEntity(command, entity);
                    command.ExecuteNonQuery();
                    return entity;
                }
            }
            catch (SQLiteException e)
            {
                throw new Exception($"Error saving entity to {TableName}", e);
            }
        }

        public bool Delete(TId id)
        {
            var entity = GetById(id);
            if (entity == null)
                return false;

            string sql = $"DELETE FROM {TableName} WHERE {PrimaryKey} = @Id";
            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@Id", id);
                    int rowsAffected = command.ExecuteNonQuery();
                    return rowsAffected > 0;
                }
            }
            catch (SQLiteException e)
            {
                throw new Exception($"Error deleting entity from {TableName}", e);
            }
        }

        public TEntity Update(TEntity entity)
        {
            string sql = $"UPDATE {TableName} SET {GetUpdateParameters()} WHERE {PrimaryKey} = @Id";
            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != ConnectionState.Open)
                        Connection.Open();

                    SaveEntity(command, entity);
                    command.Parameters.AddWithValue("@Id", GetEntityId(entity));

                    int rowsAffected = command.ExecuteNonQuery();
                    return rowsAffected > 0 ? entity : null;
                }
            }
            catch (SQLiteException e)
            {
                throw new Exception($"Error updating entity in {TableName}", e);
            }
        }

        public TEntity? GetById(TId id)
        {
            string sql = $"SELECT * FROM {TableName} WHERE {PrimaryKey} = @Id";

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@Id", id);

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return CreateEntity(reader);
                        }
                        return null;
                    }
                }
            }
            catch (SQLiteException e)
            {
                throw new Exception($"Error finding entity in {TableName}", e);
            }
        }

        public IEnumerable<TEntity> GetAll()
        {
            List<TEntity> entities = new List<TEntity>();
            string sql = $"SELECT * FROM {TableName}";

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != ConnectionState.Open)
                        Connection.Open();

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            entities.Add(CreateEntity(reader));
                        }
                        return entities;
                    }
                }
            }
            catch (SQLiteException e)
            {
                throw new Exception($"Error finding all entities in {TableName}", e);
            }
        }

        public int Count()
        {
            return GetAll().Count();
        }

        protected SQLiteConnection GetConnection()
        {
            return DatabaseConfig.GetConnection();
        }

        protected IEnumerable<TEntity> CreateList(SQLiteDataReader reader)
        {
            try
            {
                List<TEntity> list = new List<TEntity>();
                while (reader.Read())
                {
                    TEntity entity = CreateEntity(reader);
                    list.Add(entity);
                }
                return list;
            }
            catch (SQLiteException e)
            {
                throw new Exception("Error creating list from SQLiteDataReader", e);
            }
        }

        protected abstract TId GetEntityId(TEntity entity);
    }
}