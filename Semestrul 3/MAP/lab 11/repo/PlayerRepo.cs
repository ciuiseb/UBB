using System.Data;
using lab_11.domain;
using Npgsql;

namespace lab_11.repo;

/// <summary>
/// Repository implementation for Player entities using PostgreSQL database
/// </summary>
/// <param name="connectionString">The database connection string</param>
public class PlayerDatabaseRepository(string connectionString)
    : AbstractDatabaseRepository<int, Player>(connectionString, "public.players")
{
    /// <summary>
    /// Gets the SQL query for inserting a new player
    /// </summary>
    /// <returns>SQL INSERT query with parameters for name, school name, and team ID</returns>
    protected override string GetInsertQuery()
    {
        return "INSERT INTO public.players (name, school_name, team_id) VALUES (@name, @schoolName, @teamId) RETURNING id";
    }

    /// <summary>
    /// Gets the SQL query for updating an existing player
    /// </summary>
    /// <returns>SQL UPDATE query with parameters for name, school name, and team ID</returns>
    protected override string GetUpdateQuery()
    {
        return "UPDATE public.players SET name = @name, school_name = @schoolName, team_id = @teamId WHERE id = @id";
    }

    /// <summary>
    /// Adds player-specific parameters to the SQL command
    /// </summary>
    /// <param name="cmd">The SQL command to add parameters to</param>
    /// <param name="entity">The player entity containing the values</param>
    protected override void AddParameters(NpgsqlCommand cmd, Player entity)
    {
        cmd.Parameters.AddWithValue("@name", entity.Name);
        cmd.Parameters.AddWithValue("@schoolName", entity.SchoolName);
        cmd.Parameters.AddWithValue("@teamId", entity.TeamId);
    }

    /// <summary>
    /// Creates a Player entity from a database record
    /// </summary>
    /// <param name="reader">The data reader containing the player record</param>
    /// <returns>A new Player entity populated with the database record data</returns>
    protected override Player MapEntity(IDataReader reader)
    {
        return new Player
        {
            Id = reader.GetInt32(reader.GetOrdinal("id")),
            Name = reader.GetString(reader.GetOrdinal("name")),
            SchoolName = reader.GetString(reader.GetOrdinal("school_name")),
            TeamId = reader.GetInt32(reader.GetOrdinal("team_id"))
        };
    }
}