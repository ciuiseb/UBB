using lab_11.domain;
using lab_11.repo;

namespace lab_11.service;

public class PlayerService(
    IRepository<Player, int> playerRepository,
    IRepository<ActivePlayer, int> activePlayerRepository,
    IRepository<Team, int> teamRepository)
{
    public IEnumerable<Player> GetTeamPlayers(string teamName)
    {
        var team = teamRepository.FindAll()
            .FirstOrDefault(t => t.Name.Equals(teamName, StringComparison.OrdinalIgnoreCase));

        if (team == null)
            return [];

        return playerRepository.FindAll()
            .Where(p => p.TeamId == team.Id);
    }


    public List<ActivePlayer> GetTeamActivePlayersInMatch(int teamId, int matchId)
    {
        var players = playerRepository.FindAll()
            .Where(p => p.TeamId == teamId)
            .Select(p => p.Id);

        return activePlayerRepository.FindAll()
            .Where(ap => players.Contains(ap.PlayerId) && ap.MatchId == matchId)
            .ToList();
    }
    public string? GetPlayerName(int playerId)
    {
        var team = playerRepository.FindOne(playerId);
        return team?.Name;
    }
}