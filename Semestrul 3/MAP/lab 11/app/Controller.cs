using lab_11.domain;
using lab_11.repo;
using lab_11.service;

namespace lab_11.app;

/// <summary>
/// Interface defining basketball management system operations
/// </summary>
public interface IController
{
    /// <summary>
    /// Gets all players from a specific team
    /// </summary>
    /// <param name="teamName">Name of the team</param>
    /// <returns>List of players in the team</returns>
    List<Player> GetTeamPlayers(string teamName);

    /// <summary>
    /// Gets all active players from a team in a specific match
    /// </summary>
    /// <param name="teamId">ID of the team</param>
    /// <param name="matchId">ID of the match</param>
    /// <returns>List of active players in the match for the specified team</returns>
    List<ActivePlayer> GetTeamActivePlayersInMatch(int teamId, int matchId);

    /// <summary>
    /// Gets all matches within a date range
    /// </summary>
    /// <param name="startDate">Start date of the range (inclusive)</param>
    /// <param name="endDate">End date of the range (inclusive)</param>
    /// <returns>List of matches within the specified date range</returns>
    List<Match> GetMatchesInDateRange(DateTime startDate, DateTime endDate);

    /// <summary>
    /// Gets all matches on a specific date
    /// </summary>
    /// <param name="date">The date to search for</param>
    /// <returns>List of matches on the specified date</returns>
    List<Match> GetMatchesOnDate(DateTime date);

    /// <summary>
    /// Gets the score and team IDs for a specific match
    /// </summary>
    /// <param name="matchId">ID of the match</param>
    /// <returns>Tuple containing (team1Score, team2Score, team1Id, team2Id)</returns>
    (int team1Score, int team2Score, int team1Id, int team2Id) GetMatchScoreAndTeams(int matchId);

    /// <summary>
    /// Gets the name of a team by its ID
    /// </summary>
    /// <param name="teamId">ID of the team</param>
    /// <returns>Team name if found, null otherwise</returns>
    string? GetTeamName(int teamId);

    /// <summary>
    /// Gets the name of a player by their ID
    /// </summary>
    /// <param name="playerId">ID of the player</param>
    /// <returns>Player name if found, null otherwise</returns>
    string? GetPlayerName(int playerId);
}

public class Controller : IController
{
    private readonly PlayerService _playerService;
    private readonly MatchService _matchService;
    private readonly TeamService _teamService;

    public Controller(string connectionString)
    {
        var playerRepo = new PlayerDatabaseRepository(connectionString);
        var matchRepo = new MatchDatabaseRepository(connectionString);
        var activePlayerRepo = new ActivePlayerDatabaseRepository(connectionString);
        var teamRepo = new TeamRepository(connectionString);

        _playerService = new PlayerService(playerRepo, activePlayerRepo, teamRepo);
        _matchService = new MatchService(matchRepo, activePlayerRepo, playerRepo, teamRepo);
        _teamService = new TeamService(teamRepo);
    }

    public List<Player> GetTeamPlayers(string teamName)
    {
        return _playerService.GetTeamPlayers(teamName).ToList();
    }

    public List<ActivePlayer> GetTeamActivePlayersInMatch(int teamId, int matchId)
    {
        return _playerService.GetTeamActivePlayersInMatch(teamId, matchId);
    }

    public List<Match> GetMatchesInDateRange(DateTime startDate, DateTime endDate)
    {
        return _matchService.GetMatchesInDateRange(startDate, endDate);
    }

    public (int team1Score, int team2Score, int team1Id, int team2Id) GetMatchScoreAndTeams(int matchId)
    {
        return _matchService.GetMatchScore(matchId);
    }

    public string? GetTeamName(int teamId)
    {
        return _teamService.GetTeamName(teamId);
    }

    public string? GetPlayerName(int playerId)
    {
        return _playerService.GetPlayerName(playerId);
    }

    public List<Match> GetMatchesOnDate(DateTime date)
    {
        return _matchService.GetMatchesOnDate(date).ToList();
    }
}