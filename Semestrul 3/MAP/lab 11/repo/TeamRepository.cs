using System.Data;
using lab_11.domain;
using Npgsql;

namespace lab_11.repo;

public class TeamRepository(string connectionString)
    : AbstractDatabaseRepository<int, Team>(connectionString, "public.teams")
{
    protected override string GetInsertQuery()
    {
        return "INSERT INTO public.teams (name) VALUES (@name) RETURNING id";
    }

    protected override string GetUpdateQuery()
    {
        return "UPDATE public.teams SET name = @name WHERE id = @id";
    }

    protected override void AddParameters(NpgsqlCommand cmd, Team entity)
    {
        cmd.Parameters.AddWithValue("@name", entity.Name);
    }

    protected override Team MapEntity(IDataReader reader)
    {
        return new Team
        {
            Id = reader.GetInt32(reader.GetOrdinal("id")),
            Name = reader.GetString(reader.GetOrdinal("name"))
        };
    }
}