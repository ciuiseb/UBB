using System.Data;
using lab_11.domain;
using Npgsql;

namespace lab_11.repo;


public class MatchDatabaseRepository : AbstractDatabaseRepository<int, Match>
{
    public MatchDatabaseRepository(string connectionString) : base(connectionString, "public.matches") { }

    protected override string GetInsertQuery()
    {
        return "INSERT INTO public.matches (team1_id, team2_id, match_date) VALUES (@team1Id, @team2Id, @date) RETURNING id";
    }

    protected override string GetUpdateQuery()
    {
        return "UPDATE public.matches SET team1_id = @team1Id, team2_id = @team2Id, match_date = @date WHERE id = @id";
    }

    protected override void AddParameters(NpgsqlCommand cmd, Match entity)
    {
        cmd.Parameters.AddWithValue("@team1Id", entity.Team1Id);
        cmd.Parameters.AddWithValue("@team2Id", entity.Team2Id);
        cmd.Parameters.AddWithValue("@date", entity.Date);
    }

    protected override Match MapEntity(IDataReader reader)
    {
        return new Match
        {
            Id = reader.GetInt32(reader.GetOrdinal("id")),
            Team1Id = reader.GetInt32(reader.GetOrdinal("team1_id")),
            Team2Id = reader.GetInt32(reader.GetOrdinal("team2_id")),
            Date = reader.GetDateTime(reader.GetOrdinal("match_date"))
        };
    }
}