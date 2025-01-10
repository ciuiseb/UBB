using System.Data;
using lab_11.domain;
using Npgsql;

namespace lab_11.repo;

public class ActivePlayerDatabaseRepository : AbstractDatabaseRepository<int, ActivePlayer>
{
    public ActivePlayerDatabaseRepository(string connectionString) : base(connectionString, "public.active_players") { }

    protected override string GetInsertQuery()
    {
        return "INSERT INTO public.active_players (player_id, match_id, points_scored, player_type) VALUES (@playerId, @matchId, @pointsScored, @type::player_type) RETURNING id";
    }

    protected override string GetUpdateQuery()
    {
        return "UPDATE public.active_players SET player_id = @playerId, match_id = @matchId, points_scored = @pointsScored, player_type = @type::player_type WHERE id = @id";
    }

    protected override void AddParameters(NpgsqlCommand cmd, ActivePlayer entity)
    {
        cmd.Parameters.AddWithValue("@playerId", entity.PlayerId);
        cmd.Parameters.AddWithValue("@matchId", entity.MatchId);
        cmd.Parameters.AddWithValue("@pointsScored", entity.PointsScored);
        cmd.Parameters.AddWithValue("@type", entity.Type.ToString());
    }

    protected override ActivePlayer MapEntity(IDataReader reader)
    {
        return new ActivePlayer
        {
            Id = reader.GetInt32(reader.GetOrdinal("id")),
            PlayerId = reader.GetInt32(reader.GetOrdinal("player_id")),
            MatchId = reader.GetInt32(reader.GetOrdinal("match_id")),
            PointsScored = reader.GetInt32(reader.GetOrdinal("points_scored")),
            Type = Enum.Parse<PlayerType>(reader.GetString(reader.GetOrdinal("player_type")))
        };
    }
}