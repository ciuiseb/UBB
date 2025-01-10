using lab_11.domain;
using lab_11.repo;

namespace lab_11.service;

/// <summary>
/// Service class for managing basketball match operations
/// </summary>
/// <param name="matchRepository">Repository for accessing match data</param>
/// <param name="activePlayerRepository">Repository for accessing active player data</param>
/// <param name="playerRepository">Repository for accessing player data</param>
/// <param name="teamRepository">Repository for accessing team data</param>
public class MatchService(
    IRepository<Match, int> matchRepository,
    IRepository<ActivePlayer, int> activePlayerRepository,
    IRepository<Player, int> playerRepository,
    IRepository<Team, int> teamRepository)
{
    /// <summary>
    /// Finds a match between two teams on a specific date
    /// </summary>
    /// <param name="team1Name">Name of the first team</param>
    /// <param name="team2Name">Name of the second team</param>
    /// <param name="date">Date of the match</param>
    /// <returns>The match if found, null otherwise</returns>
    public Match? FindMatch(string team1Name, string team2Name, DateTime date)
    {
        var team1 = teamRepository.FindAll().FirstOrDefault(t => t.Name.Equals(team1Name, StringComparison.OrdinalIgnoreCase));
        var team2 = teamRepository.FindAll().FirstOrDefault(t => t.Name.Equals(team2Name, StringComparison.OrdinalIgnoreCase));

        if (team1 == null || team2 == null)
            return null;

        return matchRepository.FindAll()
            .FirstOrDefault(m =>
                (m.Team1Id == team1.Id && m.Team2Id == team2.Id ||
                 m.Team1Id == team2.Id && m.Team2Id == team1.Id) &&
                m.Date.Date == date.Date);
    }

    /// <summary>
    /// Retrieves all matches within a specified date range
    /// </summary>
    /// <param name="startDate">Start date of the range (inclusive)</param>
    /// <param name="endDate">End date of the range (inclusive)</param>
    /// <returns>List of matches within the date range</returns>
    public List<Match> GetMatchesInDateRange(DateTime startDate, DateTime endDate)
    {
        return matchRepository.FindAll()
            .Where(m => m.Date >= startDate && m.Date <= endDate)
            .ToList();
    }

    /// <summary>
    /// Calculates the score for a specific match
    /// </summary>
    /// <param name="matchId">ID of the match</param>
    /// <returns>Tuple containing scores and team IDs (team1Score, team2Score, team1Id, team2Id)</returns>
    /// <exception cref="Exception">Thrown when match is not found</exception>
    public (int team1Score, int team2Score, int team1Id, int team2Id) GetMatchScore(int matchId)
    {
        var match = matchRepository.FindOne(matchId);
        if (match == null) throw new Exception("Match not found");

        var activePlayers = activePlayerRepository.FindAll()
            .Where(ap => ap.MatchId == matchId)
            .ToList();

        var players = playerRepository.FindAll()
            .Where(p => activePlayers.Select(ap => ap.PlayerId).Contains(p.Id))
            .ToList();

        var team1Score = activePlayers
            .Where(ap => players.First(p => p.Id == ap.PlayerId).TeamId == match.Team1Id)
            .Sum(ap => ap.PointsScored);

        var team2Score = activePlayers
            .Where(ap => players.First(p => p.Id == ap.PlayerId).TeamId == match.Team2Id)
            .Sum(ap => ap.PointsScored);

        return (team1Score, team2Score, match.Team1Id, match.Team2Id);
    }

    /// <summary>
    /// Retrieves all matches on a specific date
    /// </summary>
    /// <param name="date">The date to search for matches</param>
    /// <returns>Collection of matches on the specified date, ordered by ID</returns>
    public IEnumerable<Match> GetMatchesOnDate(DateTime date)
    {
        return matchRepository.FindAll()
            .Where(m => m.Date.Date == date.Date)
            .OrderBy(m => m.Id);
    }
}